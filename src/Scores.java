package src;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Scores extends JPanel {
    private Font noteFont;
    private Font keyFont;
    private Font stringFont;
    private Note note1;
    private Note note2;

    public Scores() {
        note1 = new Note("\uD834\uDD5F", 2);
        note2 = new Note("\uD834\uDD5F", 0);

        noteFont = new Font("Bravura", Font.PLAIN, 36);
        keyFont = new Font("Bravura", Font.PLAIN, 50);
        stringFont = new Font("Bravura", Font.PLAIN, 10);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw lines for notes
        int lineY = 50;
        int lineX = 10;

        g2d.setFont(keyFont);
        g2d.drawString("\uD834\uDD20", 20, 80);

        g2d.setFont(stringFont);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 100; j++) {
                g2d.drawString("\uD834\uDD16", lineX, lineY);
                lineX += 5;
            }
            lineX = 10;
            lineY += 10;
        }

        g2d.setFont(noteFont);
        // Draw Notes
        drawNote(g2d, note1, 70, 50);
        drawNote(g2d, note2, 320, 50);
    }

    private void drawNote(Graphics2D g2d, Note note, int x, int y) {
        g2d.drawString(note.getSymbol(), x, y + (note.getPosition() * 20));
    }
}
