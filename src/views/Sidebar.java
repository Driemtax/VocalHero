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

        tutorialsButton.addActionListener(e -> controller.showTutorialsScreen());
        exercisesButton.addActionListener(e -> controller.showCategoryScreen());
        progressButton.addActionListener(e -> controller.showProgressScreen());

        add(tutorialsButton);
        add(exercisesButton);
        add(progressButton);
        add(Box.createVerticalGlue());

        ModernButton backToMenu = new ModernButton("⟵ " + LanguageManager.get("sidebar.menu"));
        backToMenu.addActionListener(e -> controller.showHome());
        add(backToMenu);
    }
}




