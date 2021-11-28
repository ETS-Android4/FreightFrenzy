package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "Test Gamepad Rumble", group = "testing")
public class TestGamepadVibrations extends OpMode {
    private ElapsedTime timer;
    private final Long[] rumbleTimes = {15L,30L,45L,60L,75L,90L,105L,115L,120L,130L,140L,145L};

    private SwampbotsUtil util = new SwampbotsUtil();
    @Override
    public void init() {
        timer = new ElapsedTime();

        telemetry.addData("100,200,40", util.hsvToRgb(100f/256,200f/256,40f/256));
        telemetry.addData("40,120,170", util.hsvToRgb(40f/256,120f/256,120f/256));

        for(int i=0; i<=10; i++) {
            double h = Math.random();
            double s = Math.random();
            double v = Math.random();
            telemetry.addData(util.roundTo(h*256,3)+","+util.roundTo(s*256,3)
                    +","+util.roundTo(v*256, 3)+" ("+util.roundTo(h*256,3)+","+
                    util.roundTo(s*100,3)+","+util.roundTo(v*100,3)+")",
                    util.hsvToRgb(((float) h), (float) s, (float) v));
        }

        telemetry.update();
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        if(gamepad1.a && !gamepad1.isRumbling()) {
            gamepad1.rumble(0.3,0.3,200);
        }

        if(gamepad1.b) {
            timer.reset();
        }

        if(Arrays.asList(rumbleTimes).contains(timer.time(TimeUnit.SECONDS))) {
            gamepad1.rumble(1.0,1.0,500);
        }
    }
}
