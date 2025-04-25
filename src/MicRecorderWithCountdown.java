import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MicRecorderWithCountdown {
    private static TargetDataLine targetLine;
    private static AudioFormat format;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mikrofon Recorder");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 200);
            frame.setLayout(new FlowLayout());

            JComboBox<Mixer.Info> deviceComboBox = new JComboBox<>();
            JLabel statusLabel = new JLabel("Wähle ein Mikrofon und starte die Aufnahme");
            JButton recordButton = new JButton("Aufnahme starten");

            format = new AudioFormat(22050.0f, 16, 1, true, false);

            // Look for available microphones
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                Mixer mixer = AudioSystem.getMixer(info);
                Line.Info[] targetLines = mixer.getTargetLineInfo();
                for (Line.Info lineInfo : targetLines) {
                    if (lineInfo instanceof DataLine.Info) {
                        try {
                            TargetDataLine testLine = (TargetDataLine) mixer.getLine(lineInfo);
                            testLine.open(format);
                            testLine.close();
                            deviceComboBox.addItem(info);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }

            recordButton.addActionListener(e -> {
                Mixer.Info selectedMixer = (Mixer.Info) deviceComboBox.getSelectedItem();
                if (selectedMixer == null) {
                    statusLabel.setText("Bitte ein Mikrofon auswählen!");
                    return;
                }

                new Thread(() -> {
                    try {
                        // show countdown
                        for (int i = 3; i > 0; i--) {
                            final int count = i;
                            SwingUtilities.invokeLater(() -> statusLabel.setText("Aufnahme startet in: " + count));
                            Thread.sleep(1000);
                        }

                        // start recording
                        Mixer mixer = AudioSystem.getMixer(selectedMixer);
                        targetLine = (TargetDataLine) mixer.getLine(new DataLine.Info(TargetDataLine.class, format));
                        targetLine.open(format);
                        targetLine.start();

                        statusLabel.setText("🎙️ Aufnahme läuft (3 Sekunden)...");

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buffer = new byte[4096];
                        long startTime = System.currentTimeMillis();

                        while (System.currentTimeMillis() - startTime < 3000) {
                            int bytesRead = targetLine.read(buffer, 0, buffer.length);
                            out.write(buffer, 0, bytesRead);
                        }

                        targetLine.stop();
                        targetLine.close();

                        byte[] audioData = out.toByteArray();

                        // Optionally save to file
                        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                        AudioInputStream ais = new AudioInputStream(bais, format,
                                audioData.length / format.getFrameSize());
                        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("aufnahme.wav"));

                        // Convert to shorts and detect pitch
                        short[] samples = bytesToShorts(audioData, format.isBigEndian());
                        double pitch = AutocorrelationAnalyser.detectPitch(samples);

                        // Update status
                        if (pitch != -1) {
                            statusLabel.setText(String.format("✅ Aufnahme beendet. erkannte Tonhöhe: %.2f Hz", pitch));
                        } else {
                            statusLabel.setText("❌ Keine Tonhöhe erkannt.");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> statusLabel.setText("❌ Fehler bei der Aufnahme."));
                    }
                }).start();
            });

            frame.add(deviceComboBox);
            frame.add(recordButton);
            frame.add(statusLabel);
            frame.setVisible(true);
        });
    }

    private static short[] bytesToShorts(byte[] bytes, boolean isBigEndian) {
        int samples = bytes.length / 2;
        short[] shorts = new short[samples];
    
        for (int i = 0; i < samples; i++) {
            int low = bytes[2 * i] & 0xff;
            int high = bytes[2 * i + 1] & 0xff;
            shorts[i] = isBigEndian
                    ? (short) ((high << 8) | low)
                    : (short) ((low << 8) | high);
        }
    
        return shorts;
    }
    
}
