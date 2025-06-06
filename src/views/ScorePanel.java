// Author: Jonas Rumpf, Inaas Hammoush

package views;

import javax.swing.*;

import model.MidiNote;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import utils.ScoreUtils;

public class ScorePanel extends JPanel {
    private Image clef;
    private List<Note> notes;
    private final ScoreUtils scoreUtils;
    
    // Inner class to represent a musical note
    public static class Note {
        private final int x;
        private final int pitch; // y-position will be calculated from pitch
        
        public Note(int x, int pitch) {
            this.x = x;
            this.pitch = pitch;
        }
    }

    public ScorePanel(List<MidiNote> referenceNotes) {
        setBackground(new Color(80, 80, 80));
        clef = new ImageIcon(getClass().getResource("/assets/clef.png")).getImage();
        notes = midiNotesToUiNotes(referenceNotes);
        scoreUtils = new ScoreUtils();

        //addTestNotes();
    }

    // Method to set new notes (e.g., for a new exercise)
    public void setNotes(List<Note> notes) {
        this.notes = new ArrayList<>(notes);
        repaint();
    }

    // Method to add a single note
    public void addNote(Note note) {
        notes.add(note);
        repaint();
    }

    // Method to clear all notes
    public void clearNotes() {
        notes.clear();
        repaint();
    }

    public List<Note> midiNotesToUiNotes(List<MidiNote> notes) {
        if (notes == null || notes.isEmpty()) {
            return new ArrayList<>();
        }
        List<Note> uiNotes = new ArrayList<>();
        int StartX = 200; // Starting x position for notes
        int spacing = 60; // Spacing between notes
        int counter = 0;
        for (MidiNote midiNote : notes) {
            int xPosition = StartX + counter * spacing;
            counter++;
            uiNotes.add(new Note(xPosition, midiNote.getNoteDefinition().getMidiNumber()));
        }
        return uiNotes;
    }

    // Method to add test notes (C major scale)
    public void addTestNotes() {
        clearNotes();
        
        // Starting x position and spacing between notes
        int startX = 200;
        int spacing = 60;
        
        // C major scale (MIDI notes: C4=60, D4=62, E4=64, F4=65, G4=67, A4=69, B4=71, C5=72)
        List<Note> scaleNotes = new ArrayList<>();
        scaleNotes.add(new Note(startX, 60));              // C4
        scaleNotes.add(new Note(startX + spacing, 62));    // D4
        scaleNotes.add(new Note(startX + spacing*2, 64));  // E4
        scaleNotes.add(new Note(startX + spacing*3, 65));  // F4
        scaleNotes.add(new Note(startX + spacing*4, 67));  // G4
        scaleNotes.add(new Note(startX + spacing*5, 69));  // A4
        scaleNotes.add(new Note(startX + spacing*6, 71));  // B4
        scaleNotes.add(new Note(startX + spacing*7, 72));  // C5
        
        setNotes(scaleNotes);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw score background
        int scoreWidth = w - 160;
        int scoreHeight = 160;
        int scoreX = 80;
        int scoreY = h/2 - scoreHeight/2;
        
        g2.setColor(Color.WHITE);
        g2.fillRect(scoreX, scoreY, scoreWidth, scoreHeight);

        // Draw staff lines
        int lineSpacing = 20;
        int startY = h/2 - 2 * lineSpacing;
        scoreUtils.drawStaffLines(g2, w, startY, lineSpacing);

        // Draw clef
        int clefWidth = 50;
        int clefHeight = 100;
        int clefX = 100;
        int clefY = startY;
        g2.drawImage(clef, clefX, clefY, clefWidth, clefHeight, this);

        // Draw notes
        for (Note note : notes) {
            int yPos = scoreUtils.calculateYPosition(startY, lineSpacing, note.pitch);
            scoreUtils.drawNote(g2, note.x, yPos, getHeight());
        }
    }
}

