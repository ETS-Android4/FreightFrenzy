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

        if(middle) {
            arm.middle();
        } else if(intake) {
            arm.intake();
        } else if(deposit) {
            arm.deposit();
        }

        if(telemetry != null) {
            telemetry.addLine("Arm telemetry:");
            telemetry.addData("intake?", intake);
            telemetry.addData("middle?", middle);
            telemetry.addData("deposit?", deposit);
            telemetry.addLine();
            telemetry.addData("target pos", arm.getTargetPos());
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
