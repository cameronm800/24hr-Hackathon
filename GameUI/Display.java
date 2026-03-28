package GameUI;
import javax.swing.*;
import javax.swing.border.*;

import Game1.Square;
import Game2.MemoryPlus;

import java.awt.*;
import java.awt.event.*;

public class Display {
    private final JFrame frame;

    private final Color DARK_BG = new Color(45, 45, 45);
    private final Color ACCENT_CYAN = new Color(0, 210, 255);
    private final Color TOOLBAR_BG = new Color(33, 33, 33);
    private final Color HOVER_COLOR = new Color(60, 60, 60);

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 48);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.ITALIC, 14);

    public Display() {
        this.frame = new JFrame("Isle Be Better");
        this.frame.setSize(1000, 600);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.getContentPane().setBackground(DARK_BG);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Isle Be Better", SwingConstants.CENTER);
        title.setForeground(ACCENT_CYAN);
        title.setFont(TITLE_FONT);
        title.setBorder(new EmptyBorder(100, 0, 50, 0));
        return title;
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
        buttonContainer.add(createGameGroup("REACTION SPEED", "EDP's might", this::startGameOne), gbc);

        gbc.gridx = 1;
        buttonContainer.add(createGameGroup("MEMORY GAME", "diddy's best", this::startGameTwo), gbc);

        gbc.gridx = 2;
        buttonContainer.add(createGameGroup("TYPING GAME", "epstein's favourite", this::startGameThree), gbc);

        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonContainer, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
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
        Square square = new Square(this::createMenu);
        setScreen(square.getGamePanel());
    }

    private void startGameTwo() {
        MemoryPlus mem = new MemoryPlus(2, 1);
        mem.createGamePanel();
        setScreen(mem.getGamePanel());
    }

    private void startGameThree() {
        //TypingTester typing = new TypingTester(this::createMenu);
        //setScreen(typing.getGamePanel());
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