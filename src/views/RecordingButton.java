// Author: Jonas Rumpf, Inaas Hammoush

package views;

import java.awt.Color;
import i18n.LanguageManager;

public class RecordingButton extends ModernButton {
    private static final Color IDLE_COLOR = new Color(80, 220, 100);         // Green
    private static final Color RECORDING_COLOR = new Color(200, 40, 60);     // Red

    private boolean isRecording = false;

    public RecordingButton() {
        super(LanguageManager.get("recordingbutton.start"));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);  // Make sure background color is used
        setOpaque(true);      // Make background visible
        setRolloverEnabled(false);   // Disable hover effect
        setBackground(IDLE_COLOR);
    }

    public void setRecording(boolean recording) {
        this.isRecording = recording;
        setText(recording 
            ? LanguageManager.get("levelscreen.stop_recording") 
            : LanguageManager.get("recordingbutton.start"));
        setBackground(recording ? RECORDING_COLOR : IDLE_COLOR);
    }

    public boolean isRecording() {
        return isRecording;
    }
}
