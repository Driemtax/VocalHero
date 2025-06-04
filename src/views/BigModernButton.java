// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

public class BigModernButton extends JButton {
    
    public BigModernButton(String text) {
        super(text);
        setupStyle();
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
        setForeground(unlocked ? Color.GREEN : Color.RED);
    }
}