package Game1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import General.UIStopWatch;
import GameUI.Display;

public class Square {
    private int score = 0;
    private final int UISize = 800; // Standard fallback size

    private final JPanel gamePanel;
    private final JLabel scoreLabel;
    private final JLabel timeLabel;
    private final JPanel topBar;
    private final Timer randomTimer;
    private final Random random = new Random();
    private UIStopWatch uiStopWatch;
    private final Runnable onExit;
    private final List<SquareEntity> squares = new ArrayList<>();
    private SwingWorker<Object, Object> timerLabelUpdater;
    private UIStopWatch gameTimer;

    private class TimerLabelUpdater extends SwingWorker<Object, Object> {
        UIStopWatch timer;
        JLabel timeLabel;

        public TimerLabelUpdater(UIStopWatch timer, JLabel timeLabel) {
            this.timer = timer;
            this.timeLabel = timeLabel;
        }

        @Override
        protected Object doInBackground() throws Exception {
            while (true) {
                String time = timer.timeSinceStart();
                timeLabel.setText("Time: " + time + " / 0:60s");
                Thread.sleep(100);
            }
            //return null;
        }
    }




    public Square(Runnable onExit, Display orgDisplay) {
        this.onExit = onExit;
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        timeLabel = new JLabel("Time: 0 / 0:60s");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));        
        gameTimer = new UIStopWatch();
        timerLabelUpdater = new TimerLabelUpdater(gameTimer, timeLabel);
        timerLabelUpdater.execute();
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.BLACK);
        topBar.add(timeLabel, BorderLayout.WEST);
        topBar.add(scoreLabel, BorderLayout.EAST);

        gamePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                synchronized (squares) {
                    for (SquareEntity sq : squares) {
                        g.setColor(sq.isRed ? Color.RED : Color.GREEN);
                        g.fillRect(sq.coords[0], sq.coords[1], sq.squareSize, sq.squareSize);
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 800);
            }
        };

        gamePanel.add(topBar);

        gamePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                topBar.setBounds(0, 0, gamePanel.getWidth(), 40);
            }
        });

        addNewSquare();

        randomTimer = new Timer(1000, e -> {
            for (SquareEntity sq : squares) sq.isRed = random.nextBoolean();
            gamePanel.repaint();
        });
        randomTimer.start();

        uiStopWatch = new UIStopWatch(e -> {
            long seconds = uiStopWatch.getSeconds();
            if (seconds >= 60) {
                stopGame(orgDisplay);
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClicks(e.getX(), e.getY());
            }
        });
    }

    private void handleClicks(int x, int y) {
        synchronized (squares) {
            for (int i = squares.size() - 1; i >= 0; i--) {
                SquareEntity sq = squares.get(i);
                if (x >= sq.coords[0] && x <= sq.coords[0] + sq.squareSize &&
                    y >= sq.coords[1] && y <= sq.coords[1] + sq.squareSize) {
                    
                    if (sq.isRed) {
                        score--;
                        sq.squareSize += 10;
                    } else {
                        score++;
                        sq.squareSize = Math.max(20, sq.squareSize - 10);
                    }
                    
                    sq.isRed = !sq.isRed;
                    changePlacement(sq);
                    
                    if (squares.size() < (1 + score / random.nextInt(3, 5))) addNewSquare();

                    scoreLabel.setText("Score: " + score);
                    gamePanel.repaint();
                    break;
                }
            }
        }
    }

    private void stopGame(Display orgDisplay) {
        uiStopWatch.stop();
        randomTimer.stop();
        orgDisplay.setScore(0, score);
        JOptionPane.showMessageDialog(gamePanel, "Game Over! Score: " + score);
        if (onExit != null) onExit.run();
    }

    private void addNewSquare() {
        SquareEntity newSq = new SquareEntity();
        newSq.squareSize = 100;
        changePlacement(newSq);
        synchronized (squares) {
            squares.add(newSq);
        }
    }

    private void changePlacement(SquareEntity sq) {
        // Use panel width if available, otherwise use default UISize
        int currentW = gamePanel.getWidth() > 0 ? gamePanel.getWidth() : UISize;
        int currentH = gamePanel.getHeight() > 0 ? gamePanel.getHeight() : UISize;

        int maxX = Math.max(1, currentW - sq.squareSize);
        int maxY = Math.max(41, currentH - sq.squareSize);

        sq.coords[0] = random.nextInt(maxX);
        // Ensure it spawns below the 40px top bar
        sq.coords[1] = 40 + random.nextInt(Math.max(1, maxY - 40));
    }

    public JPanel getGamePanel() { return gamePanel; }

    private static class SquareEntity {
        boolean isRed = true;
        int squareSize;
        int[] coords = new int[2];
    }
}