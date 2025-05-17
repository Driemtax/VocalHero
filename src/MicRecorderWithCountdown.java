import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import AudioIO.Recorder;
import AudioIO.Player;
import AudioProcessing.FrequencyAnalyzer;

public class MicRecorderWithCountdown {
    private Recorder recorder;
    private FrequencyAnalyzer analyzer;
    private Player player;
    private AudioFormat format;

    private JComboBox<Mixer.Info> speakerComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MicRecorderWithCountdown().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("Gesangstrainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setLayout(new GridLayout(0, 2, 10, 10));

        format = new AudioFormat(44100.0f, 16, 2, true, false);
        recorder = new Recorder(format);
        analyzer = new FrequencyAnalyzer(2048, format.getSampleRate());

        // UI Komponenten
        JComboBox<Mixer.Info> micComboBox = new JComboBox<>();
        speakerComboBox = new JComboBox<>();
        JButton recordButton = new JButton("Aufnahme starten (3 Sek.)");
        JButton playButton = new JButton("Abspielen");
        JLabel statusLabel = new JLabel("Status: Bereit");
        JComboBox<String> noteComboBox = new JComboBox<>(new String[]{"C4", "D4", "E4", "F4", "G4", "A4", "B4"});
        JLabel targetNoteLabel = new JLabel("Zielnote: -");
        JLabel detectedNoteLabel = new JLabel("Erkannte Note: -");

        // Mikrofone laden
        List<Mixer.Info> mics = recorder.getAvailableMicrophones();
        mics.forEach(micComboBox::addItem);

        // Load speaker
        List<Mixer.Info> speakers = getAvailableSpeakers(format);
        speakers.forEach(speakerComboBox::addItem);

        // Action Listener
        recordButton.addActionListener(e -> startRecording(micComboBox, statusLabel, detectedNoteLabel));
        playButton.addActionListener(e -> playRecording(statusLabel));
        noteComboBox.addActionListener(e -> targetNoteLabel.setText("Zielnote: " + noteComboBox.getSelectedItem()));

        // Layout
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        leftPanel.add(new JLabel("Mikrofon:"));
        leftPanel.add(micComboBox);
        leftPanel.add(speakerComboBox);
        leftPanel.add(recordButton);
        leftPanel.add(playButton);
        leftPanel.add(statusLabel);

        JPanel rightPanel = new JPanel(new GridLayout(3, 1));
        rightPanel.add(new JLabel("Übungsmodus:"));
        rightPanel.add(new JLabel("Wähle eine Zielnote:"));
        rightPanel.add(noteComboBox);
        rightPanel.add(targetNoteLabel);
        rightPanel.add(detectedNoteLabel);

        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.setVisible(true);
    }

    private void startRecording(JComboBox<Mixer.Info> deviceComboBox, JLabel statusLabel, JLabel detectedNoteLabel) {
        Mixer.Info selectedMixer = (Mixer.Info) deviceComboBox.getSelectedItem();
        if (selectedMixer == null) {
            statusLabel.setText("Bitte Mikrofon auswählen!");
            return;
        }

        new Thread(() -> {
            try {
                // Countdown
                for (int i = 3; i > 0; i--) {
                    updateStatus(statusLabel, "Aufnahme startet in: " + i);
                    Thread.sleep(1000);
                }

                updateStatus(statusLabel, "🎙️ Aufnahme läuft...");
                recorder.startRecording(3, selectedMixer);
                Thread.sleep(3000);
                
                byte[] audioData = recorder.getAudioData();
                double frequency = analyzer.getDominantFrequency(audioData);
                
                updateStatus(statusLabel, String.format("✅ Aufnahme beendet. Frequenz: %.2f Hz", frequency));
                detectedNoteLabel.setText("Erkannte Note: " + frequencyToNote(frequency));

            } catch (Exception ex) {
                ex.printStackTrace();
                updateStatus(statusLabel, "❌ Aufnahmefehler!");
            }
        }).start();
    }

    private void playRecording(JLabel statusLabel) {
        if (recorder.getAudioData() == null) {
            statusLabel.setText("Keine Aufnahme vorhanden!");
            return;
        }

        Mixer.Info selectedSpeaker = (Mixer.Info) speakerComboBox.getSelectedItem();
        if (selectedSpeaker == null) {
            statusLabel.setText("Bitte Lautsprecher auswählen!");
            return;
        }

        new Thread(() -> {
            try {
                player = new Player(recorder.getAudioData(), format);
                player.play(selectedSpeaker);
                updateStatus(statusLabel, "▶️ Wiedergabe läuft...");
            } catch (LineUnavailableException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                updateStatus(statusLabel, "❌ Wiedergabefehler!");
            }
        }).start();
    }

    // Mock-Methode für Noteerkennung
    private String frequencyToNote(double frequency) {
        // Hier später echte Implementierung
        return "C4 (mock)";
    }

    private void updateStatus(JLabel label, String text) {
        SwingUtilities.invokeLater(() -> label.setText(text));
    }

    private List<Mixer.Info> getAvailableSpeakers(AudioFormat format) {
        List<Mixer.Info> speakers = new ArrayList<>();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(info)) {
                speakers.add(mixerInfo);
            }
        }
        return speakers;
    }
}
