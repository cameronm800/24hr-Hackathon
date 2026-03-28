package Game_1_Reaction_Speed;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class Square {
    private boolean isRed = true;
    private int score = 0;
    private final int UISize = 100;

    private int[] coords = new int[2];
    private int squareSize = UISize / 10;

    private JPanel gamePanel;

    public Square() {
        gamePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(isRed ? Color.RED : Color.GREEN);
                g.fillRect(coords[0], coords[1], squareSize, squareSize);
            }

            protected Dimension getPrefferredSize() {
                return new Dimension(800,600);
            }
        };
        gamePanel.setLayout(null);
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

    public int getScore() {
        return this.score;
    }

    public void changePlacement() {
        Random r = new Random();
        //sets x and y to random point

        this.coords[0] = r.nextInt(Math.max(1, 800 - squareSize));
        this.coords[1] = r.nextInt(Math.max(1, 800 - squareSize));

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