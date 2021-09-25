package org.firstinspires.ftc.teamcode.testing;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadPlus;

@Disabled
@TeleOp
public class TestGamepadPlus extends OpMode {

    GamepadPlus gp1;
    int timesButtonPressed = 0;
    @Override
    public void init() {
        gp1 = new GamepadPlus(gamepad1);
        gp1.resetTimer();

        telemetry.addData("gp type",gamepad1.type());
        telemetry.update();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loop() {
        gp1.update();

        String button = GamepadPlus.BUTTONS.a.toString();
        if(gp1.wasJustPressed(button)) timesButtonPressed++;



        //Should return 'true' while the button is pressed
        telemetry.addData("Is "+ button +" pressed?",gp1.isPressed(button));
        telemetry.addLine();

        //Should return the amount of milliseconds the button was pressed"
        telemetry.addData("Time "+ button +" pressed",gp1.howLongPressed(button));
        telemetry.addLine();

        //Should return 'true' for the first few milliseconds after the button was pressed
        telemetry.addData("Was "+button+" just pressed?",gp1.wasJustPressed(button));
        telemetry.addLine();

        telemetry.addData("Times " + button + " presses", timesButtonPressed);
        telemetry.addLine();

        telemetry.addData("Timer",gp1.getTimer());
        telemetry.addLine();

        telemetry.addData("Index List",gp1.getCurrentIndexes());
        telemetry.addLine();

        telemetry.addData("Timer list",gp1.getCurrentTimes());
        telemetry.addLine();

        telemetry.addData("Index -1 List",gp1.getPreviousIndexes());
        telemetry.addLine();

        telemetry.addData("Timer -1 list",gp1.getPreviousTimes());
        telemetry.addLine();

        telemetry.addData("Sanity check",gamepad1.a);
        telemetry.update();
    }
}
