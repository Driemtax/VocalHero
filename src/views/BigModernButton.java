// Author: Jonas Rumpf, David Herrmann

package views;

import javax.swing.*;
import java.awt.*;

public class BigModernButton extends JButton {
    
    public BigModernButton(String text) {
        super(text);
        setupStyle();
    }

    @Override
    protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();

    if (getModel().isPressed()) {
        g2.setColor(getBackground().darker());
    } else {
        g2.setColor(getBackground());
    }
    g2.fillRect(0, 0, getWidth(), getHeight());

    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2.setFont(getFont());
    g2.setColor(getForeground());

    FontMetrics fm = g2.getFontMetrics();
    String text = getText();
    int x = (getWidth() - fm.stringWidth(text)) / 2;
    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
    g2.drawString(text, x, y);

    g2.dispose();
}
    
    private void setupStyle() {
        setFont(new Font("Segoe UI", Font.BOLD, 20));
        setFocusPainted(false);
        setBackground(new Color(60, 60, 60));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set preferred size to match level selection buttons
        setPreferredSize(new Dimension(200, 200));
        
        // Add hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(60, 60, 60));
            }
        });
    }
    
    // Method to set button as completed/unlocked (green) or locked (red)
    public void setLockStatus(boolean unlocked) {
        setEnabled(unlocked);
        setForeground(unlocked ? Color.GREEN : Color.RED);
        repaint();
    }
}