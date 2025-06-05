// Authors:David Herrmann, Inaas Hammoush
package model;

public enum Interval {
    P1("P1", 0),
    m2("m2", 1),
    M2("M2", 2),
    m3("m3", 3),
    M3("M3", 4),
    P4("P4", 5),
    A4("A4", 6),
    P5("P5", 7),
    m6("m6", 8),
    M6("M6", 9),
    m7("m7", 10),
    M7("M7", 11),
    P8("P8", 12);

    private final String name;
    private final int semitones;

    Interval(String name, int semitones) {
        this.name = name;
        this.semitones = semitones;
    }

    public String getName() {
        return name;
    }

    public int getSemitones() {
        return semitones;
    }

    public static Interval getIntervalBySemitones(int semitones) {
        for (Interval interval : values()) {
            if (interval.getSemitones() == semitones) {
                return interval;
            }
        }
        return null;
    }
}
