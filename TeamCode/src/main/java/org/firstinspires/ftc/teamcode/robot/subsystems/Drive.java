package org.firstinspires.ftc.teamcode.robot.subsystems;

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

public class Drive implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private final double SLOW = 0.4;
    private final double FAST = 1.0;

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

    private DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0.5097); // 509.7 mm TODO: Tune this if needed
    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(1,1,0); //TODO: Tune this


    public static final double COUNTS_PER_INCH_EMPIRICAL = 1000/24.0; // 1000 Counts every 24 inches

    //private final SynchronousPID pid = new SynchronousPID

    // Constructor
    public Drive(HardwareMap hardwareMap, boolean initIMU, boolean initCam){
        this.hardwareMap = hardwareMap;
        this.initIMU = initIMU;
    }

    public Drive(HardwareMap hardwareMap, boolean initIMU) {
       this(hardwareMap,initIMU,false);
    }

    public Drive(HardwareMap hardwareMap) {
        this(hardwareMap, false, false);
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
        rlDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        flDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rrDrive.setDirection(DcMotorSimple.Direction.REVERSE);

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
        this.goSlow = goSlow ? SLOW : FAST;
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
