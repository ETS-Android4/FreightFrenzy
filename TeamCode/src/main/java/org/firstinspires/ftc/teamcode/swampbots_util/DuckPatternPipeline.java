package org.firstinspires.ftc.teamcode.swampbots_util;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class DuckPatternPipeline extends OpenCvPipeline {
    // TODO: Check values
    private static double[] hsvHue = new double[]{80.0, 119.0};     // Starts with red outside of threshold (red is hue of 120)
    private static double[] hsvSat = new double[]{105.0, 255.0};
    private static double[] hsvVal = new double[]{50.0, 255.0};      // Testing showed 50 (min) to be enough to cut out most of the Skystone
//bot 313.23    left 193.12 right 308.82    top 182.74  bound 253.48

    // TODO: Check values
    private static double rectTop   = 182.74;
    private static double rectLeft  = 193.12;
    private static double rectBot   = 313.23;
    private static double rectRight = 308.82;

    private static double boundLeft = 253.48;
    private static double boundRight= 300.00;

    private static boolean returnHSV = false;
    private static boolean drawRect = true;
    private static boolean showBlur = false;
    private static boolean showPoint = true;



    //Outputs
    private Mat blurOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

    public static ArrayList<Point> rectPoints = new ArrayList<Point>();

    @Override
    public Mat processFrame(Mat input) {

        // Step HSV_Threshold0:

        double[] hsvThresholdHue =          hsvHue;
        double[] hsvThresholdSaturation =   hsvSat;
        double[] hsvThresholdValue =        hsvVal;
        Mat hsvThresholdInput = input;
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);


        // Step Blur0:
        Mat blurInput = input;
        GripDuckPipeline.BlurType blurType = GripDuckPipeline.BlurType.get("Gaussian Blur");
        double blurRadius = 11;
        blur(blurInput, blurType, blurRadius, blurOutput);

        // Step HSV_Threshold0:
        hsvThresholdInput = blurOutput;
        //double[] hsvThresholdHue = {10.154094476228165, 24.880546075085334};
        //double[] hsvThresholdSaturation = {153.64208633093526, 255.0};
        //double[] hsvThresholdValue = {55.03597122302158, 207.13310580204777};
        hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);


        // Step Find_Contours0:
        Mat findContoursInput = hsvThresholdOutput;
        boolean findContoursExternalOnly = false;
        findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

        // Step Filter_Contours0:
        ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
        double filterContoursMinArea = 500.0;
        double filterContoursMinPerimeter = 100.0;
        double filterContoursMinWidth = 2.0;
        double filterContoursMaxWidth = 1000;
        double filterContoursMinHeight = 0.0; //0.0 => 0 & 1, 200.0 => 4
        double filterContoursMaxHeight = 1000;
        double[] filterContoursSolidity = {0.0, 100};
        double filterContoursMaxVertices = 1000000;
        double filterContoursMinVertices = 0;
        double filterContoursMinRatio = 0;
        double filterContoursMaxRatio = 1000;
        filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);





        if(returnHSV)
            return drawExtras(hsvThresholdOutput);
        if(showBlur)
            return drawExtras(blurOutput);
        return drawExtras(input);
    }

    private Mat drawExtras(Mat mat) {
        if(drawRect) {
            Imgproc.rectangle(
                    mat,
                    new Point(  // Top left corner
                            rectLeft,   // Left value
                            rectTop),   // Top value
                    new Point( // Bottom right corner
                            rectRight,  // Right value
                            rectBot),   // Bottom value
                    new Scalar(0, 255, 0), 10); // Increased thickness to make solid bound around rings

            Imgproc.line(
                    mat,
                    new Point(
                            boundLeft,
                            rectTop),
                    new Point(
                            boundLeft,
                            rectBot),
                    new Scalar(40, 150, 190), 2);  // Changed color to roughly match rings
            Imgproc.line(
                    mat,
                    new Point(
                            boundRight,
                            rectLeft),
                    new Point(
                            boundRight,
                            rectBot),
                    new Scalar(40, 150, 190), 2);
        }

        if(showPoint) {
            ArrayList<Point> tempPoints = rectPoints;

            // Draw top points of Rectangles on screen
            for (int i = 0; i < tempPoints.size(); i++) {
                Imgproc.circle(
                        mat,
                        tempPoints.get(i),
                        5,
                        new Scalar(0, 0, 255),
                        3);
            }
        }

        return mat;
    }

    /**
     * This method is a generated getter for the output of a Blur.
     * @return Mat output from Blur.
     */
    public Mat blurOutput() {
        return blurOutput;
    }

    /**
     * This method is a generated getter for the output of a HSV_Threshold.
     * @return Mat output from HSV_Threshold.
     */
    public Mat hsvThresholdOutput() {
        return hsvThresholdOutput;
    }

    /**
     * This method is a generated getter for the output of a Find_Contours.
     * @return ArrayList<MatOfPoint> output from Find_Contours.
     */
    public ArrayList<MatOfPoint> findContoursOutput() {
        return findContoursOutput;
    }

    /**
     * This method is a generated getter for the output of a Filter_Contours.
     * @return ArrayList<MatOfPoint> output from Filter_Contours.
     */
    public ArrayList<MatOfPoint> filterContoursOutput() {
        return filterContoursOutput;
    }

    /**
     * An indication of which type of filter to use for a blur.
     * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
     */
    enum BlurType{
        BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
        BILATERAL("Bilateral Filter");

        private final String label;

        BlurType(String label) {
            this.label = label;
        }

        public static BlurType get(String type) {
            if (BILATERAL.label.equals(type)) {
                return BILATERAL;
            }
            else if (GAUSSIAN.label.equals(type)) {
                return GAUSSIAN;
            }
            else if (MEDIAN.label.equals(type)) {
                return MEDIAN;
            }
            else {
                return BOX;
            }
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Softens an image using one of several filters.
     * @param input The image on which to perform the blur.
     * @param type The blurType to perform.
     * @param doubleRadius The radius for the blur.
     * @param output The image in which to store the output.
     */
    private void blur(Mat input, GripDuckPipeline.BlurType type, double doubleRadius,
                      Mat output) {
        int radius = (int)(doubleRadius + 0.5);
        int kernelSize;
        switch(type){
            case BOX:
                kernelSize = 2 * radius + 1;
                Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
                break;
            case GAUSSIAN:
                kernelSize = 6 * radius + 1;
                Imgproc.GaussianBlur(input,output, new Size(kernelSize, kernelSize), radius);
                break;
            case MEDIAN:
                kernelSize = 2 * radius + 1;
                Imgproc.medianBlur(input, output, kernelSize);
                break;
            case BILATERAL:
                Imgproc.bilateralFilter(input, output, -1, radius, radius);
                break;
        }
    }

    /**
     * Segment an image based on hue, saturation, and value ranges.
     *
     * @param input The image on which to perform the HSL threshold.
     * @param hue The min and max hue
     * @param sat The min and max saturation
     * @param val The min and max value
     * @param out The image in which to store the output.
     */
    private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
                              Mat out) {
        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
                new Scalar(hue[1], sat[1], val[1]), out);
    }

    /**
     * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
     * @param input The image on which to perform the Distance Transform.
     * //@param type The Transform.
     * //@param maskSize the size of the mask.
     * //@param output The image in which to store the output.
     */
    private void findContours(Mat input, boolean externalOnly,
                              List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        }
        else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }


    /**
     * Filters out contours that do not meet certain criteria.
     * @param inputContours is the input list of contours
     * @param output is the the output list of contours
     * @param minArea is the minimum area of a contour that will be kept
     * @param minPerimeter is the minimum perimeter of a contour that will be kept
     * @param minWidth minimum width of a contour
     * @param maxWidth maximum width
     * @param minHeight minimum height
     * @param maxHeight maximimum height
     * @param solidity the minimum and maximum solidity of a contour
     * @param minVertexCount minimum vertex Count of the contours
     * @param maxVertexCount maximum vertex Count
     * @param minRatio minimum ratio of width to height
     * @param maxRatio maximum ratio of width to height
     */
    private void filterContours(List<MatOfPoint> inputContours, double minArea,
                                double minPerimeter, double minWidth, double maxWidth, double minHeight, double
                                        maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
                                        minRatio, double maxRatio, List<MatOfPoint> output) {
        final MatOfInt hull = new MatOfInt();
        output.clear();
        //operation
        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);
            final Rect bb = Imgproc.boundingRect(contour);
            if (bb.width < minWidth || bb.width > maxWidth) continue;
            if (bb.height < minHeight || bb.height > maxHeight) continue;
            final double area = Imgproc.contourArea(contour);
            if (area < minArea) continue;
            if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
            Imgproc.convexHull(contour, hull);
            MatOfPoint mopHull = new MatOfPoint();
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++) {
                int index = (int)hull.get(j, 0)[0];
                double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
                mopHull.put(j, 0, point);
            }
            final double solid = 100 * area / Imgproc.contourArea(mopHull);
            if (solid < solidity[0] || solid > solidity[1]) continue;
            if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
            final double ratio = bb.width / (double)bb.height;
            if (ratio < minRatio || ratio > maxRatio) continue;
            output.add(contour);
        }
    }



    //----------------------------------------------------------------------------------------------
    // Getters
    //----------------------------------------------------------------------------------------------

    public double[] gethsvHue() { return hsvHue;}
    public double[] getHsvSat() { return hsvSat;}
    public double[] getHsvVal() { return hsvVal;}

    public double getRectTop()      { return rectTop;}
    public double getRectLeft()     { return rectLeft;}
    public double getRectBot()      { return rectBot;}
    public double getRectRight()    { return rectRight;}

    public double getLeftBound()        { return boundLeft;}
    public double getBoundRight() {
        return boundRight;
    }

    public List<MatOfPoint> getFilteredContours()   {return filterContoursOutput;}




    //----------------------------------------------------------------------------------------------
    // Setters
    //----------------------------------------------------------------------------------------------

    public void setHsvHueMin(double hsvHueMin) { hsvHue[0] = hsvHueMin;}
    public void setHsvHueMax(double hsvHueMax) { hsvHue[1] = hsvHueMax;}

    public void setHsvSatMin(double hsvSatMin) { hsvSat[0] = hsvSatMin;}
    public void setHsvSatMax(double hsvSatMax) { hsvSat[1] = hsvSatMax;}

    public void setHsvValMin(double hsvValMin) { hsvVal[0] = hsvValMin;}
    public void setHsvValMax(double hsvValMax) { hsvVal[1] = hsvValMax;}

    public void setRectTop(double top)      { rectTop   =     top;}
    public void setRectLeft(double left)    { rectLeft  =    left;}
    public void setRectBot(double bot)      { rectBot   =     bot;}
    public void setRectRight(double right)  { rectRight =   right;}

    public void setLeftBound(double bound)     { boundLeft    = bound;}
    public void setRightBound(double bound) {
        boundRight = bound;
    }

    public void setReturnHSV(boolean retHsv)    { returnHSV = retHsv;}
    public void setDrawRect(boolean draw)       { drawRect  = draw;}

    //@Override
    //public Mat processFrame(Mat input) {


    //}
}
