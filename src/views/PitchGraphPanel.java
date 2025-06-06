// Author: Jonas Rumpf

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

    // Toleranz in Cent, ab wann eingerastet wird
    private static final double SNAP_TOLERANCE = 15.0;

    // Hilfslinien, auf die eingerastet werden kann
    private static final int[] SNAP_LINES = {-400, -300, -200, -100, 0, 100, 200, 300, 400};

    public PitchGraphPanel() {
        setBackground(new Color(80, 80, 80));
    }

    /** Diese Methode wird von der Audio-Verarbeitung aufgerufen (liefert Cent-Abweichung) */
    public void addPitchValue(double centOffset) {
        // 1. Clipping: Begrenze auf sichtbaren Bereich
        centOffset = Math.max(-MAX_VISIBLE_CENTS, Math.min(MAX_VISIBLE_CENTS, centOffset));

        // 2. (Optional) Glättung: Moving Average über die letzten 3 Werte
        if (pitchData.size() >= 2) {
            double prev1 = pitchData.getLast();
            double prev2 = pitchData.size() >= 2 ? pitchData.get(pitchData.size() - 2) : prev1;
            centOffset = (centOffset + prev1 + prev2) / 3.0;
        }

        // 3. Snap auf Hilfslinien, falls nahe genug
        for (int snap : SNAP_LINES) {
            if (Math.abs(centOffset - snap) < SNAP_TOLERANCE) {
                centOffset = snap;
                break;
            }
        }

        pitchData.addLast(centOffset);
        if (pitchData.size() > MAX_POINTS) {
            pitchData.removeFirst();
        }
        repaint();
        // Benachrichtige Listener, dass ein Pitch erkannt wurde
        if (pitchActivityListener != null) {
            pitchActivityListener.onPitchActivity();
        }
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

        // Blauer Verlauf mit durchgehend runder Kurve (Cubic Bézier)
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2f));
        int startX = centerX - pitchData.size() * SCROLL_SPEED;
        int n = pitchData.size();

        if (n > 2) {
            Path2D path = new Path2D.Double();
            double x0 = startX;
            double y0 = centToY(pitchData.get(0));
            path.moveTo(x0, y0);

            for (int i = 1; i < n - 1; i++) {
                int x1 = startX + i * SCROLL_SPEED;
                int x2 = startX + (i + 1) * SCROLL_SPEED;
                double y1 = centToY(pitchData.get(i));
                double y2 = centToY(pitchData.get(i + 1));

                // Kontrolle: Mittelwert der Nachbarpunkte für die Kontrolle
                double xc = (x0 + x2) / 2.0;
                double yc = (y0 + y2) / 2.0;

                path.curveTo(x0, y0, x1, y1, xc, yc);

                x0 = xc;
                y0 = yc;
            }
            // Letzten Punkt verbinden
            int xLast = startX + (n - 1) * SCROLL_SPEED;
            double yLast = centToY(pitchData.get(n - 1));
            path.lineTo(xLast, yLast);

            g2.draw(path);
        } else if (n == 2) {
            // Nur zwei Punkte: einfache Linie
            int x1 = startX;
            int x2 = startX + SCROLL_SPEED;
            double y1 = centToY(pitchData.get(0));
            double y2 = centToY(pitchData.get(1));
            g2.drawLine(x1, (int) y1, x2, (int) y2);
        }

        // Blauer Punkt in der Mitte
        if (!pitchData.isEmpty()) {
            double y = centToY(pitchData.getLast());
            g2.setColor(Color.BLUE);
            g2.fillOval(centerX - 5, (int) y - 5, 10, 10);
        }
    }

    private PitchActivityListener pitchActivityListener;

    public void setPitchActivityListener(PitchActivityListener listener) {
        this.pitchActivityListener = listener;
    }

    public interface PitchActivityListener {
        void onPitchActivity();
    }
}
