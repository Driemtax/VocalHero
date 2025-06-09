// Author: Jonas Rumpf

package utils;

import java.awt.*;
import java.awt.geom.*;

public class ScoreUtils {
    
    public void drawStaffLines(Graphics2D g2, int width, int startY, int lineSpacing) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        
        for (int i = 0; i < 5; i++) {
            int y = startY + i * lineSpacing;
            g2.drawLine(100, y, width - 100, y);
        }
    }

    public int calculateYPosition(int startY, int lineSpacing, int pitch, boolean useBassClef) {
        if (useBassClef) {
            // Linie 2 (D3, MIDI 50) als Referenz, aber um einen halben Linienabstand nach oben verschieben
            int referenceMidi = 50;
            int referenceLine = startY + 2 * lineSpacing;
            double steps = pitch - referenceMidi;
            double y = referenceLine - steps * (lineSpacing / 2.0) - (lineSpacing / 2.0);
            return (int)Math.round(y);
        } else {
            // Violinschlüssel wie gehabt
            int referenceMidi = 60;
            int referenceLine = startY + 5 * lineSpacing;
            double steps = pitch - referenceMidi;
            double y = referenceLine - steps * (lineSpacing / 2.0);
            return (int)Math.round(y);
        }
    }

    public int calculateYPositionWhiteKeys(int startY, int lineSpacing, int midi, boolean useBassClef) {
        // C = 0, D = 1, E = 2, F = 3, G = 4, A = 5, B = 6
        int[] whiteMidi = {0, 2, 4, 5, 7, 9, 11};
        int base = midi - 12;
        int octave = base / 12;
        int noteInOctave = base % 12;
        int staffIndex = 0;
        for (int i = 0; i < whiteMidi.length; i++) {
            if (noteInOctave <= whiteMidi[i]) {
                staffIndex = i;
                break;
            }
        }
        // Position im System: C0 ist ganz unten, jede weiße Taste = 1 Position höher
        int position = octave * 7 + staffIndex;
        // Referenz: Bassschlüssel D3 (MIDI 50) = positionX
        int refMidi = useBassClef ? 50 : 60;
        int refBase = refMidi - 12;
        int refOctave = refBase / 12;
        int refNoteInOctave = refBase % 12;
        int refStaffIndex = 0;
        for (int i = 0; i < whiteMidi.length; i++) {
            if (refNoteInOctave <= whiteMidi[i]) {
                refStaffIndex = i;
                break;
            }
        }
        int refPosition = refOctave * 7 + refStaffIndex;
        int refLine = useBassClef ? (startY + 2 * lineSpacing) : (startY + 5 * lineSpacing);
        int delta = position - refPosition;
        double y = refLine - delta * (lineSpacing / 2.0);
        return (int)Math.round(y);
    }

    public void drawNote(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        drawHelpLines(g2, x, y, startY, lineSpacing);

        // Note head
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        
        // Note stem
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x + 4, y, x + 4, y - 40);
    }

    // For now we draw a big nothing, but we could add a picture here
    public void drawRest(Graphics2D g2, int x, int y, int panelHeight) {
        int width = 16;
        int height = 8;
        int restY = y + 20; // slightly below die lower line

        g2.setColor(Color.BLACK);
        g2.fillRect(x - width / 2, restY, width, height);
    }

    private void drawHelpLines(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        int endY = startY + 4 * lineSpacing;

        if (y < startY || y > endY) {
            g2.setStroke(new BasicStroke(1f));

            // Help lines above staff
            if (y < startY) {
                int helpLineY = startY;
                while (helpLineY > y - lineSpacing/2) {
                    helpLineY -= lineSpacing;
                    g2.drawLine(x - 10, helpLineY, x + 10, helpLineY);
                }
            }

            // Help lines below staff
            if (y > endY) {
                int helpLineY = endY;
                while (helpLineY < y + lineSpacing/2) {
                    helpLineY += lineSpacing;
                    g2.drawLine(x - 10, helpLineY, x + 10, helpLineY);
                }
            }
        }
    }

    // === NOTENARTEN ===

    /** Ganze Note (ohne Hals) */
    public void drawWholeNote(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        drawHelpLines(g2, x, y, startY, lineSpacing);
        g2.setColor(new Color(255, 255, 255, 0));
        g2.fill(new Ellipse2D.Double(x - 8, y - 6, 16, 12));
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new Ellipse2D.Double(x - 8, y - 6, 16, 12));
    }

    /** Halbe Note (leerer Kopf, mit Hals) */
    public void drawHalfNote(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        drawHelpLines(g2, x, y, startY, lineSpacing);
        g2.setColor(new Color(255, 255, 255, 0));
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        g2.drawLine(x + 7, y, x + 7, y - 40);
    }

    /** Viertelnote (ausgefüllt, mit Hals) */
    public void drawQuarterNote(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        drawHelpLines(g2, x, y, startY, lineSpacing);
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x + 4, y, x + 4, y - 40);
    }

    /** Achtelnote (ausgefüllt, Hals, Fähnchen) */
    public void drawEighthNote(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        drawHelpLines(g2, x, y, startY, lineSpacing);
        // Notenkopf und Hals
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        g2.setStroke(new BasicStroke(2f));
        int stemX = x + 4;
        int stemTopY = y - 40;
        g2.drawLine(stemX, y, stemX, stemTopY);

        // Fähnchen (Flagge) – geschwungene Linie
        g2.setStroke(new BasicStroke(2f));
        int flagStartX = stemX;
        int flagStartY = stemTopY;
        int flagCtrlX = stemX + 16;
        int flagCtrlY = stemTopY + 10;
        int flagEndX = stemX + 12;
        int flagEndY = stemTopY + 24;
        QuadCurve2D flag = new QuadCurve2D.Float(flagStartX, flagStartY, flagCtrlX, flagCtrlY, flagEndX, flagEndY);
        g2.draw(flag);
    }

    /** Sechzehntelnote (ausgefüllt, Hals, zwei Fähnchen) */
    public void drawSixteenthNote(Graphics2D g2, int x, int y, int startY, int lineSpacing) {
        drawHelpLines(g2, x, y, startY, lineSpacing);
        // Notenkopf und Hals
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - 6, y - 6, 12, 12));
        g2.setStroke(new BasicStroke(2f));
        int stemX = x + 4;
        int stemTopY = y - 40;
        g2.drawLine(stemX, y, stemX, stemTopY);

        // Erstes Fähnchen
        int flag1CtrlX = stemX + 16;
        int flag1CtrlY = stemTopY + 10;
        int flag1EndX = stemX + 12;
        int flag1EndY = stemTopY + 24;
        QuadCurve2D flag1 = new QuadCurve2D.Float(stemX, stemTopY, flag1CtrlX, flag1CtrlY, flag1EndX, flag1EndY);
        g2.draw(flag1);

        // Zweites Fähnchen (etwas tiefer)
        int flag2StartY = stemTopY + 8;
        int flag2CtrlY = stemTopY + 18;
        int flag2EndY = stemTopY + 32;
        QuadCurve2D flag2 = new QuadCurve2D.Float(stemX, flag2StartY, flag1CtrlX, flag2CtrlY, flag1EndX, flag2EndY);
        g2.draw(flag2);
    }

    /** Triole (kleine 3 über der Note) */
    public void drawTriplet(Graphics2D g2, int x, int y) {
        g2.setFont(new Font("Serif", Font.PLAIN, 14));
        g2.drawString("3", x - 4, y - 45);
    }

    // === PAUSENARTEN ===

    /** Ganze Pause */
    public void drawWholeRest(Graphics2D g2, int x, int y) {
        g2.setColor(Color.BLACK);
        g2.fillRect(x - 8, y + 8, 16, 6);
    }

    /** Halbe Pause */
    public void drawHalfRest(Graphics2D g2, int x, int y) {
        g2.setColor(Color.BLACK);
        g2.fillRect(x - 8, y, 16, 6);
    }

    /** Viertelpause */
    public void drawQuarterRest(Graphics2D g2, int x, int y) {
        g2.setColor(Color.BLACK);
        int[] xs = {x, x + 4, x - 2, x + 2, x - 2};
        int[] ys = {y, y + 10, y + 18, y + 26, y + 34};
        for (int i = 0; i < xs.length - 1; i++) {
            g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
        }
    }

    /** Achtelpause */
    public void drawEighthRest(Graphics2D g2, int x, int y) {
        g2.setColor(Color.BLACK);
        g2.drawLine(x, y, x + 4, y + 10);
        g2.drawLine(x + 4, y + 10, x, y + 20);
        g2.fillOval(x + 2, y + 18, 6, 6);
    }

    /** Sechzehntelpause */
    public void drawSixteenthRest(Graphics2D g2, int x, int y) {
        drawEighthRest(g2, x, y);
        g2.drawLine(x + 2, y + 12, x + 6, y + 22);
        g2.fillOval(x + 4, y + 20, 5, 5);
    }

    // === DYNAMIK & MUSIKALISCHE SYMBOLE ===

    /** "p" für piano (leise) */
    public void drawPiano(Graphics2D g2, int x, int y) {
        g2.setFont(new Font("Serif", Font.ITALIC, 24));
        g2.drawString("p", x, y);
    }

    /** "f" für forte (laut) */
    public void drawForte(Graphics2D g2, int x, int y) {
        g2.setFont(new Font("Serif", Font.ITALIC, 24));
        g2.drawString("f", x, y);
    }

    /** crescendo-Zeichen (spitzes "<") */
    public void drawCrescendo(Graphics2D g2, int x, int y, int width) {
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x, y, x + width, y - 10);
        g2.drawLine(x, y, x + width, y + 10);
    }

    /** decrescendo-Zeichen (spitzes ">") */
    public void drawDecrescendo(Graphics2D g2, int x, int y, int width) {
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x, y - 10, x + width, y);
        g2.drawLine(x, y + 10, x + width, y);
    }

    public static class NotePosition {
        public final int staffIndex; // 0 = C, 1 = D, ..., 6 = B (pro Oktave)
        public final int octave;
        public final boolean isSharp;
        public final boolean isFlat;
        public final int midi; // original midi

        public NotePosition(int staffIndex, int octave, boolean isSharp, boolean isFlat, int midi) {
            this.staffIndex = staffIndex;
            this.octave = octave;
            this.isSharp = isSharp;
            this.isFlat = isFlat;
            this.midi = midi;
        }
    }

    /** Gibt die Notenposition (Linie/Zwischenraum) und Vorzeichen für eine MIDI-Nummer zurück */
    public static NotePosition getNotePosition(int midi) {
        // C = 0, D = 1, E = 2, F = 3, G = 4, A = 5, B = 6
        int[] whiteMidi = {0, 2, 4, 5, 7, 9, 11}; // C, D, E, F, G, A, B
        int base = midi - 12; // MIDI 12 = C0
        int octave = base / 12;
        int noteInOctave = base % 12;
        // Finde das nächstniedrigere weiße Tasten-Äquivalent
        int staffIndex = 0;
        for (int i = whiteMidi.length - 1; i >= 0; i--) {
            if (noteInOctave >= whiteMidi[i]) {
                staffIndex = i;
                break;
            }
        }
        boolean isSharp = false;
        boolean isFlat = false;
        if (noteInOctave != whiteMidi[staffIndex]) {
            isFlat = true; // Jetzt wird b angezeigt
        }
        return new NotePosition(staffIndex, octave, isSharp, isFlat, midi);
    }
}
