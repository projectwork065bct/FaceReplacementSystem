package hu.droidzone.iosui;

import hu.droidzone.iosui.i18n.IOSI18N;
import hu.droidzone.iosui.list.IOSUIListView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.ease.Sine;

public class IOSUIPagesControl extends IOSUIComponent {
	private static final long serialVersionUID = 3699181835727238427L;
	private Stack<IOSUIPage> pages;
	private IOSUIPage current;
	private IOSUIComponent comeIn;
	private IOSUIComponent goOut;
	private IOSUIPage pageOut;
	
	private int animationPosition;
	private long animationTime = 750;
	
	private IOSUIHeader _header;
	
	private IOSUILabel title;
	private IOSUIComponent back;

	private IOSUILabel inTitle;
	private IOSUIComponent inBack;
	private IOSUILabel outTitle;
	private IOSUIComponent outBack;
	
	//incoming's parameters
	private int inTitlePos;
	private int inBackPos;
	
	private int inTitleAlfa;
	private int inBackAlfa;

	//outgoing;s parameters
	private int outTitlePos;
	private int outBackPos;
	
	private int outTitleAlfa;
	private int outBackAlfa;
	
	public static final int HEADER_HEIGHT = 45;
	
	public int getAnimationPosition() {
		return animationPosition;
	}

	public void setAnimationPosition(int animationPosition) {
		this.animationPosition = animationPosition;
		Rectangle rIn = comeIn.getBounds();
		Rectangle rOut = goOut.getBounds();
		if(animationPosition < 0) { //in
			rIn.x = getWidth() + animationPosition;//+= animationPosition;
		} else {//out
			rIn.x = animationPosition - getWidth();//+= animationPosition;
		}
		rOut.x = animationPosition;// += animationPosition;
		comeIn.setBounds(rIn);
		goOut.setBounds(rOut);
	}

	
	public int getInTitlePos() {
		return inTitlePos;
	}

	public int getInBackPos() {
		return inBackPos;
	}
	
	public int getInTitleAlfa() {
		return inTitleAlfa;
	}
	
	public int getInBackAlfa() {
		return inBackAlfa;
	}
	
	public int getOutTitlePos() {
		return outTitlePos;
	}
	
	public int getOutBackPos() {
		return outBackPos;
	}
	
	public int getOutTitleAlfa() {
		return outTitleAlfa;
	}
	
	public int getOutBackAlfa() {
		return outBackAlfa;
	}
	
	public void setInTitlePos(int iTitlePos) {
		this.inTitlePos = iTitlePos;
		if(inTitle != null) {
			Rectangle r = inTitle.getBounds();
			r.x = iTitlePos;
			inTitle.setBounds(r);
		}
	}

	public void setInBackPos(int iBackPos) {
		this.inBackPos = iBackPos;
		if(inBack != null) {
//			System.out.println("IOSUIPagesControl.setInBackPos("+iBackPos+")");
			Rectangle r = inBack.getBounds();
			r.x = iBackPos;
			inBack.setBounds(r);
		}
	}

	public void setInTitleAlfa(int iTitleAlfa) {
		this.inTitleAlfa = iTitleAlfa;
		if(inTitle != null) {
			inTitle.setAlpha(iTitleAlfa);
		}
	}

	public void setInBackAlfa(int iBackAlfa) {
		this.inBackAlfa = iBackAlfa;
		if(inBack != null) {
//			System.out.println("IOSUIPagesControl.setInBackAlfa("+iBackAlfa+")");
			inBack.setAlpha(iBackAlfa);
		}
	}

	public void setOutTitlePos(int oTitlePos) {
		this.outTitlePos = oTitlePos;
		if(outTitle != null) {
			Rectangle r = outTitle.getBounds();
			r.x = oTitlePos;
			outTitle.setBounds(r);
		}
	}

	public void setOutBackPos(int oBackPos) {
		this.outBackPos = oBackPos;
		if(outBack != null) {
			Rectangle r = outBack.getBounds();
			r.x = oBackPos;
			outBack.setBounds(r);
		}
	}

	public void setOutTitleAlfa(int oTitleAlfa) {
		this.outTitleAlfa = oTitleAlfa;
		if(outTitle != null) {
			outTitle.setAlpha(oTitleAlfa);
		}
	}

	public void setOutBackAlfa(int oBackAlfa) {
		this.outBackAlfa = oBackAlfa;
		if(outBack != null) {
			outBack.setAlpha(oBackAlfa);
		}
	}

	private void layoutHeader() {
		if(ownHeader) _header.setBounds(0,0,getWidth(),HEADER_HEIGHT);
		int tw = title.getPreferredSize().width;
		int th = title.getPreferredSize().height;
		title.setBounds((getWidth()-tw)/2, (HEADER_HEIGHT-th)/2, tw, th);
		if(back != null) {
			int bw = back.getPreferredSize().width;
			int bh = back.getPreferredSize().height;
			back.setBounds(5,(HEADER_HEIGHT-bh)/2, bw,bh);
		}
		_header.repaint();
	}
	
	private Font pageTilteFont = new Font("Verdana", Font.BOLD, 18);
	private boolean ownHeader;
	
	private IOSUILabel createTitleLabel(String title, int initialAlfa) {
		IOSUILabel ret = new IOSUILabel();
		ret.setFont(pageTilteFont);
		ret.setTitle(title);
		if(!ownHeader) ret.setForeground(Color.WHITE);
		int tw = ret.getPreferredSize().width;
		int th = ret.getPreferredSize().height;
		ret.setBounds((getWidth()-tw)/2, (HEADER_HEIGHT-th)/2, tw, th);
		ret.setAlpha(initialAlfa);
		_header.add(ret);
		return ret;
	}
	
	private static final int IW = 23;
	private static final int IH = 19;

	private IOSUIComponent createBackButton(IOSUIPage p) {
		if(p.isImageBack()) {
			IOSUIImageButton ret = new IOSUIImageButton(getClass().getResource("/resources/back_arrow.png"));
			ret.setAction(new AbstractAction(IOSI18N.get("pages.back")) {
				@Override
				public void actionPerformed(ActionEvent e) {
					goBack();
				}
			});
			int w = IW+10;
			int h = ret.getPreferredSize().height;
			ret.setBounds(5,(HEADER_HEIGHT-h)/2, w,h);
			ret.setAlpha(0);
			_header.add(ret);
			return ret;
		} else {
			return createBackButton(p.getPageTitle());
		}
		
	}
	private IOSUIBackArrowButton createBackButton(String title) {
		IOSUIBackArrowButton ret = new IOSUIBackArrowButton(new AbstractAction(IOSI18N.get("pages.back")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		ret.setTitle(title);
		int w = ret.getPreferredSize().width;
		int h = ret.getPreferredSize().height;
		ret.setBounds(5,(HEADER_HEIGHT-h)/2, w,h);
//		ret.setBackground(Color.DARK_GRAY);
		ret.setAlpha(0);
		_header.add(ret);
		return ret;
	}
	
	public IOSUIPagesControl(IOSUIPage first, IOSUIHeader externalHeader) {
		if(externalHeader == null) {
			this._header = IOSUIHeader.createPagesViewHeader();//IOSUIView.createUninitalized();
			ownHeader = true;
		} else {
			this._header = externalHeader;
			ownHeader = false;
		}
		this.pages = new Stack<IOSUIPage>();
		this.title = createTitleLabel(first.getPageTitle(),255);
		layoutHeader();
		add(_header);
		navigateTo(first);
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(current != null) {
					if(ownHeader) current.getPageContent().setBounds(0,HEADER_HEIGHT, getWidth(), getHeight()-HEADER_HEIGHT);
					else current.getPageContent().setBounds(0,0, getWidth(), getHeight());
				}
				layoutHeader();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
	private boolean animating = false;
	
	public void navigateTo(IOSUIPage newPage) {
		if(animating) return;
		if(current != null) pages.push(current);
		newPage.setShell(this);
		if(current == null) {
			if(ownHeader) newPage.getPageContent().setBounds(0,HEADER_HEIGHT, getWidth(), getHeight()-HEADER_HEIGHT);
			else newPage.getPageContent().setBounds(0,0, getWidth(), getHeight());
			add(newPage.getPageContent());
			if(back != null) back.setVisible(false);
			current = newPage;
		} else {
			animateIn(newPage);
			current = newPage;
		}
	}

	public void goBack() {
		if(animating) return;
		if(pages.size() < 1) return;
		IOSUIPage oldPage = pages.pop();
		IOSUIPage nextBackPage = null;
		if(pages.size() >  0) {
			nextBackPage = pages.peek();
		}
		animateOut(oldPage,nextBackPage);
		current = oldPage;
	}
	
	private void fireAnimation(Integer from, Integer to, 
			Integer outTitleFrom, Integer outTitleTo, 
			Integer outBackFrom, Integer outBackTo, 
			Integer inTitleFrom, Integer inTitleTo,
			Integer inBackFrom, Integer inBackTo
			) {
		animating = true;
		Timeline tl = new Timeline(this);
		tl.setDuration(animationTime);
		tl.setEase(new Sine());
		Integer outAlfaFrom = new Integer(255);
		Integer outAlfaTo = new Integer(0);
		Integer inAlfaFrom = new Integer(0);
		Integer inAlfaTo = new Integer(255);
		
		tl.addPropertyToInterpolate("animationPosition",from, to);
		tl.addPropertyToInterpolate("outTitleAlfa",outAlfaFrom, outAlfaTo);
		tl.addPropertyToInterpolate("outBackAlfa",outAlfaFrom, outAlfaTo);
		tl.addPropertyToInterpolate("inTitleAlfa",inAlfaFrom, inAlfaTo);
		tl.addPropertyToInterpolate("inBackAlfa",inAlfaFrom, inAlfaTo);
		
		tl.addPropertyToInterpolate("inTitlePos",inTitleFrom, inTitleTo);
		if(inBackFrom != null && inBackTo != null) tl.addPropertyToInterpolate("inBackPos",inBackFrom, inBackTo);
		
		tl.addPropertyToInterpolate("outTitlePos",outTitleFrom, outTitleTo);
		if(outBackFrom != null && outBackTo != null) tl.addPropertyToInterpolate("outBackPos",outBackFrom, outBackTo);
		
		tl.addCallback(new TimelineCallback() {
			@Override
			public void onTimelineStateChanged(TimelineState oldState,TimelineState newState, float durationFraction,float timelinePosition) {
				if(newState == TimelineState.DONE) {
					pageOut.pageRemoved();
					remove(goOut);
					animating = false;
					remove(outTitle);
					if(outBack != null) remove(outBack);
					title = inTitle;
					back = inBack;
				}
//				header.repaint();
			}
			
			@Override
			public void onTimelinePulse(float durationFraction, float timelinePosition) {
			}
		});
		tl.play();
	}

	private void animateOut(IOSUIPage oldPage, IOSUIPage nextBackPage) {
		Integer from = new Integer(0);
		Integer to = new Integer(getWidth());
		goOut = current.getPageContent();
		pageOut = current;
		comeIn = oldPage.getPageContent();
		if(ownHeader) comeIn.setBounds(-getWidth(),HEADER_HEIGHT, getWidth(), getHeight()-HEADER_HEIGHT);
		else comeIn.setBounds(-getWidth(),0, getWidth(), getHeight());
		add(comeIn);

		outTitle = title;
		outBack = back;
		
		inTitle = createTitleLabel(oldPage.getPageTitle(),0);
		boolean inb = nextBackPage != null;
		if(inb)	inBack = createBackButton(nextBackPage);
		else inBack = null;
		Integer outTitleFrom = outTitle.getBounds().x;
		Integer outTitleTo = getWidth();
		Integer outBackFrom = outBack.getBounds().x;
		Integer outBackTo = getWidth()/2;//outBack.getBounds().x + outBack.getBounds().width;
		Integer inTitleFrom = -inTitle.getPreferredSize().width;
		Integer inTitleTo = (getWidth() - inTitle.getPreferredSize().width)/2;
		Integer inBackFrom = inBack == null ? null : -outBack.getBounds().width;
		Integer inBackTo = inBack == null ? null : 5;
		
		fireAnimation(from, to, outTitleFrom, outTitleTo, outBackFrom, outBackTo, inTitleFrom, inTitleTo, inBackFrom, inBackTo);
	}

	private void animateIn(IOSUIPage newPage) {
		Integer from = new Integer(0);
		Integer to = new Integer(-getWidth());
		goOut = current.getPageContent();
		pageOut = current;
		comeIn = newPage.getPageContent();
		if(ownHeader) comeIn.setBounds(getWidth(),HEADER_HEIGHT, getWidth(), getHeight()-HEADER_HEIGHT);
		else comeIn.setBounds(getWidth(),0, getWidth(), getHeight());
		add(comeIn);
		
		outTitle = title;
		outBack = back;
		
		inTitle = createTitleLabel(newPage.getPageTitle(),0);
		inBack = createBackButton(current);
		
		
		Integer outTitleFrom = outTitle.getBounds().x;
		Integer outTitleTo = 0;
		Integer outBackFrom = outBack == null ? null : outBack.getBounds().x;
		Integer outBackTo = outBack == null ? null : -outBack.getBounds().width;
		Integer inTitleFrom = getWidth();
		Integer inTitleTo = (getWidth() - inTitle.getPreferredSize().width)/2;
		Integer inBackFrom = getWidth()/2;//inBack.getPreferredSize().width + 5;
		Integer inBackTo = 5;
		
		fireAnimation(from, to, outTitleFrom, outTitleTo, outBackFrom, outBackTo, inTitleFrom, inTitleTo, inBackFrom, inBackTo);
	}

	@Override
	protected void paintUI(Graphics2D g2) {
	}
}
