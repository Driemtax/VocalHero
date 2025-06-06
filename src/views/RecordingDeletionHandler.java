package views;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;
import i18n.LanguageManager;

public class RecordingDeletionHandler {

    private final PopupFactory popupFactory;

    public RecordingDeletionHandler() {
        this.popupFactory = PopupFactory.getSharedInstance();
    }

    /**
     * Prompts user for confirmation and calls the provided deletion logic if confirmed.
     * Then shows a success or failure popup.
     *
     * @param parentComponent Component to align popup with
     * @param recordingName   The name of the recording (used for messages)
     * @param deletionLogic   A function that returns true if deletion was successful
     */
    public void confirmAndDelete(Component parentComponent, String recordingName, Supplier<Boolean> deletionLogic) {
        int result = JOptionPane.showConfirmDialog(
                parentComponent,
                LanguageManager.get("recordings.delete_confirmation"),
                LanguageManager.get("recordings.popup_title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            boolean success = deletionLogic.get();
            showPopup(parentComponent, success);
        }
    }

    private void showPopup(Component parentComponent, boolean success) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(success ? new Color(40, 140, 40) : new Color(160, 40, 40));
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel label = new JLabel(success
                ? LanguageManager.get("recordings.delete_success")
                : LanguageManager.get("recordings.delete_failure"));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        panel.add(label, BorderLayout.CENTER);

        Point location = parentComponent.getLocationOnScreen();
        int x = location.x + parentComponent.getWidth() / 2 - 100;
        int y = location.y + parentComponent.getHeight() / 2 - 30;

        Popup popup = popupFactory.getPopup(parentComponent, panel, x, y);
        popup.show();

        Timer timer = new Timer(2000, e -> popup.hide());
        timer.setRepeats(false);
        timer.start();
    }
}
