package Game2;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public GameWindow(MemoryPlus memory) {
        // Set up the frame
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);  // Set an initial size for the window
        
        // Use BorderLayout to center the gamePanel
        setLayout(new BorderLayout());
        add(memory.getGamePanel(), BorderLayout.CENTER);  // Add the game panel to the center of the window

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Make the window visible
        setVisible(true);
    }
}