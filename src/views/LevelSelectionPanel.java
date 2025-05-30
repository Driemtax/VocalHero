// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

public class LevelSelectionPanel extends JPanel {
    public LevelSelectionPanel(String category) {
        setLayout(new GridLayout(3, 4, 20, 20));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        for (int i = 1; i <= 10; i++) {
            JButton levelBtn = new JButton("Level " + i);
            levelBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
            levelBtn.setFocusPainted(false);
            levelBtn.setBackground(new Color(60, 60, 60));
            levelBtn.setForeground(i <= 2 ? Color.GREEN : Color.RED);
            levelBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            levelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Add click action to show LevelScreen
            levelBtn.addActionListener(e -> {
                Container parent = getParent();
                while (parent != null && !(parent instanceof ContentPanel)) {
                    parent = parent.getParent();
                }
                if (parent instanceof ContentPanel) {
                    ((ContentPanel) parent).showLevelScreen();
                }
            });
            
            add(levelBtn);
        }
    }
}


