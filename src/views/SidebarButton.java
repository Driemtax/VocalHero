package views;

import javax.swing.*;
import java.awt.*;

public class SidebarButton extends JButton {
    public SidebarButton(String text) {
        super(text);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(160, 40));
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setBackground(Color.GRAY);
    }
}

