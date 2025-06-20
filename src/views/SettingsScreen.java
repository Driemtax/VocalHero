// Author: Jonas Rumpf

package views;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import model.AudioSettings;
import utils.AudioPreferences;
import utils.LanguagePreferences;
import controller.WindowController;
import i18n.LanguageManager;

import java.awt.*;
import java.util.Locale;

public class SettingsScreen extends JPanel {
    private WindowController windowController;

    private JComboBox<String> inputDeviceCombo;
    private JComboBox<String> outputDeviceCombo;

    private ModernButton gotoBaseVoiceButton;

    public SettingsScreen(WindowController controller) {
        this.windowController = controller;

        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Zurück-Button oben links
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        ModernButton backButton = new ModernButton("⬅️ " + LanguageManager.get("sidebar.menu"));
        backButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        backButton.addActionListener(e -> windowController.showHome());
        add(backButton, gbc);

        // Titel mittig oben
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        JLabel titleLabel = new JLabel(LanguageManager.get("settings.title"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Mikrofon-Auswahl
        gbc.gridy = 1;
        add(createLabel(LanguageManager.get("settings.microphone")), gbc);

        gbc.gridx = 1;
        inputDeviceCombo = new JComboBox<>();
        String[] inputDevices = windowController.getAudioInputDevices();
        for (String device : inputDevices) {
            inputDeviceCombo.addItem(device);
        }
        styleComboBox(inputDeviceCombo);
        setSelectedDevice(inputDeviceCombo, AudioSettings.getInputDevice());
        add(inputDeviceCombo, gbc);

        // Ausgabegerät-Auswahl
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel(LanguageManager.get("settings.outputDevice")), gbc);

        gbc.gridx = 1;
        outputDeviceCombo = new JComboBox<>();
        String[] outputDevices = windowController.getAudioOutputDevices();
        for (String device : outputDevices) {
            outputDeviceCombo.addItem(device);
        }
        styleComboBox(outputDeviceCombo);
        setSelectedDevice(outputDeviceCombo, AudioSettings.getOutputDevice());
        add(outputDeviceCombo, gbc);

        //Basisstimmlage bestimmen
        gbc.gridx = 1;
        gbc.gridy = 4;
        gotoBaseVoiceButton = new ModernButton(LanguageManager.get("settings.baseVoice"));
        add(gotoBaseVoiceButton, gbc);    

        inputDeviceCombo.addActionListener(e -> {
            String selected = (String) inputDeviceCombo.getSelectedItem();
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                if (info.getName().equals(selected)) {
                    AudioSettings.setInputDevice(info);
                    AudioPreferences.saveSelectedDevices(info, AudioSettings.getOutputDevice());
                    break;
                }
            }
        });

        outputDeviceCombo.addActionListener(e -> {
            String selected = (String) outputDeviceCombo.getSelectedItem();
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                if (info.getName().equals(selected)) {
                    AudioSettings.setOutputDevice(info);
                    AudioPreferences.saveSelectedDevices(AudioSettings.getInputDevice(), info);
                    break;
                }
            }
        });

        gotoBaseVoiceButton.addActionListener(_1 -> windowController.showBaseVoiceRecordScreen());

        // Sprachauswahl
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(createLabel(LanguageManager.get("settings.language")), gbc);

        gbc.gridx = 1;
        String[] languages = { "Deutsch", "English" };
        JComboBox<String> languageCombo = new JComboBox<>(languages);
        languageCombo.setSelectedIndex(LanguagePreferences.getSavedLanguageIndex());
        styleComboBox(languageCombo);
        add(languageCombo, gbc);

        languageCombo.addActionListener(e -> {
            int idx = languageCombo.getSelectedIndex();
            Locale locale = (idx == 0) ? Locale.GERMAN : Locale.ENGLISH;
            LanguageManager.setLocale(locale);
            LanguagePreferences.saveLanguage(locale);
            JOptionPane.showMessageDialog(this,
                LanguageManager.get("settings.restart_required"),
                LanguageManager.get("settings.language"),
                JOptionPane.INFORMATION_MESSAGE
            );
        });

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void setSelectedDevice(JComboBox<String> comboBox, Mixer.Info selectedInfo) {
        if (selectedInfo == null) return;
        String selectedName = selectedInfo.getName();
        ComboBoxModel<String> model = comboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equals(selectedName)) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }


    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        comboBox.setFocusable(false);

        // Benutzerdefiniertes UI für die ComboBox
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton() {
                    @Override
                    public void paint(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Button Hintergrund dunkler
                        g2.setColor(new Color(60, 60, 60));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        
                        // Pfeil zeichnen
                        g2.setColor(Color.WHITE);
                        int[] xPoints = {getWidth()/4, getWidth()/2, getWidth()*3/4};
                        int[] yPoints = {getHeight()/3, getHeight()*2/3, getHeight()/3};
                        g2.fillPolygon(xPoints, yPoints, 3);
                    }
                };
                button.setBackground(new Color(60, 60, 60));  // Auch hier dunklerer Hintergrund
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(new Color(60, 60, 60));  // Hintergrund für den Textbereich
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        // Dropdown Popup und Scrollbar styling
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(100, 100, 100) : new Color(60, 60, 60));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return this;
            }
        });

        // Popup anpassen
        Object popup = comboBox.getUI().getAccessibleChild(comboBox, 0);
        if (popup instanceof JPopupMenu) {
            JScrollPane scrollPane = (JScrollPane)((JPopupMenu)popup).getComponent(0);
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(120, 120, 120);
                    this.trackColor = new Color(70, 70, 70);
                }
                
                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }

                private JButton createZeroButton() {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(0, 0));
                    return button;
                }
            });
        }
    }

}
