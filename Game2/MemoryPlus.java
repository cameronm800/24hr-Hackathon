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
    private final int UISize = 800;
    private final int cardSize = 80;
    private boolean[][] flippedCards;
    private boolean[][] matchedCards; // Keep track of matched cards
    private int score = 0;

    private JPanel gamePanel;
    private int firstFlippedRow = -1;
    private int firstFlippedCol = -1;
    private int secondFlippedRow = -1;
    private int secondFlippedCol = -1;

    public MemoryPlus(int k, int r) {
        this.grid = new int[k][k];
        this.colors = new Color[k][k];  // Use Color objects for the colors
        this.flippedCards = new boolean[k][k]; // Track flipped cards
        this.matchedCards = new boolean[k][k]; // Track matched cards
        this.generateGrid(k, r);  // Generate the grid and color pairs
        this.createGamePanel();  // Create the game panel
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

    // Generate random colors (RGB) for each pair of colors is no longer needed
    // This method is replaced by the logic in `generateGrid`

    public void addNum(int i, int j, int n) {
        this.grid[i][j] = n;
    }

    public int[][] getColors() {
        return this.grid;
    }

    public Color[][] getColorArray() {
        return this.colors;
    }

    // Create the game panel and handle card clicks
    public void createGamePanel() {
        gamePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Draw the cards
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        int x = i * cardSize + 20;
                        int y = j * cardSize + 20;

                        if (flippedCards[i][j] || matchedCards[i][j]) {
                            // Display card with color
                            g.setColor(colors[i][j]);  // Use Color object for drawing
                            g.fillRect(x, y, cardSize, cardSize);
                            g.setColor(Color.WHITE);
                        } else {
                            // Draw face-down card
                            g.setColor(Color.GRAY);
                            g.fillRect(x, y, cardSize, cardSize);
                        }
                    }
                }
            }

            public Dimension getPreferredSize() {
                return new Dimension(UISize, UISize);
            }
        };

        gamePanel.setLayout(null);
        gamePanel.addMouseListener(new MouseAdapter() {
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

    // Handle the logic when a card is clicked
    public void cardClicked(int i, int j) {
        if (!flippedCards[i][j] && !matchedCards[i][j]) {
            flippedCards[i][j] = true;

            // If this is the first card being flipped
            if (firstFlippedRow == -1) {
                firstFlippedRow = i;
                firstFlippedCol = j;
            }
            // If this is the second card being flipped
            else if (secondFlippedRow == -1) {
                secondFlippedRow = i;
                secondFlippedCol = j;

                // Use a timer to delay the checking of the second card for a moment
                Timer timer = new Timer(500, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        checkMatch(); // Check the two flipped cards
                        ((Timer)e.getSource()).stop(); // Stop the timer
                    }
                });
                timer.start();
            }
        }
    }

    // Check if two flipped cards match
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
            score -= 2;
        }

        // Reset flipped card positions
        firstFlippedRow = -1;
        firstFlippedCol = -1;
        secondFlippedRow = -1;
        secondFlippedCol = -1;
    }

    // Get the current score
    public int getScore() {
        return this.score;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }
}