package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CommandDrive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;

public class SlidesByEncoder implements Command {
    private Slides slides;

    private ElapsedTime timer;
    private double power;
    private int counts;
    private double timeout;
    private Telemetry telemetry;

    private DcMotor.RunMode prevRunMode;

    public SlidesByEncoder(Slides slides, int counts, double power, double timeout, Telemetry telemetry){
        timer = new ElapsedTime();

        this.slides = slides;
        this.counts = counts;
        this.power = power;
        this.timeout = timeout;
        this.telemetry = telemetry;
    }

    public SlidesByEncoder(Slides slides, int counts, double power, double timeout){
        this(slides, counts, power, timeout, null);
    }

    public SlidesByEncoder(Slides slides, int counts, double power){
        this(slides, counts, power, CommandDrive.DEFAULT_TIMEOUT, null);
    }

    public SlidesByEncoder(Slides slides, Slides.TARGETS target, double power, double timeout, Telemetry telemetry){
        this(slides, target.getTargets(), power, timeout, telemetry);
    }

    public SlidesByEncoder(Slides slides, Slides.TARGETS target, double power, double timeout){
        this(slides, target.getTargets(), power, timeout, null);
    }

    public SlidesByEncoder(Slides slides, Slides.TARGETS target, double power){
        this(slides, target.getTargets(), power, CommandDrive.DEFAULT_TIMEOUT, null);
    }


    @Override
    public void start() {
        timer.reset();

        //int currentPos = arm.getTargetPos();
        prevRunMode = slides.getRunMode();
        slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(power);
        slides.setTargetPos(counts);
    }

    @Override
    public void periodic() {
        if(telemetry != null) {
            telemetry.addData("Target pos", slides.getTargetPos());
            telemetry.addData("Current pos", slides.getCurrentPos());
            telemetry.addData("Power", slides.getPower());
            telemetry.addData("Timer", timer.seconds());
            telemetry.addData("Timeout", timeout);
            telemetry.update();
        }
    }

    @Override
    public void stop() {
//        slides.setRunMode(prevRunMode);
//        slides.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return !slides.isBusy() || timer.seconds() > timeout;
    }
}
