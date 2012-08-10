/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.curve;

import java.awt.Point;
import java.util.List;

/**
 *
 * @author power
 */
public interface CurveInterface {
    /**
     * pass all the sample points to estimate a second degree curve
     * @param a is array of sample points
     * 
     */
    public void setPoints(Point[] a);
    /**
     * pass an array of two points
     * @param a
     * @return a list of intermediate points which lies on the estimated curve, between points in argument
     */
    public List<Point> getPoints(Point[] a);
}
