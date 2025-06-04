// Author: Jonas Rumpf

package views;

import javax.swing.*;

import controller.WindowController;

import java.awt.*;
import i18n.LanguageManager;

public class HomeScreen extends JPanel {
    private WindowController windowController;
    public HomeScreen(WindowController controller) {
        this.windowController = controller;

        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel titleLabel = new JLabel("VocalHero");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        // 1. Start!
        gbc.gridy = 1;
        ModernButton startBtn = new ModernButton(LanguageManager.get("home.start"));
        startBtn.setPreferredSize(new Dimension(200, 50));
        startBtn.addActionListener(e -> windowController.showApp());
        add(startBtn, gbc);

        // 2. Einstellungen
        gbc.gridy = 2;
        ModernButton settingsBtn = new ModernButton(LanguageManager.get("home.settings"));
        settingsBtn.setPreferredSize(new Dimension(200, 50));
        settingsBtn.addActionListener(e -> {
            windowController.showApp();
            windowController.showSettingsScreen();
        });
        add(settingsBtn, gbc);  

        // 3. Beenden
        gbc.gridy = 3;
        ModernButton exitBtn = new ModernButton(LanguageManager.get("home.exit"));
        exitBtn.setPreferredSize(new Dimension(200, 50));
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn, gbc);
    }
}
