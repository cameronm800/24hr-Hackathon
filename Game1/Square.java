package Game1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import General.UIStopWatch;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import Database.Database;

//WARNING AI SLOP!! BEWAREW!!

public class Square {
    private int score = 0;
    private final int UISize = 1000;

    private final JPanel gamePanel;
    private final JLabel scoreLabel;
    private final JLabel timeLabel;
    private final Timer randomTimer;
    private final Random random = new Random();
    private long startTime = -1;
    private long duration = 5000;
    private UIStopWatch uiStopWatch;
    private final Runnable onExit;

    private final List<SquareEntity> squares = new ArrayList<>();

    public Square(Runnable onExit) {
        this.onExit = onExit;
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                for (SquareEntity sq : squares) {
                    g.setColor(sq.isRed ? Color.RED : Color.GREEN);
                    g.fillRect(sq.coords[0], sq.coords[1], sq.squareSize, sq.squareSize);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 800);
            }
        };

        //game panel
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.BLACK);

        //THE SCORE LABEL
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(20, 20, 150, 30);
        gamePanel.add(scoreLabel);

        addNewSquare();

        //Timer that deals with how often the squares changes colours
        randomTimer = new Timer(1000, e -> {
            toggleColorRandomly();
        });
        randomTimer.start();

        //Time label
        timeLabel = new JLabel("Time: 0");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timeLabel.setBounds(20, 50, 150, 30);

        //Timer for stopwatch label
        AtomicLong timePassed = new AtomicLong();
        uiStopWatch = new UIStopWatch();
        uiStopWatch = new UIStopWatch(e -> {
            timePassed.set(uiStopWatch.getSeconds());
            if (timePassed.get() >= 60) {
                uiStopWatch.stop();
                randomTimer.stop();
                

                try {
                    FileWriter writer = new FileWriter("scoreData.txt");
                    writer.write(Integer.toString(score));
                    writer.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }



                if (this.onExit != null) {
                    this.onExit.run();
                }

            }

            long secondLeft = 60 - timePassed.get();

            timeLabel.setText("Time: " + secondLeft);
        });

        gamePanel.add(timeLabel);

        //Checks for clicks
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mousePosX = e.getX();
                int mousePosY = e.getY();

                for (int i = squares.size() - 1; i >= 0; i--) {
                    SquareEntity sq = squares.get(i);

                    if (mousePosX >= sq.coords[0] && mousePosX <= sq.coords[0] + sq.squareSize
                            && mousePosY >= sq.coords[1] && mousePosY <= sq.coords[1] + sq.squareSize) {

                        handleSquareClick(sq);
                        break;
                    }
                }
            }
        });
    }

    private void handleSquareClick(SquareEntity sq) {

        if (sq.isRed) {
            score -= 1;
            sq.squareSize += UISize / 50;
        } else {
            score += 1;
            sq.squareSize -= UISize / 50;
        }

        sq.isRed = !sq.isRed;
        changePlacement(sq);

        int targetSquareCount = 1 + Math.max(0, score / 5);
        while (squares.size() < targetSquareCount) {
            addNewSquare();
        }

        scoreLabel.setText("Score: " + score);
        gamePanel.repaint();
    }

    private void addNewSquare() {
        SquareEntity newSq = new SquareEntity();
        newSq.squareSize = UISize / 5;
        newSq.isRed = true;
        changePlacement(newSq);
        squares.add(newSq);
    }

    private void changePlacement(SquareEntity sq) {

        int panelWidth = gamePanel.getWidth();
        int panelHeight = gamePanel.getHeight();

        sq.coords[0] = random.nextInt(Math.max(1, panelWidth - sq.squareSize));
        sq.coords[1] = random.nextInt(Math.max(1, panelHeight - sq.squareSize));
    }

    private void toggleColorRandomly() {
        for (SquareEntity sq : squares) {
            sq.isRed = random.nextBoolean();
        }

        int scoreBonus = Math.min(3500, score * 100);
        int maxRandom = 1500 - scoreBonus;
        int nextDelay = 500 + random.nextInt(Math.max(1, maxRandom));
        randomTimer.setInitialDelay(nextDelay);
        randomTimer.restart();
        gamePanel.repaint();
    }

    public int getScore() {
        return this.score;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    //thanks gemini..

    // --- Inner class to hold data for individual squares ---
    private static class SquareEntity {
        boolean isRed = true;
        int squareSize;
        int[] coords = new int[2];
    }
}