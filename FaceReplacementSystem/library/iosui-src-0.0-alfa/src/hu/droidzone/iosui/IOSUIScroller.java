package hu.droidzone.iosui;

import hu.droidzone.iosui.gestures.GestureListener;
import hu.droidzone.iosui.gestures.GestureRecognizer;
import hu.droidzone.iosui.gestures.NonAWTGestureRecognizer;
import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Sine;

public class IOSUIScroller extends IOSUIComponent {
	private static final long serialVersionUID = -1566359017146624262L;
	public static enum ScrollPolicy {CONTIGOUS, PAGED, FIXED};
	
	private ScrollPolicy verticalScrollPolicy = ScrollPolicy.CONTIGOUS; 
	private ScrollPolicy horizontalScrollPolicy = ScrollPolicy.CONTIGOUS;
	
	private IOSUIComponent view;
	
	private int verticalPageSize = 250;
	private int horizontalPageSize = 250;
	
	private class EmptyScrollerView extends IOSUIComponent {
		private final Color lineColor = new Color(234, 242, 251);
		private final Color lightLine = IOSUIHelper.setAlpha(lineColor, 150);
		private Color darkLine = IOSUIHelper.setAlpha(lineColor, 220);
		private static final int W = 2048;
		private static final int H = 2048;
		public EmptyScrollerView() {
			setSize(W, H);
		}
		
		@Override
		protected void paintUI(Graphics2D g2) {
			Rectangle clip = new Rectangle(getWidth(), getHeight());
			g2.setPaint(Color.WHITE);
			g2.fillRect(clip.x,clip.y, clip.width, clip.height);
			
			int x1 = (clip.x / 10) * 10;
			int x2 = ((clip.x + clip.width) / 10) * 10;

			int y1 = (clip.y / 10) * 10;
			int y2 = ((clip.y + clip.height) / 10) * 10;

			int cnt = 2;
			for(int x = x1;x < x2;x += 10) {
				if(cnt == 3) {
					g2.setColor(darkLine);
					cnt = 1;
				} else {
					g2.setColor(lightLine);
					cnt++;
				}
				g2.drawLine(x, y1, x, y2);
			}
			cnt = 1;
			for(int y = y1;y < y2;y += 12) {
				if(cnt == 2) {
					g2.setColor(darkLine);
					cnt = 1;
				} else {
					g2.setColor(lightLine);
					cnt++;
				}
				g2.drawLine(x1, y, x2, y);
			}
			int colCount = getWidth() / horizontalPageSize;
			int rowCount = getHeight() / verticalPageSize;
			g2.setColor(Color.GRAY);
			for(int x = 0;x < colCount;x++) {
				g2.drawLine(x * horizontalPageSize, 0, x * horizontalPageSize, getHeight());
			}
			for(int y = 0;y < rowCount;y++) {
				g2.drawLine(0, y * verticalPageSize, getWidth(), y * verticalPageSize);
			}
			for(int x = 0;x < colCount;x++) {
				for(int y = 0;y < rowCount;y++) {
					g2.drawString((x+1)+"/"+(y+1), (x * horizontalPageSize)+(horizontalPageSize/2), (y * verticalPageSize)+(verticalPageSize/2));
				}
			}
		}
	}
	
	public IOSUIScroller() {
		this(null);
	}
	
	public IOSUIScroller(IOSUIComponent view) {
		this(view, ScrollPolicy.CONTIGOUS, ScrollPolicy.CONTIGOUS, 250, 250);
	}
	
	public IOSUIScroller(IOSUIComponent view,int hps, int vps) {
		this(view, ScrollPolicy.PAGED, ScrollPolicy.PAGED, hps, vps);
	}

	public void adjustScrollPosition() {
//		System.out.println(String.format("Begin Adjust SP %d-%d",_horizontalScrollPosition, _verticalScrollPosition));
		if((view.getWidth() + _horizontalScrollPosition) < getWidth()) {
			_horizontalScrollPosition = (view.getWidth() - getWidth()) * -1; 
		}
		if((view.getHeight() + _verticalScrollPosition) < getHeight()) {
			_verticalScrollPosition = (view.getHeight() - getHeight()) * -1; 
		}
		if(_verticalScrollPosition > 0) _verticalScrollPosition = 0;
		if(_horizontalScrollPosition > 0) _horizontalScrollPosition = 0;
//		System.out.println(String.format("End Adjust SP %d-%d",_horizontalScrollPosition, _verticalScrollPosition));
	}
	
	public IOSUIScroller(IOSUIComponent view, ScrollPolicy hsp, ScrollPolicy vsp, int hps, int vps) {
		this.view = view;
		if(view == null) this.view = new EmptyScrollerView();
		horizontalScrollPolicy = hsp;
		verticalScrollPolicy = vsp;
		horizontalPageSize = hps;
		verticalPageSize = vps;
		initScroller();
	}
	
	public void setView(IOSUIComponent newView) {
		remove(view);
		_verticalScrollPosition = 0;
		_horizontalScrollPosition = 0;
		view = newView;
		view.setBounds(0, 0, view.getWidth(), view.getHeight());
		add(view);
	}
	
	
	public ScrollPolicy getVerticalScrollPolicy() {
		return verticalScrollPolicy;
	}

	public void setVerticalScrollPolicy(ScrollPolicy verticalScrollPolicy) {
		this.verticalScrollPolicy = verticalScrollPolicy;
		adjustLocation();
	}

	public ScrollPolicy getHorizontalScrollPolicy() {
		return horizontalScrollPolicy;
	}

	public void setHorizontalScrollPolicy(ScrollPolicy horizontalScrollPolicy) {
		this.horizontalScrollPolicy = horizontalScrollPolicy;
		adjustLocation();
	}

	public int getVerticalPageSize() {
		return verticalPageSize;
	}

	public void setVerticalPageSize(int verticalPageSize) {
		this.verticalPageSize = verticalPageSize;
		adjustLocation();
	}

	public int getHorizontalPageSize() {
		return horizontalPageSize;
	}

	public void setHorizontalPageSize(int horizontalPageSize) {
		this.horizontalPageSize = horizontalPageSize;
		adjustLocation();
	}

	private int _verticalScrollPosition = 0;
	private int _horizontalScrollPosition = 0;
	private void initScroller() {
		view.setBounds(0, 0, view.getWidth(), view.getHeight());
		add(view);
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				layoutView();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
//		new NonAWTGestureRecognizer(this, new GestureListener() {
		new GestureRecognizer(this, new GestureListener() {
			@Override
			public void wheelRotation(int wheelRotation) {
				int move = wheelRotation;
				int dp = move*200;
				if(dp < 0) dp = Math.max(-500, dp);
				else dp = Math.min(500, dp);
				scrollTo(_verticalScrollPosition-dp,_horizontalScrollPosition, -1);
			}
			
			@Override
			public void swipeRecognized(int dx, int dy, long dt, double speed) {
//				System.out.println(String.format("dx:%d, dy:%d, dt:%d, speed:%f", dx,dy,dt,speed));
//				System.out.println(String.format("dx:%d, dy:%d", (int)(dx * speed * 3),(int)(dy * speed * 3),dt,speed));
//				int newVerticalScrollPosition = (int) (_verticalScrollPosition + (dy * speed * 20)); 
//				int newHorizontalScrollPosition = (int) (_horizontalScrollPosition + (dx * speed * 20));
//				int newVerticalScrollPosition = _verticalScrollPosition + (500 * (dy < 0 ? -1:1));//(int) (_verticalScrollPosition + (dy * speed * 3)); 
//				int newHorizontalScrollPosition = _horizontalScrollPosition + (500 * (dx < 0 ? -1:1));//(int) (_horizontalScrollPosition + (dx * speed * 2)); 
				int newVerticalScrollPosition = (int) (_verticalScrollPosition + (dy * 3)); 
				int newHorizontalScrollPosition = (int) (_horizontalScrollPosition + (dx * 3));
//				System.out.println(String.format("NVSP : %d, NHSP : %d",newVerticalScrollPosition,newHorizontalScrollPosition));
				newVerticalScrollPosition = Math.min(0, newVerticalScrollPosition);
				newHorizontalScrollPosition = Math.min(0, newHorizontalScrollPosition);
				scrollTo(newVerticalScrollPosition, newHorizontalScrollPosition, -1);
			}
			
			@Override
			public void pinchRecognized() {
			}
			
			@Override
			public void mouseClicked(Point point) {
				stopScrolling();
			}
			
			@Override
			public void dragRecognized(int dx, int dy, long dt, int x, int y) {
				stopScrolling();
				if(horizontalScrollPolicy != ScrollPolicy.FIXED) {
					_horizontalScrollPosition += dx;
					_horizontalScrollPosition = Math.min(0, _horizontalScrollPosition);
				}

				if(verticalScrollPolicy != ScrollPolicy.FIXED) {
					_verticalScrollPosition += dy;
					_verticalScrollPosition = Math.min(0, _verticalScrollPosition);
				}
				scrollView();
			}
			
			@Override
			public void dragFinished(int dx, int dy, long dt, int x, int y) {
				adjustLocation();
			}
		});
	}
	
	protected void verticalScrollChanged() {
	}

	protected void horizontalScrollChanged() {
	}

	protected void stopScrolling() {
		if(tlScrollBy != null && scrolling) {
			tlScrollBy.abort();
			scrolling = false;
		}
	}
	
	private boolean scrolling = false;
	private Timeline tlScrollBy = null;
	protected void scrollTo(int newVerticalScrollPosition, int newHorizontalScrollPosition, double speed) {
		stopScrolling();
		if((view.getWidth() + newHorizontalScrollPosition) < getWidth()) {
			newHorizontalScrollPosition = (view.getWidth() - getWidth()) * -1; 
		}
		if((view.getHeight() + newVerticalScrollPosition) < getHeight()) {
			newVerticalScrollPosition = (view.getHeight() - getHeight()) * -1; 
		}
		if(newVerticalScrollPosition > 0) newVerticalScrollPosition = 0;
		
		long duration = 800;
		if(speed > 0) {
			int dy = _verticalScrollPosition - newVerticalScrollPosition;
			int dx = _horizontalScrollPosition - newHorizontalScrollPosition;
			double v = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
			duration = (long) (v / speed);
		}
		tlScrollBy = new Timeline(this);
		tlScrollBy.setDuration(duration);//+(Math.abs(move)*10));
		tlScrollBy.setEase(new Sine());
		scrolling = true;
		Integer fromV = new Integer(_verticalScrollPosition);
		Integer fromH = new Integer(_horizontalScrollPosition);
		Integer toV = new Integer(newVerticalScrollPosition);
		Integer toH = new Integer(newHorizontalScrollPosition);
		
		tlScrollBy.addPropertyToInterpolate("verticalScrollPosition",fromV, toV);
		tlScrollBy.addPropertyToInterpolate("horizontalScrollPosition",fromH, toH);
		tlScrollBy.addCallback(new TimelineCallback() {
			@Override
			public void onTimelineStateChanged(TimelineState oldState,TimelineState newState, float durationFraction,float timelinePosition) {
				if(newState == TimelineState.DONE) {
					scrolling = false;
					adjustLocation();
				}
			}
			
			@Override
			public void onTimelinePulse(float durationFraction, float timelinePosition) {
			}
		});
		tlScrollBy.play();
	}

	
	protected void adjustLocation() {
		if(scrolling) return;

		tlScrollBy = new Timeline(this);
		tlScrollBy.setDuration(400);//+(Math.abs(move)*10));
		tlScrollBy.setEase(new Sine());

		boolean needToAnimate = false;
		if(horizontalScrollPolicy == ScrollPolicy.PAGED) {
			int col = (-_horizontalScrollPosition / horizontalPageSize);
			int colPos = -_horizontalScrollPosition % horizontalPageSize;
			int newHorizontalScrollPosition = _horizontalScrollPosition; 
			if(colPos > horizontalPageSize/2) {
				newHorizontalScrollPosition = (col + 1) * horizontalPageSize;
			} else {
				newHorizontalScrollPosition = (col) * horizontalPageSize;
			}
			Integer fromH = new Integer(_horizontalScrollPosition);
			Integer toH = new Integer(-newHorizontalScrollPosition);
			tlScrollBy.addPropertyToInterpolate("horizontalScrollPosition",fromH, toH);
			needToAnimate = true;
		}
		
		if(verticalScrollPolicy == ScrollPolicy.PAGED) {
			int row = (-_verticalScrollPosition / verticalPageSize);
			int rowPos = -_verticalScrollPosition % verticalPageSize;
			int newVerticalScrollPosition = _verticalScrollPosition; 
			if(rowPos > verticalPageSize/2) {
				newVerticalScrollPosition = (row + 1) * verticalPageSize;
			} else {
				newVerticalScrollPosition = (row) * verticalPageSize;
			}
			Integer fromV = new Integer(_verticalScrollPosition);
			Integer toV = new Integer(-newVerticalScrollPosition);
			tlScrollBy.addPropertyToInterpolate("verticalScrollPosition",fromV, toV);
			needToAnimate = true;
		}
		
		if(needToAnimate) {
			scrolling = true;
			tlScrollBy.addCallback(new TimelineCallback() {
				@Override
				public void onTimelineStateChanged(TimelineState oldState,TimelineState newState, float durationFraction,float timelinePosition) {
					if(newState == TimelineState.DONE) {
						scrolling = false;
					}
				}
				
				@Override
				public void onTimelinePulse(float durationFraction, float timelinePosition) {
				}
			});
			tlScrollBy.play();
		}
	}

	public int getVerticalScrollPosition() {
		return _verticalScrollPosition;
	}

	public void setVerticalScrollPosition(int verticalScrollPosition) {
		if(verticalScrollPolicy == ScrollPolicy.FIXED) return;
		this._verticalScrollPosition = verticalScrollPosition;
		scrollView();
	}

	public int getHorizontalScrollPosition() {
		return _horizontalScrollPosition;
	}

	public void setHorizontalScrollPosition(int horizontalScrollPosition) {
		if(horizontalScrollPolicy == ScrollPolicy.FIXED) return;
		this._horizontalScrollPosition = horizontalScrollPosition;
		scrollView();
	}

	protected void scrollView() {
		layoutView();
	}

	protected void layoutView() {
		if((view.getWidth() + _horizontalScrollPosition) < getWidth()) {
			if(horizontalScrollPolicy != ScrollPolicy.FIXED) _horizontalScrollPosition = (view.getWidth() - getWidth()) * -1; 
		}
		if((view.getHeight() + _verticalScrollPosition) < getHeight()) {
			if(verticalScrollPolicy != ScrollPolicy.FIXED) _verticalScrollPosition = (view.getHeight() - getHeight()) * -1; 
		}
		Point lp = view.getLocation();
		if(lp.x != _horizontalScrollPosition) horizontalScrollChanged();
		if(lp.y != _verticalScrollPosition) verticalScrollChanged();
		if(lp.x != _horizontalScrollPosition || lp.y != _verticalScrollPosition) {
			view.setLocation(_horizontalScrollPosition, _verticalScrollPosition);
//			System.out.println("IOSUIScroller.layoutView()");
		}
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		// TODO Auto-generated method stub

	}

}
