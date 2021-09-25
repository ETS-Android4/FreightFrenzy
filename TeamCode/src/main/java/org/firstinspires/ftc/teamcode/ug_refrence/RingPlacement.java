package org.firstinspires.ftc.teamcode.ug_refrence;

public enum RingPlacement {
    ZERO_RINGS,
    ONE_RING,
    FOUR_RINGS,
    UNKNOWN;

    @Override
    public String toString() {
        switch (this) {
            case ZERO_RINGS:    return "ZERO";
            case ONE_RING:      return "ONE";
            case FOUR_RINGS:    return "FOUR";
            case UNKNOWN:       return "UNKNOWN";
            default:            return "[Defaulted]";
        }
    }
}
