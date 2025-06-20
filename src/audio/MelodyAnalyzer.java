package audio;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import model.*;
import utils.Helper;

public class MelodyAnalyzer {
    private final PitchDetector pitchDetector;
    private final List<MidiNote> referenceNotes;
    private final double timingTolerance;
    private final double pitchTolerance;
    private byte[] audioData;
    private double sampleRate;

    public MelodyAnalyzer(List<MidiNote> referenceNotes,
                         double sampleRate,
                         byte[] audioData) {
        this.audioData = audioData;
        this.referenceNotes = referenceNotes;
        this.sampleRate = sampleRate;
        this.pitchDetector = new PitchDetector(2048, sampleRate);
        this.timingTolerance = 0.1;
        this.pitchTolerance = 0.5;
    }

    public AnalysisResult analyze() {
        List<NoteResult> noteResults = new ArrayList<>();

        for(MidiNote note : referenceNotes) {
            double[] timeWindow = calculateTimeWindow(note);
            byte[] noteAudio = extractAudioChunk(audioData, timeWindow);
            
            double detectedFreq = pitchDetector.getDominantFrequency(noteAudio);
            double onsetTime = detectOnset(noteAudio);
            
            noteResults.add(compareNote(note, detectedFreq, onsetTime));
        }
        
        return aggregateResults(noteResults);
    }

    private byte[] extractAudioChunk(byte[] audioData, double[] window) {
        // Double check that start is not lower then 0 and end is not bigger then the actual audioData to avoid
        // IndexOutOfBoundsExceptions
        int start = Math.max(0, (int) (window[0] * sampleRate));
        int end = Math.min(audioData.length, (int) (window[1] * sampleRate));

        if (start >= end) {
            // Return an empty Array if the window is invalid
            return new byte[0];
        }

        return Arrays.copyOfRange(audioData, start, end);
    }

    private double[] calculateTimeWindow(MidiNote note) {
        // We need to make sure, that start doesnt get negative if startTime is lower then timingTolerance
        double start = Math.max(0.0, note.getStartTime() - timingTolerance);
        double end = note.getStartTime() + note.getDuration() + timingTolerance;
        return new double[]{start, end};
    }

    private NoteResult compareNote(MidiNote note, double detectedFreq, double detectedOnset) {
        double pitchDiff = Math.abs(detectedFreq - note.getFrequency());
        double timeDiff = Math.abs(detectedOnset - note.getStartTime());
        
        boolean pitchOK = pitchDiff <= pitchTolerance;
        boolean timingOK = timeDiff <= timingTolerance;
        
        return new NoteResult(pitchOK, timingOK);
    }
    
    private double detectOnset(byte[] audioChunk) {
        float[] samples = Helper.byteArrayToFloatArray(audioChunk);
        double threshold = 0.3;
        
        for(int i=1; i<samples.length; i++) {
            double diff = Math.abs(samples[i] - samples[i-1]);
            if(diff > threshold) {
                return (double)i / sampleRate;
            }
        }
        return -1; // Fallback wenn kein Onset gefunden
    }

    private AnalysisResult aggregateResults(List<NoteResult> noteResults) {
        int total = noteResults.size();
        int pitchCorrect = 0;
        int timingCorrect = 0;

        for (NoteResult result : noteResults) {
            if (result.pitchCorrect()) pitchCorrect++;
            if (result.timingCorrect()) timingCorrect++;
        }

        double pitchAccuracy = total > 0 ? (double)pitchCorrect / total : 0.0;
        double timingAccuracy = total > 0 ? (double)timingCorrect / total : 0.0;
        double overallScore = (pitchAccuracy + timingAccuracy) / 2.0;

        return new AnalysisResult(overallScore, pitchAccuracy, timingAccuracy);
    }
}

record NoteResult(boolean pitchCorrect, boolean timingCorrect) {}

