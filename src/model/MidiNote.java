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

    public double getStartTime() {return startTime;}

    public void setStartTime(double startTime) {this.startTime = startTime;}

    public double getDuration() {return duration;}

    public void setDuration(double duration) {this.duration = duration;}

    public double getFrequency() {return frequency;}

    public void setFrequency(double frequency) {this.frequency = frequency;}
}
