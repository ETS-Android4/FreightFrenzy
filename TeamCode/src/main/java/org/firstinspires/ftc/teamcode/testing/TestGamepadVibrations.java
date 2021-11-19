package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Disabled
@TeleOp(name = "Test Gamepad Rumble", group = "testing")
public class TestGamepadVibrations extends OpMode {
    private ElapsedTime timer;
    private final Long[] rumbleTimes = {15L,30L,45L,60L,75L,90L,105L,115L,120L,130L,140L,145L};

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

        if(Arrays.asList(rumbleTimes).contains(timer.time(TimeUnit.SECONDS))) {
            gamepad1.rumble(1.0,1.0,500);
        }
    }
}
