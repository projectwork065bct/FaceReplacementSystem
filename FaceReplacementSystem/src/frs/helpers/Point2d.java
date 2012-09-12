package frs.helpers;

/**
 *
 * @author User
 */

public class Point2d {	
	public double x;	
	public double y;
        
	public Point2d() {
		x = 0.0;
		y = 0.0;
	}

	public double dist(Point2d p2) {
		return (Math.sqrt((x - p2.x) * (x - p2.x) + (y - p2.y) * (y - p2.y)));
	}
        
	public String toString() {
		String data = new String("x=" + x + " y=" + y);
		return data;
	}
}
