package hu.droidzone.iosui.gestures;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import hu.droidzone.iosui.IOSUIComponent;

public class NonAWTGestureRecognizer {
	private class GestureEvent {
		long time;
		int x,y;

	}
	private Component root;
	private GestureListener listener;
	private GestureEvent firstEvent;
	private GestureEvent lastEvent;
	private double lastSpeed;
	public NonAWTGestureRecognizer(Component _root, GestureListener l) {
		this.root = _root;
		this.listener = l;
		root.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent me) {
//				if(me.getButton() == 1) {
					GestureEvent ge = new GestureEvent();
					ge.x = me.getXOnScreen();
					ge.y = me.getYOnScreen();
					ge.time = me.getWhen();
					handleDragFinished(ge);
					lastEvent = null;
					firstEvent = null;
//				}
			}
			
			@Override
			public void mousePressed(MouseEvent me) {
//				if(me.getButton() == 1) {
					GestureEvent ge = new GestureEvent();
					ge.x = me.getXOnScreen();
					ge.y = me.getYOnScreen();
					ge.time = me.getWhen();
					firstEvent = ge;
					lastEvent = ge;
//				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!root.isEnabled()) return;
				if(listener != null) {
					listener.mouseClicked(e.getPoint());
				}
			}
		});
		root.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
			@Override
			public void mouseDragged(MouseEvent me) {
//				if(me.getButton() == 1) {
					GestureEvent ge = new GestureEvent();
					ge.x = me.getXOnScreen();
					ge.y = me.getYOnScreen();
					ge.time = me.getWhen();
					handleDrag(ge);
					lastEvent = ge;
//				}
			}
		});
		root.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent mwe) {
				if(!root.isEnabled()) return;
				if(listener != null) {
					listener.wheelRotation(mwe.getWheelRotation());
				}
			}
		});
	}

	private void handleDrag(GestureEvent ge) {
		if(!root.isEnabled()) return;
		if(lastEvent == null) return;
		int dx = ge.x - lastEvent.x;
		int dy = ge.y - lastEvent.y;
		long dt = ge.time - lastEvent.time;
		double v = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
		lastSpeed = v/dt;
		if(listener != null) {
			Point p = new Point(lastEvent.x, lastEvent.y);
			SwingUtilities.convertPointFromScreen(p, root);
			listener.dragRecognized(dx,dy,dt, p.x, p.y);
		}
	}

	private void handleDragFinished(GestureEvent ge) {
		if(!root.isEnabled()) return;
		if(lastEvent == null) return;
		int dx = ge.x - firstEvent.x;
		int dy = ge.y - firstEvent.y;
		long dt = ge.time - firstEvent.time;
		long ldt = ge.time - lastEvent.time;
		
		double v = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
		double speed = v/dt;
		
		if(listener != null) {
//			if(ldt < 20) {
				listener.swipeRecognized(dx,dy,dt,speed);
//			} 
			Point p = new Point(lastEvent.x, lastEvent.y);
			SwingUtilities.convertPointFromScreen(p, root);
			listener.dragFinished(dx,dy,dt, p.x, p.y);
		}
	}

//	private boolean isButton1Pressed(MouseEvent me) {
//		return (me.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;//== MouseEvent.BUTTON1; 
//	}
//	
//	private boolean isButton1Released(MouseEvent me) {
//		return ((me.getModifiers() & MouseEvent.MOUSE_RELEASED) != 0) && me.getButton() == MouseEvent.BUTTON1;//== MouseEvent.BUTTON1; 
//	}

}
