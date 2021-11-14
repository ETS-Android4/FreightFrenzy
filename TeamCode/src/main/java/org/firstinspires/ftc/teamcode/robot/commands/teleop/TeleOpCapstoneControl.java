package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapArm;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapGrip;

public class TeleOpCapstoneControl implements Command {
    private CapGrip grip;
    private CapArm arm;
    private Gamepad gamepad;
    private Telemetry telemetry;

    public TeleOpCapstoneControl(CapArm arm, CapGrip grip, Gamepad gamepad, Telemetry telemetry) {
        this.arm = arm;
        this.grip = grip;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpCapstoneControl(CapArm arm, CapGrip grip, Gamepad gamepad) {
        this(arm, grip, gamepad, null);
    }

    @Override
    public void start() {
        grip.grip();
        arm.bottom();
    }

    @Override
    public void periodic() {
        boolean gripperGrip = gamepad.a;
        boolean gripperPlace = gamepad.b;
        boolean armDown = gamepad.x;
        boolean armUp = gamepad.y;

        if(gripperGrip) {
            grip.grip();
        } else if(gripperPlace) {
            grip.periodic();
        }

        if(armUp) {
            arm.top();
        } else if(armDown) {
            arm.bottom();
        }


        if(telemetry != null) {
            telemetry.addLine("Cap telemetry:");
            telemetry.addData("grip grip?", gripperGrip);
            telemetry.addData("grip place?", gripperPlace);
            telemetry.addData("arm down?", armDown);
            telemetry.addData("arm up?", armUp);
            telemetry.addLine();
            telemetry.addData("grip target pos", grip.getTargetPos());
            telemetry.addData("arm target pos", arm.getTargetPos());
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
