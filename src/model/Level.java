// Authors:Inaas Hammoush
package model;

import java.util.List;
import javax.sound.sampled.Mixer;

/**
 * Represents a training level in the application, encapsulating the configuration
 * for audio devices, reference notes, recording duration, mode, and difficulty.
 * This class is used as a state object to hold the necessary information for a training level
 * 
 * @author Inaas Hammoush
 * @version 1.0
 */
public class Level {
    
    private Mixer.Info selectedMic; 
    private Mixer.Info selectedSpeaker;
    private List<MidiNote> referenceNotes;
    private int recordingDuration = 3; // Default recording duration for single notes and Intervals, can be adjusted for melodies
    private final Mode mode;
    private final Difficulty difficulty;
    private Feedback feedback; // Feedback object to provide feedback on the training session
    // private final int levelNumber; // Unique identifier for the level, can be used for progress tracking

    public Level(Mode mode, Difficulty difficulty, Mixer.Info selectedMic, Mixer.Info selectedSpeaker, List<MidiNote> referenceNotes) {
        this.mode = mode;
        this.difficulty = difficulty;
        this.selectedMic = selectedMic;
        this.selectedSpeaker = selectedSpeaker;
        this.referenceNotes = referenceNotes;
    }

    // getters and setters 
    public void setReferenceNotes(List<MidiNote> referenceNotes) {this.referenceNotes = referenceNotes;}

    public List<MidiNote> getReferenceNotes() {return referenceNotes;}

    public Mixer.Info getSelectedMic() {return selectedMic;}

    public void setSelectedMic(Mixer.Info selectedMic) {this.selectedMic = selectedMic;}

    public Mixer.Info getSelectedSpeaker() {return selectedSpeaker;}

    public void setSelectedSpeaker(Mixer.Info selectedSpeaker) {this.selectedSpeaker = selectedSpeaker;}

    public int getRecordingDuration() {return recordingDuration;}

    public void setRecordingDuration(int recordingDuration) {this.recordingDuration = recordingDuration;}

    public Mode getMode() {return mode;}

    public Difficulty getDifficulty() {return difficulty;}

    public Feedback getFeedback() {return feedback;}

    public void setFeedback(Feedback feedback) {this.feedback = feedback;}
}
