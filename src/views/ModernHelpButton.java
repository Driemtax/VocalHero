// Author: Jonas Rumpf

package views;

import javax.swing.*;

import model.Mode;

import java.awt.*;
import java.awt.geom.*;

public class ModernHelpButton extends JButton {
    private Color normalBg = new Color(60, 60, 60);
    private Color hoverBg = new Color(90, 90, 90);
    private final Mode mode;

    public ModernHelpButton(Mode mode) {
        super("?");
        this.mode = mode;
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(normalBg);
        setFocusPainted(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(30, 30));
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover-Effekt
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(hoverBg);
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(normalBg);
                repaint();
            }
        });

        addActionListener(e -> showCategoryHelpDialog());
    }

    private void showCategoryHelpDialog() {
        String message = getHelpTextForCategory(mode);
        showDarkInfoDialog(this, message, "How to Play");
    }

    private String getHelpTextForCategory(Mode mode) {
        switch (mode) {
            case INTERVAL:
                return mode.getName() + " Training:\n\n"
                     + "1. Click 'Play Reference' to hear the interval\n"
                     + "2. Click 'Start Recording' and sing the interval\n"
                     + "3. The graph will show your pitch in real-time\n"
                     + "4. After 3 seconds, you'll get feedback on your performance\n\n"
                     + "Try to match the interval as closely as possible!";
            case MELODY:
                return mode.getName() + " Training:\n\n"
                     + "1. Click 'Play Reference' to hear the melody\n"
                     + "2. Click 'Start Recording' and sing the melody\n"
                     + "3. The graph will show your pitch in real-time\n"
                     + "4. After 3 seconds, you'll get feedback on your performance\n\n"
                     + "Try to match the melody as closely as possible!";
            default:
                return mode.getName() + " Training\n\n"
                     + "1. Click 'Play Reference' to hear the target note\n"
                     + "2. Click 'Start Recording' and sing the note\n"
                     + "3. The graph will show your pitch in real-time\n"
                     + "4. After 3 seconds, you'll get feedback on your performance\n\n"
                     + "Try to match the reference note as closely as possible!";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Zeichne runden Button
        g2.setColor(getBackground());
        g2.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));

        // Zeichne Text
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth("?")) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString("?", x, y);

        g2.dispose();
    }

    /**
     * Zeigt ein an die dunkle UI angepasstes Info-Popup an.
     */
    public static void showDarkInfoDialog(Component parent, String message, String title) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.add(textArea, BorderLayout.CENTER);

        UIManager.put("OptionPane.background", new Color(30, 30, 30));
        UIManager.put("Panel.background", new Color(30, 30, 30));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.INFORMATION_MESSAGE);
    }
}