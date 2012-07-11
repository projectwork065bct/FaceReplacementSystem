package hu.droidzone.iosui.popup;

import hu.droidzone.iosui.IOSUIApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.IOSUIHeader;
import hu.droidzone.iosui.IOSUISectionalButton;
import hu.droidzone.iosui.IOSUISwitch;
import hu.droidzone.iosui.i18n.IOSI18N;
import hu.droidzone.iosui.shapes.BubleRectangleShape;
import hu.droidzone.iosui.shapes.MarkerDirection;
import hu.droidzone.iosui.utils.AWTUtilitiesWrapper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;


public class IOSUIPopupWindow extends JDialog {
	private static final long serialVersionUID = 2654484548533392960L;
	private IOSUIPopupPanel pan;

	public IOSUIPopupWindow(IOSUIComponent content, int w, int h, String title) {
		this(null, content, w, h, title);
	}
	
	public IOSUIPopupWindow(IOSUIComponent owner, IOSUIComponent content, int _w, int _h, String title) {
		super(IOSUIApplication.app);
		MarkerDirection md = MarkerDirection.NONE;
		double markerLocation = BubleRectangleShape.CENTER;
		Dimension rd = IOSUIPopupPanel.calculateBoundsFor(_w, _h, IOSUIPopupPanel.DEFAULT_TITLE_HEIGHT, 0, md);
		int w = rd.width;
		int h = rd.height;
		Point sl = null;
		if(owner != null) {
			Dimension ss = getToolkit().getScreenSize();
			Rectangle sr = new Rectangle(ss);
			Point tl = new Point(0,0);
			Point br = new Point(owner.getWidth(), owner.getHeight());
			SwingUtilities.convertPointToScreen(tl, owner);
			SwingUtilities.convertPointToScreen(br, owner);
			Rectangle or = new Rectangle(tl.x, tl.y, br.x - tl.x, br.y - tl.y);
			int w2 = or.width / 2;
			int h2 = or.height / 2;
			int contentW2 = w / 2;
			int contentH2 = h / 2;
			Point p;
			Rectangle r;
			//1
			p = new Point(tl.x+w2, br.y);
			r = new Rectangle(w, h);
			r.setLocation(p.x - contentW2, p.y);
			if(sr.contains(r)) {
				sl = new Point(r.x, r.y);
				md = MarkerDirection.TOP;
			} else {
				//2
				p = new Point(tl.x+w2, tl.y);
				r.setLocation(p.x - contentW2, p.y - h);
				if(sr.contains(r)) {
					sl = new Point(r.x, r.y);
					md = MarkerDirection.BOTTOM;
				} else {
					//3
					p = new Point(br.x, tl.y+h2);
					r.setLocation(p.x, p.y - contentH2);
					if(sr.contains(r)) {
						sl = new Point(r.x, r.y);
						md = MarkerDirection.LEFT;
					} else {
						//4
						p = new Point(tl.x, tl.y+h2);
						r.setLocation(p.x - w, p.y - contentH2);
						if(sr.contains(r)) {
							sl = new Point(r.x, r.y);
							md = MarkerDirection.RIGHT;
						} else {
							Rectangle r1 = new Rectangle(0,0,ss.width/2, ss.height/2);
							Rectangle r2 = new Rectangle(ss.width/2,0,ss.width/2, ss.height/2);
							Rectangle r3 = new Rectangle(0,ss.height/2,ss.width/2, ss.height/2);
							Rectangle r4 = new Rectangle(ss.width/2,ss.height/2,ss.width/2, ss.height/2);
							if(r1.contains(or)) {
								sl = new Point(br.x,tl.y);
								md = MarkerDirection.LEFT;
								markerLocation = 20;
							} else if(r2.contains(or)) {
								sl = new Point(tl.x - w, tl.y);
								md = MarkerDirection.RIGHT;
								markerLocation = 20;
							} else if(r3.contains(or)) {
								sl = new Point(br.x,br.y-h);
								md = MarkerDirection.LEFT;
								markerLocation = h - 20;
							} else if(r4.contains(or)) {
								sl = new Point(tl.x - w, br.y-h);
								md = MarkerDirection.RIGHT;
								markerLocation = h - 20;
							}
						}
					}
				}
			}
		}
		IOSUIButton btn = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.done")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				IOSUIPopupWindow.this.setVisible(false);
			}
		});
		rd = IOSUIPopupPanel.calculateBoundsFor(_w, _h, IOSUIPopupPanel.DEFAULT_TITLE_HEIGHT, 0, md);
		w = rd.width;
		h = rd.height;

		IOSUIHeader hdr = IOSUIHeader.createApplicationHeader(0, 1, title);
		hdr.addXY(btn, 2, 1, "c,c");
		
		pan = new IOSUIPopupPanel(md, true);
		pan.setBackground(Color.white);
		pan.setFooterHeight(0);
		pan.setMarkerLocation(markerLocation);
		setModal(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(w, h));
		setMaximumSize(new Dimension(w, h));
		setSize(new Dimension(w, h));
		pan.setSize(new Dimension(w, h));
		hdr.setBounds(pan.getTitleBounds());
		pan.add(hdr);
		
		if(content != null) {
			content.setBounds(pan.getContentBounds());
			pan.add(content);
		}
		Rectangle cb = pan.getContentBounds();
//		System.out.println("Content bounds"+cb);
//		System.out.println("Calcualted Size For "+IOSUIPopupPanel.calculateBoundsFor(cb.width, cb.height, pan.getTitleBounds().height, 0, MarkerDirection.TOP));
		setUndecorated(true);
		getContentPane().add(pan);
		AWTUtilitiesWrapper.setWindowOpaque(this, false);
		AWTUtilitiesWrapper.setWindowOpacity(this, 1.0f);
		getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
		if(sl != null) setLocation(sl);
		else setLocationRelativeTo(owner);
	}
	
	public static void main(String[] args) {
		final IOSUIPopupWindow pw = new IOSUIPopupWindow(null, null, 400, 300, "");
		pw.setLocationRelativeTo(null);
		JButton btn = new JButton(new AbstractAction("Close") {
			@Override
			public void actionPerformed(ActionEvent e) {
				pw.setVisible(false);
			}
		});
		Rectangle rc = pw.pan.getContentBounds();
		btn.setBounds(rc.x + rc.width - 80, rc.y + rc.height - 30, 75, 25);
		pw.pan.add(btn);
		
		IOSUISwitch sw1 = new IOSUISwitch();
		sw1.setBounds(rc.x + rc.width - 190, rc.y + rc.height - 30, 75, 25);
		sw1.setOn(false);
		pw.pan.add(sw1);

		
		Rectangle rt = pw.pan.getTitleBounds();

		IOSUISectionalButton sb = new IOSUISectionalButton(
				new AbstractAction("Section 1") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("A1");
					}
				}, 
//				new AbstractAction("Section 2") {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						System.out.println("A2");
//					}
//				}, 
				new AbstractAction("Click!") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("A3");
					}
				} 
		);
		
		IOSUISwitch sw = new IOSUISwitch();
		sw.setBounds(rt);
		pw.pan.add(sw);
		
		rt.x = sw.getX() + sw.getWidth() + 5;
		rt.width = 170;
		rt.height = 28;//sw.getHeight();
		sb.setBounds(rt);
		pw.pan.add(sb);
		
		IOSUIButton ib = new IOSUIButton("IOSUIButton");
		ib.setBackground(Color.BLACK);
		rt.x += rt.width + 5;
		rt.width = 125;
		ib.setBounds(rt);
		pw.pan.add(ib);
		
		pw.setModal(true);
		pw.setVisible(true);
		System.exit(0);
	}
}
