package hu.droidzone.iosui;

import hu.droidzone.iosui.i18n.IOSI18N;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Sine;

public class IOSUISwitch extends IOSUIComponent {
	private static final long serialVersionUID = -4038564767187367774L;
	private static final int WW = 77;
	private static final int HH = 25;
	private static final int RIGHT_HANDLER_LOCATION = WW - HH + 2;
	private boolean on = !false;
	private boolean switching = false;
	private long animationTime = 180;
	
	private static BufferedImage imgOn;
	private static BufferedImage imgOff;
	
	public IOSUISwitch() {
		setSize(getPreferredSize());
		super.setMinimumSize(getPreferredSize());
		super.setMaximumSize(getPreferredSize());
		setupHandlerLocattion();
		prepareImages();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				triggerSwitch();
			}
		});
	}
	@Override
	public boolean isEventConsumer(MouseEvent e) {
		switch(e.getID()) {
		case MouseEvent.MOUSE_CLICKED:
			return true;
		}
		return false;
	}
	
	protected void triggerSwitch() {
		if(switching) return;
		if(!isEnabled()) return;
		switching = true;
		Timeline tl = new Timeline(this);
		tl.setDuration(animationTime );
		tl.setEase(new Sine());
		Integer from = null;
		Integer to = null;
		
		if(on) {
			from = new Integer(RIGHT_HANDLER_LOCATION);
			to = new Integer(1);
		} else {
			from = new Integer(1);
			to = new Integer(RIGHT_HANDLER_LOCATION);
		}
		tl.addPropertyToInterpolate("handlerLocation",from, to);
		tl.addCallback(new TimelineCallback() {
			@Override
			public void onTimelineStateChanged(TimelineState oldState,TimelineState newState, float durationFraction,float timelinePosition) {
				if(newState == TimelineState.DONE) {
					switching = false;
					on = !on;
					fireValueChanged(!on, on);
				}
			}
			
			@Override
			public void onTimelinePulse(float durationFraction, float timelinePosition) {
			}
		});
		tl.play();
	}

	public void setHandlerLocation(int handlerLocation) {
		this.handlerLocation = handlerLocation;
		repaint();
	}
	
	public int getHandlerLocation() {
		return handlerLocation;
	}
	
	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
		setupHandlerLocattion();
	}

	private void setupHandlerLocattion() {
        handlerLocation = 1;
        if(on) {
        	handlerLocation = RIGHT_HANDLER_LOCATION;
        }
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WW+2, HH+2);
	}
	
	@Override
	public void setMinimumSize(Dimension minimumSize) {
	}
	
	@Override
	public void setMaximumSize(Dimension maximumSize) {
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, WW+2, HH+2);
	}
	
	@Override
	public void setBounds(Rectangle r) {
		r.width = WW+2;
		r.height = HH+2;
		super.setBounds(r);
	}
	private static final Color selectedBackground = new Color(0,132,236);
	private static final Color deselectedBackground = new Color(238,238,238);
	private static final Color lightShadeColor = new Color(0,0,0,50);
	private static final Color darkShadeColor = new Color(0,0,0,0);
	private static final Color offSubShadeColor = new Color(255,255,255,150);
	private static final Color lightSubShadeColor = new Color(255,255,255,80);
	private static final Color darkSubShadeColor = new Color(255,255,255,30);
	
	private static final Color handlerBackground = new Color(238,238,255);
	private static final Color handlerFrame = new Color(0,0,0,100);
	private static final Color lightHandlerShadeColor = new Color(251,251,251,255);
	private static final Color darkHandlerShadeColor = new Color(210,210,210,255);
	
	private Font titleFont = new Font("Verdana", Font.BOLD, 14);
	private static final Color onTitleColor = Color.WHITE;
	private static final Color offTitleColor = Color.GRAY;

	private int handlerLocation = 0;
	
	private void prepareImages() {
		if(imgOn == null) {
//			System.out.println("SWITCH images created");
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			int w = WW+2;
			int h = HH+2;
			BufferedImage biOn = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			Graphics2D gOn = biOn.createGraphics();
	        gOn.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        gOn.setColor(new Color(255, 255, 255, 0));
	        gOn.fillRect(0, 0, getWidth(), getHeight());
			paintStuff(gOn,true);
			BufferedImage biOff = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			Graphics2D gOff = biOff.createGraphics();
			gOff.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gOff.setColor(new Color(255, 255, 255, 0));
			gOff.fillRect(0, 0, getWidth(), getHeight());
			paintStuff(gOff,false);
			imgOn = biOn;
			imgOff = biOff;
			gOn.dispose();
			gOff.dispose();
		}
	}
	
	private void plainPaintUI(Graphics2D g2) {
		if(switching) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();

//			// Create an image that does not support transparency
//			bimage = gc.createCompatibleImage(width, height, Transparency.OPAQUE);
//
//			// Create an image that supports transparent pixels
//			bimage = gc.createCompatibleImage(width, height, Transparency.BITMASK);
//
			// Create an image that supports arbitrary levels of transparency
			int w = getWidth();
			int h = getHeight();
			BufferedImage biOn = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			Graphics2D gOn = biOn.createGraphics();
	        gOn.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        gOn.setColor(new Color(255, 255, 255, 0));
	        gOn.fillRect(0, 0, getWidth(), getHeight());
			paintStuff(gOn,true);
			BufferedImage biOff = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			Graphics2D gOff = biOff.createGraphics();
			gOff.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gOff.setColor(new Color(255, 255, 255, 0));
			gOff.fillRect(0, 0, getWidth(), getHeight());
			paintStuff(gOff,false);
			Rectangle r = new Rectangle();
			int cut = handlerLocation + (HH/2);
			g2.drawImage(biOn, 0, 0, cut, h, 0, 0, cut, h, null);
			g2.drawImage(biOff, cut+1, 0, w+1, h, cut+1, 0, w+1, h, null);
			gOn.dispose();
			gOff.dispose();
		} else {
			paintStuff(g2,on);
		}
		paintHandler(g2);
	}
	
	private static final boolean BUFFERED_PAINT = true; 
	@Override
	protected void paintUI(Graphics2D g2) {
		if(BUFFERED_PAINT) bufferedPaintUI(g2);
		else plainPaintUI(g2);
	}
	
	private void bufferedPaintUI(Graphics2D g2) {
		int w = WW+2;
		int h = HH+2;
		if(switching) {
			BufferedImage biOn = imgOn;
			BufferedImage biOff = imgOff;
			Rectangle r = new Rectangle();
			int cut = handlerLocation + (HH/2);
			g2.drawImage(biOn, 0, 0, cut, h, 0, 0, cut, h, null);
			g2.drawImage(biOff, cut+1, 0, w+1, h, cut+1, 0, w+1, h, null);
			paintTitle(g2,true);
			paintTitle(g2,false);
		} else {
			BufferedImage bi = null;
			if(on) {
				bi = imgOn;
			} else {
				bi = imgOff;
			}
			g2.drawImage(bi, 0, 0, w, h, 0, 0, w, h, null);
			paintTitle(g2,on);
		}
		paintHandler(g2);
	}
	
	private void paintTitle(Graphics2D g2, boolean _on) {
    	g2.setFont(titleFont);
    	String title = _on ? IOSI18N.get("switch.on") : IOSI18N.get("switch.off");
    	g2.setColor(_on ? onTitleColor : offTitleColor);
    	FontMetrics fm = g2.getFontMetrics(titleFont);
    	int hgt = fm.getHeight();
    	int adv = fm.stringWidth(title);
    	int ascent = fm.getAscent();
    	Rectangle tr = null;
		if(_on) {
        	tr = new Rectangle(handlerLocation - (WW-HH-2),5,WW-HH-2,HH+2);
        } else {
        	tr = new Rectangle(handlerLocation + HH,4,WW-HH-2,HH);
        }
        int y = (tr.height - hgt)/2;
        int x = tr.x+(tr.width - adv)/2;
        
//        RoundRectangle2D rr2d = new RoundRectangle2D.Double(1,1,WW,HH,HH,HH);
//        backupClipping(g2, rr2d);
	    g2.drawString(title, x, y+ascent);
//	    restoreClipping(g2);
	}
	private void paintHandler(Graphics2D g2) {
        //draw handler
        double hy = 1;
        Ellipse2D e2d = new Ellipse2D.Double(handlerLocation,hy,HH-1,HH-1);
        Paint pHandler = new GradientPaint(getWidth()/2.0f, 0.0f, darkHandlerShadeColor,getWidth()/2.0f, HH, lightHandlerShadeColor, false);
        g2.setColor(handlerBackground);
        g2.fill(e2d);
        Ellipse2D se2d = new Ellipse2D.Double(handlerLocation+2,hy+2,HH-4,HH-4);
        g2.setPaint(pHandler);
        g2.fill(se2d);
        g2.setPaint(null);
        g2.setColor(handlerFrame);
        g2.draw(e2d);
		
	}
	private void paintStuff(Graphics2D g2, boolean _on) {
        RoundRectangle2D rr2d = new RoundRectangle2D.Double(1,1,WW,HH,HH,HH);
        
        //draw background
        if(_on) {
        	g2.setColor(selectedBackground);
        } else {
        	g2.setColor(deselectedBackground);
        }
        g2.fill(rr2d);
        
        Paint pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, 4, darkShadeColor, false);
        g2.setPaint(pShade);
        g2.fill(rr2d);
        g2.setPaint(null);
        
//        g2.setClip(null);
        
        
        //draw sub shade
        RoundRectangle2D sr2d = new RoundRectangle2D.Double(5,getHeight()/2.0,WW-8,HH/2.0,HH/2.0,HH/2.0);
        if(_on) {
	        Paint pSubShade = new GradientPaint(WW/2.0f, 0.0f, lightSubShadeColor,WW/2.0f, HH/2.0f, darkSubShadeColor, true);
	        g2.setPaint(pSubShade);
	        g2.fill(sr2d);
	        g2.setPaint(null);
        } else {
	        g2.setPaint(null);
	        g2.setColor(offSubShadeColor);
	        g2.fill(sr2d);
	        g2.setPaint(null);
        }
        
        //draw frame
        RoundRectangle2D fr2d = new RoundRectangle2D.Double(1,1,WW,HH-1,HH,HH);
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(lightShadeColor);
        g2.draw(fr2d);
        
        
        //draw title
        if(!BUFFERED_PAINT) {
        	g2.setFont(titleFont);
        	String title = _on ? IOSI18N.get("switch.on") : IOSI18N.get("switch.off");
        	g2.setColor(_on ? onTitleColor : offTitleColor);
        	FontMetrics fm = g2.getFontMetrics(titleFont);
        	int hgt = fm.getHeight();
        	int adv = fm.stringWidth(title);
        	int ascent = fm.getAscent();
        	Rectangle tr = null;
	        if(_on) {
	        	tr = new Rectangle(handlerLocation - (WW-HH-2),5,WW-HH-2,HH+2);
	        } else {
	        	tr = new Rectangle(handlerLocation + HH,4,WW-HH-2,HH);
	        }
	        int y = (tr.height - hgt)/2;
	        int x = tr.x+(tr.width - adv)/2;
	        
	        backupClipping(g2, rr2d);
		    g2.drawString(title, x, y+ascent);
		    restoreClipping(g2);
        }
	}
}
