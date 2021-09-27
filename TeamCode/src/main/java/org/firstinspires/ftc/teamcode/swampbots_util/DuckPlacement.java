package org.firstinspires.ftc.teamcode.swampbots_util;

public enum DuckPlacement {
    LEFT,
    CENTER,
    RIGHT,
    UNKNOWN;

    @Override
    public String toString() {
        switch (this) {
            case LEFT:    return "ZERO";
            case CENTER:      return "ONE";
            case RIGHT:    return "FOUR";
            case UNKNOWN:       return "UNKNOWN";
            default:            return "[Defaulted]";
        }
    }
}
