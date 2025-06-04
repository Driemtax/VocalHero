// Author: Jonas Rumpf

package views;

import javax.swing.*;

import model.Mode;

import java.awt.*;

public class CategoryScreen extends JPanel {
    
    public CategoryScreen(ContentPanel contentPanel) {
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));
        
        // Container für die Kategorien
        JPanel categoryContainer = new JPanel();
        categoryContainer.setLayout(new GridLayout(1, 3, 20, 20));
        categoryContainer.setBackground(new Color(50, 50, 50));
        categoryContainer.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        Mode[] options = {
            Mode.NOTE,
            Mode.INTERVAL,
            Mode.MELODY
        };
        
        for (Mode mode : options) {
            BigModernButton categoryBtn = new BigModernButton(mode.getName());
            
            // Add click action to show LevelSelection
            categoryBtn.addActionListener(e -> {
                contentPanel.showLevelSelection(mode);
            });
            
            categoryContainer.add(categoryBtn);
        }
        
        // Titel hinzufügen
        JLabel titleLabel = new JLabel("Übungen");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Layout mit GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(categoryContainer, gbc);
    }
}