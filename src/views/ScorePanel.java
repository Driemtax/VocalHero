package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ScorePanel extends JPanel {
    private Image clef;

    public ScorePanel() {
        setBackground(new Color(80, 80, 80));
        clef = new ImageIcon(getClass().getResource("/assets/clef.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Weißer Hintergrund für den Notenbereich
        int scoreWidth = w - 160; // 80px Rand links und rechts
        int scoreHeight = 160;    // Höhe des Notenbereichs
        int scoreX = 80;
        int scoreY = h/2 - scoreHeight/2;
        
        g2.setColor(Color.WHITE);
        g2.fillRect(scoreX, scoreY, scoreWidth, scoreHeight);

        // Notenlinien
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        
        int lineSpacing = 20;
        int startY = h/2 - 2 * lineSpacing;
        for (int i = 0; i < 5; i++) {
            int y = startY + i * lineSpacing;
            g2.drawLine(100, y, w - 100, y);
        }

        // Violinschlüssel - angepasste Position
        int clefWidth = 50;
        int clefHeight = 100;
        int clefX = 100;  // Jetzt direkt am Anfang der Notenlinien
        int clefY = startY;  // Position so angepasst, dass der Schlüssel auf der 2. Linie sitzt
        g2.drawImage(clef, clefX, clefY, clefWidth, clefHeight, this);

        // Beispiel-Noten zeichnen
        int C4 = startY + 5*lineSpacing;       // erste Hilfslinie unter System
        int D4 = C4 - lineSpacing/2;           // Zwischen C4 und E4
        int E4 = C4 - lineSpacing;             // Erste Linie von unten
        int B3 = C4 + lineSpacing/2;           // Unter C4

        drawNote(g2, 200, C4);     // C4 (erste Hilfslinie unten)
        drawNote(g2, 300, D4);     // D4 (zwischen C4 und E4)
        drawNote(g2, 400, E4);     // E4 (erste Linie)
        drawNote(g2, 500, B3);     // B3 (zwischen C4 und A3)
    }

    private void drawNote(Graphics2D g2, int x, int y) {
        // Hilfslinien zeichnen wenn nötig
        drawHelpLines(g2, x, y);

        // Notenkopf
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        
        // Notenhals
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x + 4, y, x + 4, y - 40);
    }

    private void drawHelpLines(Graphics2D g2, int x, int y) {
        int lineSpacing = 20;
        int startY = getHeight()/2 - 2 * lineSpacing;
        int endY = startY + 4 * lineSpacing;
        
        // Wenn die Note über oder unter dem System liegt
        if (y < startY || y > endY) {
            g2.setStroke(new BasicStroke(1f));
            
            // Hilfslinien über dem System
            if (y < startY) {
                int helpLineY = startY;
                while (helpLineY > y - lineSpacing/2) {
                    helpLineY -= lineSpacing;
                    g2.drawLine(x - 10, helpLineY, x + 10, helpLineY);
                }
            }
            
            // Hilfslinien unter dem System
            if (y > endY) {
                int helpLineY = endY;
                while (helpLineY < y + lineSpacing/2) {
                    helpLineY += lineSpacing;
                    g2.drawLine(x - 10, helpLineY, x + 10, helpLineY);
                }
            }
        }
    }
}

