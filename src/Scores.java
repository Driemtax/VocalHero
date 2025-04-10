package src;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Scores extends JPanel {
    private String QUARTER_NOTE = "\uD834\uDD5F";
    private String KEY_SYMBOL = "\uD834\uDD1E";
    private String STRING_SYMBOL = "\uD834\uDD16";
    private String BAR_SYMBOL = "\uD834\uDD00";


    private Font noteFont;
    private Font keyFont;
    private Font stringFont;
    private Font barFont;
    private Note note1;
    private Note note2;

    public Scores() {
        note1 = new Note(NoteType.QUARTER, QUARTER_NOTE, 2);
        note2 = new Note(NoteType.QUARTER, QUARTER_NOTE, 0);

        noteFont = new Font("Bravura", Font.PLAIN, 36);
        keyFont = new Font("Bravura", Font.PLAIN, 50);
        stringFont = new Font("Bravura", Font.PLAIN, 10);
        barFont = new Font("Bravura", Font.PLAIN, 42);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        drawBar(g2d);

        g2d.setFont(noteFont);
        // Draw Notes
        drawNote(g2d, note1, 90, 50);
        drawNote(g2d, note2, 320, 50);
    }

    private void drawNote(Graphics2D g2d, Note note, int x, int y) {
        g2d.drawString(note.getSymbol(), x, y + (note.getPosition() * 20));
    }

    private void drawBar(Graphics2D g2d){
        // Draw lines for notes
        int lineY = 50;
        int lineX = 10;
        int barPosX = lineX;

        g2d.setFont(barFont);
        g2d.drawString(BAR_SYMBOL, lineX - 2 , lineY + 36);

        g2d.setFont(keyFont);
        g2d.drawString(KEY_SYMBOL, 20, 80);

        g2d.setFont(stringFont);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 100; j++) {
                g2d.drawString(STRING_SYMBOL, lineX, lineY);
                lineX += 5;
            }
            barPosX = barPosX + (lineX - barPosX);
            lineX = 10;
            lineY += 10;
        }

        g2d.setFont(barFont);
        g2d.drawString(BAR_SYMBOL, barPosX + 2 , 86);
    }
}
