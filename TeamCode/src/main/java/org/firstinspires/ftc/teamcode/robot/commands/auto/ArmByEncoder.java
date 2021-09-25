package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class ArmByEncoder implements Command {
    private Arm arm;

    private ElapsedTime timer;
    private double power;
    private int counts;
    private double timeout;
    private Telemetry telemetry;

    private final double DEFAULT_TIMEOUT = 5.0;
    private DcMotor.RunMode prevRunMode;

    public ArmByEncoder(Arm arm, int counts, double power, double timeout){
        timer = new ElapsedTime();

        this.arm = arm;
        this.counts = counts;
        this.power = power;
        this.timeout = timeout;
        telemetry = null;
    }

    public ArmByEncoder(Arm arm, int counts, double power, double timeout, Telemetry telemetry){
        timer = new ElapsedTime();

        this.arm = arm;
        this.counts = counts;
        this.power = power;
        this.timeout = timeout;
        this.telemetry = telemetry;
    }

    public ArmByEncoder(Arm arm, int counts, double power){
        timer = new ElapsedTime();

        this.arm = arm;
        this.counts = counts;
        this.power = power;
        this.timeout = DEFAULT_TIMEOUT;
        telemetry = null;
    }

    @Override
    public void start() {
        timer.reset();

        //int currentPos = arm.getTargetPos();
        prevRunMode = arm.getRunMode();
        arm.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(power);
        arm.setTargetPos(counts);
    }

    @Override
    public void periodic() {
        if(telemetry != null) {
            telemetry.addData("Target pos", arm.getTargetPos());
            telemetry.addData("Current pos", arm.getCurrentPos());
            telemetry.addData("Power", arm.getPower());
            telemetry.addData("Timer", timer.seconds());
            telemetry.addData("Timeout", timeout);
            telemetry.update();
        }
    }

    @Override
    public void stop() {
//        arm.setRunMode(prevRunMode);
//        arm.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return arm.isBusy() || timer.seconds() > timeout;
    }
}
