package frs.helpers;

/**
 *
 * @author User
 */
public class SnakeConfigDriver {

	private double maxDisplacement0;
	private double maxDisplacement1;
	private double inv_alphaD0;
	private double inv_alphaD1;
	private double reg0;
	private double reg1;
	private double step;       

	/**
	 *  Constructor for the snakeConfig object
	 */
	public SnakeConfigDriver() {
		maxDisplacement0 = 2.0;
		maxDisplacement1 = 0.1;
		inv_alphaD0 = 1.0 / 0.5;
		inv_alphaD1 = 1.0 / 2.0;
		reg0 = 2.0;
		reg1 = 0.1;
		step = 0.99;
	}


	/**
	 *  Sets the maxDisplacement attribute of the SnakeConfigDriver object
	 *
	 *@param  min  The new maxDisplacement value
	 *@param  max  The new maxDisplacement value
	 */
	public void setMaxDisplacement(double min, double max) {
		maxDisplacement1 = min;
		maxDisplacement0 = max;
	}


	/**
	 *  Sets the invAlphaD attribute of the SnakeConfigDriver object
	 *
	 *@param  min  The new invAlphaD value
	 *@param  max  The new invAlphaD value
	 */
	public void setInvAlphaD(double min, double max) {
		inv_alphaD1 = min;
		inv_alphaD0 = max;
	}


	/**
	 *  Sets the reg attribute of the SnakeConfigDriver object
	 *
	 *@param  min  The new reg value
	 *@param  max  The new reg value
	 */
	public void setReg(double min, double max) {
		reg1 = min;
		reg0 = max;
	}


	/**
	 *  Sets the step attribute of the SnakeConfigDriver object
	 *
	 *@param  s  The new step value
	 */
	public void setStep(double s) {
		step = s;
	}


	/**
	 *  Gets the step attribute of the SnakeConfigDriver object
	 *
	 *@return    The step value
	 */
	public double getStep() {
		return step;
	}


	/**
	 *  Gets the alphaD attribute of the SnakeConfigDriver object
	 *
	 *@param  min  Description of the Parameter
	 *@return      The alphaD value
	 */
	public double getInvAlphaD(boolean min) {
		if (min) {
			return inv_alphaD1;
		} else {
			return inv_alphaD0;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  min  Description of the Parameter
	 *@return      Description of the Return Value
	 */
	public double getMaxDisplacement(boolean min) {
		if (min) {
			return maxDisplacement1;
		} else {
			return maxDisplacement0;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  min  Description of the Parameter
	 *@return      Description of the Return Value
	 */
	public double getReg(boolean min) {
		if (min) {
			return reg1;
		} else {
			return reg0;
		}
	}

}

