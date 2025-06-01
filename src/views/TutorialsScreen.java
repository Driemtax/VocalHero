package views;

import javax.swing.*;
import java.awt.*;

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

        JLabel title = new JLabel("Grundlegende Singtechniken");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(230, 230, 230));
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        addTutorialSection("🎶 Atemtechnik",
            "Atme tief in den Bauch ein, nicht nur in die Brust. Eine gute Atemstütze hilft dir, Töne sicher und lang zu halten.", gbc);

        addTutorialSection("🗣️ Haltung",
            "Stehe oder sitze aufrecht. Eine entspannte, aber aufrechte Haltung verbessert deine Stimmkontrolle und Klangqualität.", gbc);

        addTutorialSection("👄 Artikulation",
            "Sprich und singe die Vokale und Konsonanten deutlich aus. Das macht deinen Gesang verständlicher und klarer.", gbc);

        addTutorialSection("🎯 Intonation",
            "Höre genau auf die Töne und versuche, sie möglichst sauber zu treffen. Übe mit einem Klavier oder einer Stimm-App.", gbc);

        addTutorialSection("🔄 Übung macht den Meister",
            "Regelmäßiges Üben ist der Schlüssel. Singe täglich kurze Übungen, um deine Stimme zu trainieren und Fortschritte zu machen.", gbc);
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
