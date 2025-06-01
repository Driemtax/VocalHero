// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;

public class LevelSelectionPanel extends JPanel {
    private WindowController windowController;
    public LevelSelectionPanel(WindowController controller, String category) {
        this.windowController = controller;

        setLayout(new GridLayout(3, 4, 20, 20));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        for (int i = 1; i <= 10; i++) {
            BigModernButton levelBtn = new BigModernButton("Level " + i);
            levelBtn.setLockStatus(i <= 2); // First two levels unlocked
            
            final int levelNumber = i;
            // Add click action to show LevelScreen
            levelBtn.addActionListener(e -> {
                windowController.showApp();
                windowController.showLevelScreen(category, levelNumber);
            });
            
            add(levelBtn);
        }
    }
}


