package views;

import javax.swing.*;
import java.awt.*;

public class LevelSelectionFrame extends JFrame {
    public LevelSelectionFrame(String title) {
        setTitle("Levelauswahl: " + title);
        setSize(800, 600);
        setLayout(new GridLayout(4, 3, 20, 20));
        setLocationRelativeTo(null);

        for (int i = 1; i <= 10; i++) {
            JButton levelBtn = new JButton("Level " + i);
            levelBtn.setFont(new Font("Arial", Font.BOLD, 20));
            if (i <= 2) {
                levelBtn.setForeground(Color.GREEN);
            } else {
                levelBtn.setForeground(Color.RED);
            }
            add(levelBtn);
        }
    }
}

