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

public abstract class ArrowRoundedRectangleShape extends RectangularShape {
	public static class Float extends ArrowRoundedRectangleShape implements Serializable
	{
		public float x;
		public float y;
		public float width;
		public float height;
		public float arcwidth;
		public float archeight;
		
		public Float() {
		}
		
		public Float(float x, float y, float w, float h,float arcw, float arch)	{
			setBubleRect(x, y, w, h, arcw, arch);
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

		public void setRoundRect(double x, double y, double w, double h,double arcw, double arch) {
			this.x = (float) x;
			this.y = (float) y;
			this.width = (float) w;
			this.height = (float) h;
			this.arcwidth = (float) arcw;
			this.archeight = (float) arch;
		}

		public Rectangle2D getBounds2D() {
			return new Rectangle2D.Float(x, y, width, height);
		}

		private static final long serialVersionUID = -3423150618393866922L;

		@Override
		public void setBubleRect(double x, double y, double w, double h,double arcWidth, double arcHeight) {
			setRoundRect(x, y, w, h, arcWidth, arcHeight);
		}
	}

	public static class Double extends ArrowRoundedRectangleShape implements Serializable
	{
		public double x;
		public double y;
		public double width;
		public double height;
		public double arcwidth;
		public double archeight;
		
		public Double() {
		}
		
		@Override
		public void setBubleRect(double x, double y, double w, double h,double arcWidth, double arcHeight) {
			setRoundRect(x, y, w, h, arcWidth, arcHeight);
		}
		
		public Double(double x, double y, double w, double h,double arcw, double arch) {
			setBubleRect(x, y, w, h, arcw, arch);
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

		public void setRoundRect(ArrowRoundedRectangleShape rr) {
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
	}

	protected ArrowRoundedRectangleShape() {
	}

	public double getMarkerHeight() {
		return getHeight()/2.0f;
	}

	public abstract double getArcWidth();

	public abstract double getArcHeight();

	public abstract void setBubleRect(double x, double y, double w, double h, double arcWidth, double arcHeight);

	public void setBubleRect(ArrowRoundedRectangleShape rr) {
		setBubleRect(rr.getX(), rr.getY(), rr.getWidth(), rr.getHeight(), rr.getArcWidth(), rr.getArcHeight());
	}

	public void setFrame(double x, double y, double w, double h) {
		setBubleRect(x, y, w, h, getArcWidth(), getArcHeight());
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
		return new ArrowRoundedRectangleIterator(this, at);
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
		if (obj instanceof ArrowRoundedRectangleShape) {
			ArrowRoundedRectangleShape rr2d = (ArrowRoundedRectangleShape) obj;
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
				
				ArrowRoundedRectangleShape brA = new ArrowRoundedRectangleShape.Double(20, 20, w2-40, h2-40, 10, 10);
				g2.setColor(Color.YELLOW);
				g2.fill(brA);
				g2.setColor(Color.BLACK);
				g2.draw(brA);
				
				ArrowRoundedRectangleShape brB = new ArrowRoundedRectangleShape.Double(w2+20, 20, w2-40, h2-40, 20, 20);
				g2.setColor(Color.YELLOW);
				g2.fill(brB);
				g2.setColor(Color.BLACK);
				g2.draw(brB);
				
				ArrowRoundedRectangleShape brC = new ArrowRoundedRectangleShape.Double(20, h2+20, w2-40, h2-40, 20, 20);
				g2.setColor(Color.YELLOW);
				g2.fill(brC);
				g2.setColor(Color.BLACK);
				g2.draw(brC);
				
				ArrowRoundedRectangleShape brD = new ArrowRoundedRectangleShape.Double(w2+20, h2+20, w2-40, h2-40, 20, 20);
				g2.setColor(Color.YELLOW);
				g2.fill(brD);
				g2.setColor(Color.BLACK);
				g2.draw(brD);
				
				RoundRectangle2D rr2d = new RoundRectangle2D.Double(w2+100, h2 + 100, 100, 27,27,27);
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
