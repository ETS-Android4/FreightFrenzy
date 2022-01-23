package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Carousel;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;

public class RunCarouselForTime implements Command {
    private Carousel carousel;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public RunCarouselForTime(Carousel carousel, double seconds, double power){
        this.carousel = carousel;
        this.seconds = seconds;
        this.power = power;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
        carousel.setDirection(CRServo.Direction.FORWARD);
        carousel.setPower(power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        carousel.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}
