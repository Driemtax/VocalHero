package model;

public class MidiNote {
    public double startTime;
    public double duration;
    public double frequency;
    
    public MidiNote(double start, double duration, double freq) {
        this.startTime = start;
        this.duration = duration;
        this.frequency = freq;
    }
}
