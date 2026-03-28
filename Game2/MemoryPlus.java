package Game2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MemoryPlus {
    private int[][] grid;
    private Color[][] colors;  // Use Color objects instead of integers
    private final int UISize = 580;  // Size of the entire UI (game panel)
    private final int cardSize = 80;  // Size of each card
    private boolean[][] flippedCards;
    private boolean[][] matchedCards; // Keep track of matched cards
    private int score = 0;
    private long startTime;  // Variable to track the start time
    private JLabel scoreLabel;  // Label to display score
    private JLabel timeLabel;  // Label to display time
    private Timer gameTimer;  // Timer for time tracking

    private JPanel gamePanel;
    private int firstFlippedRow = -1;
    private int firstFlippedCol = -1;
    private int secondFlippedRow = -1;
    private int secondFlippedCol = -1;

    private boolean showingCards = true; // Flag to show the cards for 1 second

    private Runnable onGameOver;  // Runnable to return to the menu after the game ends

    public MemoryPlus(int k, int r, Runnable onGameOver) {
        this.grid = new int[k][k];
        this.colors = new Color[k][k];  // Use Color objects for the colors
        this.flippedCards = new boolean[k][k]; // Track flipped cards
        this.matchedCards = new boolean[k][k]; // Track matched cards
        this.onGameOver = onGameOver;  // Store the onGameOver Runnable
        
        this.generateGrid(k, r);  // Generate the grid and color pairs
        this.createGamePanel();  // Create the game panel
        startGameTimer();  // Start the game timer to track the elapsed time
        
        // Show all cards for 1 second at the beginning of the game
        Timer startTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showingCards = false;  // Hide the cards after 1 second
                gamePanel.repaint();   // Repaint the panel to hide the cards
                ((Timer)e.getSource()).stop(); // Stop the timer
            }
        });
        startTimer.start();  // Start the timer
    }

    // Generate grid with random pairs of colors
    public void generateGrid(int k, int r) {
        Random rand = new Random();

        // Create a list of pairs of random colors
        List<Color> colorPairs = new ArrayList<>();

        // Create pairs of random colors, ensuring each color is used twice
        for (int i = 0; i < (k * k) / 2; i++) {
            Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)); // Random color
            colorPairs.add(randomColor);
            colorPairs.add(randomColor); // Add the same color again for pairing
        }

        // Shuffle the list to randomize color positions
        Collections.shuffle(colorPairs);

        // Assign the colors to the grid
        int index = 0;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                this.colors[i][j] = colorPairs.get(index++);
            }
        }
    }

    // Create the game panel and handle card clicks
    public void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);  // Set the background color to black for the game panel

        // Create the grid panel for the cards
        JPanel gridPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the cards based on their flipped and matched states
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        int x = i * cardSize + 20;
                        int y = j * cardSize + 20;

                        // Show all cards if the showingCards flag is true or the card is flipped/matched
                        if (showingCards || flippedCards[i][j] || matchedCards[i][j]) {
                            g.setColor(colors[i][j]);  // Use the Color object for drawing the card
                            g.fillRect(x, y, cardSize, cardSize);
                        } else {
                            g.setColor(Color.GRAY);  // Draw face-down card
                            g.fillRect(x, y, cardSize, cardSize);
                        }
                    }
                }
            }

            public Dimension getPreferredSize() {
                return new Dimension(UISize, UISize);  // Set the size of the grid panel
            }
        };

        // Add gridPanel to the center of the gamePanel
        gamePanel.add(gridPanel, BorderLayout.CENTER);

        // Add score and time labels to the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.BLACK);  // Make sure the top panel also has a black background
        topPanel.setOpaque(true);  // Ensure that it doesn't inherit transparency

        scoreLabel = new JLabel("Score: " + score);
        timeLabel = new JLabel("Time: 0s");

        scoreLabel.setForeground(Color.WHITE);
        timeLabel.setForeground(Color.WHITE);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));  // Increase font size for score
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));  // Increase font size for time

        topPanel.add(scoreLabel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(timeLabel);

        gamePanel.add(topPanel, BorderLayout.NORTH);

        // MouseListener to handle card clicks
        gridPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int mousePosX = e.getX();
                int mousePosY = e.getY();

                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        int x = i * cardSize + 20;
                        int y = j * cardSize + 20;

                        if (mousePosX >= x && mousePosX <= x + cardSize && mousePosY >= y && mousePosY <= y + cardSize) {
                            cardClicked(i, j);
                            gamePanel.repaint();
                            return;
                        }
                    }
                }
            }
        });
    }

    // Handle card click logic
    public void cardClicked(int i, int j) {
        if (!flippedCards[i][j] && !matchedCards[i][j]) {
            flippedCards[i][j] = true;

            // If this is the first card flipped
            if (firstFlippedRow == -1) {
                firstFlippedRow = i;
                firstFlippedCol = j;
            }
            // If this is the second card flipped
            else if (secondFlippedRow == -1) {
                secondFlippedRow = i;
                secondFlippedCol = j;

                // Use a timer to delay checking the second card for a moment
                javax.swing.Timer timer = new javax.swing.Timer(500, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        checkMatch(); // Check the two flipped cards
                        ((javax.swing.Timer)e.getSource()).stop(); // Stop the timer
                    }
                });
                timer.start();
            }
        }
    }

    // Check if the flipped cards match
    public void checkMatch() {
        if (colors[firstFlippedRow][firstFlippedCol].equals(colors[secondFlippedRow][secondFlippedCol])) {
            // If they match, mark as matched and increase score
            matchedCards[firstFlippedRow][firstFlippedCol] = true;
            matchedCards[secondFlippedRow][secondFlippedCol] = true;
            score += 10;
        } else {
            // If they don't match, hide the cards again
            flippedCards[firstFlippedRow][firstFlippedCol] = false;
            flippedCards[secondFlippedRow][secondFlippedCol] = false;
            score -= 1;
        }

        // Update the score display
        scoreLabel.setText("Score: " + score);

        // Reset flipped card positions
        firstFlippedRow = -1;
        firstFlippedCol = -1;
        secondFlippedRow = -1;
        secondFlippedCol = -1;

        // Check if all cards are matched
        if (isGameOver()) {
            stopGameTimer();  // Stop the timer when the game is over
            saveGameResult();  // Save the result to the database
            endGame();  // Show a game over message and stop the game
        }
    }

    // Start the game timer
    public void startGameTimer() {
        startTime = System.currentTimeMillis();  // Store the start time

        gameTimer = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                timeLabel.setText("Time: " + elapsedTime + "s");
            }
        });
        gameTimer.start();  // Start the timer
    }

    // Stop the game timer
    public void stopGameTimer() {
        gameTimer.stop();  // Stop the timer
    }

    // Check if the game is over (all cards are matched)
    public boolean isGameOver() {
        for (int i = 0; i < matchedCards.length; i++) {
            for (int j = 0; j < matchedCards[i].length; j++) {
                if (!matchedCards[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Save the game result (score and time) to the database
    public void saveGameResult() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;  // Time in seconds
        // Here you can implement a database connection to store score and time
        System.out.println("Game Over! Score: " + score + ", Time: " + elapsedTime + "s");
    }

    // Show a game over message and stop the game
    public void endGame() {
        JOptionPane.showMessageDialog(gamePanel, "Game Over! Your score is: " + score, 
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
        
        // Call the onGameOver Runnable to go back to the menu
        if (onGameOver != null) {
            onGameOver.run();
        }
    }

    // Get the game panel for display
    public JPanel getGamePanel() {
        return gamePanel;
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Getter for time spent
    public long getTimeSpent() {
        return (System.currentTimeMillis() - startTime) / 1000;  // Return time in seconds
    }
}