package org.firstinspires.ftc.teamcode.swampbots_util;

public enum DuckPlacement {
    LEFT,
    CENTER,
    RIGHT,
    UNKNOWN;

    @Override
    public String toString() {
        switch (this) {
            case LEFT:    return "LEFT";
            case CENTER:      return "CENTER";
            case RIGHT:    return "RIGHT";
            case UNKNOWN:       return "UNKNOWN";
            default:            return "[Defaulted]";
        }
    }
}
