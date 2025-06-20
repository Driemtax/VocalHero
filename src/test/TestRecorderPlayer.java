// Authors: Lars Beer, Inaas Hammoush

package test;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import audio.PitchDetector;
import audio.Recorder;
import model.MidiNote;
import utils.AudioUtil;
import audio.Player;

public class TestRecorderPlayer {
    private Recorder recorder;
    private PitchDetector analyzer;
    private Player player;
    private AudioFormat format;
    private AudioUtil audioUtil = new AudioUtil();

    private JComboBox<Mixer.Info> speakerComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestRecorderPlayer().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("Gesangstrainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setLayout(new GridLayout(0, 2, 10, 10));

        format = new AudioFormat(44100, 16, 2, true, false);
        recorder = new Recorder(format);
        analyzer = new PitchDetector(2048, format.getSampleRate());
        player = new Player();

        // UI Komponenten
        JComboBox<Mixer.Info> micComboBox = new JComboBox<>();
        speakerComboBox = new JComboBox<>();
        JButton recordButton = new JButton("Aufnahme starten (3 Sek.)");
        JButton playButton = new JButton("Abspielen");
        JLabel statusLabel = new JLabel("Status: Bereit");
        JComboBox<String> noteComboBox = new JComboBox<>(new String[]{"C4", "D4", "E4", "F4", "G4", "A4", "B4"});
        JLabel targetNoteLabel = new JLabel("Zielnote: -");
        JLabel detectedNoteLabel = new JLabel("Erkannte Note: -");
        JButton playTargetNoteButton = new JButton("Zielnote abspielen");

        // Mikrofone laden
        List<Mixer.Info> mics = audioUtil.getAvailableMicrophones(format);
        mics.forEach(micComboBox::addItem);

        // Load speaker
        List<Mixer.Info> speakers = audioUtil.getAvailableSpeakers(format);
        speakers.forEach(speakerComboBox::addItem);

        // Action Listener
        recordButton.addActionListener(e -> startRecording(micComboBox, statusLabel, detectedNoteLabel));
        playButton.addActionListener(e -> playRecording(statusLabel));
        noteComboBox.addActionListener(e -> targetNoteLabel.setText("Zielnote: " + noteComboBox.getSelectedItem()));
        playTargetNoteButton.addActionListener(e -> playTargetNote(MidiNote.Note.A4));


        // Layout
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        leftPanel.add(new JLabel("Mikrofon:"));
        leftPanel.add(micComboBox);
        leftPanel.add(new JLabel("Lautsprecher:"));
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
        rightPanel.add(playTargetNoteButton);

        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (player != null) {
                    player.close(); // Gibt die Synthesizer-Ressourcen frei
                }
            }
        });
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
                //recorder.startRecording(3, selectedMixer);
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

        // new Thread(() -> {
        //     try {
        //         updateStatus(statusLabel, "▶️ Wiedergabe läuft...");
        //         Player recordingPlayer = new Player();
        //         //recordingPlayer.play(selectedSpeaker, recorder.getAudioData(), format);
        //     } catch (LineUnavailableException ex) {
        //         System.out.println(ex.getMessage());
        //         ex.printStackTrace();
        //         updateStatus(statusLabel, "❌ Wiedergabefehler!");
        //     }
        //     updateStatus(statusLabel, "✅ Wiedergabe beendet.");
        // }).start();
    }

    // Mock-Methode für Noteerkennung
    private String frequencyToNote(double frequency) {
        // Hier später echte Implementierung
        return "C4 (mock)";
    }

    private void updateStatus(JLabel label, String text) {
        SwingUtilities.invokeLater(() -> label.setText(text));
    }

    private void playTargetNote(MidiNote.Note note) {
        new Thread(() -> {
            double frequency = note.getFrequency();
            if (frequency > 0) {
                // Spielt die Note für 1 Sekunde (1000 ms) ab
                //player.playNote(frequency, 1000);
            }
        }).start();
    }
}
