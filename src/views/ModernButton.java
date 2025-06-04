// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private final Color ENABLED_BACKGROUND = new Color(40, 40, 40);
    private final Color HOVER_BACKGROUND = new Color(60, 60, 60);
    private final Color DISABLED_BACKGROUND = new Color(100, 100, 100);
    private final Color ENABLED_FOREGROUND = Color.WHITE;
    private final Color DISABLED_FOREGROUND = Color.LIGHT_GRAY;


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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackground(ENABLED_BACKGROUND);
            setForeground(ENABLED_FOREGROUND);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setBackground(DISABLED_BACKGROUND);
            setForeground(DISABLED_FOREGROUND);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        repaint();
    }

    public void highlight(Color color) {
        setForeground(color);
    }
}

