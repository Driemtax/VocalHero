//Authors: Inaas Hammoush, Jonas Rumpf
package manager;

import model.AnalysisResult;
import model.Feedback;
import model.MidiNote;
import model.PitchListener;
import utils.Helper;

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

    public void updatePitchGraph(double pitch, double targetFrequency) {
        // Convert frequency to cent offset before updating the pitch graph
        double centOffset = Helper.frequencyToCentOffset(pitch, targetFrequency);
        if (pitchListener != null) {
            pitchListener.onPitchUpdate(centOffset);
        }
    }

    public Feedback calculateFeedbackForRecordedNote(double recordedPitch, double targetPitch) {

        System.out.println("Recorded Pitch: " + recordedPitch + " Hz, Target Pitch: " + targetPitch + " Hz");

        // Calculate the absolute difference in cents between the recorded and target pitch
        double centsDifference = 1200 * Math.log(recordedPitch / targetPitch) / Math.log(2);

        // Score calculation: 100 if perfect, linearly decreasing with cents difference
        // For example, -50 points at 100 cents (1 semitone) off, 0 at 200 cents (2 semitones) or more
        float score;
        double absCents = Math.abs(centsDifference);
        if (absCents >= 200) {
            score = 0f;
        } else {
            score = (float) Math.max(0, 100 - (absCents / 2));
        }
        return new Feedback(score);
    }

    public Feedback calculateFeedbackForRecordedMelody(AnalysisResult analysisResult) {
        return new Feedback((float) analysisResult.overallScore() * 100);
    }

}

