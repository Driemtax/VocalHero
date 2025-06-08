package views;

import javax.swing.*;
import java.awt.*;
import i18n.LanguageManager;

public class TutorialsScreen extends JPanel {

    public TutorialsScreen() {
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 12, 18, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel(LanguageManager.get("tutorials.title"));
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(230, 230, 230));
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        addTutorialSection("🎶 " + LanguageManager.get("tutorials.breathing.heading"),
            LanguageManager.get("tutorials.breathing.text"), gbc);

        addTutorialSection("🗣️ " + LanguageManager.get("tutorials.posture.heading"),
            LanguageManager.get("tutorials.posture.text"), gbc);

        addTutorialSection("👄 " + LanguageManager.get("tutorials.articulation.heading"),
            LanguageManager.get("tutorials.articulation.text"), gbc);

        addTutorialSection("🎯 " + LanguageManager.get("tutorials.intonation.heading"),
            LanguageManager.get("tutorials.intonation.text"), gbc);

        addTutorialSection("🔄 " + LanguageManager.get("tutorials.practice.heading"),
            LanguageManager.get("tutorials.practice.text"), gbc);
    }

    private void addTutorialSection(String heading, String text, GridBagConstraints gbc) {
        gbc.gridy++;
        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headingLabel.setForeground(new Color(0xF9A826));
        add(headingLabel, gbc);

        gbc.gridy++;
        JLabel textLabel = new JLabel("<html><div style='width:340px;'>" + text + "</div></html>");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textLabel.setForeground(new Color(220, 220, 220));
        add(textLabel, gbc);
    }
}
