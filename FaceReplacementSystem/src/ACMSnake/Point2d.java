package ACMSnake;

/**
 *
 * @author User
 */




public class Point2d {

	/**
	 *  Description of the Field
	 */
	public double x;
	/**
	 *  Description of the Field
	 */
	public double y;


	/**
	 *  Constructor for the Point2d object
	 */
	public Point2d() {
		x = 0.0;
		y = 0.0;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  p2  Description of the Parameter
	 *@return     Description of the Return Value
	 */
	public double dist(Point2d p2) {
		return (Math.sqrt((x - p2.x) * (x - p2.x) + (y - p2.y) * (y - p2.y)));
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String toString() {
		String data = new String("x=" + x + " y=" + y);
		return data;
	}

}

