package Game1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
public class Square {
    private boolean isRed = true;
    private int score = 0;
    private final int UISize = 580;

    private int[] coords = new int[2];
    private int squareSize = UISize / 2;

    private JPanel gamePanel;
    private JLabel scoreLabel;

    private Timer randomTimer;
    private Random random = new Random();

    public Square() {
        gamePanel = new JPanel() {

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(isRed ? Color.RED : Color.GREEN);
                g.fillRect(coords[0], coords[1], squareSize, squareSize);
            }

            public Dimension getPreferredSize() {
                return new Dimension(800,600);
            }
        };
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.BLACK);
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(20, 20, 150, 30);
        gamePanel.add(scoreLabel);

        randomTimer = new Timer(1000, e -> {
            toggleColorRandomly();
        });

        randomTimer.start();

        gamePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int mousePosX = e.getX();
                int mousePosY = e.getY();

                if (mousePosX >= coords[0] && mousePosX <= coords[0] + squareSize && mousePosY >= coords[1] && mousePosY <= coords[1] + squareSize) {
                    Pressed();
                    isRed = !isRed;
                    changePlacement();

                    scoreLabel.setText("Score: " + getScore());
                    scoreLabel.repaint();
                    gamePanel.repaint();
                }
            }
        });
        System.out.println(score);
    }


    public boolean getColour() {
        return this.isRed;
    }

    public void setColour(boolean isRed) {
        this.isRed = isRed;
    }

    public void Pressed() {
        if (isRed) {
            score -=1;
            squareSize += (int) UISize / 100;
        }
        else {
            score +=1;
            squareSize -= (int) UISize / 100;
        }
    }

    private void toggleColorRandomly() {
        isRed = random.nextBoolean();
        int scoreBonus = Math.min(3500, score * 100);
        int maxRandom = 4000 - scoreBonus;
        int nextDelay = 500 + random.nextInt(Math.max(1, maxRandom));
        randomTimer.setInitialDelay(nextDelay);
        randomTimer.restart();
        gamePanel.repaint();
        System.out.println("HI");
    }

    public int getScore() {
        return this.score;
    }

    public void changePlacement() {
        Random r = new Random();
        //sets x and y to random point

        this.coords[0] = r.nextInt(Math.max(1, 580 - squareSize));
        this.coords[1] = r.nextInt(Math.max(1, 580 - squareSize));

       // this.coords[0] = r.nextInt((int) (-UISize * (score / 5)), (int) UISize * (score / 5));
        //this.coords[1] = r.nextInt((int) (-UISize * (score / 5)), (int) UISize * (score / 5));
    }

    public int[] getCoords() {
        return coords;
    }

    public int getSize() {
        return this.squareSize;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }
}