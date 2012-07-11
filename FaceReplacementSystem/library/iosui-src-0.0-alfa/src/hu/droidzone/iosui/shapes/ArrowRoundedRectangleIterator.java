package hu.droidzone.iosui.shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

class ArrowRoundedRectangleIterator implements PathIterator {
    double x, y, w, h, aw, ah, mp, mh;
    AffineTransform affine;
    int index;

    ArrowRoundedRectangleIterator(ArrowRoundedRectangleShape rr, AffineTransform at) {
	this.x = rr.getX();
	this.y = rr.getY();
	this.w = rr.getWidth();
	this.h = rr.getHeight();
	this.aw = Math.min(w, Math.abs(rr.getArcWidth()));
	this.ah = Math.min(h, Math.abs(rr.getArcHeight()));
	mp = h/2 + 1;
	mh = h/2;
	this.affine = at;
	if (aw < 0 || ah < 0) {
	    // Don't draw anything...
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
    //     6 values for each point {v0, v1, v2, v3, v4, v5}:
    //         point = (x + v0 * w + v1 * arcWidth + mh * v4,
    //                  y + v2 * h + v3 * arcHeight + mp * v5);
    private static double ctrlpts[][] = {
	{  0.0,  0.0,  0.0,  0.0, 1.0, 0.0 },//move
	{  0.0,  0.0,  0.0,  0.0, 0.0, 1.0 },//line
	{  0.0,  0.0,  1.0,  0.0, 1.0, 0.0 },//line
	{  1.0, -0.5,  1.0,  0.0, 0.0, 0.0 },//line
	{  1.0, -acv,  1.0,  0.0, 0.0, 0.0, 1.0,  0.0,  1.0, -acv, 0.0, 0.0, 1.0,  0.0,  1.0, -0.5, 0.0, 0.0 },//arc
	{  1.0,  0.0,  0.0,  0.5, 0.0, 0.0 },//line
	{  1.0,  0.0,  0.0,  acv, 0.0, 0.0, 1.0, -acv,  0.0,  0.0, 0.0, 0.0,1.0, -0.5,  0.0,  0.0, 0.0, 0.0 },//arc
	{  0.0,  0.0,  0.0,  0.0, 1.0, 0.0 },//line
	{},
    };
    private static int types[] = {
	SEG_MOVETO,
	SEG_LINETO, 
	SEG_LINETO,
	SEG_LINETO, 
	SEG_CUBICTO,
	SEG_LINETO, 
	SEG_CUBICTO,
	SEG_LINETO, 
	SEG_CLOSE,
    };
    
//    private static double ctrlpts[][] = {
//	{  0.0,  0.0,  0.0,  0.5 },//move
//	{  0.0,  0.0,  1.0, -0.5 },//line
//	{  0.0,  0.0,  1.0, -acv, 0.0,  acv,  1.0,  0.0, 0.0,  0.5,  1.0,  0.0 },//arc
//	{  1.0, -0.5,  1.0,  0.0 },//line
//	{  1.0, -acv,  1.0,  0.0, 1.0,  0.0,  1.0, -acv, 1.0,  0.0,  1.0, -0.5 },//arc
//	{  1.0,  0.0,  0.0,  0.5 },//line
//	{  1.0,  0.0,  0.0,  acv, 1.0, -acv,  0.0,  0.0, 1.0, -0.5,  0.0,  0.0 },//arc
//	{  0.0,  0.5,  0.0,  0.0 },//line
//	{  0.0,  acv,  0.0,  0.0, 0.0,  0.0,  0.0,  acv, 0.0,  0.0,  0.0,  0.5 },//arc
//	{},
//    };
//    private static int types[] = {
//	SEG_MOVETO,
//	SEG_LINETO, SEG_CUBICTO,
//	SEG_LINETO, SEG_CUBICTO,
//	SEG_LINETO, SEG_CUBICTO,
//	SEG_LINETO, SEG_CUBICTO,
//	SEG_CLOSE,
//    };

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A float array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of float x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    public int currentSegment(float[] coords) {
	if (isDone()) {
	    throw new NoSuchElementException("roundrect iterator out of bounds");
	}
	double ctrls[] = ctrlpts[index];
	int nc = 0;
	for (int i = 0; i < ctrls.length; i += 6) {
	    coords[nc++] = (float) (x + ctrls[i + 0] * w + ctrls[i + 1] * aw + ctrls[i + 4] * mh);
	    coords[nc++] = (float) (y + ctrls[i + 2] * h + ctrls[i + 3] * ah + ctrls[i + 5] * mp);
	}
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, nc / 2);
	}
	return types[index];
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A double array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of double x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    public int currentSegment(double[] coords) {
	if (isDone()) {
	    throw new NoSuchElementException("roundrect iterator out of bounds");
	}
	double ctrls[] = ctrlpts[index];
	int nc = 0;
	for (int i = 0; i < ctrls.length; i += 4) {
	    coords[nc++] = (x + ctrls[i + 0] * w + ctrls[i + 1] * aw + ctrls[i + 4] * mh);
	    coords[nc++] = (y + ctrls[i + 2] * h + ctrls[i + 3] * ah + ctrls[i + 5] * mp);
	}
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, nc / 2);
	}
	return types[index];
    }
}
