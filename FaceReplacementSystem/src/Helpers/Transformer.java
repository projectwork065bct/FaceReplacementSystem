/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.awt.Point;

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
}
