package Game2;
import javax.swing.*;
import java.awt.BorderLayout;

public class GameWindow extends JFrame {


    public GameWindow(MemoryPlus memory) {
        // Set up the frame
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);  // Set an initial size for the window
        
        // Use BorderLayout and center the gamePanel
        setLayout(new BorderLayout());
        add(memory.getGamePanel(), BorderLayout.CENTER);
        
        // Center the window on the screen
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Example: Start the game
        MemoryPlus memory = new MemoryPlus(4, 4);  // Adjust the grid size as necessary
        new GameWindow(memory);
    }
}