package org.firstinspires.ftc.teamcode.swampbots_util;

import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class SwampbotsUtil {


    public SwampbotsUtil() {

    }

    public boolean isCloseEnough(int val1, int val2, int tolerance) {
        return Math.abs(val1 - val2) < tolerance;
    }
    public boolean isCloseEnough(int val1, int val2) {
        return isCloseEnough(val1, val2, 10);
    }

    public int InchToCount(double inch){return (int)(inch* Drive.COUNTS_PER_INCH_EMPIRICAL);}
}
