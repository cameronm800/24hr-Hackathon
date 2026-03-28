package GameUI;
import javax.swing.*;
import javax.swing.border.*;

import Game1.Square;
import Game2.MemoryPlus;
import Game3.TypingTester;

import java.awt.*;
import java.awt.event.*;

public class Display {
    private JFrame frame;

    private final Color DARK_BG = new Color(45, 45, 45);
    private final Color ACCENT_CYAN = new Color(0, 210, 255);
    private final Color TOOLBAR_BG = new Color(33, 33, 33);
    private final Color HOVER_COLOR = new Color(60, 60, 60);

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 48);
    private static final Font MENU_ITEM_FONT = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 28);

    public Display() {
        this.frame = new JFrame("Isle Be Better");
        this.frame.setSize(800, 600);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.getContentPane().setBackground(DARK_BG);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JLabel createTitle(String text) {
        JLabel title = new JLabel(text, SwingConstants.CENTER);
        title.setForeground(ACCENT_CYAN);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setBorder(new EmptyBorder(50, 0, 0, 0));
        return title;
    }
    public void createMenu() {
        JLabel title = createTitle("Isle Be Better");
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JButton optionButton = createMenuButton("OPTIONS");
        JPopupMenu popupMenu = createDropdownMenu(optionButton);

        optionButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenu.show(optionButton, 0, optionButton.getHeight());
            }
        });

        centerPanel.add(optionButton);

        setScreen(centerPanel);
        frame.add(title, BorderLayout.NORTH);

    }

    private JMenuItem createStyledItem(String text, Runnable action) {
        JMenuItem item = new JMenuItem(text);
        item.setPreferredSize(new Dimension(180, 40));
        item.setBackground(DARK_BG);
        item.setForeground(Color.WHITE);
        item.setFont(new Font("SansSerif", Font.PLAIN, 13));
        item.setBorder(new EmptyBorder(0, 15, 0, 0));

        item.addActionListener(e -> {
            if(text.contains("Quit")) System.exit(0);
            else JOptionPane.showMessageDialog(frame, text + " Selected");

            if (action != null) {
                action.run();
            }
        });

        return item;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(ACCENT_CYAN);
        button.setBackground(TOOLBAR_BG);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(HOVER_COLOR); }
            public void mouseExited(MouseEvent e) { button.setBackground(TOOLBAR_BG); }
        });
        return button;
    }

    private JPopupMenu createDropdownMenu(JButton owner) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(new LineBorder(Color.BLACK));
        menu.setBackground(DARK_BG);

        menu.add(createStyledItem("Reaction Speed", this::startGameOne));
        menu.add(createStyledItem("Memory Game", this::startGameTwo));
        menu.add(createStyledItem("Typing Game", this::startGameThree));
        menu.addSeparator();
        menu.add(createStyledItem("Quit Program", () -> System.exit(0)));
        return menu;
    }

    private void startGameOne() {
        Square square = new Square();
        setScreen(square.getGamePanel());
    }

    private void startGameTwo() {
        MemoryPlus mem = new MemoryPlus(2, 1);
        mem.createGamePanel();
        setScreen(mem.getGamePanel());
    }

    private void startGameThree() {
        
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