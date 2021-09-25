package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RingPatternPipeline;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;

public class Camera {
    private HardwareMap hardwareMap;

    private OpenCvCamera webcam;
    private RingPatternPipeline vision;

    // HSV Threshold input variables
    private static final double HUE_MAX = 180.0;
    private static final double SAT_MAX = 255.0;
    private static final double VAL_MAX = 255.0;
    private static final double HSV_MIN = 0.0;

    // Initializes HSV values to the range used during testing
    private static double[] hsvHue = new double[]{100.0, 120.0};
    private static double[] hsvSat = new double[]{103.0, 255.0};
    private static double[] hsvVal = new double[]{75.0, 207.0};
    //bot 313.23    left 193.12 right 308.82    top 182.74  bound 253.48
    private static double rectTop   = 182.47;
    private static double rectLeft  = 193.12;
    private static double rectBot   = 313.23;
    private static double rectRight = 308.82;

    private static final int IMG_WIDTH = 480;
    private static final int IMG_HEIGHT = 640;

    private static boolean returnHSV = false;
    private static boolean drawRect = true;
    private static boolean showBlur = false;
    private static boolean showPoint = true;

    private double bound;

    //Outputs
    private Mat blurOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

    public Camera(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        initHardware();
    }

    public void initHardware() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam1"), cameraMonitorViewId);
        webcam.openCameraDevice();
        vision = new RingPatternPipeline();
        webcam.setPipeline(vision);
        webcam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT);

        FtcDashboard.getInstance().startCameraStream(webcam, 20);
    }

    public void periodic() {
        vision.setRectLeft(rectLeft);
        vision.setRectTop(rectTop);
        vision.setRectRight(rectRight);
        vision.setRectBot(rectBot);
        vision.setBound(bound);

        vision.setReturnHSV(returnHSV);
        vision.setDrawRect(drawRect);

        vision.setHsvHueMin(hsvHue[0]);
        vision.setHsvHueMax(hsvHue[1]);
        vision.setHsvSatMin(hsvSat[0]);
        vision.setHsvSatMax(hsvSat[1]);
        vision.setHsvValMin(hsvVal[0]);
        vision.setHsvValMax(hsvVal[1]);

        filterContoursOutput = vision.filterContoursOutput();
    }

    public double[] getHsvHue() {
        return vision.gethsvHue();
    }

    public double[] getHsvSat() {
        return vision.getHsvSat();
    }

    public double[] getHsvVal() {
        return vision.getHsvVal();
    }



    public boolean isDrawRect() {
        return drawRect;
    }


    public double getBound() {
        return bound;
    }

    public ArrayList<MatOfPoint> getFindContoursOutput() {
        return findContoursOutput;
    }

    public static double getRectBot() {
        return rectBot;
    }

    public static double getRectLeft() {
        return rectLeft;
    }

    public static double getRectRight() {
        return rectRight;
    }

    public static double getRectTop() {
        return rectTop;
    }

    public void setHsvHueMin(double hueMin) {
        hsvHue[0] = Math.max(hueMin, HSV_MIN);
    }

    public void setHsvHueMax(double hueMax) {
        hsvHue[1] = Math.min(hueMax, HUE_MAX);
    }

    public void setHsvSatMin(double satMin) {
        hsvSat[0] = Math.max(satMin, HSV_MIN);
    }

    public void setHsvSatMax(double satMax) {
        hsvSat[1] = Math.min(satMax, SAT_MAX);
    }
    public void setHsvValMin(double valMin) {
        hsvVal[0] = Math.max(valMin, HSV_MIN);
    }

    public void setHsvValMax(double valMax) {
        hsvVal[1] = Math.min(valMax, VAL_MAX);
    }



    public static void setRectBot(double rectBot) {
        Camera.rectBot = rectBot;
    }

    public static void setRectLeft(double rectLeft) {
        Camera.rectLeft = rectLeft;
    }

    public static void setRectRight(double rectRight) {
        Camera.rectRight = rectRight;
    }

    public static void setRectTop(double rectTop) {
        Camera.rectTop = rectTop;
    }

    public List<MatOfPoint> getContoursOutput(){
        return filterContoursOutput;
    }

    public void setReturnHSV(boolean returnHSV){
        Camera.returnHSV = returnHSV;
    }

    public void setShowBlur(boolean showBlur) {
        Camera.showBlur = showBlur;
    }

    public void setShowPoint(boolean showPoint) {
        Camera.showPoint = showPoint;
    }

    public void setDrawRect(boolean drawRect) {
        Camera.drawRect = drawRect;
    }

    public void setBound(double bound) {
        this.bound = bound;
    }

}
