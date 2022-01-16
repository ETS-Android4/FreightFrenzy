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

    private States currentState;
    private double currentTimeout = -1;

    private boolean cancelMove = false;

    private enum States {
        INTAKE,
        LOW_SHARED,
        HIGH_SHARED,
        DEPOSIT,
        MOVING,
        MANUAL_CONTROL;


    }

    private enum TIMEOUTS {
        ARM_INTAKE_TO_MIDDLE,
        DROP;

        public double getTimeout() { // TODO: Check values
            switch (this) {
                case ARM_INTAKE_TO_MIDDLE:
                    return 1.3;
                case DROP:
                    return 0.4;
                default:
                    return 0.0;
            }
        }
    }

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

        currentState = States.INTAKE;
    }

    @Override
    public void periodic() {
        boolean intake      = false;//gamepad.a;    //intake
        boolean middle      = false;//gamepad.x;    //middle
//        boolean deposit     = false;//gamepad.b;    //deposit
        boolean powerIn     = false;//gamepad.dpad_up;      //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean powerOut    = false;//gamepad.dpad_down;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
        boolean encoderIn   = false;//gamepad.left_bumper;
        boolean encoderOut  = false;//gamepad.x; //gamepad.right_bumper;

        boolean setAutoOn   = gamepad.dpad_left;
        boolean setAutoOff  = gamepad.dpad_right;


        boolean dropAndReturn = gamepad.left_bumper;
        boolean cancelMoveAndReturn = gamepad.right_bumper;
        boolean deposit = gamepad.a;
        boolean lowShared = gamepad.b;
        boolean highShared = gamepad.x;

        if(setAutoOn) {
            autoMode = true;
            slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            slides.setPower(POWER_SCALAR);
        }
        if(setAutoOff) {
            autoMode = false;
        }

        double deltaT = timer.seconds() - t0;
        t0 = timer.seconds();

        if(autoMode) {

            //TODO: Make sure this all works
            switch (currentState) {
                case INTAKE:

                    if(oneOfThree(deposit, lowShared, highShared)) { // Only one is pressed
                        goingDown = false;

                        intakeTimer = 0.0;
                        slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        arm.middle();
                    }

                    if(intakeTimer > IN_TIME) {
                        setTarget(deposit, lowShared, highShared);
                    }


                    break;
                case DEPOSIT:

                    if(cancelMoveAndReturn && !goingDown) {
                        cancelMove = true;
                        arm.middle(); // Prob need a fix for if you cancel and are too close to 'middle' s.t. arm hits robot
                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                    }
                    if(cancelMove && slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets() + 50) {
                        if(oneOfThree(deposit, lowShared, highShared)) {
                            cancelMove = false;

                            setTarget(deposit, lowShared, highShared);
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50)) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(dropAndReturn) {
                        //Drop what is used to drop
                        droppingTimer = 0.0;
                        goingDown = true;
                    }

                    if(goingDown) {
                        if(droppingTimer > TIMEOUTS.DROP.getTimeout()) {
                            arm.middle();
                            slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets())) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(lowShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());
                        arm.lowShared();
                        currentState = States.LOW_SHARED;
                    }

                    if(highShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.HIGH_SHARED.getTargets());
                        arm.highShared();
                        currentState = States.HIGH_SHARED;
                    }

                    break;
                case HIGH_SHARED:

                    if(cancelMoveAndReturn && !goingDown) {
                        cancelMove = true;
                        arm.middle(); // Prob need a fix for if you cancel and are too close to 'middle' s.t. arm hits robot
                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                    }
                    if(cancelMove && slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets() + 50) {
                        if(oneOfThree(deposit, lowShared, highShared)) {
                            cancelMove = false;

                            setTarget(deposit, lowShared, highShared);
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50)) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(dropAndReturn) {
                        //Drop what is used to drop
                        droppingTimer = 0.0;
                        goingDown = true;
                    }

                    if(goingDown) {
                        if(droppingTimer > TIMEOUTS.DROP.getTimeout()) {
                            arm.middle();
                            slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets())) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }


                    if(deposit && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
                        arm.deposit();
                        currentState = States.LOW_SHARED;
                    }

                    if(lowShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());
                        arm.lowShared();
                        currentState = States.LOW_SHARED;
                    }

                    break;
                case LOW_SHARED:

                    if(cancelMoveAndReturn && !goingDown) {
                        cancelMove = true;
                        arm.middle(); // Prob need a fix for if you cancel and are too close to 'middle' s.t. arm hits robot
                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                    }
                    if(cancelMove && slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets() + 50) {
                        if(oneOfThree(deposit, lowShared, highShared)) {
                            cancelMove = false;

                            setTarget(deposit, lowShared, highShared);
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets(), 50)) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(dropAndReturn) {
                        //Drop what is used to drop
                        droppingTimer = 0.0;
                        goingDown = true;
                    }

                    if(goingDown) {
                        if(droppingTimer > TIMEOUTS.DROP.getTimeout()) {
                            arm.middle();
                            slides.setTargetPos(Slides.TARGETS.MIDDLE.getTargets());
                        }

                        if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.MIDDLE.getTargets())) {
                            arm.intake();
                            slides.setTargetPos(Slides.TARGETS.IN.getTargets());
                            currentState = States.INTAKE;
                        }
                    }

                    if(deposit && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
                        arm.deposit();
                        currentState = States.LOW_SHARED;
                    }

                    if(highShared && !goingDown) {
                        slides.setTargetPos(Slides.TARGETS.HIGH_SHARED.getTargets());
                        arm.highShared();
                        currentState = States.HIGH_SHARED;
                    }

                    break;
            }

            // Arm pos = f(slide pos) = {intake if slide pos > middle, middle if slideMax < slide pos < middle, out only by input}
//            if(encoderIn ^ encoderOut) {
//                slides.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
//                slides.setPower(POWER_SCALAR);
//                if(encoderOut) {
//                    goingDown = false;
//
//                    intakeTimer = 0.0;
//                }
//            }
//
//            if(slides.getRunMode() == DcMotor.RunMode.RUN_TO_POSITION) {
//                if (goingDown) { // Coming in
//                    if(droppingTimer >= DROP_TIME) {
//                        slides.setTargetPos(Slides.TARGETS.IN.getTargets());
//
//                        if (slides.getCurrentPos() > Slides.TARGETS.MIDDLE.getTargets()) {
//                            arm.intake();
//                        } else {
//                            arm.middle();
//                        }
//
//                    }
//                } else { // Going out
//                    arm.middle();
//
//                    if (intakeTimer >= IN_TIME) {
//                        slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
//
//                        if (encoderIn && util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 30)) {
//                            arm.deposit();
//
//                            droppingTimer = 0.0;
//
//                            goingDown = true;
//                        } else if(encoderIn && !util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets(), 600)) {
//                            goingDown = true;
//                        }
//                    } else if(encoderIn) {
//                        goingDown = true;
//                    }
//
//                }
//
//
////                if(util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.OUT.getTargets()) ||
////                        util.isCloseEnough(slides.getCurrentPos(), Slides.TARGETS.IN.getTargets())) {
////                    slides.setPower(1);
////                }
//            }

            droppingTimer += deltaT;
            intakeTimer += deltaT;



        } else {
            intake      = gamepad.a;    //intake
            middle      = gamepad.x;    //middle
            deposit     = gamepad.b;    //deposit
            powerIn     = gamepad.dpad_up;      //gamepad.left_trigger > CommandDrive.TRIGGER_THRESHOLD;
            powerOut    = gamepad.dpad_down;    //gamepad.right_trigger > CommandDrive.TRIGGER_THRESHOLD;
            encoderIn   = gamepad.left_bumper;
            encoderOut  = gamepad.x; //gamepad.right_bumper;
            lowShared   = gamepad.dpad_left;    // Remove button collisions
            highShared  = gamepad.dpad_right;   // Remove button collisions

            if(middle) {
                arm.middle();
            } else if(intake) {
                arm.intake();
            } else if(deposit) {
                arm.deposit();
            } else if(lowShared) {
                arm.lowShared();
            } else if(highShared) {
                arm.highShared();
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
            telemetry.addData("low shared?", lowShared);
            telemetry.addData("high shared?", highShared);
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
            telemetry.addData("cancelling action?", cancelMoveAndReturn);
            telemetry.addLine();
            telemetry.addData("current state", currentState);
            telemetry.addLine();
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

    private boolean oneOfThree(boolean a, boolean b, boolean c) {
        return (a ^ b) ^ (a ^ c) ^ (b ^ c);
    }

    private void setTarget(boolean deposit, boolean lowShared, boolean highShared) {
        if(deposit) {
            slides.setTargetPos(Slides.TARGETS.OUT.getTargets());
            currentState = States.DEPOSIT;
        }
        if(lowShared) {
            slides.setTargetPos(Slides.TARGETS.LOW_SHARED.getTargets());
            currentState = States.LOW_SHARED;
        }
        if(highShared) {
            slides.setTargetPos(Slides.TARGETS.HIGH_SHARED.getTargets());
            currentState = States.HIGH_SHARED;
        }
    }
}
