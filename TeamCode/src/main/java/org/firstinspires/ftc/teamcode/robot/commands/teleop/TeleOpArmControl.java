package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

@Config
public class TeleOpArmControl implements Command {
    private Arm arm;
    private Gamepad gamepad;
    private Telemetry telemetry;

    public static double scalar = 0.3;

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

//        arm.power = gamepad.left_stick_y * scalar;
        if(gamepad.right_bumper) {
            arm.setTargetPos(((int) arm.target));
        }
        if(gamepad.left_bumper) {
            arm.setTargetPos(0);
        }
        if(!gamepad.left_bumper && !gamepad.right_bumper) {
            arm.power = 0;
        }

        if(telemetry != null) {
            telemetry.addLine("Arm telemetry:");
            telemetry.addData("intake?", intake);
            telemetry.addData("middle?", middle);
            telemetry.addData("deposit?", deposit);
            telemetry.addLine();
            telemetry.addData("power", arm.power);
            telemetry.addData("current pos", arm.getCurrentPos());
            telemetry.addData("target pos", arm.testTarget);
            telemetry.addData("init pos", arm.getInitialPos());
            telemetry.addData("net counts", arm.getCurrentPos() - arm.getInitialPos());
            telemetry.addData("adj power", arm.calculatePower(arm.getCurrentPos(), arm.getTargetPos().getPosition()));
            telemetry.addLine();
            telemetry.addData("error", arm.error);
            telemetry.addData("kSqrt", arm.kSqrt);
            telemetry.addData("kLog", arm.kLog);
            telemetry.addData("kInv", arm.kInverse);
//            telemetry.addData("pid error", arm.pid.getError());
//            telemetry.addData("pid", arm.pid.get());
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
