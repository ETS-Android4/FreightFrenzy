package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class CarouselColorSensor implements Subsystem {
    private HardwareMap hardwareMap;
    private ColorSensor sensor;

    private Telemetry telemetry;
    private ArrayList<int[]> controlValues = new ArrayList<>();
    private final int MAX_SAMPLE_SIZE = 100;
    private NormalDistribution[] controlDists = new NormalDistribution[4];

    public CarouselColorSensor(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.telemetry = null;
    }

    public CarouselColorSensor(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    @Override
    public void initHardware() {
       sensor = hardwareMap.get(ColorSensor.class, "color_carousel");
//        controlValues = new ArrayList<int[]>();
//        controlValues.add(new int[]{sensor.alpha(), sensor.red(), sensor.green(), sensor.blue()});

    }

    @Override
    public void periodic() {
       if(controlValues.size() <= MAX_SAMPLE_SIZE) {
           controlValues.add(new int[]{sensor.alpha(), sensor.red(), sensor.green(), sensor.blue()});
       }
        int n = controlValues.size();

       if(n == 0) {
           return;
       }

        int sum_alpha = 0;
        int sum_square_alpha = 0;
        int sum_red = 0;
        int sum_square_red = 0;
        int sum_green = 0;
        int sum_square_green = 0;
        int sum_blue = 0;
        int sum_square_blue = 0;

        for(int[] i : controlValues) {
            int a = i[0];
            int r = i[1];
            int g = i[2];
            int b = i[3];

            sum_alpha += a;
            sum_square_alpha += a * a;
            sum_red += r;
            sum_square_red += r * r;
            sum_green += g;
            sum_square_green += g * g;
            sum_blue += b;
            sum_square_blue += b * b;
        }

        double mu_alpha = ((double) sum_alpha) / n;
        double mu_red   = ((double) sum_red) / n;
        double mu_green = ((double) sum_green) / n;
        double mu_blue  = ((double) sum_blue) / n;

        // std_x = sqrt(E[x^2]-E[x]^2)
        double std_alpha = Math.sqrt(((double) sum_square_alpha) / n - mu_alpha * mu_alpha);
        double std_red   = Math.sqrt(((double) sum_square_red)   / n - mu_red * mu_red);
        double std_green = Math.sqrt(((double) sum_square_green) / n - mu_green * mu_green);
        double std_blue  = Math.sqrt(((double) sum_square_blue)  / n - mu_blue * mu_blue);

        if(std_alpha == 0) std_alpha = 1;
        if(std_red   == 0) std_red   = 1;
        if(std_green == 0) std_green = 1;
        if(std_blue  == 0) std_blue  = 1;

        if(telemetry != null) {
            telemetry.addData("a", sensor.alpha());
            telemetry.addData("r", sensor.red());
            telemetry.addData("g", sensor.green());
            telemetry.addData("b", sensor.blue());
            telemetry.addLine();
            telemetry.addData("sa",         sum_alpha);
            telemetry.addData("sa2", sum_square_alpha);
            telemetry.addData("muA",         mu_alpha);
            telemetry.addData("stdA",       std_alpha);
            telemetry.addLine();
            telemetry.addData("sr",         sum_red);
            telemetry.addData("sr2", sum_square_red);
            telemetry.addData("muR",         mu_red);
            telemetry.addData("stdR",       std_red);
            telemetry.addLine();
            telemetry.addData("sg",         sum_green);
            telemetry.addData("sg2", sum_square_green);
            telemetry.addData("muG",         mu_green);
            telemetry.addData("stdG",       std_green);
            telemetry.addLine();
            telemetry.addData("sb",         sum_blue);
            telemetry.addData("sb2", sum_square_blue);
            telemetry.addData("muB",         mu_blue);
            telemetry.addData("stdB",       std_blue);
            telemetry.addLine();
        }

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
            telemetry.addData("p(a)=",controlDists[0].cumulativeProbability(sensor.alpha()));
            telemetry.addData("p(r)=",controlDists[1].cumulativeProbability(sensor.red()));
            telemetry.addData("p(g)=",controlDists[2].cumulativeProbability(sensor.green()));
            telemetry.addData("p(b)=",controlDists[3].cumulativeProbability(sensor.blue()));
            telemetry.addLine();
            telemetry.addData("q=",
                    controlDists[0].cumulativeProbability(sensor.alpha()) *
                    controlDists[1].cumulativeProbability(sensor.red()) *
                    controlDists[2].cumulativeProbability(sensor.green()) *
                    controlDists[3].cumulativeProbability(sensor.blue()));
            telemetry.addData("p=",1 -
                    controlDists[0].cumulativeProbability(sensor.alpha()) *
                    controlDists[1].cumulativeProbability(sensor.red()) *
                    controlDists[2].cumulativeProbability(sensor.green()) *
                    controlDists[3].cumulativeProbability(sensor.blue()));


            telemetry.update();
        }



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

            double q = pAlpha * pRed * pGreen * pBlue;
            double p = 1-q;
            return p < 0.001;
        } catch (Exception e) {
            return false;
        }

    }

}
