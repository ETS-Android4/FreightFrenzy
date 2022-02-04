package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Cap;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapArm;
import org.firstinspires.ftc.teamcode.robot.subsystems.CapGrip;

public class TeleOpCapstoneControl implements Command {
    private Cap cap;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private final double POWER_SCALAR = 0.9;
    private final double POWER_PRE_SCALAR = 0.7;

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
        cap.setPower(0.0);
    }

    @Override
    public void periodic() {
        double power = Range.clip(gamepad.right_stick_y / POWER_PRE_SCALAR, -1, 1);

        cap.setPower(power * POWER_SCALAR);

        if(telemetry != null) {
            telemetry.addLine("Cap telemetry:");
            telemetry.addData("power", cap.getPower());
            telemetry.addData("direction", cap.getDirection());
            telemetry.update();
        }
    }

    @Override
    public void stop() {
        cap.setPower(0.0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
