package views;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private Image clef;

    public ScorePanel() {
        setBackground(new Color(80, 80, 80));
        clef = new ImageIcon(getClass().getResource("/assets/clef.png")).getImage(); // siehe Hinweis unten
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));

        // Notenlinien (Mitte vertikal)
        int lineSpacing = 20;
        int startY = h / 2 - 2 * lineSpacing;
        for (int i = 0; i < 5; i++) {
            int y = startY + i * lineSpacing;
            g2.drawLine(100, y, w - 100, y);
        }

        // Violinschlüssel links einblenden (Bild)
        g2.drawImage(clef, 100 - 50, startY - 30, 50, 100, this);
    }
}

