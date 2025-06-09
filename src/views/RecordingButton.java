// Author: Jonas Rumpf

package views;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import i18n.LanguageManager;

public class RecordingButton extends ModernButton {
    private static final Color IDLE_COLOR = new Color(80, 220, 100);
    private static final Color RECORDING_COLOR = new Color(200, 40, 60);
    private static final Color HOVER_RECORDING_COLOR = new Color(180, 60, 60);      // Red 
    private static final Color HOVER_IDLE_COLOR = new Color(60, 180, 60);  // Green

    private boolean isRecording = false;

    public RecordingButton() {
        super(LanguageManager.get("recordingbutton.start"));
        setBackground(IDLE_COLOR);
        
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(isRecording ? new Color(180, 40, 60) : new Color(40, 200, 60));
            }
            public void mouseExited(MouseEvent e) {
                setBackground(isRecording ? HOVER_RECORDING_COLOR : HOVER_IDLE_COLOR);
            }
        });
    }

    public void setRecording(boolean recording) {
        this.isRecording = recording;
        setBackground(recording ? RECORDING_COLOR : IDLE_COLOR);
    }

    public boolean isRecording() {
        return isRecording;
    }
}