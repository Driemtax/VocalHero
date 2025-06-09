// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import i18n.LanguageManager;

public class Sidebar extends JPanel {
    public Sidebar(ContentPanel contentPanel, WindowController controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        ModernButton tutorialsButton = new ModernButton(LanguageManager.get("sidebar.tutorials"));
        ModernButton exercisesButton = new ModernButton(LanguageManager.get("sidebar.exercises"));
        ModernButton progressButton = new ModernButton(LanguageManager.get("sidebar.progress"));
        ModernButton recordingsButton = new ModernButton(LanguageManager.get("sidebar.recordings"));

        tutorialsButton.addActionListener(e -> controller.showTutorialsScreen());
        exercisesButton.addActionListener(e -> controller.showCategoryScreen());
        progressButton.addActionListener(e -> controller.showProgressScreen());
        recordingsButton.addActionListener(e -> controller.showRecordingsScreen());

        add(tutorialsButton);
        add(exercisesButton);
        add(progressButton);
        add(recordingsButton);
        add(Box.createVerticalGlue());

        ModernButton backToMenu = new ModernButton("⬅️ " + LanguageManager.get("sidebar.menu"));
        backToMenu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        backToMenu.addActionListener(e -> controller.showHome());
        add(backToMenu);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled); // controls this Jpanel

        // Iterate over all components in this JPanel and control them aswell
        for (Component comp : getComponents()) {
            comp.setEnabled(enabled);
        }
    }
}




