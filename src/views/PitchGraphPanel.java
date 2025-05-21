package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.LinkedList;

public class PitchGraphPanel extends JPanel {
    private final LinkedList<Double> pitchData = new LinkedList<>();
    //private final Timer timer;

    private static final int MAX_POINTS = 1000; // Datenpunkte im Graph
    //private static final int UPDATE_INTERVAL = 20; // ms
    private static final int SCROLL_SPEED = 2; // Pixel pro Frame

    public PitchGraphPanel() {
        setBackground(new Color(80, 80, 80));

        // Dummy-Daten: Sinusverlauf zur Simulation
        // Wichtig: Entfernen, wenn echte Audio-Daten verarbeitet werden
        /*timer = new Timer(UPDATE_INTERVAL, e -> {
            double t = System.currentTimeMillis() / 200.0;
            double pitchOffset = Math.sin(t) * 50; // simulierte Abweichung
            addPitchValue(pitchOffset);
        });
        timer.start();*/
    }

    private double centToY(double centOffset) {
        int h = getHeight();
        double centerY = h / 2.0;
        double pixelsPerCent = h / 2.0 / 100.0; // 100 Cent == halbe Höhe
        return centerY + (-centOffset * pixelsPerCent); // minus = oben
    }


    // Diese Methode wird von der Audio-Verarbeitung aufgerufen
    public void updatePitch(double frequency) {
        // Konvertiere die Frequenz in eine relative Abweichung vom Zielton
        // Als Beispiel: Abweichung in Cents (100 Cents = 1 Halbton)
        double targetFrequency = 440.0; // A4 als Beispiel
        double deviation = 1200 * Math.log(frequency/targetFrequency) / Math.log(2);
        
        // Skaliere die Abweichung für die Anzeige
        double scaledDeviation = deviation * 0.5; // Anpassung der Darstellung
        
        addPitchValue(scaledDeviation);
    }

    public void addPitchValue(double value) {
        pitchData.addLast(value);
        if (pitchData.size() > MAX_POINTS) {
            pitchData.removeFirst();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerY = h / 2;

        // Grüner Idealton-Balken
        int barHeight = 20;
        g2.setColor(Color.GREEN);
        g2.fillRect(0, centerY - barHeight / 2, w, barHeight);

        // Graph
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2f));


        int centerX = w / 2;
        int numPoints = pitchData.size();
        int startX = centerX - numPoints * SCROLL_SPEED;

        Path2D path = new Path2D.Double();
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

        // Punkt in der Mitte (immer aktuelle Tonhöhe)
        g2.setColor(Color.BLUE);
        if (!pitchData.isEmpty()) {
            double currentY = centToY(pitchData.getLast());
            g2.fillOval(centerX - 5, (int) currentY - 5, 10, 10);
        }
    }
}
