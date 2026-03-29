package GameUI;

import Game1.*;
import Game2.*;
import Game3.*;
import Database.Database;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
public class Display {
    private final JFrame frame;

    private final Color DARK_BG = new Color(45, 45, 45);
    private final Color ACCENT_CYAN = new Color(0, 210, 255);
    private final Color TOOLBAR_BG = new Color(33, 33, 33);
    private final Color HOVER_COLOR = new Color(60, 60, 60);
    private int[] scores = {0, 0, 0};
    private boolean[] isDone = {false, false, false};
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 48);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.ITALIC, 14);

    public Display() {
        this.frame = new JFrame("Isle Be Better");
        this.frame.setSize(1000, 600);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.getContentPane().setBackground(DARK_BG);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resetScore();
    }

    public void setScore(int game, int score) {
        scores[game] = score;
        isDone[game] = true;
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Isle Be Better", SwingConstants.CENTER);
        title.setForeground(ACCENT_CYAN);
        title.setFont(TITLE_FONT);
        title.setBorder(new EmptyBorder(100, 0, 50, 0));
        return title;
    }

    private void resetScore() {
        try {
            FileWriter writer = new FileWriter("scoreData.txt");
            writer.write("0");
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel title = createTitle();

        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.gridy = 0;

        gbc.gridx = 0;
        if (!isDone[0]) {
            buttonContainer.add(createGameGroup("REACTION SPEED", "Score: " + scores[0], this::startGameOne), gbc);
        }
        else {
            buttonContainer.add(createGameGroup("REACTION SPEED", "Score: " + scores[0], () -> {}), gbc);
        }

        gbc.gridx = 1;
        if (!isDone[1]) {
            buttonContainer.add(createGameGroup("MEMORY GAME", "Score: " + scores[1], this::startGameTwo), gbc);
        }
        else {
            buttonContainer.add(createGameGroup("MEMORY GAME", "Score: " + scores[1], () -> {}), gbc);
        }

        gbc.gridx = 2;
        if (!isDone[2]) {
            buttonContainer.add(createGameGroup("TYPING GAME", "Score: " + scores[2], this::startGameThree), gbc);
        }
        else {
            buttonContainer.add(createGameGroup("TYPING GAME", "Score: " + scores[2], () -> {}), gbc);
        }

        // Once all games are done, add a username field and a submit button
        if (isDone[0] && isDone[1] && isDone[2]) {
            buttonContainer.removeAll();  // Remove all game buttons

            // Create username input and submit button
            buttonContainer.add(createUsernameInput(), gbc);
        }

        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonContainer, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createUsernameInput() {
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setOpaque(false);

        // Create label and input for username
        JLabel usernameLabel = new JLabel("Enter Username:");
        usernameLabel.setForeground(Color.GRAY);
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(280, 40));
        usernameField.setPreferredSize(new Dimension(280, 40));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(BUTTON_FONT);
        submitButton.setForeground(ACCENT_CYAN);
        submitButton.setBackground(TOOLBAR_BG);
        submitButton.setFocusPainted(false);
        submitButton.setEnabled(false); // Initially disabled
        submitButton.setMaximumSize(new Dimension(280, 50));
        submitButton.setPreferredSize(new Dimension(280, 50));
        submitButton.setBorder(new LineBorder(ACCENT_CYAN, 2));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Enable submit button only if username is entered
        usernameField.addCaretListener(e -> {
            String username = usernameField.getText().trim();
            submitButton.setEnabled(!username.isEmpty());
        });

        // Submit button action
        submitButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            // Add your logic for handling the username submission

            //TODO: Need bash file to contain export CLASSPATH=${CLASSPATH}:./Database/sqlite-jdbc-3.51.3.0.jar
            Database database = new Database();
            database.insertTable(username, scores[0], scores[1], scores[2]);
        });

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.add(submitButton);

        return usernamePanel;
    }

    private JPanel createGameGroup(String btnText, String labelText, Runnable action) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);

        JButton button = createMenuButton(btnText);
        button.addActionListener(e -> action.run());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setForeground(Color.GRAY);
        label.setFont(LABEL_FONT);
        label.setBorder(new EmptyBorder(10, 0, 0, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        group.add(button);
        group.add(label);
        return group;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(ACCENT_CYAN);
        button.setBackground(TOOLBAR_BG);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(280, 150));
        button.setPreferredSize(new Dimension(280, 150));
        button.setBorder(new LineBorder(ACCENT_CYAN, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
                button.setBorder(new LineBorder(Color.WHITE, 2));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(TOOLBAR_BG);
                button.setBorder(new LineBorder(ACCENT_CYAN, 2));
            }
        });
        return button;
    }

    private void startGameOne() {
        Square square = new Square(this::createMenu, this);
        setScreen(square.getGamePanel());
    }
    
    private void startGameTwo() {
        MemoryPlus mem = new MemoryPlus(2, 1, () -> createMenu(), this);
        setScreen(mem.getGamePanel());
    }
    
    private void startGameThree() {
        setScreen(new Game3UI(this::createMenu, this));
    }

    private void setScreen(Component component) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(component, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public void showWindow() {
        createMenu();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}