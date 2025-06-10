// Author: Jonas Rumpf, Inaas Hammoush

package views;

import javax.swing.*;

import model.MidiNote;
import model.NoteType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import utils.ScoreUtils;

public class ScorePanel extends JPanel {
    private Image clef;
    private Image bassClef;
    private List<Note> notes;
    private final ScoreUtils scoreUtils;
    
    // Inner class to represent a musical note
    public static class Note {
        private final int x;
        private final int pitch;
        private final boolean isRest;
        private final NoteType noteType;

        public Note(int x, int pitch, boolean isRest, NoteType noteType) {
            this.x = x;
            this.pitch = pitch;
            this.isRest = isRest;
            this.noteType = noteType;
        }

        public boolean isRest() { return isRest; }
        public NoteType getNoteType() { return noteType; }
    }

    public ScorePanel(List<MidiNote> referenceNotes) {
        setBackground(new Color(80, 80, 80));
        clef = new ImageIcon(getClass().getResource("/assets/clef.png")).getImage();
        bassClef = new ImageIcon(getClass().getResource("/assets/bassclef.png")).getImage(); // Bild muss noch hinzugefügt werden
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

    public List<Note> midiNotesToUiNotes(List<MidiNote> midiNotes) {
        if (midiNotes == null || midiNotes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Note> uiNotes = new ArrayList<>();
        int startX = 200; // Starting x position for notes
        int spacing = 20; // Spacing between notes
        int counter = 0;

        for (int i = 0; i < midiNotes.size(); i++) {
            MidiNote current = midiNotes.get(i);
            MidiNote next = (i < midiNotes.size() - 1) ? midiNotes.get(i + 1) : null;

            int xPosition = startX + counter * spacing;
            counter++;

            boolean isRest = current.getNoteDefinition() == MidiNote.Note.REST;
            int pitch = isRest ? 0 : current.getNoteDefinition().getMidiNumber();
            NoteType noteType = getNoteTypeForTiming(current, next); // Hier neue Methode verwenden

            uiNotes.add(new Note(xPosition, pitch, isRest, noteType));
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
        scaleNotes.add(new Note(startX, 60, false, NoteType.QUARTER));              // C4
        scaleNotes.add(new Note(startX + spacing, 62, false, NoteType.QUARTER));    // D4
        scaleNotes.add(new Note(startX + spacing*2, 64, false, NoteType.QUARTER));  // E4
        scaleNotes.add(new Note(startX + spacing*3, 65, false, NoteType.QUARTER));  // F4
        scaleNotes.add(new Note(startX + spacing*4, 67, false, NoteType.QUARTER));  // G4
        scaleNotes.add(new Note(startX + spacing*5, 69, false, NoteType.QUARTER));  // A4
        scaleNotes.add(new Note(startX + spacing*6, 71, false, NoteType.QUARTER));  // B4
        scaleNotes.add(new Note(startX + spacing*7, 72, false, NoteType.QUARTER));  // C5
        
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
        g2.fillRect(0, 0, w, h);

        // Draw staff lines
        int lineSpacing = 20;
        int startY = h/2 - 2 * lineSpacing;
        scoreUtils.drawStaffLines(g2, w, startY, lineSpacing);

        // Entscheide, ob Violin- oder Bassschlüssel
        boolean useBassClef = notes.stream().anyMatch(n -> !n.isRest() && n.pitch > 0 && n.pitch < 54);
        Image clefToDraw = useBassClef ? bassClef : clef;

        // Draw clef
        int clefWidth, clefHeight, clefX, clefY;
        if (useBassClef) {
            clefWidth = 70;
            clefHeight = 70;
            clefX = 100;
            clefY = startY - lineSpacing/4; // Bassschlüssel sitzt tiefer
        } else {
            clefWidth = 50;
            clefHeight = 100;
            clefX = 100;
            clefY = startY;
        }
        g2.drawImage(clefToDraw, clefX, clefY, clefWidth, clefHeight, this);

        // Draw notes
        for (Note note : notes) {
            if (note.isRest()) {
                
            } else {
                // Notenposition und Vorzeichen bestimmen
                ScoreUtils.NotePosition pos = ScoreUtils.getNotePosition(note.pitch);
                int yPos = scoreUtils.calculateYPositionWhiteKeys(startY, lineSpacing, note.pitch, useBassClef);

                // Vorzeichen zeichnen, falls nötig
                if (pos.isFlat) {
                    g2.setFont(new Font("Serif", Font.BOLD, 22));
                    g2.drawString("♭", note.x - 18, yPos + 7);
                }

                // Beispiel: Verschiedene Notenarten zeichnen
                switch (note.getNoteType()) { // noteType musst du selbst bestimmen/übergeben
                    case WHOLE:
                        scoreUtils.drawWholeNote(g2, note.x, yPos, startY, lineSpacing);
                        System.err.println("Drawing whole note at x: " + note.x + ", y: " + yPos);
                        break;
                    case HALF:
                        scoreUtils.drawHalfNote(g2, note.x, yPos, startY, lineSpacing);
                        System.err.println("Drawing half note at x: " + note.x + ", y: " + yPos);
                        break;
                    case QUARTER:
                        scoreUtils.drawQuarterNote(g2, note.x, yPos, startY, lineSpacing);
                        System.err.println("Drawing quarter note at x: " + note.x + ", y: " + yPos);
                        break;
                    case EIGHTH:
                        scoreUtils.drawEighthNote(g2, note.x, yPos, startY, lineSpacing);
                        System.err.println("Drawing eighth note at x: " + note.x + ", y: " + yPos);
                        break;
                    case SIXTEENTH:
                        scoreUtils.drawSixteenthNote(g2, note.x, yPos, startY, lineSpacing);
                        System.err.println("Drawing sixteenth note at x: " + note.x + ", y: " + yPos);
                        break;
                    default:
                        scoreUtils.drawQuarterNote(g2, note.x, yPos, startY, lineSpacing);
                        System.err.println("Unknown note type: " + note.getNoteType());
                }
            }
        }
    }

    private NoteType getNoteTypeForDuration(double duration) {
        if (duration >= 3.5) return NoteType.WHOLE;
        if (duration >= 1.5) return NoteType.HALF;
        if (duration >= 0.75) return NoteType.QUARTER;
        if (duration >= 0.35) return NoteType.EIGHTH;
        return NoteType.SIXTEENTH;
    }

    private double bpm = 120.0;

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    private NoteType getNoteTypeForTiming(MidiNote currentNote, MidiNote nextNote) {
        double beatDuration = 60.0 / bpm;
        double timeUntilNext = (nextNote != null)
                ? nextNote.getStartTime() - currentNote.getStartTime()
                : currentNote.getDuration(); // Fallback

        double beats = timeUntilNext / beatDuration;

        if (beats >= 4.0) return NoteType.WHOLE;        // 4 Schläge
        if (beats >= 2.0) return NoteType.HALF;         // 2 Schläge
        if (beats >= 1.0) return NoteType.QUARTER;      // 1 Schlag
        if (beats >= 0.5) return NoteType.EIGHTH;       // ½ Schlag
        return NoteType.SIXTEENTH;                      // < ½ Schlag
    }   

}

