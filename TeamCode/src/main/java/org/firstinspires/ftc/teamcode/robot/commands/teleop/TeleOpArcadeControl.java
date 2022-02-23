package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class TeleOpArcadeControl implements Command {
    private Drive drive;
    private Gamepad gamepad;
    private Telemetry telemetry;

    // Constructor
    public TeleOpArcadeControl(Drive drive, Gamepad gamepad, Telemetry telemetry) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }
    public TeleOpArcadeControl(Drive drive, Gamepad gamepad) {
        this(drive ,gamepad, null);
    }

    @Override
    public void start() {
        drive.setPower(0,0);
    }

    @Override
    public void periodic() {
        double speed = gamepad.left_stick_y;
        double turn = gamepad.right_stick_x;
        boolean goSlow = gamepad.left_bumper || gamepad.right_bumper;

        drive.setArcadePower(speed, turn, goSlow);

        if(telemetry != null) {
            telemetry.addLine("Drive Telemetry:");
            telemetry.addData("speed:", speed);
            telemetry.addData("turn:", turn);
            telemetry.addData("goSlow?", goSlow);
            telemetry.addLine();
            telemetry.addData("FL Target:", drive.getCurrentPositions()[0]);
            telemetry.addData("FR Target:", drive.getCurrentPositions()[1]);
            telemetry.addData("RL Target:", drive.getCurrentPositions()[2]);
            telemetry.addData("RR Target:", drive.getCurrentPositions()[3]);
            telemetry.update();

        }

    }

    @Override
    public void stop() {
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted(){
        return false;
    }


















}
