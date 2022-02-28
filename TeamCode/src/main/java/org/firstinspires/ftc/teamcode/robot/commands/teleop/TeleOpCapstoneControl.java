package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CommandDrive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Cap;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapArm;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapGrip;

public class TeleOpCapstoneControl implements Command {
    private Cap cap;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private final double INCREMENT_VAL = 0.00002;

    public TeleOpCapstoneControl(Cap cap, Gamepad gamepad, Telemetry telemetry) {
        this.cap = cap;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public TeleOpCapstoneControl(Cap cap, Gamepad gamepad) {
        this(cap, gamepad, null);
    }

    @Override
    public void start() {
        cap.setPosition(Cap.POSITIONS.IN);
    }

    @Override
    public void periodic() {
        cap.increment(
                gamepad.right_stick_y > CommandDrive.TRIGGER_THRESHOLD ? INCREMENT_VAL :
                gamepad.right_stick_y < -CommandDrive.TRIGGER_THRESHOLD ? -INCREMENT_VAL : 0);

        if(telemetry != null) {
            telemetry.addLine("Cap telemetry:");
            telemetry.addData("pos", cap.getPosition());
            telemetry.addData("direction", cap.getDirection());
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        cap.setPosition(cap.getPosition());
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
