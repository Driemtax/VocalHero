// Authors:David Herrmann, Jonas Rumpf
package model;

public enum Mode {

    NOTE("Einzelnoten"),
    INTERVAL("Intervalle"),
    MELODY("Melodien");

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

    private final String name;

    private Mode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
