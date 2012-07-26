/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreAlgorithms;

import Helpers.DeepCopier;
import DataStructures.FloatingCoordinate;
import Jama.LUDecomposition;
import Jama.Matrix;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 *
 * @author Robik Singh Shrestha
 *
 * @description
 *
 * This class uses two matrices containing the (sumX,sumY) coordinates of
 * feature points of source and target. It performs composite spatial
 * transformation (including: scaling, shifting and rotating all the
 * coordinates).
 *
 * Algorithm:
 *
 * 1. Specify the feature points of source and target.
 *
 * 2. Apply least square method to obtain the elements of composite
 * transformation matrix.
 *
 * 3. Apply that matrix to the original coordinates, so that they get
 * transformed according to the coordinates of the feature points of the target.
 *
 * Equation: Target Coordinate (T)= Composite Transformation Matrix (CTM)*Source
 * Coordinate (S)
 *
 * This equation is first used to find out CTM by filling T and S with feature
 * point coordinates and applying Least Square Method.
 *
 * Then, CTM is used to convert S to T i.e. for all the coordinates of source
 * face.
 *
 * To use this class,
 *
 * 1. Set the feature Points of the source and target and specify the width and
 * height of the source
 *
 *
 * 2. Call the function warp()
 *
 * 3. Call the function getWarpedMatrix() to retrieve the warped matrix OR call
 * the function getReverseMap() to retrieve the reverse map
 *
 * 4. Call the function getWarpedWidth() and getWarpedHeight() to retrieve new
 * width and height
 *
 */
public class Warper {

    //It can be used to set the parameters, run the entire algorithm and get the result, the result being the reverse map
    public FloatingCoordinate[][] setRunGet(Point[] sourceFeaturePoints, Point[] targetFeaturePoints, int width, int height) {
        setParameters(sourceFeaturePoints, targetFeaturePoints, width, height, originIndex);
        return this.runGet();
    }

    public FloatingCoordinate[][] runGet() {
        warp();
        return getReverseMap();
    }

    //Constructors
    public Warper() {
    }

    //originIndex is the index inside the feature points array which is to be considered as the origin
    public Warper(Point[] sourceFeaturePoints, Point[] targetFeaturePoints, int width, int height, int originIndex) {
        setParameters(sourceFeaturePoints, targetFeaturePoints, width, height, originIndex);
    }

    public void setParameters(Point[] sourceFeaturePoints, Point[] targetFeaturePoints, int width, int height, int originIndex) {
        this.sourceFeaturePoints = DeepCopier.getPoints(sourceFeaturePoints);
        this.targetFeaturePoints = DeepCopier.getPoints(targetFeaturePoints);
        this.initialWidth = width;
        this.initialHeight = height;
        this.originIndex = originIndex;
    }

    //ImageWarper can be started after the feature points of the source and target images
    //have been specified. This is the main function of this class.
    public void warp() {
        findHowMuchToShift();
        shiftFeaturePoints();
        calculateValuesForLSM();
        applyLSM();
        initializeWarper();
        mapFP();
        findWarpedMatrixSize();
        decreaseMinFromMapped();
        findReverseMap();
    }
    Point shifted[] = new Point[8];

    //It finds out the length by which the source and the target coordinates need to be shifted by
    protected void findHowMuchToShift() {
        sourceShiftX = sourceFeaturePoints[originIndex].x;
        sourceShiftY = sourceFeaturePoints[originIndex].y;
        targetShiftX = targetFeaturePoints[originIndex].x;
        targetShiftY = targetFeaturePoints[originIndex].y;
    }
    //It shifts the feature points to their respective "new origins"

    protected void shiftFeaturePoints() {
        for (int i = 0; i < sourceFeaturePoints.length; i++) {
            sourceFeaturePoints[i].x -= sourceShiftX;
            sourceFeaturePoints[i].y -= sourceShiftY;
        }

        for (int i = 0; i < targetFeaturePoints.length; i++) {
            targetFeaturePoints[i].x -= targetShiftX;
            targetFeaturePoints[i].y -= targetShiftY;
        }
    }
    //It calculates the values required by Least Square Method (LSM)

    protected void calculateValuesForLSM() {
        int sX, sY;//source
        int tX, tY;//target
        for (int i = 0; i < sourceFeaturePoints.length; i++) {
            sX = sourceFeaturePoints[i].x;
            sY = sourceFeaturePoints[i].y;
            tX = targetFeaturePoints[i].x;
            tY = targetFeaturePoints[i].y;

            sumX += sX;
            sumY += sY;
            sumOfXY += sX * sY;
            sumOfXSquare += sX * sX;
            sumOfYSquare += sY * sY;


            sumOfTarX += tX;
            sumOfTarY += tY;
            sumOfTarXSrcX += tX * sX;
            sumOfTarYSrcY += tY * sY;
            sumOfTarXSrcY += tX * sY;
            sumOfTarYSrcX += tY * sX;
        }
    }

    //It is used to solve the matrix equation, to obtain the values for elements of warper
    protected void applyLSM() {
        //Equation: A[3][3] M[3] = B[3], we need to find out M
        double A[][] = {
            {sumOfXSquare, sumOfXY, sumX},
            {sumOfXY, sumOfYSquare, sumY},
            {sumX, sumY, sourceFeaturePoints.length}
        };

        double BX[] = {sumOfTarXSrcX, sumOfTarXSrcY, sumOfTarX};
        double BY[] = {sumOfTarYSrcX, sumOfTarYSrcY, sumOfTarY};

        //Now we have matrices to solve the equation
        Matrix matrixA = new Matrix(A);
        Matrix matrixBX = new Matrix(BX, BX.length);
        Matrix matrixBY = new Matrix(BY, BY.length);

        LUDecomposition luDecompositionA = new LUDecomposition(matrixA);
        matrixX = luDecompositionA.solve(matrixBX);
        luDecompositionA = new LUDecomposition(matrixA);
        matrixY = luDecompositionA.solve(matrixBY);
    }

    //It calculates the elements required to warp the source face
    //The transformation matrix is 
    //matrix[1st row] = [m1 m2 m3]
    //matrix[2nd row] = [m4 m5 m6]
    //matrix[3rd row] = [0  0  1 ]
    protected void initializeWarper() {
        //1st row
        m1 = (float) matrixX.get(0, 0);
        m2 = (float) matrixX.get(1, 0);
        m3 = (float) matrixX.get(2, 0);
        //2nd row
        m4 = (float) matrixY.get(0, 0);
        m5 = (float) matrixY.get(1, 0);
        m6 = (float) matrixY.get(2, 0);
    }

    //It maps the feature points to warped image
    protected void mapFP() {
        mappedFeaturePoints = new Point[sourceFeaturePoints.length];
        for (int i = 0; i < sourceFeaturePoints.length; i++) {
            int sX = sourceFeaturePoints[i].x;
            int sY = sourceFeaturePoints[i].y;
            int tX = targetFeaturePoints[i].x;
            int tY = targetFeaturePoints[i].y;
            Point mappedPoint = getShiftedMappedPoint(sX, sY);
            mappedFeaturePoints[i] = mappedPoint;
        }

    }
    //It finds out the size of the warped image. It does this by forward mapping (converting)
    //source coordinate to warped coordinate and finding out the minimum and maximum values of 
    //x and y

    protected void findWarpedMatrixSize() {
        int warpedX, warpedY;
        int sourceStartX = -sourceShiftX;
        int sourceStartY = -sourceShiftY;
        int sourceEndX = this.initialWidth + sourceStartX;
        int sourceEndY = this.initialHeight + sourceStartY;
        int count = 0;
        for (int x = sourceStartX; x < sourceEndX; x += this.initialWidth - 1) {
            for (int y = sourceStartY; y < sourceEndY; y += this.initialHeight - 1) {
                //Get the warped coordinates of source image
                Point mappedPoint = getShiftedMappedPoint(x, y);
                warpedX = mappedPoint.x;
                warpedY = mappedPoint.y;
                //warpedPoints.add(new Point(warpedX, warpedY));
                //Find out the warped width and warped height
                minWarpedX = warpedX < minWarpedX ? warpedX : minWarpedX;
                minWarpedY = warpedY < minWarpedY ? warpedY : minWarpedY;
                maxWarpedX = warpedX > maxWarpedX ? warpedX : maxWarpedX;
                maxWarpedY = warpedY > maxWarpedY ? warpedY : maxWarpedY;
            }
        }
        this.warpedWidth = maxWarpedX - minWarpedX;
        this.warpedHeight = maxWarpedY - minWarpedY;
    }

    protected void decreaseMinFromMapped() {
        for (int i = 0; i < shifted.length; i++) {
            shifted[i] = new Point(mappedFeaturePoints[i].x - minWarpedX, mappedFeaturePoints[i].y - minWarpedY);
            //System.out.printf("shifted fp(%d,%d)\n",shifted[i].x,shifted[i].y);
        }
    }

    //Initialize the warped matrix
   /*
     * protected void initializeWarpedMatrix() { int i = 0; warpedMatrix = new
     * Point[initialWidth][initialHeight]; for (int x = 0; x < initialWidth;
     * x++) { warpedMatrix[x] = new Point[initialHeight]; for (int y = 0; y <
     * initialHeight; y++) { warpedMatrix[x][y] = new Point();
     * warpedMatrix[x][y].x = warpedPoints.get(i).x; warpedMatrix[x][y].y =
     * warpedPoints.get(i).y; i++; } } }
     */
    //It finds out the reverse map from the warped matrix to the original matrix
    protected void findReverseMap() {
        double denominator = m1 * m5 - m2 * m4;
        float numeratorX, numeratorY;
        float xOriginal, yOriginal;
        int indexX, indexY;
        reverseMap = new FloatingCoordinate[warpedWidth][warpedHeight];
        for (int x = minWarpedX; x < maxWarpedX; x++) {
            indexX = x - minWarpedX;
            reverseMap[indexX] = new FloatingCoordinate[warpedHeight];
            for (int y = minWarpedY; y < maxWarpedY; y++) {
                indexY = y - minWarpedY;
                numeratorX = m5 * x - m2 * y - m3 * m5 + m2 * m6;
                numeratorY = -1 * m4 * x + m1 * y - m1 * m6 + m3 * m4;
                xOriginal = (float) (numeratorX / denominator);
                yOriginal = (float) (numeratorY / denominator);
                reverseMap[indexX][indexY] = new FloatingCoordinate();
                //The actual coordinates inside the image is found out by reshifting to the original origin
                reverseMap[indexX][indexY].x = xOriginal + sourceShiftX;
                reverseMap[indexX][indexY].y = yOriginal + sourceShiftY;

            }
        }
    }

    //The point it provides is with reference to originIndex, not the top left corner of the image
    //This is forward mapping
    protected Point getShiftedMappedPoint(int x, int y) {
        //Point mappedPoint = warpedMatrix[point.x][point.y];
        Point mappedPoint = new Point();
        mappedPoint.x = (int) (m1 * x + m2 * y + m3);
        mappedPoint.y = (int) (m4 * x + m5 * y + m6);
        return mappedPoint;
    }

    //This is a publc function
    //It can map point (with origin at top left corner of source image) to point in target image 
    //(with origin at top left corner)
    public Point getMappedPoint(Point sourcePoint) {
        Point mappedPoint = getShiftedMappedPoint(sourcePoint.x - sourceShiftX, sourcePoint.y - sourceShiftY);
        mappedPoint.x -= minWarpedX;
        mappedPoint.y -= minWarpedY;
        return mappedPoint;
    }

    public Point getMappedOrigin() {
        return shifted[originIndex];
    }

    public boolean isWithinBounds(int x, int y, BufferedImage image) {
        if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            return true;
        } else {
            return false;
        }
    }
    //Variables
    protected Point sourceFeaturePoints[];//Coordinate for each feature point
    protected Point targetFeaturePoints[];
    protected Point mappedFeaturePoints[];
    protected int sumX = 0, sumY = 0;//Sum of sumX and sumY of sfp
    protected int sumOfXSquare = 0;//Sum of squares of sumX coordinates of source feature points (sfp)
    protected int sumOfYSquare = 0;//Sum of squares of sumY coordinates of source feature points
    protected int sumOfXY = 0;//Sum of product of sumX and sumY components of coordinates of sfp
    protected int sumOfTarXSrcX = 0;//Sum of product of target sumX and source sumX coordinates of fp
    protected int sumOfTarYSrcY = 0;
    protected int sumOfTarYSrcX = 0;
    protected int sumOfTarXSrcY = 0;//Sum of product of target sumX and source sumY coordinates of fp
    protected int sumOfTarX = 0;//Sum of sumX component of coordiantes of target fp
    protected int sumOfTarY;
    protected Matrix matrixX, matrixY;//These hold the elements of composite transformation matrices i.e. the values of m1,m2,m3,m4,m5,m6
    protected float m1 = 0, m2 = 0, m3 = 0, m4 = 0, m5 = 0, m6 = 0;
    protected Point[][] warpedMatrix;//This is the matrix obtained after warping the coordinates.
    //warpedMatrix[x][y].x gives the warped x-coordinate of the original coordinate (x,y)
    protected int initialWidth, initialHeight;
    protected int minWarpedX, minWarpedY, maxWarpedX, maxWarpedY;//The boundaries of the warped matrix
    protected int warpedWidth, warpedHeight;
    protected Vector<Point> warpedPoints;//Initially, the size of the warped matrix is not known, so we need to store the points into a list
    //Setters and Getters
    protected FloatingCoordinate reverseMap[][];//It maps the warped coordinates to the coordinates in the original
    //matrix. It stores the information of the reverse map. The coordinates are stored
    //[x1][y1].x = map from (x1,y1) of the warped matrix to the floating x-coordinate of the original image/matrix
    protected int originIndex;
    protected int sourceShiftX, sourceShiftY, targetShiftX, targetShiftY;
    protected Point originPoint;
    //Setters and Getters

    public Point[] getSourceFeaturePoints() {
        return sourceFeaturePoints;
    }

    public void setSourceFeaturePoints(Point[] sourceFeaturePoints) {
        this.sourceFeaturePoints = DeepCopier.getPoints(sourceFeaturePoints);
    }

    public Point[] getTargetFeaturePoints() {
        return targetFeaturePoints;
    }

    public void setTargetFeaturePoints(Point[] targetFeaturePoints) {
        this.targetFeaturePoints = DeepCopier.getPoints(targetFeaturePoints);
    }

    public void setFeaturePoints(Point[] sourceFeaturePoints, Point[] targetFeaturePoints) {
        this.sourceFeaturePoints = DeepCopier.getPoints(sourceFeaturePoints);
        this.targetFeaturePoints = DeepCopier.getPoints(targetFeaturePoints);
    }

    public void setInitialHeight(int initialHeight) {
        this.initialHeight = initialHeight;
    }

    public void setInitialWidth(int initialWidth) {
        this.initialWidth = initialWidth;
    }

    public int getWarpedHeight() {
        return warpedHeight;
    }

    public Point[][] getWarpedMatrix() {
        return warpedMatrix;
    }

    public int getWarpedWidth() {
        return warpedWidth;
    }

    public int getMinWarpedX() {
        return minWarpedX;
    }

    public int getMinWarpedY() {
        return minWarpedY;
    }

    public Point getOriginPoint() {
        return originPoint;
    }

    public FloatingCoordinate[][] getReverseMap() {
        return reverseMap;
    }

    public static void main(String[] args) {
    }
}
