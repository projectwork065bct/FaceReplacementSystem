package hu.droidzone.iosui;

import hu.droidzone.iosui.IOSUIProgressPainter.Size;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public abstract class IOSUIComponent extends JComponent { //Panel {
	private static final long serialVersionUID = 4450395604146311853L;
	private float alpha = 1.0f;
	protected boolean clippingSafe = true;
	private IOSUIProgressPainter pp;
	
	public IOSUIComponent() {
		setOpaque(false);
		setLayout(null);
		setBackground(new Color(255,255,255,0));
		setDoubleBuffered(true);
		setPreferredSize(new Dimension(75,25));
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				resizeBusyPainter();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	protected void setPreferredSizeFor(String str,int h) {
		setPreferredSizeFor(str, h, 0);
	}
	protected void setPreferredSizeFor(String str) {
		setPreferredSizeFor(str, -1, 0);
	}
	protected void setPreferredSizeFor(String str,int h,int extra) {
        FontMetrics fm = getFontMetrics(getFont());
        int w = fm.stringWidth(str);
        int hgt = h == -1 ? fm.getHeight() : h;
		setPreferredSize(new Dimension(w+extra,hgt));
	}

	public boolean isEventConsumer(MouseEvent e) {
		return false;
	}
	
	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(int alfa) {
		setAlpha((float)alfa/255.0f);
	}
	public void setAlpha(float alfa) {
		this.alpha = alfa;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		if(clippingSafe) {
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);			
//	        g2.setColor(getBackground());//new Color(255, 255, 255, 0));
//	        g2.fillRect(0, 0, getWidth(), getHeight());
	        if(alpha < 1.0f) {
		        AlphaComposite ac = AlphaComposite.SrcOver;
		        ac = ac.derive(alpha);
		        g2.setComposite(ac);
	        }
			paintUI(g2);
		} else {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
	
			int w = getWidth();
			int h = getHeight();
			BufferedImage bi = gc.createCompatibleImage(w, h, Transparency.BITMASK);//TRANSLUCENT);
			Graphics2D g2c = bi.createGraphics();
	        g2c.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2c.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);			
	        if(alpha < 1.0f) {
		        AlphaComposite ac = AlphaComposite.SrcOver;
		        ac = ac.derive(alpha);
		        g2c.setComposite(ac);
	        }
	        g2c.setColor(new Color(255, 255, 255, 0));
	        g2c.fillRect(0, 0, w, h);
	        paintUI(g2c);
			g2.drawImage(bi, 0, 0, w, h, 0, 0, w, h, null);
			g2c.dispose();
		}
	}

	private Shape oldClipping = null;
	public void backupClipping(Graphics2D g2,Shape newClipping) {
        oldClipping = g2.getClip();
        if(oldClipping != null) {
    		Area areaOne = new Area(oldClipping);
    		Area areaTwo = new Area(newClipping);
   			areaOne.intersect(areaTwo);
   			g2.setClip(areaOne);
        } else {
	        g2.setClip(newClipping);
        }
	}
	
	public void restoreClipping(Graphics2D g2) {
        g2.setClip(oldClipping);
	}
	
	protected abstract void paintUI(Graphics2D g2);
	private List<PropertyChangeListener>valueListeners = new ArrayList<PropertyChangeListener>();
	public void addValueListener(PropertyChangeListener l) {
		valueListeners.add(l);
	}
	public void removeValueListener(PropertyChangeListener l) {
		valueListeners.remove(l);
	}

	public void fireValueChanged(Object oldValue, Object newValue) {
		PropertyChangeEvent pce = new PropertyChangeEvent(this, "value", oldValue, newValue);
		for(PropertyChangeListener l : valueListeners) {
			l.propertyChange(pce);
		}
	}
	
	private boolean busy = false;
	
	protected IOSUIProgressPainter getProgressPainter() {
		int s = Math.min(getWidth(), getHeight());
		Size ss = Size.SMALL;
		if(s > 80) ss = Size.LARGE;
		else if(s > 20) ss = Size.MEDIUM;
		return new IOSUIProgressPainter(ss,this);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for(Component c : getComponents()) {
			c.setEnabled(enabled);
		}
	}
	
	protected void resizeBusyPainter() {
		if(busy) {
			if(pp != null) pp.stop();
			pp = getProgressPainter();
			pp.start();
		}
	}
	
	public void setBusy(boolean busy) {
		if(this.busy == busy) return;
		this.busy = busy;
		if(busy) {
			if(pp == null) {
				pp = getProgressPainter();
			}
			pp.start();
			setEnabled(false);
		} else {
			if(pp != null) pp.stop();
			setEnabled(true);
		}
	}

	public boolean isBusy() {
		return busy;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(pp != null && busy) pp.paint((Graphics2D) g);
	}
	
	public void applicationOpened() {
		for(Component c : getComponents()) {
			if(c instanceof IOSUIComponent) {
				IOSUIComponent ios = (IOSUIComponent) c;
				ios.applicationOpened();
			}
		}
	}

	protected void drawTextInRect(Graphics2D g2, Rectangle r, String text) {
        FontMetrics fm = g2.getFontMetrics(getFont());
		Icon icon = null;
		int verticalAlignment = SwingConstants.CENTER;
		int horizontalAlignment = SwingConstants.CENTER;
		int verticalTextPosition = SwingConstants.CENTER;
		int horizontalTextPosition = SwingConstants.CENTER;
		Rectangle viewR = r;
		Rectangle iconR = new Rectangle();
		Rectangle textR = new Rectangle();
		int textIconGap = 0;
        int ascent = fm.getAscent();
		String str = SwingUtilities.layoutCompoundLabel(fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
		g2.drawString(str, textR.x, textR.y+ascent);
	}
}
