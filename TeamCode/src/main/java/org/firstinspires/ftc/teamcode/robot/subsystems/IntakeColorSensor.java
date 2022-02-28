package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Scalar;

import java.util.ArrayList;

public class IntakeColorSensor implements Subsystem {
    private ColorSensor sensor;
    private HardwareMap hardwareMap;

    private Telemetry telemetry;
//    private ArrayList<int[]> controlValues = new ArrayList<>();
    private final int MAX_SAMPLE_SIZE = 100;
    private int sampleSize = 0;
    private NormalDistribution[] controlDists = new NormalDistribution[4];
    private final double SENSITIVITY = 0.05;

    private final double STDDEV_OVERRIDE = 25.0;

    private Scalar color;

    private long sum_alpha = 0;
    private long sum_square_alpha = 0;
    private long sum_red = 0;
    private long sum_square_red = 0;
    private long sum_green = 0;
    private long sum_square_green = 0;
    private long sum_blue = 0;
    private long sum_square_blue = 0;
    private double mu_alpha = 0;
    private double mu_red = 0;
    private double mu_green = 0;
    private double mu_blue = 0;
    private double std_alpha = 0;
    private double std_red = 0;
    private double std_green = 0;
    private double std_blue = 0;

    private HAS has = HAS.NOTHING;

    public enum HAS {
        CUBE,
        BALL,
        NOTHING;
    }

    public IntakeColorSensor(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public IntakeColorSensor(HardwareMap hardwareMap) {
        this(hardwareMap, null);
    }

    @Override
    public void initHardware() {
        sensor = hardwareMap.get(ColorSensor.class, "intake_color");
        color = new Scalar(0, 0, 0);
    }

    @Override
    public void periodic() {
        color = new Scalar(sensor.red(), sensor.green(), sensor.blue());

        if(sampleSize <= MAX_SAMPLE_SIZE) {
//            controlValues.add(new int[]{sensor.alpha(), sensor.red(), sensor.green(), sensor.blue()});

            int n = ++sampleSize;

            if (n == 0) {
                return;
            }


//            for (int[] i : controlValues) {
                int a = sensor.alpha();//i[0];
                int r = sensor.red();//i[1];
                int g = sensor.green();//i[2];
                int b = sensor.blue();//i[3];

                sum_alpha += a;
                sum_square_alpha += Math.multiplyExact(a, a);
                sum_red += r;
                sum_square_red += Math.multiplyExact(r, r);
                sum_green += g;
                sum_square_green += Math.multiplyExact(g, g);
                sum_blue += b;
                sum_square_blue += Math.multiplyExact(b, b);
//            }

            mu_alpha = ((double) sum_alpha) / n;
            mu_red = ((double) sum_red) / n;
            mu_green = ((double) sum_green) / n;
            mu_blue = ((double) sum_blue) / n;

            // std_x = sqrt(E[x^2]-E[x]^2)
            std_alpha = Math.sqrt(((double) sum_square_alpha) / n - mu_alpha * mu_alpha);
            std_red = Math.sqrt(((double) sum_square_red) / n - mu_red * mu_red);
            std_green = Math.sqrt(((double) sum_square_green) / n - mu_green * mu_green);
            std_blue = Math.sqrt(((double) sum_square_blue) / n - mu_blue * mu_blue);

            if (std_alpha == 0) std_alpha = 1;
            if (std_red == 0) std_red = 1;
            if (std_green == 0) std_green = 1;
            if (std_blue == 0) std_blue = 1;
        }
        if(STDDEV_OVERRIDE > 0) {
            std_alpha   = STDDEV_OVERRIDE;
            std_red     = STDDEV_OVERRIDE;
            std_blue    = STDDEV_OVERRIDE;
            std_green   = STDDEV_OVERRIDE;
        }


//        if(telemetry != null) {
//            telemetry.addData("a", sensor.alpha());
//            telemetry.addData("r", sensor.red());
//            telemetry.addData("g", sensor.green());
//            telemetry.addData("b", sensor.blue());
//            telemetry.addData("n", sampleSize);
//            telemetry.addLine();
//            telemetry.addData("sa",         sum_alpha);
//            telemetry.addData("sa2", sum_square_alpha);
//            telemetry.addData("muA",         mu_alpha);
//            telemetry.addData("stdA",       std_alpha);
//            telemetry.addLine();
//            telemetry.addData("sr",         sum_red);
//            telemetry.addData("sr2", sum_square_red);
//            telemetry.addData("muR",         mu_red);
//            telemetry.addData("stdR",       std_red);
//            telemetry.addLine();
//            telemetry.addData("sg",         sum_green);
//            telemetry.addData("sg2", sum_square_green);
//            telemetry.addData("muG",         mu_green);
//            telemetry.addData("stdG",       std_green);
//            telemetry.addLine();
//            telemetry.addData("sb",         sum_blue);
//            telemetry.addData("sb2", sum_square_blue);
//            telemetry.addData("muB",         mu_blue);
//            telemetry.addData("stdB",       std_blue);
//            telemetry.addLine();
//        }

        try {
            controlDists[0] = new NormalDistribution(mu_alpha, std_alpha);
            controlDists[1] = new NormalDistribution(mu_red, std_red);
            controlDists[2] = new NormalDistribution(mu_green, std_green);
            controlDists[3] = new NormalDistribution(mu_blue, std_blue);
        } catch (Exception e) {
            if(telemetry != null) {
                telemetry.addData("error", e);
            }
        }


        if(telemetry != null) {
            toTelemetry();
            telemetry.update();
        }

//        if(telemetry != null) {
//            telemetry.addData("p(a)=",controlDists[0].cumulativeProbability(sensor.alpha()));
//            telemetry.addData("p(r)=",controlDists[1].cumulativeProbability(sensor.red()));
//            telemetry.addData("p(g)=",controlDists[2].cumulativeProbability(sensor.green()));
//            telemetry.addData("p(b)=",controlDists[3].cumulativeProbability(sensor.blue()));
//            telemetry.addLine();
//            telemetry.addData("q=",
//                    controlDists[0].cumulativeProbability(sensor.alpha()) *
//                            controlDists[1].cumulativeProbability(sensor.red()) *
//                            controlDists[2].cumulativeProbability(sensor.green())); //*
////                            controlDists[3].cumulativeProbability(sensor.blue()));
//            telemetry.addData("p=",1 -
//                    controlDists[0].cumulativeProbability(sensor.alpha()) *
//                            controlDists[1].cumulativeProbability(sensor.red()) *
//                            controlDists[2].cumulativeProbability(sensor.green())); // *
////                            controlDists[3].cumulativeProbability(sensor.blue()));
//            telemetry.addLine();
//            telemetry.addData("do we got something?", doesSensorSeeAnything());
//            telemetry.addData("what do we have?", has);
//
//
//            telemetry.update();
//        }
    }

    public Scalar getColor() {
        return color;
    }

    public int getSensorRed() {
        return sensor.red();
    }

    public int getSensorGreen() {
        return sensor.green();
    }

    public int getSensorBlue() {
        return sensor.blue();
    }

    public int getSensorAlpha() {
        return sensor.alpha();
    }

    public boolean doesSensorSeeAnything() {
        try {
            double pAlpha   = controlDists[0].cumulativeProbability(sensor.alpha());
            double pRed     = controlDists[1].cumulativeProbability(sensor.red());
            double pGreen   = controlDists[2].cumulativeProbability(sensor.green());
            double pBlue    = controlDists[3].cumulativeProbability(sensor.blue());

            double q = pAlpha * pRed * pGreen;
            double p = 1-q;

            if(p < SENSITIVITY) {
//                double muARG = (pAlpha + pRed + pGreen) / 3.0;
//                double sum2ARG = pAlpha * pAlpha + pRed * pRed + pGreen * pGreen;
//                double pBlueDiff = new NormalDistribution(muARG, Math.sqrt(sum2ARG / 3.0 + muARG * muARG)).cumulativeProbability(pBlue);

                double pBlueDiff = pAlpha + pRed + pGreen - 3.0 * pBlue; // sum(pX - pBlue)

                if(telemetry != null) {
                    telemetry.addData("A-B", pAlpha);
                    telemetry.addData("R-B", pRed);
                    telemetry.addData("G-B", pGreen);
                    telemetry.addData("pBlueDiff", pBlueDiff);
                }

                if(pBlueDiff < 0.0001) {
                    has = HAS.BALL;
                } else {
                    has = HAS.CUBE;
                }


                return true;
            }
            has = HAS.NOTHING;
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    public void toTelemetry(Telemetry t) {
        t.addLine("Intake Color Sensor Telemetry:");
        t.addData("a", sensor.alpha());
        t.addData("r", sensor.red());
        t.addData("g", sensor.green());
        t.addData("b", sensor.blue());
        t.addData("n", sampleSize);
        t.addLine();
        t.addData("sa",         sum_alpha);
        t.addData("sa2", sum_square_alpha);
        t.addData("muA",         mu_alpha);
        t.addData("stdA",       std_alpha);
        t.addLine();
        t.addData("sr",         sum_red);
        t.addData("sr2", sum_square_red);
        t.addData("muR",         mu_red);
        t.addData("stdR",       std_red);
        t.addLine();
        t.addData("sg",         sum_green);
        t.addData("sg2", sum_square_green);
        t.addData("muG",         mu_green);
        t.addData("stdG",       std_green);
        t.addLine();
        t.addData("sb",         sum_blue);
        t.addData("sb2", sum_square_blue);
        t.addData("muB",         mu_blue);
        t.addData("stdB",       std_blue);
        t.addLine();

        try {
            t.addData("p(a)=",controlDists[0].cumulativeProbability(sensor.alpha()));
            t.addData("p(r)=",controlDists[1].cumulativeProbability(sensor.red()));
            t.addData("p(g)=",controlDists[2].cumulativeProbability(sensor.green()));
            t.addData("p(b)=",controlDists[3].cumulativeProbability(sensor.blue()));
            t.addLine();
            t.addData("q=",
                    controlDists[0].cumulativeProbability(sensor.alpha()) *
                            controlDists[1].cumulativeProbability(sensor.red()) *
                            controlDists[2].cumulativeProbability(sensor.green())); //*
//                            controlDists[3].cumulativeProbability(sensor.blue()));
            t.addData("p=",1 -
                    controlDists[0].cumulativeProbability(sensor.alpha()) *
                            controlDists[1].cumulativeProbability(sensor.red()) *
                            controlDists[2].cumulativeProbability(sensor.green())); // *
//                            controlDists[3].cumulativeProbability(sensor.blue()));
            t.addLine();
            t.addData("do we got something?", doesSensorSeeAnything());
            t.addData("what do we have?", has);
        } catch (Exception e) {
            t.addData("Error:", e);
        } finally {
            t.addLine();
        }
    }

    public void toTelemetry() {
        if(telemetry != null) {
            toTelemetry(telemetry);
        }
    }

}
