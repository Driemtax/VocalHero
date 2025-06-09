package views;

import javax.swing.*;
import java.awt.*;
import controller.WindowController;
import model.MidiNote.Note;
import utils.FileUtils;
import java.util.List;

public class BaseVoicePanel extends JPanel {
    private final WindowController windowController;
    private JLabel currentVoiceLabel;
    private JLabel newVoiceLabel;
    private ModernButton recordButton;
    private ModernButton continueButton;
    private Runnable onContinue;

    private static final String BASE_VOICE_FILE = "baseVoice.txt";

    public BaseVoicePanel(WindowController windowController) {
        this.windowController = windowController;
        setBackground(new Color(50, 50, 50));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Basisstimmlage erkennen");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        add(title, gbc);

        gbc.gridy++;
        currentVoiceLabel = new JLabel("Aktuelle Basisstimmlage: " + readBaseVoiceFromFile());
        currentVoiceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        currentVoiceLabel.setForeground(Color.WHITE);
        add(currentVoiceLabel, gbc);

        gbc.gridy++;
        newVoiceLabel = new JLabel("Neue Basisstimmlage: -");
        newVoiceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        newVoiceLabel.setForeground(new Color(180, 220, 255));
        add(newVoiceLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        recordButton = new ModernButton("🎤 Aufnahme starten");
        recordButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        add(recordButton, gbc);

        gbc.gridx = 1;
        continueButton = new ModernButton("Weiter");
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(continueButton, gbc);

        // Button-Listener
        recordButton.addActionListener(e -> startBaseVoiceRecording());
        continueButton.addActionListener(e -> {
            windowController.showSettingsScreen();
        });
    }

    public void setOnContinue(Runnable onContinue) {
        this.onContinue = onContinue;
    }

    private String readBaseVoiceFromFile() {
        List<String> lines = FileUtils.loadVoiceFromTXT(BASE_VOICE_FILE);
        return lines.isEmpty() ? "-" : lines.get(0);
    }

    private void updateBaseVoiceLabels() {
        String baseVoice = readBaseVoiceFromFile();
        currentVoiceLabel.setText("Aktuelle Basisstimmlage: " + baseVoice);
        newVoiceLabel.setText("Neue Basisstimmlage: " + baseVoice);
    }

    private void startBaseVoiceRecording() {
        recordButton.setEnabled(false);
        newVoiceLabel.setText("Neue Basisstimmlage: Aufnahme läuft...");
        windowController.recordForBaseVoice(() -> {
            // UI-Update nach Aufnahme
            SwingUtilities.invokeLater(() -> {
                updateBaseVoiceLabels();
                recordButton.setEnabled(true);
            });
        });
    }
}