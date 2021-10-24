package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class TeleOpDriveControl implements Command {
    private Drive drive;
    private Gamepad gamepad;
    private Telemetry telemetry;

    // Constructor
    public TeleOpDriveControl(Drive drive, Gamepad gamepad, Telemetry telemetry) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }
    public TeleOpDriveControl(Drive drive, Gamepad gamepad) {
        this(drive ,gamepad, null);
    }

    @Override
    public void start() {
        drive.setPower(0,0);
    }

    @Override
    public void periodic() {
        double dri = -gamepad.left_stick_y;
        double str = gamepad.left_stick_x;
        double twi = gamepad.right_stick_x;
        boolean goSlow = gamepad.left_bumper;

        drive.setMecanumPower(dri, str, twi, goSlow);

        if(telemetry != null) {
            telemetry.addLine("Drive Telemetry");
            telemetry.addData("dri:", dri);
            telemetry.addData("str:", str);
            telemetry.addData("twi:", twi);
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
