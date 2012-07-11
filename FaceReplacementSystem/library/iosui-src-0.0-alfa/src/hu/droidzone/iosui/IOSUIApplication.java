package hu.droidzone.iosui;

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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class IOSUIApplication extends JFrame {
	private static final long serialVersionUID = -6177942526972350258L;
	private IOSUIPopupPanel pan;
	private IOSUIView content;
	private IOSUIView header;

	public static IOSUIApplication app;
	
	public static void startIOSUIApplication(int w, int h, IOSUIView cnt, IOSUIHeader hdr) {
		initIOSUI();
		app = new IOSUIApplication(w, h, cnt, hdr);
	}
	
	public static void startIOSUIApplication(IOSUIView cnt) {
		initIOSUI();
		app = new IOSUIApplication(cnt);
	}
	
	public static void startIOSUIApplication(IOSUIView cnt, IOSUIView hdr) {
		initIOSUI();
		app = new IOSUIApplication(cnt, hdr);
	}
	
	private IOSUIApplication(IOSUIView cnt) {
		this(cnt, null);
	}
	
	private IOSUIApplication(IOSUIView cnt, IOSUIView hdr) {
		this(800,600,cnt, hdr);
	}
	
	private IOSUIApplication(int w, int h, IOSUIView cnt, IOSUIView hdr) {
		getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(w, h));
		setUndecorated(true);
		setLocationRelativeTo(null);
		AWTUtilitiesWrapper.setWindowOpaque(this, false);
		AWTUtilitiesWrapper.setWindowOpacity(this, 1.0f);
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				if(content != null) content.applicationOpened();
				if(header != null) header.applicationOpened();
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
//				AWTUtilitiesWrapper.setWindowOpacity(IOSUIApplication.this, 0.8f);
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
//				AWTUtilitiesWrapper.setWindowOpacity(IOSUIApplication.this, 1.0f);
			}
		});
		this.content = cnt;
		this.header = hdr;
		pan = new IOSUIPopupPanel(MarkerDirection.NONE);
		if(header != null) pan.setTitleHeight(header.getPreferredSize().getHeight()); 
		else pan.setTitleHeight(0);
//		System.out.println("PAN TH "+pan.getTitleHeight());
		pan.setFooterHeight(0);
		pan.setBackground(content.getBackground());
		pan.add(content);
		if(header != null) pan.add(header);
		getContentPane().add(pan);
		pan.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
				repaint();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
//				System.out.println("Content bounds "+pan.getContentBounds());
				content.setBounds(pan.getContentBounds());
				if(header != null) header.setBounds(pan.getTitleBounds());
//				System.out.println("Title bounds "+pan.getTitleBounds());
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

		setVisible(true);
	}

	private boolean resizing = false;
	private boolean moving = false;
	
	public static void initIOSUI() {
		System.setProperty("apple.awt.graphics.UseQuartz", "true");
		System.setProperty("apple.awt.antialiasing","true");
		System.setProperty("apple.awt.rendering","VALUE_RENDER_QUALITY");
		
		System.setProperty("apple.laf.useScreenMenuBar","false");
		System.setProperty("apple.awt.draggableWindowBackground","false");
		System.setProperty("apple.awt.showGrowBox","false");
		
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("sun.awt.erasebackgroundonresize", "true");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, IOSI18N.get("init.laf"), IOSI18N.get("msg.error"), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}
		if(
				!AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSLUCENT)
//				|| !AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT)
				|| !AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.TRANSLUCENT)
				) {
			JOptionPane.showMessageDialog(null, IOSI18N.get("init.unsupported.os"), IOSI18N.get("msg.error"), JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
