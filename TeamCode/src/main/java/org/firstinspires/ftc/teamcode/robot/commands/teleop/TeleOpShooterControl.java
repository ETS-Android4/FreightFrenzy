package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;


public class TeleOpShooterControl implements Command {
    private Shooter shooter;
    private Gamepad gamepad;

    private boolean shootToggleCheck = true;
    private Shooter.MODE shootType;

    public TeleOpShooterControl(Shooter shooter, Gamepad gamepad){
        this.shooter = shooter;
        this.gamepad = gamepad;
        this.shootType = Shooter.MODE.POWER;
    }

    public TeleOpShooterControl(Shooter shooter, Gamepad gamepad, Shooter.MODE type) {
        this.shooter = shooter;
        this.gamepad = gamepad;
        this.shootType = type;
    }

    @Override
    public void start() {
        shooter.setShoot(false);
        if(shootType.getType())
            shooter.setPower(0.85);     // Default Shooter Power
        else
            shooter.setVelocity(0);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        // Far: dUp, Short: dLeft, Power Shot: dDown, Shoot: a (toggle)

        if(gamepad.dpad_up) {
            if (shootType.getType()) {
                shooter.setPower(Shooter.POWER_LEVELS.FAR.getPower());
            } else {
                shooter.setVelocity(Shooter.VELO_LEVELS.FAR.getVelo());
            }
        }
        if(gamepad.dpad_left) {
            if (shootType.getType()) {
                shooter.setPower(Shooter.POWER_LEVELS.SHORT.getPower());
            } else {
                shooter.setVelocity(Shooter.VELO_LEVELS.SHORT.getVelo());
            }
        }
        if(gamepad.dpad_down) {
            if (shootType.getType()) {
                shooter.setPower(Shooter.POWER_LEVELS.POWER_SHOT.getPower());
            } else {
                shooter.setVelocity(Shooter.VELO_LEVELS.POWER_SHOT.getVelo());
            }
        }

        if(gamepad.a && shootToggleCheck) {
            shooter.shoot();
            shootToggleCheck = false;
        } else if(!gamepad.a && !shootToggleCheck) {
            shootToggleCheck = true;
        }
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
