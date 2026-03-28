package GameUI;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import Game1.*;
public class Display {
    private JFrame frame;

    public Display() {
        this.frame = new JFrame("Display");
        this.frame.setSize(800, 600);
        this.frame.getContentPane().setBackground(new Color(60, 63, 65));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createMenu() {

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(45, 45, 45));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK)); // Bottom line
        menuBar.setPreferredSize(new Dimension(0, 50));
        JMenu menu = new JMenu("Menu");
        menu.setForeground(new Color(0, 191, 255));

        JMenuItem m1 = new JMenuItem("Game 1");
        JMenuItem m2 = new JMenuItem("Game 2");
        JMenuItem m3 = new JMenuItem("Game 3");
        JMenuItem m4 = new JMenuItem("Exit");
        JMenuItem m5 = new JMenuItem("Quit");

        JMenuItem[] items = new JMenuItem[]{m1,m2,m3,m4,m5};

        for (JMenuItem item : items) {
            styleItem(item);
            menu.add(item);
        }

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menu);
        menuBar.add(Box.createHorizontalGlue());

        frame.setJMenuBar(menuBar);
    }

    private void styleItem(JMenuItem item) {
        item.setFont(item.getFont().deriveFont(item.getFont().getStyle() | Font.BOLD));
        item.setBackground(new Color(70, 70, 70));
        item.setForeground(Color.WHITE);
        item.setPreferredSize(new Dimension(150, 40));
        item.setBorder(new EmptyBorder(5, 10, 5, 10));
    }
    public void showWindow() {
        createMenu();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void showWindow() {

    }
}