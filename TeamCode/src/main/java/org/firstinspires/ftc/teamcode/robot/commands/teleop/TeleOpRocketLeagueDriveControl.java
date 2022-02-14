package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.swampbots_util.TrackRobotPosition;

public class TeleOpRocketLeagueDriveControl implements Command {
    private Drive drive;
    private Gamepad gamepad;
    private Telemetry telemetry;

    // Constructor
    public TeleOpRocketLeagueDriveControl(Drive drive, Gamepad gamepad, Telemetry telemetry) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }
    public TeleOpRocketLeagueDriveControl(Drive drive, Gamepad gamepad) {
        this(drive ,gamepad, null);
    }

    @Override
    public void start() {
        drive.setPower(0,0);
    }

    @Override
    public void periodic() {
        double speed = gamepad.right_trigger - gamepad.left_trigger;
        double turn = gamepad.left_stick_x;
        boolean goSlow = gamepad.left_bumper || gamepad.right_bumper;

        drive.setRocketLeaguePower(speed, turn, goSlow);

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
