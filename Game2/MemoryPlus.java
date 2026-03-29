package Game2;

import GameUI.Display;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MemoryPlus {
    private int[][] grid;
    private Display display;
    private Color[][] colors;
    private final int UISize = 580;
    private final int cardSize = 80;
    private boolean[][] flippedCards;
    private boolean[][] matchedCards;
    private int score = 0;
    private long startTime;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private Timer gameTimer;

    private JPanel gamePanel;
    private int firstFlippedRow = -1;
    private int firstFlippedCol = -1;
    private int secondFlippedRow = -1;
    private int secondFlippedCol = -1;

    private boolean showingCards = true;
    private Runnable onGameOver;

    private boolean isProcessing = false;

    public MemoryPlus(int k, int r, Runnable onGameOver, Display display) {
        this.display = display;
        this.grid = new int[k][k];
        this.colors = new Color[k][k];
        this.flippedCards = new boolean[k][k];
        this.matchedCards = new boolean[k][k];
        this.onGameOver = onGameOver;

        this.generateGrid(k, r);
        this.createGamePanel();
        startGameTimer();

        Timer startTimer = new Timer(1000, e -> {
            showingCards = false;
            gamePanel.repaint();
            ((Timer)e.getSource()).stop();
        });
        startTimer.setRepeats(false);
        startTimer.start();
    }

    public void generateGrid(int k, int r) {
        Random rand = new Random();
        List<Color> colorPairs = new ArrayList<>();

        for (int i = 0; i < (k * k) / 2; i++) {
            Color randomColor = new Color(
                rand.nextInt(256),
                rand.nextInt(256),
                rand.nextInt(256)
            );
            colorPairs.add(randomColor);
            colorPairs.add(randomColor);
        }

        Collections.shuffle(colorPairs);

        int index = 0;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                this.colors[i][j] = colorPairs.get(index++);
            }
        }
    }

    public void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);

        JPanel gridPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        int x = i * cardSize + 20;
                        int y = j * cardSize + 20;

                        if (showingCards || flippedCards[i][j] || matchedCards[i][j]) {
                            g.setColor(colors[i][j]);
                        } else {
                            g.setColor(Color.GRAY);
                        }

                        g.fillRect(x, y, cardSize, cardSize);
                    }
                }
            }

            public Dimension getPreferredSize() {
                return new Dimension(UISize, UISize);
            }
        };

        gamePanel.add(gridPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.BLACK);

        scoreLabel = new JLabel("Score: " + score);
        timeLabel = new JLabel("Time: 0s");

        scoreLabel.setForeground(Color.WHITE);
        timeLabel.setForeground(Color.WHITE);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        topPanel.add(scoreLabel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(timeLabel);

        gamePanel.add(topPanel, BorderLayout.NORTH);

        gridPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int mousePosX = e.getX();
                int mousePosY = e.getY();

                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        int x = i * cardSize + 20;
                        int y = j * cardSize + 20;

                        if (mousePosX >= x && mousePosX <= x + cardSize &&
                            mousePosY >= y && mousePosY <= y + cardSize) {

                            cardClicked(i, j);
                            gamePanel.repaint();
                            return;
                        }
                    }
                }
            }
        });
    }

    public void cardClicked(int i, int j) {
        if (isProcessing || flippedCards[i][j] || matchedCards[i][j]) {
            return;
        }

        flippedCards[i][j] = true;

        if (firstFlippedRow == -1) {
            firstFlippedRow = i;
            firstFlippedCol = j;
        } else if (secondFlippedRow == -1) {
            secondFlippedRow = i;
            secondFlippedCol = j;

            isProcessing = true;

            gamePanel.repaint();

            Timer timer = new Timer(500, e -> {
                checkMatch();
                ((Timer)e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();

            return;
        }

        gamePanel.repaint();
    }

    public void checkMatch() {
        if (colors[firstFlippedRow][firstFlippedCol]
                .equals(colors[secondFlippedRow][secondFlippedCol])) {

            matchedCards[firstFlippedRow][firstFlippedCol] = true;
            matchedCards[secondFlippedRow][secondFlippedCol] = true;
            score += 10;

        } else {
            flippedCards[firstFlippedRow][firstFlippedCol] = false;
            flippedCards[secondFlippedRow][secondFlippedCol] = false;
            score -= 1;
        }

        scoreLabel.setText("Score: " + score);

        firstFlippedRow = -1;
        firstFlippedCol = -1;
        secondFlippedRow = -1;
        secondFlippedCol = -1;

        if (isGameOver()) {
            stopGameTimer();
            saveGameResult();
            endGame();
        }

        isProcessing = false;
        gamePanel.repaint();
    }

    public void startGameTimer() {
        startTime = System.currentTimeMillis();

        gameTimer = new Timer(1000, e -> {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            timeLabel.setText("Time: " + elapsedTime + "s");
        });

        gameTimer.start();
    }

    public void stopGameTimer() {
        gameTimer.stop();
    }

    public boolean isGameOver() {
        for (int i = 0; i < matchedCards.length; i++) {
            for (int j = 0; j < matchedCards[i].length; j++) {
                if (!matchedCards[i][j]) return false;
            }
        }
        return true;
    }

    public void saveGameResult() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
    }

    public void endGame() {
        JOptionPane.showMessageDialog(
            gamePanel,
            "Game Over! Your score is: " + score,
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE
        );

        display.setScore(1, score);

        if (onGameOver != null) {
            onGameOver.run();
        }
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public int getScore() {
        return score;
    }

    public long getTimeSpent() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}