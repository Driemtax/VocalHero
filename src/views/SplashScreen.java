// Author: Jonas Rumpf

package views;

import javax.swing.*;

import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen(Runnable onFinish) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridBagLayout());

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/VocalHeroLogo.png"));
        Image scaledImage = logoIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        panel.add(logoLabel);

        add(panel);
        setSize(500, 500);
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

