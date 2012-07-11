package hu.droidzone.iosui.popup;

import hu.droidzone.iosui.IOSUIApplication;
import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.IOSUIHeader;
import hu.droidzone.iosui.IOSUIView;
import hu.droidzone.iosui.ResizerFrame;
import hu.droidzone.iosui.Utils;
import hu.droidzone.iosui.gestures.GestureAdapter;
import hu.droidzone.iosui.gestures.GestureListener;
import hu.droidzone.iosui.gestures.NonAWTGestureRecognizer;
import hu.droidzone.iosui.i18n.IOSI18N;
import hu.droidzone.iosui.list.IOSUIListView;
import hu.droidzone.iosui.popup.IOSUIPopupPanel;
import hu.droidzone.iosui.shapes.MarkerDirection;
import hu.droidzone.iosui.utils.AWTUtilitiesWrapper;
import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class IOSUIDialog extends JDialog {
	private static final long serialVersionUID = -6177942526972350258L;
	private IOSUIPopupPanel pan;
	private IOSUIComponent content;
	private IOSUIView header;
	private IOSUIView footer;
	
	public IOSUIDialog(IOSUIView cnt) {
		this(cnt, null);
	}
	
	public IOSUIDialog(IOSUIComponent cnt, IOSUIHeader hdr) {
		this(800,600,cnt, hdr,null);
	}
	
	public IOSUIDialog(int w, int h, IOSUIComponent cnt, IOSUIView hdr,IOSUIView ftr) {
		super(IOSUIApplication.app);
		getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(w, h));
		setUndecorated(true);
		setLocationRelativeTo(IOSUIApplication.app);
		setModal(true);
		AWTUtilitiesWrapper.setWindowOpaque(this, false);
		this.content = cnt;
		this.header = hdr;
		this.footer = ftr;
		
		pan = new IOSUIPopupPanel(MarkerDirection.NONE,true);
		if(header != null) pan.setTitleHeight(header.getPreferredSize().getHeight()); 
		else pan.setTitleHeight(0);
		if(footer != null) pan.setFooterHeight(footer.getPreferredSize().getHeight()); 
		else pan.setFooterHeight(0);
		
		if(content != null) {
			pan.setBackground(content.getBackground());
			pan.add(content);
		}
		if(header != null) pan.add(header);
		if(footer != null) pan.add(footer);
		getContentPane().add(pan);
		pan.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
				repaint();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(content != null) content.setBounds(pan.getContentBounds());
				if(header != null) header.setBounds(pan.getTitleBounds());
				if(footer != null) footer.setBounds(pan.getFooterBounds());
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		new NonAWTGestureRecognizer(this, new GestureAdapter() {
			@Override
			public void dragRecognized(int dx, int dy, long dt, int x, int y) {
				if((y < 40 && !resizing) || moving) {
					Point lp = getLocation();
					lp.x += dx;
					lp.y += dy;
					moving = true;
					setLocation(lp);
				} else {
					Dimension d = getSize();
					if(((x > d.width - 10) && (y > d.height - 10)) || resizing) {
						d.width += dx;
						d.height += dy;
						if(Utils.isOSX() || Utils.isLinux()) {
							if(resizing) {
								Rectangle rb = ResizerFrame.getCurrentSize(); 
								rb.width += dx;
								rb.height += dy;
								ResizerFrame.refreshResize(rb);
							} else {
								Rectangle rb = getBounds();
								rb.width = d.width;
								rb.height = d.height;
								ResizerFrame.startResize(rb);
								resizing = true;
							}
						} else {
							setSize(d);
						}
					}
				}
			}

			@Override
			public void dragFinished(int dx, int dy, long dt, int x, int y) {
				if(resizing) {
					Dimension d = getSize();
					d.width += dx;
					d.height += dy;
					Rectangle rb = getBounds();
					rb.width = d.width;
					rb.height = d.height;
					setBounds(rb);
					ResizerFrame.finishResize();
				}
				resizing = false;
				moving = false;
			}
		});
	}

	private boolean resizing = false;
	private boolean moving = false;
	
	public static void main(String[] args) {
		new IOSUIDialog(null);
	}
}
