//Author:David Herrmann, Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import i18n.LanguageManager;

public class FirstPanel extends JPanel {

    private ModernButton settingsButton;
    private ModernButton startButton;
    private WindowController windowController;

    public FirstPanel(WindowController windowController) {
        this.windowController = windowController;

        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Titel
        JLabel titleLabel = new JLabel("VocalHero", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        // Beschreibung
        gbc.gridy++;
        gbc.insets = new Insets(10, 100, 10, 100);
        JTextArea descriptionLabel = new JTextArea(LanguageManager.get("start.description"));
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBackground(new Color(50, 50, 50));
        descriptionLabel.setEditable(false);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(descriptionLabel, gbc);

        // Button Panel
        gbc.gridy++;
        gbc.insets = new Insets(40, 0, 0, 0);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(50, 50, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        settingsButton = new ModernButton(LanguageManager.get("start.settings"));
        settingsButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        settingsButton.setPreferredSize(new Dimension(220, 60));
        settingsButton.setMaximumSize(new Dimension(220, 60));
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new ModernButton(LanguageManager.get("start.start"));
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        startButton.setPreferredSize(new Dimension(220, 60));
        startButton.setMaximumSize(new Dimension(220, 60));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createHorizontalStrut(40));
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel, gbc);

        // Listener
        settingsButton.addActionListener(_1 -> windowController.showSettingsScreen());
        startButton.addActionListener(_1 -> windowController.showHome());
    }
}
