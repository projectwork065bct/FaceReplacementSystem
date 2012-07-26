/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package H_Curve_Image;

import CurveFitting.SquarePolynomial_003;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author power
 */
public class CurveEstimator {
    //This class depends on curveFitting
    private Point[] initialPoints=null;//There should be at least three separate points
    private Point[] edgePoints=null;//Two points
    
    //constructor
    public CurveEstimator(){}
    public CurveEstimator(Point[] initialPoints){
        this.initialPoints=initialPoints;
    }

    public List<Point> getEstimatedPoints(){
        SquarePolynomial_003 sq=new SquarePolynomial_003();
        return sq.getPoints(edgePoints);
    }
    //setters
    public void setEdgePoints(Point[] edgePoints) {
        this.edgePoints = edgePoints;
    }
    public void setInitialPoints(Point[] initialPoints) {
        this.initialPoints = initialPoints;
    }
    
    
    
}
