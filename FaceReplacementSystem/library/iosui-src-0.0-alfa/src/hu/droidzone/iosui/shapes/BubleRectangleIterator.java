package hu.droidzone.iosui.shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.*;

/**
 * A utility class to iterate over the path segments of an rounded rectangle
 * through the PathIterator interface.
 *
 * @version 10 Feb 1997
 * @author	Jim Graham
 */
class BubleRectangleIterator implements PathIterator {
	double x, y, w, h, aw, ah;
	AffineTransform affine;
	int index;
	MarkerDirection direction;
	double mw,mh,ml;
	
	BubleRectangleIterator(BubleRectangleShape rr, AffineTransform at) {
		this.x = rr.getX();
		this.y = rr.getY();
		this.w = rr.getWidth();
		this.h = rr.getHeight();
		this.direction = rr.getMarkerDirection();
		this.mw = rr.getMarkerWidth();
		this.mh = rr.getMarkerHeight();
		this.ml = rr.getMarkerLocation();
		
		double mh = rr.getMarkerHeight();
		
		switch(direction) {
		case BOTTOM:
			h -= mh;
			break;
		case LEFT:
			x += mh;
			w -= mh;
			break;
		case RIGHT:
			w -= mh;
			break;
		case TOP:
			y += mh;
			h -= mh;
			break;
		}
		this.aw = Math.min(w, Math.abs(rr.getArcWidth()));
		this.ah = Math.min(h, Math.abs(rr.getArcHeight()));
		this.affine = at;
		prepareCtrlpts();
		if (aw < 0 || ah < 0) {
			index = ctrlpts.length;
		}
	}

	/**
	 * Return the winding rule for determining the insideness of the
	 * path.
	 * @see #WIND_EVEN_ODD
	 * @see #WIND_NON_ZERO
	 */
	public int getWindingRule() {
		return WIND_NON_ZERO;
	}

	/**
	 * Tests if there are more points to read.
	 * @return true if there are more points to read
	 */
	public boolean isDone() {
		return index >= ctrlpts.length;
	}

	/**
	 * Moves the iterator to the next segment of the path forwards
	 * along the primary direction of traversal as long as there are
	 * more points in that direction.
	 */
	public void next() {
		index++;
	}

	private static final double angle = Math.PI / 4.0;
	private static final double a = 1.0 - Math.cos(angle);
	private static final double b = Math.tan(angle);
	private static final double c = Math.sqrt(1.0 + b * b) - 1 + a;
	private static final double cv = 4.0 / 3.0 * a * b / c;
	private static final double acv = (1.0 - cv) / 2.0;

	// For each array:
	//     4 values for each point {v0, v1, v2, v3}:
	//         point = (x + v0 * w + v1 * arcWidth,
	//                  y + v2 * h + v3 * arcHeight);
//	private static double ctrlpts[][] = {
//		{  0.0,  0.0,  0.0,  0.5 },
//		{  0.0,  0.0,  1.0, -0.5 },
//		{  0.0,  0.0,  1.0, -acv, 0.0,  acv,  1.0,  0.0, 0.0,  0.5,  1.0,  0.0 },
//		{  1.0, -0.5,  1.0,  0.0 },
//		{  1.0, -acv,  1.0,  0.0, 1.0,  0.0,  1.0, -acv, 1.0,  0.0,  1.0, -0.5 },
//		{  1.0,  0.0,  0.0,  0.5 },
//		{  1.0,  0.0,  0.0,  acv, 1.0, -acv,  0.0,  0.0, 1.0, -0.5,  0.0,  0.0 },
//		{  0.0,  0.5,  0.0,  0.0 },
//		{  0.0,  acv,  0.0,  0.0, 0.0,  0.0,  0.0,  acv, 0.0,  0.0,  0.0,  0.5 },
//		{},
//	};
//	private static int types[] = {
//		SEG_MOVETO,
//		SEG_LINETO, SEG_CUBICTO,
//		SEG_LINETO, SEG_CUBICTO,
//		SEG_LINETO, SEG_CUBICTO,
//		SEG_LINETO, SEG_CUBICTO,
//		SEG_CLOSE,
//	};

	private static double ctrlptsBOTTOM[][] = {
		{  0.0,  0.0,  0.0,  0.5 },
		{  0.0,  0.0,  1.0, -0.5 },
		{  0.0,  0.0,  1.0, -acv, 0.0,  acv,  1.0,  0.0, 0.0,  0.5,  1.0,  0.0 },
		{},{},{},{},
		{  1.0, -acv,  1.0,  0.0, 1.0,  0.0,  1.0, -acv, 1.0,  0.0,  1.0, -0.5 },
		{  1.0,  0.0,  0.0,  0.5 },
		{  1.0,  0.0,  0.0,  acv, 1.0, -acv,  0.0,  0.0, 1.0, -0.5,  0.0,  0.0 },
		{  0.0,  0.5,  0.0,  0.0 },
		{  0.0,  acv,  0.0,  0.0, 0.0,  0.0,  0.0,  acv, 0.0,  0.0,  0.0,  0.5 },
		{},
	};
	private static int typesBOTTOM[] = {
		SEG_MOVETO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_LINETO, SEG_LINETO,  SEG_LINETO,SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_CLOSE,
	};
	
	private static double ctrlptsTOP[][] = {
		{  0.0,  0.0,  0.0,  0.5 },
		{  0.0,  0.0,  1.0, -0.5 },
		{  0.0,  0.0,  1.0, -acv, 0.0,  acv,  1.0,  0.0, 0.0,  0.5,  1.0,  0.0 },
		{  1.0, -0.5,  1.0,  0.0 },
		{  1.0, -acv,  1.0,  0.0, 1.0,  0.0,  1.0, -acv, 1.0,  0.0,  1.0, -0.5 },
		{  1.0,  0.0,  0.0,  0.5 },
		{  1.0,  0.0,  0.0,  acv, 1.0, -acv,  0.0,  0.0, 1.0, -0.5,  0.0,  0.0 },
		{},{},{},{},
		{  0.0,  acv,  0.0,  0.0, 0.0,  0.0,  0.0,  acv, 0.0,  0.0,  0.0,  0.5 },
		{},
	};
	private static int typesTOP[] = {
		SEG_MOVETO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_LINETO, SEG_LINETO, SEG_LINETO, SEG_CUBICTO,
		SEG_CLOSE,
	};
	
	private static double ctrlptsRIGHT[][] = {
		{  0.0,  0.0,  0.0,  0.5 },
		{  0.0,  0.0,  1.0, -0.5 },
		{  0.0,  0.0,  1.0, -acv, 0.0,  acv,  1.0,  0.0, 0.0,  0.5,  1.0,  0.0 },
		{  1.0, -0.5,  1.0,  0.0 },
		{  1.0, -acv,  1.0,  0.0, 1.0,  0.0,  1.0, -acv, 1.0,  0.0,  1.0, -0.5 },
		{},{},{},{},
		{  1.0,  0.0,  0.0,  acv, 1.0, -acv,  0.0,  0.0, 1.0, -0.5,  0.0,  0.0 },
		{  0.0,  0.5,  0.0,  0.0 },
		{  0.0,  acv,  0.0,  0.0, 0.0,  0.0,  0.0,  acv, 0.0,  0.0,  0.0,  0.5 },
		{},
	};
	private static int typesRIGHT[] = {
		SEG_MOVETO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_LINETO, SEG_LINETO, SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_CLOSE,
	};
	
	private static double ctrlptsLEFT[][] = {
		{  0.0,  0.0,  0.0,  0.5 },
		{},{},{},{},
		{  0.0,  0.0,  1.0, -acv, 0.0,  acv,  1.0,  0.0, 0.0,  0.5,  1.0,  0.0 },
		{  1.0, -0.5,  1.0,  0.0 },
		{  1.0, -acv,  1.0,  0.0, 1.0,  0.0,  1.0, -acv, 1.0,  0.0,  1.0, -0.5 },
		{  1.0,  0.0,  0.0,  0.5 },
		{  1.0,  0.0,  0.0,  acv, 1.0, -acv,  0.0,  0.0, 1.0, -0.5,  0.0,  0.0 },
		{  0.0,  0.5,  0.0,  0.0 },
		{  0.0,  acv,  0.0,  0.0, 0.0,  0.0,  0.0,  acv, 0.0,  0.0,  0.0,  0.5 },
		{},
	};
	private static int typesLEFT[] = {
		SEG_MOVETO,
		SEG_LINETO, SEG_LINETO, SEG_LINETO, SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_LINETO, SEG_CUBICTO,
		SEG_CLOSE,
	};
	
	private double ctrlpts[][];
	private int types[];
	private void prepareCtrlpts() {
		switch(direction) {
		case BOTTOM:
			ctrlpts = ctrlptsBOTTOM;
			types = typesBOTTOM;
			break;
		case LEFT:
			ctrlpts = ctrlptsLEFT;
			types = typesLEFT;
			break;
		case RIGHT:
			ctrlpts = ctrlptsRIGHT;
			types = typesRIGHT;
			break;
		case TOP:
			ctrlpts = ctrlptsTOP;
			types = typesTOP;
			break;
		}
		
	}
	
	public int currentSegment(float[] coords) {
		if (isDone()) {
			throw new NoSuchElementException("roundrect iterator out of bounds");
		}
		double dcoords[] = new double[6];
		int type = currentSegment(dcoords);
		for(int i = 0; i < coords.length;i++) {
			coords[i] = (float)dcoords[i];
		}
		return type;
	}

	public int currentSegment(double[] coords) {
		if (isDone()) {
			throw new NoSuchElementException("roundrect iterator out of bounds");
		}
		double ctrls[] = ctrlpts[index];
		switch(direction) {
		case BOTTOM:
			if(index == 3) {
				double w2 = w/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? w2 : ml;
				coords[0] = x + mcp - mw2;
				coords[1] = y + h;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 4) {
				double w2 = w/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? w2 : ml;
				coords[0] = x + mcp;
				coords[1] = y + h + mh;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 5) {
				double w2 = w/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? w2 : ml;
				coords[0] = x + mcp + mw2;
				coords[1] = y + h;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 6) {
				coords[0] = x + w - (aw/2.0);
				coords[1] = y + h;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else {
				calcNormalSides(coords,ctrls);
			}
			break;
		case LEFT:
			if(index == 1) {
				double h2 = h/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? h2 : ml;
				coords[0] = x;
				coords[1] = y + mcp - mw2;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 2) {
				double h2 = h/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? h2 : ml;
				coords[0] = x - mh;
				coords[1] = y + mcp;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 3) {
				double h2 = h/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? h2 : ml;
				coords[0] = x;
				coords[1] = y + mcp + mw2;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 4) {
				coords[0] = x;
				coords[1] = y + h - (ah/2);
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else {
				calcNormalSides(coords,ctrls);
			}
			break;
		case RIGHT:
			if(index == 5) {
				double h2 = h/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? h2 : ml;
				coords[0] = x + w;
				coords[1] = y + mcp + mw2;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 6) {
				double h2 = h/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? h2 : ml;
				coords[0] = x + w + mh;
				coords[1] = y + mcp;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 7) {
				double h2 = h/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? h2 : ml;
				coords[0] = x + w;
				coords[1] = y + mcp - mw2;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 8) {
				coords[0] = x + w;
				coords[1] = y + (ah/2);
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else {
				calcNormalSides(coords,ctrls);
			}
			break;
		case TOP:
			if(index == 7) {
				double w2 = w/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? w2 : ml;
				coords[0] = x + mcp + mw2;
				coords[1] = y;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 8) {
				double w2 = w/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? w2 : ml;
				coords[0] = x + mcp;
				coords[1] = y - mh;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 9) {
				double w2 = w/2.0;
				double mw2 = mw/2.0;
				double mcp = ml == BubleRectangleShape.CENTER ? w2 : ml;
				coords[0] = x + mcp - mw2;
				coords[1] = y;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else if(index == 10) {
				coords[0] = x + (aw/2.0);
				coords[1] = y;
				if (affine != null) {
					affine.transform(coords, 0, coords, 0, 1);
				}
			} else {
				calcNormalSides(coords,ctrls);
			}
			break;
		}
		
//		int nc = 0;
//		
//		for (int i = 0; i < ctrls.length; i += 4) {
//			coords[nc++] = (x + ctrls[i + 0] * w + ctrls[i + 1] * aw);
//			coords[nc++] = (y + ctrls[i + 2] * h + ctrls[i + 3] * ah);
//		}
//		if (affine != null) {
//			affine.transform(coords, 0, coords, 0, nc / 2);
//		}
		return types[index];
	}
	
	private void calcNormalSides(double[] coords,double[] ctrls) {
		int nc = 0;
		for (int i = 0; i < ctrls.length; i += 4) {
			coords[nc++] = (x + ctrls[i + 0] * w + ctrls[i + 1] * aw);
			coords[nc++] = (y + ctrls[i + 2] * h + ctrls[i + 3] * ah);
		}
		if (affine != null) {
			affine.transform(coords, 0, coords, 0, nc / 2);
		}
	}
	
//	public int currentSegment(double[] coords) {
//		if (isDone()) {
//			throw new NoSuchElementException("roundrect iterator out of bounds");
//		}
//		double ctrls[] = ctrlpts[index];
//		int nc = 0;
//		
//		for (int i = 0; i < ctrls.length; i += 4) {
//			coords[nc++] = (x + ctrls[i + 0] * w + ctrls[i + 1] * aw);
//			coords[nc++] = (y + ctrls[i + 2] * h + ctrls[i + 3] * ah);
//		}
//		if (affine != null) {
//			affine.transform(coords, 0, coords, 0, nc / 2);
//		}
//		return types[index];
//	}
	
}
