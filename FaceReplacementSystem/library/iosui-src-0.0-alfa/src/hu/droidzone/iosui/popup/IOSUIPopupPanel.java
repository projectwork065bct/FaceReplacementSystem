package hu.droidzone.iosui.popup;

import hu.droidzone.iosui.Utils;
import hu.droidzone.iosui.shapes.BubleRectangleShape;
import hu.droidzone.iosui.shapes.MarkerDirection;
import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class IOSUIPopupPanel extends JComponent {
	private static final long serialVersionUID = 8407724006351600212L;
	public static final int DEFAULT_TITLE_HEIGHT = 38;
//	IOSUIPopupWindow parent;

	private double titleHeight = DEFAULT_TITLE_HEIGHT;
	private double footerHeight = 38;
	
	private static final Color frameFillColor = new Color(11,22,41,250);
//	private static final Color frameColorLight = new Color(210,210,210,50);
	private static final Color lightFrameColor = new Color(185,185,195,50);
	private static final Color darkFrameColor = new Color(8,8,8,250);
	private static final Color lightShadeColor = new Color(185,185,195,250);
	private static final Color darkShadeColor = new Color(11,22,41,250);
	private static final int SHADED_TOP_HEIGHT = 22;

	private static final boolean DEBUG_LAYOUT = false;
	
	private int markerWidth = 36;
	private int markerHeight = 18;
	private MarkerDirection markerDirection;
	private double markerLocation = BubleRectangleShape.CENTER;
	private boolean paintContent;
	
	public IOSUIPopupPanel() {
		this(MarkerDirection.TOP,true);
	}
	
	public double getTitleHeight() {
		return titleHeight;
	}

	public void setTitleHeight(double titleHeight) {
		titleHeight += 8;
		this.titleHeight = Math.max(titleHeight,6);
	}

	
	public double getFooterHeight() {
		return footerHeight;
	}

	public void setFooterHeight(double footerHeight) {
		this.footerHeight = footerHeight;
	}

	public IOSUIPopupPanel(MarkerDirection markerDirection) {
		this(markerDirection, true);
	}
	
	
	public double getMarkerLocation() {
		return markerLocation;
	}

	public void setMarkerLocation(double markerLocation) {
		this.markerLocation = markerLocation;
	}

	public IOSUIPopupPanel(MarkerDirection markerDirection,boolean paintContent) {
		this.markerDirection = markerDirection;
		this.paintContent = paintContent;
		if(markerDirection == MarkerDirection.NONE) {
			this.markerHeight = 0;
			this.markerDirection = MarkerDirection.TOP;
		}
		setOpaque(Utils.isLinux());
		setLayout(null);
	}
	
	public static Dimension calculateBoundsFor(int w, int h, int titleH, int footerH, MarkerDirection md) {
		Rectangle br = new Rectangle(w+12+10,h+titleH+footerH+4+6+8);
		int markerHeight = 18;
		switch(md) {
		case BOTTOM:
		case TOP:
			br.height += markerHeight;
			break;
		case LEFT:
		case RIGHT:
			br.width += markerHeight;
			break;
		}
		return new Dimension(br.width, br.height);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
		BubleRectangleShape brA = new BubleRectangleShape.Double(2, 2, getWidth()-4, getHeight()-4, 20, 20, markerDirection, markerLocation, markerWidth, markerHeight);

        RoundRectangle2D r2d = brA.getRoundRect();
		Rectangle upperR = new Rectangle(r2d.getBounds());
		upperR.height = SHADED_TOP_HEIGHT;
		Rectangle bottomR = new Rectangle(brA.getBounds());
		bottomR.y = upperR.y + upperR.height;
		
		Rectangle fullUpperR = new Rectangle(getWidth(), bottomR.y);
        g2.setColor(frameFillColor);
        g2.setClip(bottomR);
        g2.fill(brA);
        g2.setClip(fullUpperR);
        
        Paint pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, fullUpperR.height, darkShadeColor, true);
        g2.setPaint(pShade);
        g2.fill(brA);
        g2.setPaint(null);
        
        g2.setClip(null);
        

        if(paintContent) {
        	g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        	g2.setColor(lightFrameColor);
        	g2.draw(brA);
        	g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        	g2.setColor(darkShadeColor);
        	g2.draw(brA);
	        RoundRectangle2D rr = brA.getRoundRect();
	        rr.setRoundRect(rr.getX() + 6, rr.getY() + titleHeight, rr.getWidth() - 12 , rr.getHeight() - 6 - titleHeight - footerHeight, 20 - 6, 20 - 6);
	        g2.setColor(getBackground());
	        g2.fill(rr);
	        
	        
	        upperR = rr.getBounds();
	        upperR.height = 5;
	        upperR.x -= 2;
	        upperR.width += 4;
	        
	        Color darkShadow = IOSUIHelper.setAlpha(Color.BLACK,200);
	        Color lightShadow = IOSUIHelper.setAlpha(Color.LIGHT_GRAY,0);
	        pShade = new GradientPaint(getWidth()/2.0f, upperR.y, darkShadow,getWidth()/2.0f, upperR.y + upperR.height, lightShadow, false);
	        g2.setClip(upperR);
	        g2.setPaint(pShade);
	        g2.fill(rr);
	        g2.setPaint(null);
	        g2.setClip(null);
			g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.setColor(lightFrameColor);
			g2.draw(rr);
			g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        g2.setColor(darkFrameColor);
			g2.draw(rr);
        }
        
        if(DEBUG_LAYOUT) {
	        g2.setColor(Color.GREEN);
	        g2.draw(getContentBounds());
	        g2.setColor(Color.RED);
	        g2.draw(getTitleBounds());
	        g2.draw(getFooterBounds());
        }
	}

	public Rectangle getContentBounds() {
		BubleRectangleShape brA = new BubleRectangleShape.Double(2, 2, getWidth()-4, getHeight()-4, 20, 20, markerDirection, BubleRectangleShape.CENTER, markerWidth, markerHeight);
        RoundRectangle2D rr = brA.getRoundRect();
        rr.setRoundRect(rr.getX() + 6, rr.getY() + titleHeight, rr.getWidth() - 12 , rr.getHeight() - 6 - titleHeight - footerHeight, 20 - 6, 20 - 6);
        Rectangle r = rr.getBounds();
		return new Rectangle(r.x+4,r.y+5,r.width-6, r.height - 8);
	}
	
	public Rectangle getTitleBounds() {
		BubleRectangleShape brA = new BubleRectangleShape.Double(1, 1, getWidth()-2, getHeight()-2, 20, 20, markerDirection, BubleRectangleShape.CENTER, markerWidth, markerHeight);
        RoundRectangle2D rr = brA.getRoundRect();
        rr.setRoundRect(rr.getX() + 6, rr.getY() + 4, rr.getWidth() - 12 , titleHeight, 20 - 6, 20 - 6);
        Rectangle r = rr.getBounds();
		return new Rectangle(r.x+2,r.y+2,r.width-5, r.height - 4);
	}
	
	public Rectangle getFooterBounds() {
		BubleRectangleShape brA = new BubleRectangleShape.Double(1, 1, getWidth()-2, getHeight()-2, 20, 20, markerDirection, BubleRectangleShape.CENTER, markerWidth, markerHeight);
        RoundRectangle2D rr = brA.getRoundRect();
        rr.setRoundRect(rr.getX() + 6, rr.getY() + rr.getHeight() - 6 - footerHeight, rr.getWidth() - 12 , footerHeight, 20 - 6, 20 - 6);
        Rectangle r = rr.getBounds();
		return new Rectangle(r.x+2,r.y+2,r.width-5, r.height - 4);
	}
}
