// Author: Jonas Rumpf

package views;

import i18n.LanguageManager;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    public StartPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        JLabel label = new JLabel(LanguageManager.get("welcome"));
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        add(label);
    }
}

