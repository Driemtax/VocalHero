// Authors: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ProgressPanel extends JPanel {
    private JProgressBar category1Bar;
    private JProgressBar category2Bar;
    private JProgressBar category3Bar;

    public ProgressPanel() {
        // Lädt Fortschritt aus progress.csv, wenn vorhanden, sonst Standardwerte
        this(loadProgressFromCSV("src/savedata/progress.csv"));
    }

    public ProgressPanel(Map<String, int[]> progressData) {
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 12, 18, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        addCategoryProgress("Einzelnote", progressData.getOrDefault("Einzelnote", new int[]{0, 10}), gbc, 0, new Color(0x4F8A8B));
        addCategoryProgress("Intervalle",  progressData.getOrDefault("Intervalle",  new int[]{0, 8}),  gbc, 1, new Color(0xF9A826));
        addCategoryProgress("Melodie",  progressData.getOrDefault("Melodie",  new int[]{0, 12}), gbc, 2, new Color(0xF76E6C));
    }

    private void addCategoryProgress(String name, int[] data, GridBagConstraints gbc, int row, Color barColor) {
        int completed = data[0];
        int total = data[1];
        JLabel label = new JLabel(name + ":");
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(230, 230, 230)); // Helle Schrift

        JProgressBar bar = new JProgressBar(0, total);
        bar.setValue(completed);
        bar.setStringPainted(true);
        bar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bar.setForeground(barColor);
        bar.setBackground(new Color(60, 60, 60)); // Dunkler Balken-Hintergrund
        bar.setBorder(BorderFactory.createLineBorder(barColor, 2));
        bar.setPreferredSize(new Dimension(260, 32));
        bar.setString(completed + " / " + total);

        // Optional: Fortschrittsbalken-Textfarbe anpassen
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            protected Color getSelectionBackground() { return Color.WHITE; }
            protected Color getSelectionForeground() { return Color.WHITE; }
        });

        gbc.gridy = row;
        gbc.gridx = 0;
        add(label, gbc);

        gbc.gridx = 1;
        add(bar, gbc);

        switch (row) {
            case 0 -> category1Bar = bar;
            case 1 -> category2Bar = bar;
            case 2 -> category3Bar = bar;
        }
    }

    // Methoden zum Aktualisieren des Fortschritts
    public void setCategory1Progress(int completed, int total) {
        category1Bar.setMaximum(total);
        category1Bar.setValue(completed);
        category1Bar.setString(completed + " / " + total);
    }

    public void setCategory2Progress(int completed, int total) {
        category2Bar.setMaximum(total);
        category2Bar.setValue(completed);
        category2Bar.setString(completed + " / " + total);
    }

    public void setCategory3Progress(int completed, int total) {
        category3Bar.setMaximum(total);
        category3Bar.setValue(completed);
        category3Bar.setString(completed + " / " + total);
    }

    // CSV-Ladefunktion
    public static Map<String, int[]> loadProgressFromCSV(String filePath) {
        Map<String, int[]> progressData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Header überspringen
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int completed = Integer.parseInt(parts[1]);
                int total = Integer.parseInt(parts[2]);
                progressData.put(name, new int[]{completed, total});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressData;
    }
}
