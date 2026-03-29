package Game3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import GameUI.Display;
import java.awt.event.KeyEvent;
import java.io.IOException;

import General.UIStopWatch;

public class Game3UI extends JPanel implements KeyEventDispatcher {
    private Display display;
    UIStopWatch timer;
    TypingTester typingTester;
    SwingWorker<Object, Object> timerLabelUpdater;
    Runnable onGameOver;

    JLabel timeLabel;
    JLabel textLabel;
    JLabel scoreLabel;


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // This fires on every single key press
        if (e.getID() == KeyEvent.KEY_PRESSED) {

            TypingTester.TypeResult typeResult = typingTester.typeChar(e.getKeyChar());

            String labelText;
            switch (typeResult) {
                case TypingTester.TypeResult.COMPLETE:
                    timerLabelUpdater.cancel(true);
                    labelText = String.format("<html>" +
                            "<font color='green'>%s</font>" +
                            "</html>", typingTester.getTypedString());
                    textLabel.setText(labelText);

                    JOptionPane.showMessageDialog(this, "Game Over! Your score is: " + typingTester.getScore(),
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
                    if (onGameOver != null) {
                        display.setScore(2, typingTester.getScore());
                        onGameOver.run();
                    }
                    break;
                case TypingTester.TypeResult.CORRECT:
                    labelText = String.format("<html>" +
                            "<font color='green'>%s</font>" +
                            "<font color='black'>%s%s</font> " +
                            "</html>", typingTester.getTypedString(), typingTester.getCharAsString(), typingTester.getUntypedString());
                    textLabel.setText(labelText);
                    break;
                case TypingTester.TypeResult.INCORRECT:
                    labelText = String.format("<html>" +
                            "<font color='green'>%s</font>" +
                            "<font color='red'>%s</font>" +
                            "<font color='black'>%s</font>" +
                            "</html>", typingTester.getTypedString(), typingTester.getCharAsString(), typingTester.getUntypedString());
                    textLabel.setText(labelText);
                    break;
            }

            scoreLabel.setText("Score: " + Integer.toString(typingTester.getScore()));
        }



        // Return false to allow the event to continue to the component
        // Return true to "consume" the event and stop it from reaching buttons/text fields
        return false;
    }

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
                timeLabel.setText("Time: " + timer.timeSinceStart() + "s");
                Thread.sleep(100);
            }
            //return null;
        }
    }

    public Game3UI(Runnable onGameOver, Display display) {
        //TODO Make sure directional
        super(new BorderLayout());
        this.display = display;
        this.onGameOver = onGameOver;
        timeLabel = new JLabel("Time: 00:00:000");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textLabel = new JLabel();
        textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));



        timeLabel.setForeground(Color.WHITE);
        textLabel.setForeground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setPreferredSize(new Dimension(100, 30));
        // 1. Use BorderLayout for the sub-panel
        JPanel grid = new JPanel(new BorderLayout()); 
        grid.setBackground(Color.BLACK);

        // 2. Add labels to the specific edges
        grid.add(timeLabel, BorderLayout.WEST);   // Sticks to the left
        grid.add(scoreLabel, BorderLayout.EAST);  // Sticks to the right

        // 3. Optional: Add padding so they aren't touching the very edge
        grid.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 4. Add the grid to the main panel
        add(grid, BorderLayout.NORTH);
        add(textLabel, BorderLayout.CENTER);

        try {
            typingTester = new TypingTester(15);
            timer = new UIStopWatch();

            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

            textLabel.setText(String.format("<html>" +
                    "<font color='black'>%s</font>" +
                    "</html>",  typingTester.getParagraph()));
            textLabel.setHorizontalAlignment(JLabel.CENTER);

            //Updates the label displaying the timer
            timerLabelUpdater = new TimerLabelUpdater(timer, timeLabel);
            timerLabelUpdater.execute();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //TODO Handle error
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }

}