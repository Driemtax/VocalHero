// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;

public class Sidebar extends JPanel {
    public Sidebar(ContentPanel contentPanel, WindowController controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        ModernButton tutorialsButton = new ModernButton("Tutorials");
        ModernButton exercisesButton = new ModernButton("Übungen");
        ModernButton progressButton = new ModernButton("Fortschritt");

        exercisesButton.addActionListener(e -> controller.showCategoryScreen());
        progressButton.addActionListener(e -> controller.showProgressScreen());

        add(tutorialsButton);
        add(exercisesButton);
        add(progressButton);
        add(Box.createVerticalGlue());

        ModernButton backToMenu = new ModernButton("⟵ Hauptmenü");
        backToMenu.addActionListener(e -> controller.showHome());
        add(backToMenu);
    }
}




