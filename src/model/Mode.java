// Authors:David Herrmann, Jonas Rumpf
package model;

public enum Mode {

    NOTE,
    INTERVAL,
    MELODY;

    public static Mode fromString(String mode) {
        switch (mode) {
            case "Einzelnote":
                return NOTE;
            case "Intervalle":
                return INTERVAL;
            case "Melodien":
                return MELODY;
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }

}
