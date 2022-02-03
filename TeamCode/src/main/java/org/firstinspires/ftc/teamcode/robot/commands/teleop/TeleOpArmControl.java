package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class TeleOpArmControl implements Command {
    private Arm arm;
    private Gamepad gamepad;
    private Telemetry telemetry;

    public TeleOpArmControl(Arm arm, Gamepad gamepad, Telemetry telemetry) {
        this.arm = arm;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpArmControl(Arm arm, Gamepad gamepad) {
        this(arm, gamepad, null);
    }

    @Override
    public void start() {
        arm.intake();
    }

    @Override
    public void periodic() {
        boolean intake = gamepad.a; //intake
        boolean middle = gamepad.x; //middle
        boolean deposit = gamepad.b; //deposit
        boolean high = gamepad.dpad_up;
        boolean low = gamepad.dpad_down;

        if(middle) {
            arm.middle();
        } else if(intake) {
            arm.intake();
        } else if(deposit) {
            arm.deposit();
        } else if(high) {
            arm.highShared();
        } else if(low) {
            arm.lowShared();
        }

        if(telemetry != null) {
            telemetry.addLine("Arm telemetry:");
            telemetry.addData("intake?", intake);
            telemetry.addData("middle?", middle);
            telemetry.addData("deposit?", deposit);
            telemetry.addLine();
            telemetry.addData("current pos", arm.getCurrentPos());
            telemetry.addData("target pos", arm.getTargetPos().getPosition());
            telemetry.addData("init pos", arm.getInitialPos());
            telemetry.addData("adj power", arm.calculatePower(arm.getCurrentPos(), arm.getTargetPos().getPosition()));
            telemetry.update();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
