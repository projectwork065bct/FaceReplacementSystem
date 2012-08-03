/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import DataStructures.FeaturePoint;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Dell
 */
public class Transformer {

    //It rotated the point through "theta" with respect to "origin"
    public static Point rotatePoint(Point point, double theta, Point origin) {
        Point shiftedPoint = new Point(point.x - origin.x, point.y - origin.y);
        Point rotatedPoint = new Point();
        rotatedPoint.x = (int) (shiftedPoint.x * Math.cos(theta) - shiftedPoint.y * Math.sin(theta));
        rotatedPoint.y = (int) (shiftedPoint.x * Math.sin(theta) + shiftedPoint.y * Math.cos(theta));
        Point finalPoint = new Point(rotatedPoint.x + origin.x, rotatedPoint.y + origin.y);
        return finalPoint;
    }

    public static Point[] shiftOrigin(Point originalPoints[], Point origin) {
        Point shiftedPoints[] = new Point[originalPoints.length];
        for (int i = 0; i < shiftedPoints.length; i++) {
            int x = originalPoints[i].x - origin.x;
            int y = originalPoints[i].y - origin.y;
            shiftedPoints[i] = new Point(x, y);
        }
        return shiftedPoints;
    }

    public static Rectangle getRectangleUsingFP(Point[] featurePoints) {
        Rectangle rectangleUsingFeaturePoints = new Rectangle();
        //Distance between the two eyes
        int distEye = Math.abs(featurePoints[FeaturePoint.RIGHT_EYE].x - featurePoints[FeaturePoint.LEFT_EYE].x);
        rectangleUsingFeaturePoints.height = (int)(3.5 * distEye);
        rectangleUsingFeaturePoints.width = (int) (2.5 * distEye);
        //mid point between the two eyes
        Point midPoint = new Point(featurePoints[FeaturePoint.LEFT_EYE].x+distEye/2, featurePoints[FeaturePoint.LEFT_EYE].y);
        rectangleUsingFeaturePoints.x=midPoint.x-(int)(1.25*distEye);
        rectangleUsingFeaturePoints.y=midPoint.y-(int)(1.5*distEye);
        return rectangleUsingFeaturePoints;
    }
    
//    public static Rectangle getRectangleUsingFP(Point[] featurePoints)
//    {
//        
//        int xmin, xmax, ymin, ymax;
//        xmin = xmax = featurePoints[0].x;
//        ymin = ymax = featurePoints[0].y;
//        for (int i = 1; i < featurePoints.length; i++) {
//            xmin = featurePoints[i].x < xmin ? featurePoints[i].x : xmin;
//            ymin = featurePoints[i].y < ymin ? featurePoints[i].y : ymin;
//            xmax = featurePoints[i].x > xmax ? featurePoints[i].x : xmax;
//            ymax = featurePoints[i].y > ymax ? featurePoints[i].y : ymax;
//        }
//        rectangleUsingFeaturePoints = new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
//    }
}
