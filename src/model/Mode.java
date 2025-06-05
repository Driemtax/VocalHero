// Authors:David Herrmann, Jonas Rumpf
package model;

public enum Mode {

    NOTE("Einzelnoten"),
    INTERVAL("Intervalle"),
    MELODY("Melodien");

    private final String name;

    private Mode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
