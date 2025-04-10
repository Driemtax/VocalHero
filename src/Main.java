import javax.swing.*;

import java.awt.BorderLayout;

import javax.sound.sampled.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Note Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(550, 200);

            // Fügen Sie den Audioaufnahme-Code hier ein
            JComboBox<String> deviceComboBox = new JComboBox<>();
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            Line.Info[] lineInfos = AudioSystem.getTargetLineInfo(info);
            for (Line.Info lineInfo : lineInfos) {
                if (lineInfo instanceof TargetDataLine) {
                    deviceComboBox.addItem(lineInfo.toString());
                }
            }

            JButton recordButton = new JButton("Record");
            recordButton.addActionListener(e -> {
                int selectedIndex = deviceComboBox.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < lineInfos.length) {
                    Line.Info selectedLineInfo = lineInfos[selectedIndex];
                    try {
                        TargetDataLine selectedLine = (TargetDataLine) AudioSystem.getLine(selectedLineInfo);
                        selectedLine.open(format);
                        selectedLine.start();
                        byte[] buffer = new byte[selectedLine.getBufferSize() / 5];
                        int count;
                        while ((count = selectedLine.read(buffer, 0, buffer.length)) > 0) {
                            // Verarbeiten Sie die aufgenommenen Daten hier
                        }
                        selectedLine.stop();
                        selectedLine.close();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "No audio input device selected.");
                }
            });

            frame.getContentPane().add(deviceComboBox, BorderLayout.NORTH);
            frame.getContentPane().add(recordButton, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}
