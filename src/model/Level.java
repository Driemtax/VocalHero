// Authors:Inaas Hammoush, David Herrmann
package model;

import java.util.List;

/**
 * Represents a training level in the application, encapsulating the configuration
 * for audio devices, reference notes, recording duration, mode, and difficulty.
 * This class is used as a state object to hold the necessary information for a training level
 * 
 * @author Inaas Hammoush
 * @version 1.0
 */
public class Level {
    
    private List<MidiNote> referenceNotes;
    private int recordingDuration = 3; // Default recording duration for single notes and Intervals, can be adjusted for melodies
    private final Mode mode;
    private final Difficulty difficulty;
    private MidiNote targetIntervalNote; // Target note for interval training, can be null if not applicable
    private String intervalName; // Name of the interval, can be used for display purposes
    private Feedback feedback; // Feedback object to provide feedback on the training session
    // private final int levelNumber; // Unique identifier for the level, can be used for progress tracking

    public Level(Mode mode, Difficulty difficulty, List<MidiNote> referenceNotes) {
        this.mode = mode;
        this.difficulty = difficulty;
        this.referenceNotes = referenceNotes;
    }

    // getters and setters 
    public void setReferenceNotes(List<MidiNote> referenceNotes) {this.referenceNotes = referenceNotes;}

    public List<MidiNote> getReferenceNotes() {return referenceNotes;}

    public int getRecordingDuration() {return recordingDuration;}

    public void setRecordingDuration(int recordingDuration) {this.recordingDuration = recordingDuration;}

    public Mode getMode() {return mode;}

    public Difficulty getDifficulty() {return difficulty;}

    public Feedback getFeedback() {return feedback;}

    public void setFeedback(Feedback feedback) {this.feedback = feedback;}

    public MidiNote getTargetIntervalNote() {return targetIntervalNote;}

    public void setTargetIntervalNote(MidiNote targetIntervalNote) {this.targetIntervalNote = targetIntervalNote;}

    public String getIntervalName() {return intervalName;}

    public void setIntervalName(String intervalName) {this.intervalName = intervalName;}
}
