package org.firstinspires.ftc.teamcode.ug_refrence.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems.Shooter;


public class SoloTeleOpShooterControl implements Command {
    private Shooter shooter;
    private Gamepad gamepad;

    private boolean shootToggleCheck = true;

    public SoloTeleOpShooterControl(Shooter shooter, Gamepad gamepad){
        this.shooter = shooter;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        shooter.setShoot(false);
        shooter.setPower(0.90);     // Default Shooter Power
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        if(gamepad.y && shootToggleCheck) {
            shooter.shoot();
            shootToggleCheck = false;
        } else if(!gamepad.a && !shootToggleCheck) {
            shootToggleCheck = true;
        }

        // Far: dUp, Short: dLeft, Power Shot: dDown, Shoot: a (toggle)
        if(gamepad.dpad_left)     shooter.setPower(.95);
        if(gamepad.dpad_right)   shooter.setPower(.9);
    }

    @Override
    public void stop() {
        shooter.setPower(0);
        shooter.setShoot(false);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
