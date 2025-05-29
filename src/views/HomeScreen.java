// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {
    public HomeScreen(MainFrame frame) {
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
        ModernButton startBtn = new ModernButton("Start!");
        startBtn.setPreferredSize(new Dimension(200, 50));
        startBtn.addActionListener(e -> frame.showApp());
        add(startBtn, gbc);

        // 2. Einstellungen
        gbc.gridy = 2;
        ModernButton settingsBtn = new ModernButton("Einstellungen");
        settingsBtn.setPreferredSize(new Dimension(200, 50));
        settingsBtn.addActionListener(e -> {
            frame.showApp();
            frame.getContentPanel().showSettingsScreen();
        });
        add(settingsBtn, gbc);  

        // 3. Beenden
        gbc.gridy = 3;
        ModernButton exitBtn = new ModernButton("Beenden");
        exitBtn.setPreferredSize(new Dimension(200, 50));
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn, gbc);
    }
}
