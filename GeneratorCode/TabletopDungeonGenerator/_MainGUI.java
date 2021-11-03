import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class _MainGUI {

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Project Stygia Dungeon Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        frame.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        
        JPanel inputPanel = new JPanel();
        inputPanel.setSize(200,400);
        JPanel displayPanel = new JPanel();
        displayPanel.setSize(200,400);
        
        frame.add(inputPanel);
        frame.add(displayPanel);
        
        JLabel label = new JLabel("Hello");
        inputPanel.add(label);

        displayPanel.add(new JLabel("Goodbye"));
        
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        
        createAndShowGUI();
        
    }
    
}
