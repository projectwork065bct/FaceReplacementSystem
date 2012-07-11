package hu.droidzone.iosui.list;

import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.gestures.GestureAdapter;
import hu.droidzone.iosui.gestures.GestureListener;
import hu.droidzone.iosui.gestures.GestureRecognizer;
import hu.droidzone.iosui.gestures.NonAWTGestureRecognizer;
import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Sine;
import org.pushingpixels.trident.ease.Spline;

public class IOSUIListView extends IOSUIComponent {
	private static final long serialVersionUID = 6033872576221859762L;
	private int scrollPosition = 0;
	
	@Override
	public void invalidate() {
	}
	
	@Override
	public void doLayout() {
	}
	
	public static class RowMapItem {
		int group;
		int row;
		int rowBegin;
		int rowEnd;
		public boolean groupHeader = false;
		public boolean groupFooter = false;
		public boolean selected = false;
		WeakReference<IOSUIListViewRow>refView;
	}
	
	List<RowMapItem>rowMap = new ArrayList<IOSUIListView.RowMapItem>();

	private IOSUIListViewModel model;
	
	public int getScrollPosition() {
		return scrollPosition;
	}

	public void setScrollPosition(int scrollPosition) {
		if(scrollPosition < 0) scrollPosition = 0;
		if(getHeight() > rowMap.get(rowMap.size()-1).rowEnd) {
			scrollPosition = 0;
		} else {
			if(rowMap.size() > 0) if(scrollPosition > rowMap.get(rowMap.size()-1).rowEnd - getHeight()) scrollPosition = rowMap.get(rowMap.size()-1).rowEnd - getHeight();
			if(this.scrollPosition == scrollPosition) return;
		}
		if(this.scrollPosition == scrollPosition) return;
		this.scrollPosition = scrollPosition;
		placeComponent();
	}

	public IOSUIListView(IOSUIListViewModel model) {
		setModel(model);
		setBackground(new Color(219,222,227));
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(getHeight() > rowMap.get(rowMap.size()-1).rowEnd) scrollPosition = 0;
				placeComponent();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		new NonAWTGestureRecognizer(this, new GestureAdapter() {
			@Override
			public void wheelRotation(int wheelRotation) {
				int move = wheelRotation;
				int dp = move*200;
				if(dp < 0) dp = Math.max(-500, dp);
				else dp = Math.min(500, dp);
				scrollBy(dp,-1);
			}
			
			@Override
			public void swipeRecognized(int dx, int dy, long dt, double speed) {
				double v = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
//				int amnt = (int) (v*speed*5);
				int amnt = (int) (dy*3*-1);
//				if(dy > 0) amnt *= -1;
				scrollBy(amnt, speed);
			}
			
			@Override
			public void dragRecognized(int dx, int dy, long dt, int x, int y) {
				scrollToBy(dy);
			}
			
			@Override
			public void mouseClicked(Point point) {
				handleClick(point);
			}
		});
	}

	protected void handleClick(Point point) {
		int y = point.y + scrollPosition;
		for(RowMapItem rmi : rowMap) {
			if(rmi.rowBegin < y && rmi.rowEnd > y) {
				selectRow(rmi);
				model.rowClicked(rmi.group, rmi.row);
			}
		}
	}
	
	protected void selectRow(RowMapItem __rmi) {
		for(RowMapItem _rmi : rowMap) {
			if(_rmi.selected) {
				IOSUIListViewRow view = null;
				if(_rmi.refView != null) view = _rmi.refView.get();
				if(view != null) {
					view.setSelected(false);
				}
				_rmi.selected = false;
				break;
			}
		}
		IOSUIListViewRow view = null;
		if(__rmi.refView != null) view = __rmi.refView.get();
		if(view != null) {
			view.setSelected(true);
			__rmi.selected = true;
//			repaint(rmix, y, width, height);
		}
		repaint();
	}

	protected void scrollToBy(int dy) {
		setScrollPosition(getScrollPosition()-dy);
	}

	private void setModel(IOSUIListViewModel model) {
		this.model = model;
		recreateRowMap();
	}

	private void recreateRowMap() {
		for(RowMapItem rmi : rowMap) {
			if(rmi.refView != null) {
				IOSUIComponent view = rmi.refView.get();
				if(view != null) {
					remove(view);
					rmi.refView = null;
				}
			}
		}
		rowMap.clear();
		int lrb = 0;
		switch(model.getListKind()) {
		case GROUPPED:
			for(int g = 0; g < model.getGroupCount();g++) {
				for(int r = 0;r < model.getRowCount(g);r++) {
					RowMapItem rmi = IOSUIListViewRow.measureRow(model, g, r, lrb);
					lrb = rmi.rowEnd;
					rowMap.add(rmi);
				}
			}
			break;
		case SEPARATED:
			for(int g = 0; g < model.getGroupCount();g++) {
				for(int r = 0;r < model.getRowCount(g);r++) {
					RowMapItem rmi = IOSUIListViewRow.measureRow(model, g, r, lrb);
					lrb = rmi.rowEnd;
					rowMap.add(rmi);
				}
			}
			break;
		case SIMPLE:
			for(int r = 0;r<model.getRowCount();r++) {
				RowMapItem rmi = IOSUIListViewRow.measureRow(model, -1, r, lrb);
				lrb = rmi.rowEnd;
				rowMap.add(rmi);
			}
			break;
		}
		setScrollPosition(0);
	}

	public IOSUIListViewModel getModel() {
		return model;
	}

	private void placeComponent() {
		Rectangle r = new Rectangle(getWidth(), 0);
		int begin = scrollPosition;
		int end = begin + getHeight();
		for(RowMapItem rmi : rowMap) {
			if(IOSUIHelper.isOverlap(begin, end, rmi.rowBegin, rmi.rowEnd)) {
				r.y = rmi.rowBegin;
				r.height = rmi.rowEnd - rmi.rowBegin;
				r.y -= scrollPosition;
				r.x = 0;
				r.width = getWidth();
				IOSUIListViewRow view = null;
				if(rmi.refView != null) view = rmi.refView.get();
				if(view != null) {
					view.setBounds(r);
				} else {
					IOSUIComponent mview = null;
					if(rmi.group == -1) mview = model.getRow(rmi.group, rmi.row);
					else mview = model.getRow(rmi.group, rmi.row);
					view = createRowView(mview,rmi);
					if(view != null) {
						rmi.refView = new WeakReference<IOSUIListViewRow>(view);
						view.setBounds(r);
						add(view);
					}
				}
				
			} else {
				IOSUIComponent view = null;
				if(rmi.refView != null) view = rmi.refView.get();
				if(view != null) {
					remove(view);
				}
				rmi.refView = null;
			}
		}
		repaint();
	}

	private IOSUIListViewRow createRowView(IOSUIComponent mview, RowMapItem rmi) {
		return new IOSUIListViewRow(model,mview,rmi, this, rmi.selected);
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	private boolean scrolling = false;
	private Timeline tlScrollBy = null;
	protected void scrollBy(int move, double speed) {
		if(tlScrollBy != null && scrolling) {
			tlScrollBy.abort();
		}
//		tlScrollBy = new Timeline(this);
//		
//		tlScrollBy.setEase(new Sine());
		scrolling = true;
		Integer from = new Integer(getScrollPosition());
		int ito = getScrollPosition()+move;
		if(ito < 0) ito = 0;
		if(rowMap.size() > 0) if(ito > rowMap.get(rowMap.size()-1).rowEnd - getHeight()) ito = rowMap.get(rowMap.size()-1).rowEnd - getHeight();

		long duration = 800;
		if(speed > 0) {
			double v = Math.abs(from - ito);
			duration = (long) (v / speed);
		}
		
		tlScrollBy = new Timeline(this);
		tlScrollBy.setDuration(duration);//+(Math.abs(move)*10));
		
		tlScrollBy.setEase(new Sine());
		
		Integer to = new Integer(ito); 
		
		tlScrollBy.addPropertyToInterpolate("scrollPosition",from, to);
		tlScrollBy.addCallback(new TimelineCallback() {
			@Override
			public void onTimelineStateChanged(TimelineState oldState,TimelineState newState, float durationFraction,float timelinePosition) {
				if(newState == TimelineState.DONE) scrolling = false;
			}
			
			@Override
			public void onTimelinePulse(float durationFraction, float timelinePosition) {
			}
		});
		tlScrollBy.play();
		
	}
}
