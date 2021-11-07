package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Slides;
import org.firstinspires.ftc.teamcode.swampbots_util.SwampbotsUtil;

public class TeleOpArmSlideControl implements Command {
    private Arm arm;
    private Slides slides;
    private Gamepad gamepad;
    private Telemetry telemetry;

    private ElapsedTime timer;
    private double t0;
    
    private SwampbotsUtil util;

    private boolean autoMode = true;
    private double droppingTimer = -1;
    private double intakeTimer = -1;
    private boolean goingDown = false;

    private final double DROP_TIME = 1.0;   //seconds
    private final double IN_TIME = 0.8;
    private final double POWER_SCALAR = 0.7;

    public TeleOpArmSlideControl(Arm arm, Slides slides, Gamepad gamepad, Telemetry telemetry) {
        this.arm = arm;
        this.slides = slides;
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        timer = new ElapsedTime();
        util = new SwampbotsUtil();
    }

    public TeleOpArmSlideControl(Arm arm, Slides slides, Gamepad gamepad) {
        this(arm, slides, gamepad, null);
    }

    @Override
    public void start() {
        arm.intake();

        slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.setPower(0.0);

        timer.reset();
        t0 = timer.seconds();
        droppingTimer = DROP_TIME;
        intakeTimer = IN_TIME;
    }

    @Override
    public void periodic() {
        boolean intake      = gamepad.a;    //intake
        boolean middle      = gamepad.x;    //middle
        boolean deposit     = gamepad.b;    //deposit
        boolean powerIn     = gamepad.dpad_up;      //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean powerOut    = gamepad.dpad_down;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean encoderIn   = gamepad.left_bumper;
        boolean encoderOut  = gamepad.right_bumper;

        boolean setAutoOn   = gamepad.dpad_left;
        boolean setAutoOff  = gamepad.dpad_right;

        if(setAutoOn) {
            autoMode = true;
        }
        if(setAutoOff) {
            autoMode = false;
        }

        double deltaT = timer.seconds() - t0;
        t0 = timer.seconds();

        if(autoMode) {
            // Arm pos = f(slide pos) = {intake if slide pos > middle, middle if slideMax < slide pos < middle, out only by input}
            if(encoderIn ^ encoderOut) {
                slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
                slides.setPower(POWER_SCALAR);
                if(encoderOut) {
                    goingDown = false;

                    intakeTimer = 0.0;
                }
            }

            if(slides.getRunMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                if (goingDown) { // Coming in
                    if(droppingTimer >= DROP_TIME) {
                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());

                        if (slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets()) {
                            arm.intake();
                        } else {
                            arm.middle();
                        }

                    }
                } else { // Going out
                    arm.middle();

                    if (intakeTimer >= IN_TIME) {
                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());

                        if (encoderIn && util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 30)) {
                            arm.deposit();

                            droppingTimer = 0.0;

                            goingDown = true;
                        } else if(encoderIn && !util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 600)) {
                            goingDown = true;
                        }
                    } else if(encoderIn) {
                        goingDown = true;
                    }

                }


//                if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets()) ||
//                        util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.IN.getTargets())) {
//                    slides.setPower(1);
//                }
            }

            droppingTimer += deltaT;
            intakeTimer += deltaT;



        } else {
            if(middle) {
                arm.middle();
            } else if(intake) {
                arm.intake();
            } else if(deposit) {
                arm.deposit();
            }
            if(powerIn || powerOut) {
                slides.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slides.setPower(((powerIn ? 1 : 0) - (powerOut ? 1 : 0)) * POWER_SCALAR);
            } else if(encoderIn ^ encoderOut) {
                slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
                slides.setPower(POWER_SCALAR);
                if(encoderIn) {
                    slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                }
                if(encoderOut) {
                    slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
                }
            } else /*if(slides.getRunMode() != DcMotor.RunMode.RUN_TO_POSITION)*/{ //TODO: Uncomment once we finish testing
                slides.setPower(0);
            }
        }




        if(telemetry != null) {
            telemetry.addLine("Arm telemetry:");
            telemetry.addData("intake?", intake);
            telemetry.addData("middle?", middle);
            telemetry.addData("deposit?", deposit);
            telemetry.addLine();
            telemetry.addData("target pos:", arm.getTargetPos());
            telemetry.addLine();

            telemetry.addLine("Slide Telemetry:");
            telemetry.addData("power in?", powerIn);
            telemetry.addData("power out?",powerOut);
            telemetry.addData("encoder in?", encoderIn);
            telemetry.addData("encoder out?",encoderOut);
            telemetry.addLine();
            telemetry.addData("power:", slides.getPower());
            telemetry.addData("current pos:", slides.getCurrentPos());
            telemetry.addData("target pos:", slides.getTargetPos());
            telemetry.addData("runMode:", slides.getRunMode());
            telemetry.addLine();

            telemetry.addLine("FSM Telemetry:");
            telemetry.addData("fsm active?", autoMode);
            telemetry.addData("going down?", goingDown);
            telemetry.addData("delta t", deltaT);
            telemetry.addData("t0", t0);
            telemetry.addData("dropping timer", droppingTimer);
            telemetry.addData("intake timer", intakeTimer);

            telemetry.update();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }

}
