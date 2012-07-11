package hu.droidzone.iosui;

import hu.droidzone.iosui.icons.IOSUIIconPainter;
import hu.droidzone.iosui.icons.IconMode;
import hu.droidzone.iosui.icons.IconSize;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.net.URL;

public class IOSUIIcon extends IOSUIComponent {
	private static final long serialVersionUID = 6203690584895853858L;
	private IOSUIIconPainter painter;
	public IOSUIIcon(IconSize is, IconMode ... modes) {
		this(IOSUIIcon.class.getResource("/resources/empty.png"),is,modes);
	}
	public IOSUIIcon(URL url, IconSize is, IconMode ... modes) {
		int wh = 16;
		switch (is) {
		case S:
			wh = 16;
			break;
		case M:
			wh = 24;
			break;
		case L:
			wh = 32;
			break;
		case XL:
			wh = 48;
			break;
		case XXL:
			wh = 64;
			break;
		case XXXL:
			wh = 128;
			break;
		}
		setPreferredSize(new Dimension(wh+1,wh+1));
		painter = new IOSUIIconPainter(this, 0, 0, wh, wh, url, modes);
	}
	
	@Override
	protected void paintUI(Graphics2D g2) {
		painter.paint(g2);
//		g2.setColor(Color.RED);
//		g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}

}
