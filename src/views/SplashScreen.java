// Author: Jonas Rumpf

package views;

import javax.swing.*;

import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen(Runnable onFinish) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("🎤 VocalHero");
        label.setFont(new Font("Segoe UI", Font.BOLD, 36));
        label.setForeground(new Color(0, 255, 128));
        panel.add(label);

        add(panel);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        Timer timer = new Timer(2000, e -> {
            dispose();
            onFinish.run();
        });
        timer.setRepeats(false);
        timer.start();
    }
}

