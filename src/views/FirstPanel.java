//Author:David Herrmann

package views;

import javax.swing.*;
import java.awt.*;
import controller.WindowController;
import i18n.LanguageManager;

public class FirstPanel extends JPanel {

    private JTextArea descriptionLabel;
    private ModernButton settingsButton;
    private ModernButton startButton;
    private WindowController windowController;

    public FirstPanel(WindowController windowController) {
        this.windowController = windowController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 50, 50));
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void setupListeners() {
        settingsButton.addActionListener(_1 -> windowController.showSettingsScreen());
        startButton.addActionListener(_1 -> windowController.showHome());
    }

    private void setupLayout() {
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionLabel.setMargin(new Insets(10, 10, 10, 10));
        descriptionLabel.setMaximumSize(new Dimension(900, 400));
        descriptionLabel.setPreferredSize(new Dimension(900, 400));

        add(Box.createVerticalStrut(100));
        add(descriptionLabel);
        add(Box.createVerticalStrut(50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(50, 50, 50));
        buttonPanel.setMaximumSize(new Dimension(600, 200));

        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(startButton);

        add(buttonPanel);
    }

    private void initializeComponents() {
        descriptionLabel = new JTextArea(LanguageManager.get("start.description"));

        settingsButton = new ModernButton(LanguageManager.get("start.settings"));
        startButton = new ModernButton(LanguageManager.get("start.start"));

        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBackground(new Color(50, 50, 50));
        descriptionLabel.setEnabled(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
    }
}
