// Author: Jonas Rumpf, David Herrmann

package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controller.WindowController;
import i18n.LanguageManager;
import model.LevelState;
import model.Mode;

public class LevelSelectionPanel extends JPanel {
    private WindowController windowController;
    public LevelSelectionPanel(WindowController controller, Mode mode ,List<LevelState> levelList) {
        this.windowController = controller;

        setLayout(new GridLayout(3, 4, 20, 20));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        for (LevelState level : levelList ) {
            BigModernButton levelBtn = new BigModernButton(LanguageManager.get("level") + " " + level.level());
            levelBtn.setLockStatus(level.isUnlocked()); // First two levels unlocked
            
            final int levelNumber = level.level();
            // Add click action to show LevelScreen
            levelBtn.addActionListener(e -> {
                windowController.showApp();
                windowController.showLevelScreen(mode, levelNumber);
            });
            
            add(levelBtn);
        }

    }
}


