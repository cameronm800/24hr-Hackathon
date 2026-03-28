

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Display {
    private JFrame frame;
    // Modern Color Palette
    private final Color DARK_BG = new Color(45, 45, 45);
    private final Color ACCENT_CYAN = new Color(0, 210, 255);
    private final Color TOOLBAR_BG = new Color(33, 33, 33);
    private final Color HOVER_COLOR = new Color(60, 60, 60);

    public Display() {
        this.frame = new JFrame("Welcome!");
        this.frame.setSize(800, 600);
        this.frame.getContentPane().setBackground(DARK_BG);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createMenu() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(TOOLBAR_BG);
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(new LineBorder(Color.BLACK));
        popupMenu.setBackground(DARK_BG);

        popupMenu.add(createStyledItem("Game 1"));
        popupMenu.add(createStyledItem("Game 2"));
        popupMenu.addSeparator();
        popupMenu.add(createStyledItem("Quit Program"));

        JButton button = new JButton("OPTIONS ▾");
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(ACCENT_CYAN);
        button.setBackground(TOOLBAR_BG);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenu.show(button, 0, button.getHeight());
            }
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(TOOLBAR_BG);
            }
        });

        toolBar.add(button);
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    private JMenuItem createStyledItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setPreferredSize(new Dimension(180, 40));
        item.setBackground(DARK_BG);
        item.setForeground(Color.WHITE);
        item.setFont(new Font("SansSerif", Font.PLAIN, 13));
        item.setBorder(new EmptyBorder(0, 15, 0, 0));

        item.addActionListener(e -> {
            if(text.contains("Quit")) System.exit(0);
            else JOptionPane.showMessageDialog(frame, text + " Selected");
        });

        return item;
    }

    public void showWindow() {
        createMenu();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}