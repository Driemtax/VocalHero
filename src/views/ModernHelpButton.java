// Author: Jonas Rumpf

package views;

import javax.swing.*;

import i18n.LanguageManager;
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
        showDarkInfoDialog(this, message, LanguageManager.get("help.title"));
    }

    private String getHelpTextForCategory(Mode mode) {
        switch (mode) {
            case INTERVAL:
                return mode.getName() + " " + LanguageManager.get("help.training") + ":\n\n"
                     + LanguageManager.get("help.interval.1") + "\n"
                     + LanguageManager.get("help.interval.2") + "\n"
                     + LanguageManager.get("help.interval.3") + "\n"
                     + LanguageManager.get("help.interval.4") + "\n\n"
                     + LanguageManager.get("help.interval.tip");
            case MELODY:
                return mode.getName() + " " + LanguageManager.get("help.training") + ":\n\n"
                     + LanguageManager.get("help.melody.1") + "\n"
                     + LanguageManager.get("help.melody.2") + "\n"
                     + LanguageManager.get("help.melody.3") + "\n"
                     + LanguageManager.get("help.melody.4") + "\n\n"
                     + LanguageManager.get("help.melody.tip");
            default:
                return mode.getName() + " " + LanguageManager.get("help.training") + "\n\n"
                     + LanguageManager.get("help.single.1") + "\n"
                     + LanguageManager.get("help.single.2") + "\n"
                     + LanguageManager.get("help.single.3") + "\n"
                     + LanguageManager.get("help.single.4") + "\n\n"
                     + LanguageManager.get("help.single.tip");
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