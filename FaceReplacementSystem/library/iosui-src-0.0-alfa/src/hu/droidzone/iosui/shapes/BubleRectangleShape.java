package hu.droidzone.iosui.shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class BubleRectangleShape extends RectangularShape {
	public static final double CENTER = -1.0;
	public static class Float extends BubleRectangleShape implements Serializable
	{
		public float x;
		public float y;
		public float width;
		public float height;
		public float arcwidth;
		public float archeight;
		public float markerLocation;
		public float markerWidth;
		public float markerHeight;
		
		public Float() {
		}
		public Float(float x, float y, float w, float h,float arcw, float arch, MarkerDirection direction)	{
			this(x, y, w, h, arcw, arch, direction, (float)CENTER, (float)38.0, (float)20.0);
		}
		public Float(float x, float y, float w, float h,float arcw, float arch)	{
			this(x, y, w, h, arcw, arch, MarkerDirection.TOP, (float)CENTER, (float)38.0, (float)20.0);
		}
		public Float(float x, float y, float w, float h,float arcw, float arch, MarkerDirection direction, float markerLocation, float markerWidth, float markerHeigth)	{
			setBubleRect(x, y, w, h, arcw, arch, markerWidth, markerHeigth, direction, markerLocation);
		}

		public double getX() {
			return (double) x;
		}

		public double getY() {
			return (double) y;
		}

		public double getWidth() {
			return (double) width;
		}

		public double getHeight() {
			return (double) height;
		}

		public double getArcWidth() {
			return (double) arcwidth;
		}

		public double getArcHeight() {
			return (double) archeight;
		}

		public boolean isEmpty() {
			return (width <= 0.0f) || (height <= 0.0f);
		}

//		public void setRoundRect(float x, float y, float w, float h,float arcw, float arch) {
//			this.x = x;
//			this.y = y;
//			this.width = w;
//			this.height = h;
//			this.arcwidth = arcw;
//			this.archeight = arch;
//		}

		public void setRoundRect(double x, double y, double w, double h,double arcw, double arch) {
			this.x = (float) x;
			this.y = (float) y;
			this.width = (float) w;
			this.height = (float) h;
			this.arcwidth = (float) arcw;
			this.archeight = (float) arch;
		}

//		public void setRoundRect(BubleRectangleShape rr) {
//			this.x = (float) rr.getX();
//			this.y = (float) rr.getY();
//			this.width = (float) rr.getWidth();
//			this.height = (float) rr.getHeight();
//			this.arcwidth = (float) rr.getArcWidth();
//			this.archeight = (float) rr.getArcHeight();
//		}

		public Rectangle2D getBounds2D() {
			return new Rectangle2D.Float(x, y, width, height);
		}

		private static final long serialVersionUID = -3423150618393866922L;

		@Override
		public double getMarkerLocation() {
			return markerLocation;
		}
		@Override
		public double getMarkerWidth() {
			return markerWidth;
		}
		@Override
		public double getMarkerHeight() {
			return markerHeight;
		}
		@Override
		public void setBubleRect(double x, double y, double w, double h,
				double arcWidth, double arcHeight, double markerWidth,
				double markerHeight, MarkerDirection direction,
				double markerLocation) {
			setRoundRect(x, y, w, h, arcWidth, arcHeight);
			this.markerHeight = (float) markerHeight;
			this.markerWidth = (float) markerWidth;
			this.markerLocation = (float)markerLocation;
			setMarkerDirection(direction);
		}
	}

	public static class Double extends BubleRectangleShape implements Serializable
	{
		public double x;
		public double y;
		public double width;
		public double height;
		public double arcwidth;
		public double archeight;
		public double markerLocation;
		public double markerWidth;
		public double markerHeight;
		
		public Double() {
		}
		
		@Override
		public void setBubleRect(double x, double y, double w, double h,
				double arcWidth, double arcHeight, double markerWidth,
				double markerHeight, MarkerDirection direction,
				double markerLocation) {
			setRoundRect(x, y, w, h, arcWidth, arcHeight);
			this.markerHeight = markerHeight;
			this.markerWidth = markerWidth;
			this.markerLocation = markerLocation;
			setMarkerDirection(direction);
		}
		
		public Double(double x, double y, double w, double h,double arcw, double arch, MarkerDirection direction)	{
			this(x, y, w, h, arcw, arch, direction, CENTER, 38.0, 20.0);
		}
		
		public Double(double x, double y, double w, double h,double arcw, double arch)	{
			this(x, y, w, h, arcw, arch, MarkerDirection.TOP, CENTER, 38.0, 20.0);
		}
		
		public Double(double x, double y, double w, double h,double arcw, double arch, MarkerDirection direction, double markerLocation, double markerWidth, double markerHeigth) {
			setBubleRect(x, y, w, h, arcw, arch, markerWidth, markerHeigth, direction, markerLocation);
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getWidth() {
			return width;
		}

		public double getHeight() {
			return height;
		}

		public double getArcWidth() {
			return arcwidth;
		}

		public double getArcHeight() {
			return archeight;
		}

		public boolean isEmpty() {
			return (width <= 0.0f) || (height <= 0.0f);
		}

		public void setRoundRect(double x, double y, double w, double h,double arcw, double arch) {
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
			this.arcwidth = arcw;
			this.archeight = arch;
		}

		public void setRoundRect(BubleRectangleShape rr) {
			this.x = rr.getX();
			this.y = rr.getY();
			this.width = rr.getWidth();
			this.height = rr.getHeight();
			this.arcwidth = rr.getArcWidth();
			this.archeight = rr.getArcHeight();
		}

		public Rectangle2D getBounds2D() {
			return new Rectangle2D.Double(x, y, width, height);
		}

		private static final long serialVersionUID = 1048939333485206117L;

		@Override
		public double getMarkerLocation() {
			return markerLocation;
		}

		@Override
		public double getMarkerWidth() {
			// TODO Auto-generated method stub
			return markerWidth;
		}

		@Override
		public double getMarkerHeight() {
			return markerHeight;
		}
	}

	protected BubleRectangleShape() {
	}

	
	private MarkerDirection markerDirection;
	
	public MarkerDirection getMarkerDirection() {
		return markerDirection;
	}

	public void setMarkerDirection(MarkerDirection markerDirection) {
		this.markerDirection = markerDirection;
	}

	public RoundRectangle2D getRoundRect() {
		double xx = getX();
		double yy = getY();
		double ww = getWidth();
		double hh = getHeight();
		double mh = getMarkerHeight();
		
		switch(markerDirection) {
		case BOTTOM:
			hh -= mh;
			break;
		case LEFT:
			xx += mh;
			ww -= mh;
			break;
		case RIGHT:
			ww -= mh;
			break;
		case TOP:
			yy += mh;
			hh -= mh;
			break;
		}
		return new RoundRectangle2D.Double(xx,yy,ww,hh,getArcWidth(), getArcHeight());
	}
	
	public abstract double getMarkerLocation();
	public abstract double getMarkerWidth();
	public abstract double getMarkerHeight();
	
	public abstract double getArcWidth();

	public abstract double getArcHeight();

	public abstract void setBubleRect(double x, double y, double w, double h, double arcWidth, double arcHeight, double markerWidth, double markerHeight, MarkerDirection direction, double markerLocation);

	public void setBubleRect(BubleRectangleShape rr) {
		setBubleRect(rr.getX(), rr.getY(), rr.getWidth(), rr.getHeight(), rr.getArcWidth(), rr.getArcHeight(), rr.getMarkerWidth(), rr.getMarkerHeight(), rr.getMarkerDirection(), rr.getMarkerLocation());
	}

	public void setFrame(double x, double y, double w, double h) {
		setBubleRect(x, y, w, h, getArcWidth(), getArcHeight(), getMarkerWidth(), getMarkerHeight(), getMarkerDirection(), getMarkerLocation());
	}

	public boolean contains(double x, double y) {
		if (isEmpty()) {
			return false;
		}
		double rrx0 = getX();
		double rry0 = getY();
		double rrx1 = rrx0 + getWidth();
		double rry1 = rry0 + getHeight();
		// Check for trivial rejection - point is outside bounding rectangle
		if (x < rrx0 || y < rry0 || x >= rrx1 || y >= rry1) {
			return false;
		}
		double aw = Math.min(getWidth(), Math.abs(getArcWidth())) / 2.0;
		double ah = Math.min(getHeight(), Math.abs(getArcHeight())) / 2.0;
		// Check which corner point is in and do circular containment
		// test - otherwise simple acceptance
		if (x >= (rrx0 += aw) && x < (rrx0 = rrx1 - aw)) {
			return true;
		}
		if (y >= (rry0 += ah) && y < (rry0 = rry1 - ah)) {
			return true;
		}
		x = (x - rrx0) / aw;
		y = (y - rry0) / ah;
		return (x * x + y * y <= 1.0);
	}

	private int classify(double coord, double left, double right,double arcsize)
	{
		if (coord < left) {
			return 0;
		} else if (coord < left + arcsize) {
			return 1;
		} else if (coord < right - arcsize) {
			return 2;
		} else if (coord < right) {
			return 3;
		} else {
			return 4;
		}
	}

	public boolean intersects(double x, double y, double w, double h) {
		if (isEmpty() || w <= 0 || h <= 0) {
			return false;
		}
		double rrx0 = getX();
		double rry0 = getY();
		double rrx1 = rrx0 + getWidth();
		double rry1 = rry0 + getHeight();
		// Check for trivial rejection - bounding rectangles do not intersect
		if (x + w <= rrx0 || x >= rrx1 || y + h <= rry0 || y >= rry1) {
			return false;
		}
		double aw = Math.min(getWidth(), Math.abs(getArcWidth())) / 2.0;
		double ah = Math.min(getHeight(), Math.abs(getArcHeight())) / 2.0;
		int x0class = classify(x, rrx0, rrx1, aw);
		int x1class = classify(x + w, rrx0, rrx1, aw);
		int y0class = classify(y, rry0, rry1, ah);
		int y1class = classify(y + h, rry0, rry1, ah);
		// Trivially accept if any point is inside inner rectangle
		if (x0class == 2 || x1class == 2 || y0class == 2 || y1class == 2) {
			return true;
		}
		// Trivially accept if either edge spans inner rectangle
		if ((x0class < 2 && x1class > 2) || (y0class < 2 && y1class > 2)) {
			return true;
		}
		// Since neither edge spans the center, then one of the corners
		// must be in one of the rounded edges.  We detect this case if
		// a [xy]0class is 3 or a [xy]1class is 1.  One of those two cases
		// must be true for each direction.
		// We now find a "nearest point" to test for being inside a rounded
		// corner.
		x = (x1class == 1) ? (x = x + w - (rrx0 + aw)) : (x = x - (rrx1 - aw));
		y = (y1class == 1) ? (y = y + h - (rry0 + ah)) : (y = y - (rry1 - ah));
		x = x / aw;
		y = y / ah;
		return (x * x + y * y <= 1.0);
	}

	public boolean contains(double x, double y, double w, double h) {
		if (isEmpty() || w <= 0 || h <= 0) {
			return false;
		}
		return (contains(x, y) &&
				contains(x + w, y) &&
				contains(x, y + h) &&
				contains(x + w, y + h));
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return new BubleRectangleIterator(this, at);
	}

	public int hashCode() {
		long bits = java.lang.Double.doubleToLongBits(getX());
		bits += java.lang.Double.doubleToLongBits(getY()) * 37;
		bits += java.lang.Double.doubleToLongBits(getWidth()) * 43;
		bits += java.lang.Double.doubleToLongBits(getHeight()) * 47;
		bits += java.lang.Double.doubleToLongBits(getArcWidth()) * 53;
		bits += java.lang.Double.doubleToLongBits(getArcHeight()) * 59;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof BubleRectangleShape) {
			BubleRectangleShape rr2d = (BubleRectangleShape) obj;
			return ((getX() == rr2d.getX()) &&
					(getY() == rr2d.getY()) &&
					(getWidth() == rr2d.getWidth()) &&
					(getHeight() == rr2d.getHeight()) &&
					(getArcWidth() == rr2d.getArcWidth()) &&
					(getArcHeight() == rr2d.getArcHeight()));
		}
		return false;
	}
	
	public static void main(String[] args) {
		JFrame frm = new JFrame("BubleRectangleShape Tester");
		JPanel pan = new JPanel() {
			public void paint(java.awt.Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0,0,getWidth(),getHeight());
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				double w2 = (double)getWidth()/2.0;
				double h2 = (double)getHeight()/2.0;
				g2.setColor(Color.BLACK);
				g2.drawLine((int)w2,0, (int)w2, getHeight());
				g2.drawLine(0, (int)h2, getWidth(), (int)h2);
				
				BubleRectangleShape brA = new BubleRectangleShape.Double(20, 20, w2-40, h2-40, 20, 20, MarkerDirection.TOP, CENTER, 38, 20);
				g2.setColor(Color.YELLOW);
				g2.fill(brA);
				g2.setColor(Color.BLACK);
				g2.draw(brA);
				
				BubleRectangleShape brB = new BubleRectangleShape.Double(w2+20, 20, w2-40, h2-40, 20, 20, MarkerDirection.BOTTOM, CENTER, 20, 10);
				g2.setColor(Color.YELLOW);
				g2.fill(brB);
				g2.setColor(Color.BLACK);
				g2.draw(brB);
				
				BubleRectangleShape brC = new BubleRectangleShape.Double(20, h2+20, w2-40, h2-40, 20, 20, MarkerDirection.LEFT, CENTER, 20, 10);
				g2.setColor(Color.YELLOW);
				g2.fill(brC);
				g2.setColor(Color.BLACK);
				g2.draw(brC);
				
				BubleRectangleShape brD = new BubleRectangleShape.Double(w2+20, h2+20, w2-40, h2-40, 20, 20, MarkerDirection.RIGHT, CENTER, 20, 10);
				g2.setColor(Color.YELLOW);
				g2.fill(brD);
				g2.setColor(Color.BLACK);
				g2.draw(brD);

//				BubleRectangleShape brE = new BubleRectangleShape.Double(w2+100, h2 + 100, 100, 27, 10, 10, MarkerDirection.RIGHT, CENTER, 27, 10);
//				g2.setColor(Color.GREEN);
//				g2.fill(brE);
//				g2.setColor(Color.BLACK);
//				g2.draw(brE);

				RoundRectangle2D rr2d = new RoundRectangle2D.Double(w2+100, h2 + 100, 100, 100,40,70);
				g2.setColor(Color.BLACK);
				g2.draw(rr2d);
			};
		};
		frm.getContentPane().add(pan);
		frm.setSize(800,600);
		frm.setLocationRelativeTo(null);
		frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
}
