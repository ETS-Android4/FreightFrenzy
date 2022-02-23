package org.firstinspires.ftc.teamcode.swampbots_util;

import android.graphics.Color;
import android.util.Pair;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.HexParser;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Triple;

public class SwampbotsUtil {
    public static double COUNTS_PER_INCH = 3000.0 / 83.0;

    public enum COLORS {
        RED, GREEN, BLUE,
        CYAN, YELLOW, MAGENTA,
        WHITE, BLACK,
        LIGHT_GRAY, GRAY, DARK_GRAY,
        RED_TAPE, BLUE_TAPE, BALL, CUBE;

        public Scalar getRGB() {
            switch (this) {
                case RED: return new Scalar(255,0,0);
                case GREEN: return new Scalar(0,255,0);
                case BLUE: return new Scalar(0,0,255);
                case CYAN: return new Scalar(0,255,255);
                case YELLOW: return new Scalar(255,255,0);
                case MAGENTA: return new Scalar(255,0,255);
                case WHITE: return new Scalar(255,255,255);
                case BLACK: return new Scalar(0,0,0);
                case LIGHT_GRAY: return new Scalar(192,192,192);
                case GRAY: return new Scalar(128,128,128);
                case DARK_GRAY: return new Scalar(64,64,64);
                case RED_TAPE: return new Scalar(255,0,0);
                case BLUE_TAPE: return new Scalar(0,0,255);
                case BALL: return new Scalar(255,255,255);
                case CUBE: return new Scalar(255,255,0);
                default: return new Scalar(0,0,0);
            }
        }

        public Scalar getHSV() {
            SwampbotsUtil util = new SwampbotsUtil();
            Scalar rgb = this.getRGB();
            return util.rgbToHsv((float) rgb.val[0], (float) rgb.val[1], (float) rgb.val[2]);
        }

    }

    public enum TIME_UNITS {
        SECONDS,
        MILLISECONDS,
        NANOSECONDS,
        MINUTES;
    }

    public SwampbotsUtil() {

    }

    public boolean isCloseEnough(int val1, int val2, int tolerance) {
        return Math.abs(val1 - val2) < tolerance;
    }
    public boolean isCloseEnough(int val1, int val2) {
        return isCloseEnough(val1, val2, 10);
    }

    public int InchToCount(double inch){return (int)(inch* Drive.COUNTS_PER_INCH_EMPIRICAL);}

    public Scalar rgbToHsv(float r, float g, float b) {
        r = r / 255.0f;
        g = g / 255.0f;
        b = b / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float diff = max - min;
        float h = 0;
        float s;
        float v = max * 100;

        if (max == min)   h = 0;
        else if (max == r) h = (60 * ((g - b) / diff) + 360) % 360;
        else if (max == g) h = (60 * ((b - r) / diff) + 120) % 360;
        else if (max == b) h = (60 * ((r - g) / diff) + 240) % 360;

        if (max == 0) s = 0;
        else s = (diff / max) * 100;

        return new Scalar(h, s, v);
    }

    public Scalar hsvToRgb(float hue, float saturation, float value) {

        int h = (int)((hue % 1) * 6);
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

    public float mean(@NonNull double[] arr) {
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

    public Scalar getClosestRgbColor(Scalar c) {
        Scalar closest = new Scalar(0, 0, 0);
        double minDiff = Double.POSITIVE_INFINITY;
        for (COLORS color : COLORS.values()) {
            Scalar rgb = color.getRGB();
            double diff = 0;
            for(int i = 0; i < 3; i ++) {
                diff += (rgb.val[i] - c.val[i]) * (rgb.val[i] - c.val[i]);
            }
            minDiff = Math.min(minDiff, diff);
            closest = minDiff == diff ? rgb : closest;

        }

        return closest;
    }
    public Scalar getClosestHsvColor(Scalar c) {
        Scalar closest = new Scalar(0, 0, 0);
        double minDiff = Double.POSITIVE_INFINITY;
        for (COLORS color : COLORS.values()) {
            Scalar hsv = color.getHSV();
            double diff = 0;
            for(int i = 0; i < 3; i ++) {
                diff += (hsv.val[i] - c.val[i]) * (hsv.val[i] - c.val[i]);
            }
            minDiff = Math.min(minDiff, diff);
            closest = minDiff == diff ? hsv : closest;

        }

        return closest;
    }

    public static int mean(@NonNull int ...vals) {
        int mu = 0;
        for(int v : vals) {
            mu += v;
        }

        return mu / vals.length;
    }

    public static int inchToCount(double inches) {
        return (int) (inches * COUNTS_PER_INCH);
    }
}
