package Game3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import GameUI.Display;
import java.awt.event.KeyEvent;

import General.UIStopWatch;

public class Game3UI extends JPanel implements KeyEventDispatcher {
    private Display display;
    UIStopWatch timer;
    TypingTester typingTester;
    SwingWorker<Object, Object> timerLabelUpdater;

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
                    display.setScore(2, typingTester.getScore());
                    break;
                case TypingTester.TypeResult.CORRECT:
                    labelText = String.format("<html>" +
                            "<font color='green'>%s</font>" +
                            "<font color='white'>%s%s</font> " +
                            "</html>", typingTester.getTypedString(), typingTester.getCharAsString(), typingTester.getUntypedString());
                    textLabel.setText(labelText);
                    break;
                case TypingTester.TypeResult.INCORRECT:
                    labelText = String.format("<html>" +
                            "<font color='green'>%s</font>" +
                            "<font color='red'>%s</font>" +
                            "<font color='white'>%s</font>" +
                            "</html>", typingTester.getTypedString(), typingTester.getCharAsString(), typingTester.getUntypedString());
                    textLabel.setText(labelText);
                    break;
            }

            scoreLabel.setText(Integer.toString(typingTester.getScore()));
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
                timeLabel.setText(timer.timeSinceStart());
                Thread.sleep(100);
            }
            //return null;
        }
    }

    public Game3UI(Display display) {
        //TODO Make sure directional
        super(new BorderLayout());
        this.display = display;

        timeLabel = new JLabel("00:00:000");
        textLabel = new JLabel("hello World");
        scoreLabel = new JLabel("0");

        timeLabel.setForeground(Color.WHITE);
        textLabel.setForeground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);

        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);

        add(timeLabel, BorderLayout.NORTH);
        add(textLabel, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.EAST);

        typingTester = new TypingTester("hello world");
        timer = new UIStopWatch();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

        //Updates the label displaying the timer
        timerLabelUpdater = new TimerLabelUpdater(timer, timeLabel);
        timerLabelUpdater.execute();

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }

}