package manager;

import model.AnalysisResult;
import model.Feedback;
public class FeedbackManager {
    private PitchListener pitchListener;

    public void setPitchListener(PitchListener listener) {
        this.pitchListener = listener;
    }

    public void updatePitchGraph(double pitch) {
        if (pitchListener != null) {
            pitchListener.onPitchUpdate(pitch);
        }
    }

    public Feedback calculateFeedbackForRecordedNote(double recordedPitch, double targetPitch) {
        // Here you would implement the logic to calculate feedback based on the recorded pitch
        return new Feedback();
    }

    public Feedback calculateFeedbackForRecordedMelody(AnalysisResult analysisResult) {
        // Here you would implement the logic to calculate feedback based on the analysis result
        return new Feedback();
    }

}

