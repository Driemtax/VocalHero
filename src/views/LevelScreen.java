package views;

import javax.swing.*;
import java.awt.*;
import audio.MicrophonePitchDetector;

public class LevelScreen extends JPanel {
    private MicrophonePitchDetector detector;

    public LevelScreen() {
        setLayout(new GridLayout(2, 1));

        PitchGraphPanel pitchPanel = new PitchGraphPanel();
        add(pitchPanel);
        add(new ScorePanel());

        detector = new MicrophonePitchDetector(pitchPanel);
        detector.start();
    }

    public void stop() {
        detector.stopDetection();
    }
}


