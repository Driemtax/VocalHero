//Authors: Inaas Hammoush
package manager;

import model.AnalysisResult;
import model.Feedback;
import model.MidiNote;
import java.util.List;

public class FeedbackManager {
    private PitchListener pitchListener;
    private List<MidiNote> referenceNotes;

    public FeedbackManager(List<MidiNote> referenceNotes) {
        this.referenceNotes = referenceNotes;
    }

    public void setPitchListener(PitchListener listener) {
        this.pitchListener = listener;
    }

    public void updatePitchGraph(double pitch) {
        if (pitchListener != null) {
            pitchListener.onPitchUpdate(pitch);
        }
    }
    // TODO: Create a Feedback object based on the detected pitch
    public Feedback calculateFeedbackForRecordedNote(double recordedPitch, double targetPitch) {
        // Here you would implement the logic to calculate feedback based on the recorded pitch
        return new Feedback();
    }

    // TODO: Create a Feedback object based on the analysis result 
    public Feedback calculateFeedbackForRecordedMelody(AnalysisResult analysisResult) {
        // Here you would implement the logic to calculate feedback based on the analysis result
        return new Feedback();
    }

}

