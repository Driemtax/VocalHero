// Authors: Lars Beer, David Herrmann
package model;

import java.security.InvalidParameterException;

public enum Note {
    C2("C2", 65.41),
    Cs2("C#2", 69.30),
    D2("D2", 73.42),
    Ds2("D#2", 77.78),
    E2("E2", 82.41),
    F2("F2", 87.31),
    Fs2("F#2", 92.50),
    G2("G2", 98.00),
    Gs2("G#2", 103.83),
    A2("A2", 110.00),
    As2("A#2", 116.54),
    B2("B2", 123.47),

    C3("C3", 130.81),
    Cs3("C#3", 138.59),
    D3("D3", 146.83),
    Ds3("D#3", 155.56),
    E3("E3", 164.81),
    F3("F3", 174.61),
    Fs3("F#3", 185.00),
    G3("G3", 196.00),
    Gs3("G#3", 207.65),
    A3("A3", 220.00),
    As3("A#3", 233.08),
    B3("B3", 246.94),

    C4("C4", 261.63),
    Cs4("C#4", 277.18),
    D4("D4", 293.66),
    Ds4("D#4", 311.13),
    E4("E4", 329.63),
    F4("F4", 349.23),
    Fs4("F#4", 369.99),
    G4("G4", 392.00),
    Gs4("G#4", 415.30),
    A4("A4", 440.00),
    As4("A#4", 466.16),
    B4("B4", 493.88),

    C5("C5", 523.25),
    Cs5("C#5", 554.37),
    D5("D5", 587.33),
    Ds5("D#5", 622.25),
    E5("E5", 659.25),
    F5("F5", 698.46),
    Fs5("F#5", 739.99),
    G5("G5", 783.99),
    Gs5("G#5", 830.61),
    A5("A5", 880.00),
    As5("A#5", 932.33),
    B5("B5", 987.77),

    C6("C6", 1046.50);

    private final String name;
    private final double frequency;

    Note(String name, double frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public double getFrequency() {
        return frequency;
    }
}
