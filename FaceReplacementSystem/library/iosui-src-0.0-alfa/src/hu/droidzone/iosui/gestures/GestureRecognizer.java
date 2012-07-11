package hu.droidzone.iosui.gestures;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.ResizerFrame;

public class GestureRecognizer implements AWTEventListener {
	private class GestureEvent {
		long time;
		int x,y;

	}
	private Component root;
	private GestureListener listener;
	private GestureEvent firstEvent;
	private boolean dragged = false;
	private GestureEvent lastEvent;
	private double lastSpeed;
	public GestureRecognizer(Component root, GestureListener listener) {
		this.root = root;
		this.listener = listener;
		Toolkit.getDefaultToolkit().addAWTEventListener(this,AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK);
	}

	@Override
	public void eventDispatched(AWTEvent e) {
		boolean isOur = false;
		if(ResizerFrame.isResizing()) return;
		if(!root.isShowing()) return;
		JRootPane ourRoot = SwingUtilities.getRootPane(root);
		JRootPane srcRoot = SwingUtilities.getRootPane((Component) e.getSource());
		if(ourRoot != srcRoot) return;
		if(e instanceof MouseEvent) {
			Point tl = root.getLocationOnScreen();
			Rectangle rr = new Rectangle(root.getSize());
			rr.setLocation(tl);
			PointerInfo pi = MouseInfo.getPointerInfo();
			isOur = rr.contains(pi.getLocation()); 
		}
		
//		if(e.getSource() == root || SwingUtilities.isDescendingFrom((Component) e.getSource(), root) || dragged) {
		if(isOur || dragged) {
			if(e instanceof MouseEvent) {
				MouseEvent me = (MouseEvent) e;
				if(e.getSource() instanceof IOSUIComponent) {
					IOSUIComponent iosComponent = (IOSUIComponent) e.getSource();
					if(iosComponent.isEventConsumer(me)) {
						return;
					}
				}
			}
			if(e instanceof MouseWheelEvent) {
				MouseWheelEvent mwe = (MouseWheelEvent) e;
				if(listener != null) {
					listener.wheelRotation(mwe.getWheelRotation());
				}
			} else if(e instanceof MouseEvent) {
				MouseEvent me = (MouseEvent) e;
				switch(me.getID()) {
				case MouseEvent.MOUSE_PRESSED:
					if(isButton1Pressed(me)) {
						GestureEvent ge = new GestureEvent();
						ge.x = me.getXOnScreen();
						ge.y = me.getYOnScreen();
						ge.time = me.getWhen();
						firstEvent = ge;
						lastEvent = ge;
					}
					break;
				case MouseEvent.MOUSE_RELEASED:
					if(dragged) {
						if(isButton1Released(me)) {
							GestureEvent ge = new GestureEvent();
							ge.x = me.getXOnScreen();
							ge.y = me.getYOnScreen();
							ge.time = me.getWhen();
							handleDragFinished(ge);
							lastEvent = null;
							firstEvent = null;
						}
					}
					dragged = false;
					break;
				case MouseEvent.MOUSE_CLICKED:
					if(isButton1Pressed(me)) System.out.println("CLICKED");
					break;
				case MouseEvent.MOUSE_ENTERED:
					break;
				case MouseEvent.MOUSE_EXITED:
					break;
				case MouseEvent.MOUSE_MOVED:
					if(isButton1Pressed(me)) System.out.println("MOVED");
					break;
				case MouseEvent.MOUSE_DRAGGED:
					if(isButton1Pressed(me)) {
						dragged = true;
						GestureEvent ge = new GestureEvent();
						ge.x = me.getXOnScreen();
						ge.y = me.getYOnScreen();
						ge.time = me.getWhen();
						handleDrag(ge);
						lastEvent = ge;
					}
					break;
				case MouseEvent.MOUSE_WHEEL:
					System.out.println(me.paramString());
					if(listener != null) {
					}
					break;
				default:
					System.out.println(me.paramString());
				}
			}
		}
	}

	private void handleDrag(GestureEvent ge) {
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
		if(firstEvent == null) return; 
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
//	private void handleDragFinished(GestureEvent ge) {
//		if(lastEvent == null) return;
//		int dx = ge.x - firstEvent.x;
//		int dy = ge.y - firstEvent.y;
//		long dt = ge.time - lastEvent.time;
//		if(dt < 20) {
//			if(listener != null) {
//				listener.swipeRecognized(dx,dy,dt,lastSpeed);
//			}
//		}
//	}

	private boolean isButton1Pressed(MouseEvent me) {
		return (me.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;//== MouseEvent.BUTTON1; 
	}
	private boolean isButton1Released(MouseEvent me) {
		return ((me.getModifiers() & MouseEvent.MOUSE_RELEASED) != 0) && me.getButton() == MouseEvent.BUTTON1;//== MouseEvent.BUTTON1; 
	}

}
