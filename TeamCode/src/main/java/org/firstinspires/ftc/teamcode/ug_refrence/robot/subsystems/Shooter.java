package org.firstinspires.ftc.teamcode.ug_refrence.robot.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
public class Shooter implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private DcMotorEx shooter;

    private double      power = POWER_LEVELS.FAR.getPower();
    private boolean     shoot = false;
    private MODE        mode  = MODE.POWER;

    public static double FAR_VELO = 55;
    public static double POWER_SHOT_VELO = 47;
    public static double PIDF0_P = 10.0;
    public static double PIDF1_I = 0;
    public static double PIDF2_D = 0;
    public static double PIDF3_F = 0;

    public enum MODE {
        POWER,
        VELOCITY;

        public boolean getType() {
            return this == POWER;
        }
    }

    public enum POWER_LEVELS {
        FAR,
        SHORT,
        POWER_SHOT, POWER_SHOT_FAR;

        public double getPower() {
            switch (this) {
                case FAR: return 1.0;
                case SHORT: return 0.90;
                case POWER_SHOT: return 0.85;
                case POWER_SHOT_FAR: return 0.87;
                default: return 0.85;
            }
        }
    }

    public enum VELO_LEVELS {
        FAR,
        SHORT,
        POWER_SHOT, POWER_SHOT_FAR;

        public double getVelo() {
            switch (this) {
                case FAR: return FAR_VELO;
                case SHORT: return 50;
                case POWER_SHOT: return POWER_SHOT_VELO;
                case POWER_SHOT_FAR: return POWER_SHOT_VELO + 1;
                default: return 0.85;
            }
        }
    }

    public Shooter(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(PIDF0_P, PIDF1_I, PIDF2_D, PIDF3_F));
    }

    @Override
    public void periodic() {
        if(mode.getType())
            shooter.setPower(power * (shoot ? 1 : 0));
        else
            shooter.setVelocity(power * (shoot ? 1 : 0), AngleUnit.RADIANS);
    }

    public void setPower(double power) {
        this.mode = MODE.POWER;
        this.power = power;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        shooter.setDirection(direction);
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public void setVelocity(double velocity) {
        power = velocity;
        mode = MODE.VELOCITY;
    }

    public void reverse() {
        if(shooter.getDirection() == DcMotorSimple.Direction.FORWARD) shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        else shooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public double getPower() {
        return power;
    }

    public DcMotorSimple.Direction getReverse() {
        return shooter.getDirection();
    }

    public double getVelocity() {
        return shooter.getVelocity(AngleUnit.RADIANS);
    }

    public boolean getShoot() {
        return shoot;
    }

    public void shoot() {
        this.shoot = !this.shoot;
    }
}
