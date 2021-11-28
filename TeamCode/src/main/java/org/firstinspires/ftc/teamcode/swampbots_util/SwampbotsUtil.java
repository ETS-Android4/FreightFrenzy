package org.firstinspires.ftc.teamcode.swampbots_util;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.HexParser;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public Scalar hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToScalar(value, t, p);
            case 1: return rgbToScalar(q, value, p);
            case 2: return rgbToScalar(p, value, t);
            case 3: return rgbToScalar(p, q, value);
            case 4: return rgbToScalar(t, p, value);
            case 5: return rgbToScalar(value, p, q);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public Scalar rgbToScalar(float r, float g, float b) {
        return new Scalar(r * 255, g * 255, b * 255);
    }

    public float mean(double[] arr) {
        float mu = 0;
        for(double d : arr) {
            mu += d;
        }
        return mu/arr.length;
    }

    public float roundTo(float f, int decimals) {
        return (float) (Math.round(f * Math.pow(10, decimals)) * Math.pow(10, -decimals));
    }
    public double roundTo(double d, int decimals) {
        return Math.round(d * Math.pow(10, decimals)) * Math.pow(10, -decimals);
    }
}
