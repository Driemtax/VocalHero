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

    public void updatePitchGraph(double pitch) {
        double targetFrequency = referenceNotes.get(0).getFrequency();
        // Convert frequency to cent offset before updating the pitch graph
        double centOffset = Helper.frequencyToCentOffset(pitch, targetFrequency);
        if (pitchListener != null) {
            pitchListener.onPitchUpdate(centOffset);
        }
    }

    public Feedback calculateFeedbackForRecordedNote(double recordedPitch, double targetPitch) {
        System.out.println("Recorded Pitch: " + recordedPitch + " Hz, Target Pitch: " + targetPitch + " Hz");

        // MIDI-Nummern berechnen
        int recordedMidi = (int) Math.round(69 + 12 * Math.log(recordedPitch / 440.0) / Math.log(2));
        int targetMidi = (int) Math.round(69 + 12 * Math.log(targetPitch / 440.0) / Math.log(2));

        float score;
        double centsDifference = 1200 * Math.log(recordedPitch / targetPitch) / Math.log(2);
        double absCents = Math.abs(centsDifference);

        if (recordedMidi == targetMidi) {
            // 100 Punkte bei exakt, linear runter bis 90 bei ±50 Cent
            score = (float) Math.max(90, 100 - (absCents / 5));
        } else {
            if (absCents >= 200) {
                score = 0f;
            } else {
                score = (float) Math.max(0, 100 - (absCents / 2));
            }
        }
        return new Feedback(score);
    }

    public Feedback calculateFeedbackForRecordedMelody(AnalysisResult analysisResult) {
        return new Feedback((float) analysisResult.overallScore() * 100);
    }

}

