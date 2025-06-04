// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import model.Mode;

public class LevelSelectionPanel extends JPanel {
    private WindowController windowController;
    public LevelSelectionPanel(WindowController controller, Mode mode) {
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
                windowController.showLevelScreen(mode, levelNumber);
            });
            
            add(levelBtn);
        }

        // TODO: add function to create a LevelInfo Object for each level
        // windowController.startTrainingSession(LevelIno)
        // The LevelInfo should contain the Mode and the Difficulty 
        // (and any other necessary parameters e.g. data needed to track progress)
    }
}


