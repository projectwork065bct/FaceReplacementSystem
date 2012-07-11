package hu.droidzone.iosui;

import hu.droidzone.iosui.utils.AWTUtilitiesWrapper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ResizerFrame extends JFrame {
	private static final long serialVersionUID = 432109232216154673L;
	private Dimension dim;
	private Rectangle resizeRect;
	private Rectangle prevRect;
	
	private static ResizerFrame resizerFrame;
	
	public static void refreshResize(Rectangle rb) {
		if(resizerFrame != null) resizerFrame._refreshResize(rb);
	}
	
	public static boolean isResizing() {
		return resizerFrame != null && resizerFrame.isVisible();
	}
	
	public static void startResize(Rectangle rb) {
		if(resizerFrame == null) {
			try {
				resizerFrame = new ResizerFrame();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		if(resizerFrame != null) resizerFrame._startResize(rb);
	}
	
	private void _startResize(Rectangle rb) {
		this.resizeRect = rb;
		prevRect = null;
		setVisible(true);
	}

	private void _refreshResize(Rectangle rb) {
		if(prevRect != null) {
			Point tl = new Point(prevRect.x, prevRect.y);
			SwingUtilities.convertPointFromScreen(tl, this);
			repaint(tl.x, tl.y, prevRect.width+1, prevRect.height+1);
		}
		prevRect = resizeRect;
		this.resizeRect = rb;
		repaint();
	}
	
	private ResizerFrame() throws AWTException {
		getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
		dim = Toolkit.getDefaultToolkit ().getScreenSize ();
		setBounds(0, 0, dim.width, dim.height);
		setUndecorated(true);
		setAlwaysOnTop(true);
		AWTUtilitiesWrapper.setWindowOpaque(this, false);
		setBackground(new Color(255,255,255,0));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		if(resizeRect != null) {
			Point tl = new Point(resizeRect.x, resizeRect.y);
			SwingUtilities.convertPointFromScreen(tl, this);
			g2.setColor(Color.GREEN);
			g2.drawRect(tl.x, tl.y, resizeRect.width, resizeRect.height);
		}
	}

	public static void finishResize() {
		if(resizerFrame != null) resizerFrame.setVisible(false);
	}

	public static Rectangle getCurrentSize() {
		if(resizerFrame != null) return resizerFrame.resizeRect;
		return null;
	}
}
