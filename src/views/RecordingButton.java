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
        setContentAreaFilled(true);
        setOpaque(true);
        setRolloverEnabled(false);
        setBackground(IDLE_COLOR);

        setHoverEffectEnabled(false); // Standard-Hover deaktivieren

        // Eigener Hover-Effekt (z.B. heller machen)
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!isRecording) {
                    setBackground(new Color(100, 240, 120)); // Helleres Grün beim Hover im Idle-Zustand
                } else {
                    setBackground(new Color(220, 60, 80));   // Helleres Rot beim Hover im Recording-Zustand
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(isRecording ? RECORDING_COLOR : IDLE_COLOR);
            }
        });
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
