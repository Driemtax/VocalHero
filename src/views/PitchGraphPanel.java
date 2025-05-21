package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.LinkedList;

public class PitchGraphPanel extends JPanel {
    private final LinkedList<Double> pitchData = new LinkedList<>();

    private static final int MAX_POINTS = 1000; // Datenpunkte im Graph
    private static final int SCROLL_SPEED = 2;  // Pixel pro Frame
    private static final double MAX_VISIBLE_CENTS = 400.0; // ±400 Cent sichtbar

    private String targetNote = "C4"; // Standard-Zielnote
    private double targetFrequency = noteToFrequency(targetNote);

    public PitchGraphPanel() {
        setBackground(new Color(80, 80, 80));
    }

    /** Diese Methode wird von der Audio-Verarbeitung aufgerufen (liefert Cent-Abweichung) */
    public void addPitchValue(double centOffset) {
        pitchData.addLast(centOffset);
        if (pitchData.size() > MAX_POINTS) {
            pitchData.removeFirst();
        }
        repaint();
    }

    /** Umrechnen von Cent-Abweichung (±100) in Pixelposition */
    private double centToY(double centOffset) {
        int h = getHeight();
        double centerY = h / 2.0;
        double pixelsPerCent = h / 2.0 / MAX_VISIBLE_CENTS;
        return centerY - (centOffset * pixelsPerCent);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        int centerY = h / 2;
        int centerX = w / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hilfslinien
        int[] helperLines = {-400, -300, -200, -100, 0, 100, 200, 300, 400};
        for (int cent : helperLines) {
            int y = (int) centToY(cent);
            g2.setColor(cent == 0 ? Color.GREEN : Color.DARK_GRAY);
            g2.setStroke(cent == 0 ?
                new BasicStroke(2f) :
                new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2.drawLine(0, y, w, y);
        }


        // Grüner Balken = Zielton (0 Cent)
        int barHeight = 20;
        g2.setColor(Color.GREEN);
        g2.fillRect(0, centerY - barHeight / 2, w, barHeight);

        // Blauer Verlauf
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2f));
        Path2D path = new Path2D.Double();
        int startX = centerX - pitchData.size() * SCROLL_SPEED;
        int x = startX;

        for (double pitchOffset : pitchData) {
            double y = centToY(pitchOffset);
            if (x == startX) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            x += SCROLL_SPEED;
        }
        g2.draw(path);

        // Blauer Punkt in der Mitte
        if (!pitchData.isEmpty()) {
            double y = centToY(pitchData.getLast());
            g2.setColor(Color.BLUE);
            g2.fillOval(centerX - 5, (int) y - 5, 10, 10);
        }
    }

    /** Berechne Frequenz einer Note (z. B. C4 → 261.63 Hz) */
    public static double noteToFrequency(String noteName) {
        String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        int semitoneOffset = 0;

        String note = noteName.replaceAll("[0-9]", "");
        int octave = Integer.parseInt(noteName.replaceAll("[^0-9]", ""));
        for (int i = 0; i < noteNames.length; i++) {
            if (noteNames[i].equals(note)) {
                semitoneOffset = i;
                break;
            }
        }

        int midi = (octave + 1) * 12 + semitoneOffset;
        return 440.0 * Math.pow(2, (midi - 69) / 12.0);
    }

    public void setTargetNote(String note) {
        this.targetNote = note;
        this.targetFrequency = noteToFrequency(note);
    }

    public double getTargetFrequency() {
        return targetFrequency;
    }

    public String getTargetNote() {
        return targetNote;
    }
}
