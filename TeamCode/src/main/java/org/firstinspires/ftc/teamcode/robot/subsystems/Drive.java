package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.DifferentialDriveKinematics;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.DifferentialDriveWheelSpeeds;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.swampbots_util.Units;

@Config
public class Drive implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private final double SLOW = 0.4;
    private final double FAST = 1.0;

    private enum SPEEDS {
        SLOW, FAST,
        RL_SLOW;

        public double getSpeed() {
            switch (this) {
                case SLOW:
                    return 0.4;
                case FAST:
                    return 1.0;
                case RL_SLOW:
                    return slowModeValue;
                default:
                    return SPEEDS.FAST.getSpeed();
            }
        }
    }

    // Components
    private DcMotor flDrive;
    private DcMotor frDrive;
    private DcMotor rlDrive;
    private DcMotor rrDrive;
    private BNO055IMU imu;

    // State and interface variables
    private double flPower = 0;
    private double frPower = 0;
    private double rlPower = 0;
    private double rrPower = 0;
    private double goSlow = FAST;
    private boolean initIMU;

    private DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0.4064); // 509.7 mm TODO: Tune this if needed
    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(1,1,0); //TODO: Tune this

    private static final double MOTOR_COUNTS_PER_REV = 100548.0 / 187.0 / 4.0; // ~537.69 ppr / 4 counts/pulse
    private static final double MOTOR_REV_TO_WHEEL_REV = 1.0 / 1.0 * 27.0 / 17.0; // 1:1 bevel & 27:17 pulley
    private static final double WHEEL_REV_TO_METERS = Math.PI * 72.0 / 1000.0; // ~0.226 m, 8.9 in
    public static final double COUNTS_PER_INCH_EMPIRICAL = MOTOR_COUNTS_PER_REV * MOTOR_REV_TO_WHEEL_REV * Units.metersToInches(WHEEL_REV_TO_METERS); // 1000 Counts every 24 inches
    public static final int NUMBER_OF_ENCODERS = 4;

    // pubic static variables for Dashboard
    public static double turningMultiplier = 0.5;
    public static double movingTurnMultiplier = 1.0;
    public static double speedMultiplier = 1.0;
    public static double movingDeadzone = 0.05;
    public static double slowModeValue = 0.3;

    //private final SynchronousPID pid = new SynchronousPID

    // Constructor
    public Drive(HardwareMap hardwareMap, boolean initIMU){
        this.hardwareMap = hardwareMap;
        this.initIMU = initIMU;
    }

    public Drive(HardwareMap hardwareMap) {
        this(hardwareMap, false);
    }

    @Override
    public void initHardware() {
        flDrive = hardwareMap.get(DcMotor.class, "fl_drive");
        frDrive = hardwareMap.get(DcMotor.class, "fr_drive");
        rlDrive = hardwareMap.get(DcMotor.class, "rl_drive");
        rrDrive = hardwareMap.get(DcMotor.class, "rr_drive");

        if(initIMU){
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile  = "BNO055IMUCalibration.json";
            parameters.loggingEnabled       = true;
            parameters.loggingTag           = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
        }


        // Reverse front right
//        frDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rlDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        flDrive.setDirection(DcMotorSimple.Direction.REVERSE);
//        rrDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    @Override
    public void periodic() {
        flDrive.setPower(flPower);
        frDrive.setPower(frPower);
        rlDrive.setPower(rlPower);
        rrDrive.setPower(rrPower);

    }

    /**
     * Sets power to the motors on opposite sides. Functions the same as normal drive.
     *
     * @param leftPower power for the left motors
     * @param rightPower power for the right motors
     */
    public void setPower(double leftPower, double rightPower) {
        this.flPower = leftPower;
        this.frPower = rightPower;
        this.rlPower = leftPower;
        this.rrPower = rightPower;
    }

    /**
     * Sets power to the motors on opposite sides. Functions the same as normal drive.
     *
     * @param flPower power for the front left motor
     * @param frPower power for the front right motor
     * @param rlPower power for the rear left motor
     * @param rrPower power for the rear right motor
     */
    public void setPower(double flPower, double frPower, double rlPower, double rrPower) {
        this.flPower = flPower;
        this.frPower = frPower;
        this.rlPower = rlPower;
        this.rrPower = rrPower;
    }

    /**
     * Sets the power to motors diagonal from each other. Function the same as strafing.
     *
     * @param fl_rrPower power for the Front Left and Back Right motors
     * @param fr_rlPower power for the Front Right and Back Left motors
     */
    public void setDiagonalPower(double fl_rrPower, double fr_rlPower) {
        this.flPower = fl_rrPower;
        this.frPower = fr_rlPower;
        this.rlPower = fr_rlPower;
        this.rrPower = fl_rrPower;
    }

    /**
     * Sets the appropriate power for Mecanum Drive.
     *
     * @param drive power for front and back movement
     * @param strafe power for left and right movement
     * @param twist power for turning
     * @param goSlow sets the speed multiplier
     */
    public void setMecanumPower(double drive, double strafe, double twist, boolean goSlow) {
        this.goSlow = goSlow ? SPEEDS.SLOW.getSpeed() : SPEEDS.FAST.getSpeed();
        flPower = (drive + strafe + twist) * this.goSlow;
        frPower = (drive - strafe - twist) * this.goSlow;
        rlPower = (drive - strafe + twist) * this.goSlow;
        rrPower = (drive + strafe - twist) * this.goSlow;
    }

    /**
     * Set the velocity of each motor using a Feedforward controller. Functions similar to normal driving.
     *
     * @param leftVelocity velocity for left motors
     * @param rightVelocity velocity for right motors
     */
    public void setFeedforwardPower(double leftVelocity, double rightVelocity) {
        setPower(feedforward.calculate(leftVelocity), feedforward.calculate(rightVelocity));
    }

    /**
     * Set the velocity of each motor using a Feedforward controller. Functions similar to normal driving.
     *
     * @param wheelSpeeds velocity for each motor
     */
    public void setFeedforwardPower(DifferentialDriveWheelSpeeds wheelSpeeds) {
        setFeedforwardPower(wheelSpeeds.leftMetersPerSecond, wheelSpeeds.rightMetersPerSecond);
    }

    /**
     * Sets the power for a Differential Drive train inspired by Rocket League
     *
     * @param speed how fast to move forward and back
     * @param turn how fast to turn
     * @param goSlow scale total speed down
     */
    public void setRocketLeaguePower(double speed, double turn, boolean goSlow) {
//        this.goSlow = goSlow ? SLOW : FAST;
//        speed   = speed * this.goSlow;
//        turn    = turn * this.goSlow;

        // Change turning speed for if you are moving or not
        if(Math.abs(speed) < movingDeadzone) {
            // Assume "stationary" turning
            speed   = speed * speedMultiplier;
            turn    = turn  * turningMultiplier;
        } else {
            // Assume "moving" turning
            speed   = speed * speedMultiplier;
            turn    = turn  * movingTurnMultiplier;
        }

        this.goSlow = goSlow ? SPEEDS.RL_SLOW.getSpeed() : SPEEDS.FAST.getSpeed();
        speed   = speed * this.goSlow;
        turn    = turn  * this.goSlow;

        if(speed > 0.0) {
            setPower(speed - turn, speed + turn);
        } else {
            setPower(speed + turn, speed - turn);
        }
    }

    /**
     * Sets the targets for each motor
     *
     * @param flTarget target for Front Left motor
     * @param frTarget target for Front Right motor
     * @param rlTarget target for Back Left motor
     * @param rrTarget target for Back Right motor
     */
    public void setTargets(int flTarget, int frTarget, int rlTarget, int rrTarget) {
        flDrive.setTargetPosition(flTarget);
        frDrive.setTargetPosition(frTarget);
        rlDrive.setTargetPosition(rlTarget);
        rrDrive.setTargetPosition(rrTarget);
    }

    /**
     * Sets the Run Mode for all four motors
     *
     * @param runMode the Run Mode
     */
    public void setRunMode(DcMotor.RunMode runMode) {
        flDrive.setMode(runMode);
        frDrive.setMode(runMode);
        rlDrive.setMode(runMode);
        rrDrive.setMode(runMode);
    }

    /**
     * Stop all drive motors
     */
    public void stop() {
        flDrive.setPower(0);
        frDrive.setPower(0);
        rlDrive.setPower(0);
        rrDrive.setPower(0);
    }

    /**
     * Gets the current positions of all drive motors
     *
     * @return an int[] of motor positions
     */
    public int[] getCurrentPositions() {
        return new int[]{flDrive.getCurrentPosition(), frDrive.getCurrentPosition(), rlDrive.getCurrentPosition(), rrDrive.getCurrentPosition()};
    }

    public int inchToCounts(double inches) {
        return (int) (inches / COUNTS_PER_INCH_EMPIRICAL);
    }

    /**
     * Gets the current Run Mode
     *
     * @return the current Run Mode of all motors
     */
    public DcMotor.RunMode getRunMode() {
        return flDrive.getMode();
    }

    /**
     * Check to see if all drive motor are finished running
     *
     * @return a boolean if all motors are finished running
     */
    public boolean driveIsBusy() {
        // If these are combined with || (ORs), then it waits for all four motors to finish. We are switching to && (ANDs) so that only one motor need finish
        return flDrive.isBusy() || frDrive.isBusy() || rlDrive.isBusy() || rrDrive.isBusy(); //Edited by Blake on Jan 16 2021
//        return flDrive.isBusy() && frDrive.isBusy() && rlDrive.isBusy() && rrDrive.isBusy();  // Reflipped order Feb 27 2021
    }

    /**
     * Gets the heading of the robot
     *
     * @return a float of the heading
     */
    public float heading(){
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    /**
     * Normalizes the input to be within the range (-180, 180]
     *
     * @author Blake
     * @param angle the value to be normalized
     * @return the normalized value
     */
    public double normalize180(double angle) {
        while(angle > 180) {
            angle -= 360;
        }
        while(angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    public void reverseAllMotors(){
        for(DcMotor m : new DcMotor[]{flDrive, frDrive, rlDrive, rrDrive}){
            if(m.getDirection() == DcMotorSimple.Direction.FORWARD) m.setDirection(DcMotorSimple.Direction.REVERSE);
            else m.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }

    public void reverseFlDrive(){
        if(flDrive.getDirection() == DcMotorSimple.Direction.FORWARD) flDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        else flDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public DifferentialDriveKinematics getKinematics() {
        return kinematics;
    }

    public SimpleMotorFeedforward getFeedforward() {
        return feedforward;
    }
}
