package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.opencv.core.Scalar;

public class LineColorSensor implements Subsystem {
    private com.qualcomm.robotcore.hardware.ColorSensor sensorRight;
    private com.qualcomm.robotcore.hardware.ColorSensor sensorLeft;
    private HardwareMap hardwareMap;

    private Scalar colorRight;
    private Scalar colorLeft;

    public LineColorSensor(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        sensorRight = hardwareMap.get(ColorSensor.class, "line_right");
        sensorLeft = hardwareMap.get(ColorSensor.class, "line_left");
        colorRight = new Scalar(0, 0, 0);
        colorLeft = new Scalar(0, 0, 0);
    }

    @Override
    public void periodic() {
        colorRight = new Scalar(sensorRight.red(), sensorRight.green(), sensorRight.blue());
        colorLeft = new Scalar(sensorLeft.red(), sensorLeft.green(), sensorLeft.blue());
    }

    public Scalar getColorRight() {
        return colorRight;
    }

    public Scalar getColorLeft() {
        return colorLeft;
    }

    /**
     * Test to see if we are over the colored tape
     *
     * @return First value is confidence of being over red tape. Second value is confidence of being over blue tape.
     */
    public double[] isOverTape() {
        double[] confidence = new double[]{0.0, 0.0};

        double[] normLeft = normalizeByMax(colorLeft, 255.0);
        double[] normRight = normalizeByMax(colorRight, 255.0);

        if(normLeft[0] > 200 && normLeft[1] < 100 && normLeft[2] < 100) {
            confidence[0] = normLeft[0] / 255.0;
        }


        return confidence;
    }

    public double[] normalizeByMax(Scalar scalar) {
        return normalizeByMax(scalar, 1);
    }
    public double[] normalizeByMax(Scalar scalar, double maxVal) {
        double[] vals = scalar.val;
        double max = Math.max(Math.max(vals[0], vals[1]), vals[2]);
        for (int i = 0; i < vals.length; i++) {
            vals[i] *= maxVal / max;
        }

        return vals;
    }

}
