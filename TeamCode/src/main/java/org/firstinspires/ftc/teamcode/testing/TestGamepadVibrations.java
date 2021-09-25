package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "Test Gamepad Rumble", group = "testing")
public class TestGamepadVibrations extends OpMode {
    private ElapsedTime timer;
    private long[] rumbleTimes = {15,30,45,60,75,90,105,115,120,130,140,145};

    @Override
    public void init() {
        timer = new ElapsedTime();
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

        if(Arrays.asList().contains(timer.time(TimeUnit.SECONDS))) {
            gamepad1.rumble(1.0,1.0,500);
        }
    }
}
