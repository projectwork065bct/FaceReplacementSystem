/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.curve;

import frs.dataTypes.FeaturePoint;
import frs.helpers.ColorModelConverter;
import frs.helpers.DeepCopier;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robik Shrestha
 */
public class BestSideCurves {

    protected BufferedImage image;
    protected float[][][] ycbcr;
    protected int LEFT = 1, RIGHT = 2;
    // protected Curve curve, rightCurve1;
    protected float maxLeftScore, maxRightScore;
    protected List<Point> bestLeftPts, bestRightPts;
    protected Point[] fp;//2 points in each list
    protected int eyeDist;
    protected int rx, lx;// x -coordinates of right and left eyes

    public BestSideCurves(BufferedImage image, Point[] fp) {
        this.image = image;
        ycbcr = ColorModelConverter.getYCbCr(image);
        this.fp = fp;
        rx = fp[FeaturePoint.RIGHT_EYE].x;
        lx = fp[FeaturePoint.LEFT_EYE].x;
        eyeDist = Math.abs(rx - lx);
        findBestLeftPts();
        findBestRightPts();

    }

    public void findBestLeftPts() {
        List<Point> tempBestLeftPts = new ArrayList();
        //First step is to find the best curve from right cheek to middle of right cheek and eye (middle means middle along y-axis)
        int xMin = (int) (lx - .5 * eyeDist);
        int xMax = (int) (lx - .4 * eyeDist);
        int y = (fp[FeaturePoint.LEFT_EYE].y) * 2 / 3;
        Point pMin = new Point(xMin, y);
        Point pMax = new Point(xMax, y);
        tempBestLeftPts.addAll(findCurve(fp[FeaturePoint.LEFT_CHIN], fp[FeaturePoint.LEFT_CHEEK], pMin, pMax, LEFT));
        bestLeftPts = new ArrayList();
        for (int i = 0; i < tempBestLeftPts.size(); i++) {
            Point p = tempBestLeftPts.get(i);
            if (p.y >= fp[FeaturePoint.LEFT_EYE].y) {
                bestLeftPts.add(p);
            }
        }
    }

    public void findBestRightPts() {
        List<Point> tempBestRightPts = new ArrayList();
        //First step is to find the best curve from right cheek to middle of right cheek and eye (middle means middle along y-axis)
        int xMin = (int) (rx + .4 * eyeDist);
        int xMax = (int) (rx + .5 * eyeDist);
        int y = (fp[FeaturePoint.RIGHT_EYE].y) * 2 / 3;
        Point pMin = new Point(xMin, y);
        Point pMax = new Point(xMax, y);
        tempBestRightPts.addAll(findCurve(fp[FeaturePoint.RIGHT_CHIN], fp[FeaturePoint.RIGHT_CHEEK], pMin, pMax, RIGHT));
        bestRightPts = new ArrayList();
        for (int i = 0; i < tempBestRightPts.size(); i++) {
            Point p = tempBestRightPts.get(i);
            if (p.y >= fp[FeaturePoint.RIGHT_EYE].y) {
                bestRightPts.add(p);
            }
        }

    }

    /*
     * int xMin = (int) (rx + .35 * eyeDist); int xMax = (int) (rx + 0.6 *
     * eyeDist); int y = (fp[FeaturePoint.RIGHT_CHEEK].y +
     * fp[FeaturePoint.RIGHT_EYE].y) / 2; Point pMin = new Point(xMin, y), pMax
     * = new Point(xMax, y);
     * bestRightPts.addAll(findCurve(fp[FeaturePoint.RIGHT_CHIN],
     * fp[FeaturePoint.RIGHT_CHEEK], pMin, pMax)); //The values .35 and .7 were
     * found by some experimentation. There is no guarantee that this range is
     * correct //Following code finds the best curve from the right cheek to
     * height of right eye xMin = (int) (rx + .35 * (float) eyeDist);// +
     * (float) eyeDist / 3 xMax = (int) (rx + .7 * (float) eyeDist); y =
     * fp[FeaturePoint.RIGHT_EYE].y; pMin = new Point(xMin, y); pMax = new
     * Point(xMax, y);
     * bestRightPts.addAll(findCurve(fp[FeaturePoint.RIGHT_CHEEK],
     * bestRightPts.get(0), pMin, pMax));
     */
    //A curve is found out start from p1, controlled by p2 till an end point whose x-coordinate lies along pMin.x and 
    //pMax.x and y-coordinate is equal to pMin.y which is equal to pMax.y
    public List<Point> findCurve(Point p1, Point p2, Point pMin, Point pMax, int side) {
        float maxScore = 0;//maximum score upto now
        int xMin = pMin.x;
        int xMax = pMax.x;
        List<Point> curvePoints;
        List<Point> bestPts = null;
        for (int x = xMin; x <= xMax; x++) {
            SquarePolynomial_003 curve = new SquarePolynomial_003();
            Point[] threeCurvePoints = {p1, p2, new Point(x, pMin.y)};//points for the curve to be evaluated
            Point[] curveEnds = {new Point(x, pMin.y), p2};
            curve.setPoints(threeCurvePoints);
            try {
                curvePoints = curve.getPoints(curveEnds);
            } catch (Exception e) {
                System.out.println("Exception thrown");
                continue;
            }
            //for each point in the trial curve
            float score;
            if (side == RIGHT) {
                score = evaluateRightCurve(curvePoints);
            } else {
                score = evaluateLeftCurve(curvePoints);
            }
            if (score > maxScore) {
                maxScore = score;
                bestPts = DeepCopier.getPoints(curvePoints);
            }
        }
        return bestPts;
    }

    public List<Point> getLeftCurve() {
        return bestLeftPts;
    }

    public List<Point> getRightCurve() {
        return bestRightPts;
    }

    //Find out how good that left curve really is
    public float evaluateLeftCurve(List<Point> curvePoints) {
        List<Point> simPoints = new ArrayList();//these points should be similar to the curve
        List<Point> diffPoints = new ArrayList();//these points should be different to the curve
        for (int i = 0; i < curvePoints.size(); i++) {
            Point p = curvePoints.get(i);
            for (int xSim = 1; xSim <= 5; xSim++) {
                simPoints.add(new Point(p.x + xSim, p.y));
            }
            for (int xDiff = 1; xDiff <= 5; xDiff++) {
                diffPoints.add(new Point(p.x - xDiff, p.y));
            }
        }
        return findScore(simPoints, curvePoints, diffPoints);
    }
    //Find out how good that right curve really is

    public float evaluateRightCurve(List<Point> curvePoints) {
        List<Point> simPoints = new ArrayList();//these points should be similar to the curve
        List<Point> diffPoints = new ArrayList();//these points should be different to the curve
        for (int i = 0; i < curvePoints.size(); i++) {
            Point p = curvePoints.get(i);
            for (int xSim = 1; xSim <= 5; xSim++) {
                simPoints.add(new Point(p.x - xSim, p.y));
            }
            for (int xDiff = 1; xDiff <= 5; xDiff++) {
                diffPoints.add(new Point(p.x + xDiff, p.y));
            }
        }
        return findScore(simPoints, curvePoints, diffPoints);
    }

    //Each curve has a score based on the similarity with inner points and dissimilarity with outer points
    // i.e. inner or outer to face
    public float findScore(List<Point> simPoints, List<Point> curvePoints, List<Point> diffPoints) {
        float diffScore = (float) ((float) findDiffScore(curvePoints, diffPoints) * .5);
        float simScore = (float) ((float) (1 - findDiffScore(curvePoints, simPoints)) * .25);//find similarity between curvePoints and simPoints
        float skinScore = (float) ((float) (findSkinScore(curvePoints)) * 0.25);
        return (diffScore + simScore + skinScore);
        //float skinScore = findSkinScore(curvePoints);
        //float nonSkinScore = findNonSkinScore(curvePoints);
    }

    //How different are the "diffPoints" wrt "curvePoints" ?
    public float findDiffScore(List<Point> curvePoints, List<Point> diffPoints) {
        float score = 0;
        int n = 0;
        for (int i = 0; i < diffPoints.size(); i++) {
            Point cp = curvePoints.get(i % (curvePoints.size()));//curve point
            Point dp = diffPoints.get(i);// diff point
            try {
                for (int j = 0; j < 3; j++) {
                    score += Math.abs(ycbcr[cp.x][cp.y][j] - ycbcr[dp.x][dp.y][j]);
                }
                n++;
            } catch (Exception e) {
                continue;
            }
        }
        //3 for y, cb, cr and 255 for max. diff = 255
        //normalization for value to be between 0 and 1
        score /= (255 * n * 3);
        return score;
    }

    public float findSkinScore(List<Point> curvePoints) {
        int nSkin = 0;
        int n = 0;
        float Y, Cb, Cr;
        for (int i = 0; i < curvePoints.size(); i++) {
            Point p = curvePoints.get(i);
            try {
                Y = ycbcr[p.x][p.y][0];
                Cb = ycbcr[p.x][p.y][1];
                Cr = ycbcr[p.x][p.y][2];
            } catch (Exception e) {
                continue;
            }
            if (Y > 50 && Y < 240 && Cb >= -50 && Cb <= -5 && Cr > 0 && Cr < 50) {
                nSkin++;
            }
            n++;
        }
        return (float) nSkin / n;
    }

    public float findNonSkinScore(List<Point> curvePoints) {
        return 1;
    }
//    public float findScore(List<Point> simPoints, List<Point> curvePoints, List<Point> diffPoints) {
//        float[] simSum, curveSum, diffSum;
//        simSum = findSum(simPoints);
//        curveSum = findSum(curvePoints);
//        diffSum = findSum(diffPoints);
//        float simMean[] = new float[3], curveMean[] = new float[3], diffMean[] = new float[3];
//        float score = 0;
//        for (int i = 0; i < 3; i++) {
//            simMean[i] = (simSum[i]) / (simPoints.size());
//            curveMean[i] = (curveSum[i]) / curvePoints.size();
//            diffMean[i] = (diffSum[i] / diffPoints.size());
//            score += (float) Math.abs(diffMean[i] - curveMean[i]) / Math.abs(simMean[i] - curveMean[i]);
//        }
//        return score;
//    }
//
//    //Find the sum of ycbcr values
//    public float[] findSum(List<Point> points) {
//        float sum[] = new float[3];
//        for (int i = 0; i < points.size(); i++) {
//            Point p = points.get(i);
//            try {
//                sum[0] += ycbcr[p.x][p.y][0];
//                sum[1] += ycbcr[p.x][p.y][1];
//                sum[2] += ycbcr[p.x][p.y][2];
//            } catch (Exception e) {
//                continue;
//            }
//        }
//        return sum;
//    }
}
