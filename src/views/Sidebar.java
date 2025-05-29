// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {
    public Sidebar(ContentPanel contentPanel, MainFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        add(new ModernButton("Tutorials"));
        add(new ExercisesMenu(contentPanel));
        add(new ModernButton("Fortschritt"));
        add(Box.createVerticalGlue());

        ModernButton backToMenu = new ModernButton("⟵ Hauptmenü");
        backToMenu.addActionListener(e -> frame.showHome());
        add(backToMenu);
    }
}




