// Authors:David Herrmann, Inaas Hammoush
package model;

import i18n.LanguageManager;

public enum Interval {
    P1("P1", 0),
    m2(LanguageManager.get("interval.name.1"), 1),
    M2(LanguageManager.get("interval.name.2"), 2),
    m3(LanguageManager.get("interval.name.3"), 3),
    M3(LanguageManager.get("interval.name.4"), 4),
    P4(LanguageManager.get("interval.name.5"), 5),
    A4(LanguageManager.get("interval.name.6"), 6),
    P5(LanguageManager.get("interval.name.7"), 7),
    m6(LanguageManager.get("interval.name.8"), 8),
    M6(LanguageManager.get("interval.name.9"), 9),
    m7(LanguageManager.get("interval.name.10"), 10),
    M7(LanguageManager.get("interval.name.11"), 11),
    P8(LanguageManager.get("interval.name.12"), 12);

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
