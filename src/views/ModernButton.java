package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    public ModernButton(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setBackground(new Color(40, 40, 40));
        setFocusPainted(false);
        setBorderPainted(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(160, 40));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(60, 60, 60));
            }
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(40, 40, 40));
            }
        });
    }
    public void highlight(Color color) {
        setForeground(color);
    }
}

