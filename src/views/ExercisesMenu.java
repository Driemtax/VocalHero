package views;

import javax.swing.*;
import java.awt.*;

public class ExercisesMenu extends JPanel {
    private JPanel subMenu;

    public ExercisesMenu(ContentPanel contentPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));

        ModernButton mainButton = new ModernButton("Übungen");
        mainButton.addActionListener(e -> toggleSubMenu());
        add(mainButton);

        subMenu = new JPanel();
        subMenu.setLayout(new BoxLayout(subMenu, BoxLayout.Y_AXIS));
        subMenu.setBackground(new Color(30, 30, 30));
        subMenu.setVisible(false);

        String[] options = {"Einzeltöne", "Intervalle", "Melodien"};
        for (String option : options) {
            ModernButton btn = new ModernButton(option);
            btn.addActionListener(ev -> contentPanel.showLevelSelection(option));
            subMenu.add(btn);
        }

        add(subMenu);
    }

    private void toggleSubMenu() {
        subMenu.setVisible(!subMenu.isVisible());
        revalidate();
        repaint();
    }
}



