// Author: Jonas Rumpf

package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import model.Mode;;

public class ExercisesMenu extends JPanel {
    private JPopupMenu popupMenu;
    private ModernButton mainButton;

    public ExercisesMenu(ContentPanel contentPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));

        mainButton = new ModernButton("Übungen");
        add(mainButton);

        // Popup-Menü erstellen
        popupMenu = new JPopupMenu();
        popupMenu.setBackground(new Color(40, 40, 40));
        popupMenu.setBorder(new EmptyBorder(5, 0, 5, 0));

        Mode[] options = {
            Mode.NOTE,
            Mode.INTERVAL,
            Mode.MELODY
        };
        for (Mode mode : options) {
            JMenuItem menuItem = createStyledMenuItem(mode.getName());
            menuItem.addActionListener(ev -> contentPanel.showLevelSelection(mode));
            popupMenu.add(menuItem);
        }

        // Mouse Listener für Hover-Effekt
        mainButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                popupMenu.show(mainButton, mainButton.getWidth(), 0);
            }
        });

        // Listener für Popup-Menü, um es zu schließen wenn die Maus es verlässt
        popupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {}
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {}
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {}
        });

        // Mouse Listener für das Popup-Menü
        popupMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!mainButton.getBounds().contains(evt.getPoint())) {
                    popupMenu.setVisible(false);
                }
            }
        });
    }

    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.BOLD, 16));
        item.setForeground(Color.WHITE);
        item.setBackground(new Color(40, 40, 40));
        item.setBorder(new EmptyBorder(8, 15, 8, 15));
        item.setPreferredSize(new Dimension(160, 40));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(60, 60, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(40, 40, 40));
            }
        });
        
        return item;
    }
}



