package frs.helpers;

/**
 *
 * @author User
 */
public class SnakeConfig {

	private double gradThreshold;
	private double maxDisplacement;
	private double maxSearch;
	private double regMin;
	private double regMax;
	private double alphaDeriche;


	/**
	 *  Constructor for the snakeConfig object
	 *
	 * @param  gt     Description of the Parameter
	 * @param  md     Description of the Parameter
	 * @param  rmin   Description of the Parameter
	 * @param  rmax   Description of the Parameter
	 * @param  ms     Description of the Parameter
	 * @param  alpha  Description of the Parameter
	 */
	public SnakeConfig(double gt, double md, double ms, double rmin, double rmax, double alpha) {
		gradThreshold = gt;
		maxDisplacement = md;
		maxSearch = ms;
		regMin = rmin;
		regMax = rmax;
		alphaDeriche = alpha;
	}


	/**
	 *  Constructor for the SnakeConfig object
	 *
	 * @param  conf  Description of the Parameter
	 */
	public SnakeConfig(SnakeConfig conf) {
		gradThreshold = conf.getGradThreshold();
		maxDisplacement = conf.getMaxDisplacement();
		maxSearch = conf.getMaxSearch();
		regMin = conf.getRegMin();
		regMax = conf.getRegMax();
		alphaDeriche = conf.getAlpha();
	}


	/**
	 *  Gets the gradThreshold attribute of the SnakeConfig object
	 *
	 * @return    The gradThreshold value
	 */
	public double getGradThreshold() {
		return gradThreshold;
	}


	/**
	 *  Gets the maxDisplacement attribute of the SnakeConfig object
	 *
	 * @return    The maxDisplacement value
	 */
	public double getMaxDisplacement() {
		return maxDisplacement;
	}


	/**
	 *  Gets the maxSearch attribute of the SnakeConfig object
	 *
	 * @return    The maxSearch value
	 */
	public double getMaxSearch() {
		return maxSearch;
	}


	/**
	 *  Gets the regMin attribute of the SnakeConfig object
	 *
	 * @return    The regMin value
	 */
	public double getRegMin() {
		return regMin;
	}


	/**
	 *  Gets the regMax attribute of the SnakeConfig object
	 *
	 * @return    The regMax value
	 */
	public double getRegMax() {
		return regMax;
	}


	/**
	 *  Gets the alpha attribute of the SnakeConfig object
	 *
	 * @return    The alpha value
	 */
	public double getAlpha() {
		return alphaDeriche;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  mul  Description of the Parameter
	 */
	public void update(double mul) {
		alphaDeriche /= mul;
		//maxDisplacement *= mul;
		//maxSearch *= mul;
		regMax *= mul;
		regMin *= mul;
	}
}

