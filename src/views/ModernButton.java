// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private final Color ENABLED_BACKGROUND = new Color(40, 40, 40);
    private final Color HOVER_BACKGROUND = new Color(60, 60, 60);
    private final Color ENABLED_FOREGROUND = Color.WHITE;


    public ModernButton(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setForeground(ENABLED_FOREGROUND);
        setBackground(ENABLED_BACKGROUND);
        setFocusPainted(false);
        setBorderPainted(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(160, 40));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_BACKGROUND);
            }
            public void mouseExited(MouseEvent e) {
                setBackground(ENABLED_BACKGROUND);
            }
        });
    }
    public void highlight(Color color) {
        setForeground(color);
    }
}

