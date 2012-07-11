package hu.droidzone.iosui;

import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class IOSUIProgressPainter implements ActionListener {
	private static final long serialVersionUID = -4246742841816902732L;
	public static enum Size { SMALL, MEDIUM, LARGE }
	private static List<BufferedImage>imgs_small = new ArrayList<BufferedImage>();
	private static List<BufferedImage>imgs_medium = new ArrayList<BufferedImage>();
	private static List<BufferedImage>imgs_large = new ArrayList<BufferedImage>();
	
	private Size size;
	private int max_cnt;
	private int ptr = -1;
	private boolean started = false;
	private IOSUIComponent owner;
	private Timer tmr;
	private int ww;
	private int hh;
	private int iw;
	private int ih;
	
	static {
		loadImages();
	}
	
	private static void loadImages() {
		for(int i = 0;i < 16;i++) {
			String rn = "/resources/gear/gear"+(i+1)+"_small.png";
			URL url = IOSUIProgressPainter.class.getResource(rn);
			try {
				BufferedImage bi = ImageIO.read(url);
				imgs_small.add(bi);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		for(int i = 0;i < 12;i++) {
			String rn = "/resources/gear/gear"+(i+1)+".png";
			URL url = IOSUIProgressPainter.class.getResource(rn);
			try {
				BufferedImage bi = ImageIO.read(url);
				imgs_medium.add(bi);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		for(int i = 0;i < 12;i++) {
			String rn = "/resources/gear/gear"+(i+1)+"_large.png";
			URL url = IOSUIProgressPainter.class.getResource(rn);
			try {
				BufferedImage bi = ImageIO.read(url);
				imgs_large.add(bi);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public IOSUIProgressPainter(Size size, IOSUIComponent owner) {
		this.size = size;
		this.owner = owner;
		switch(size) {
		case SMALL:
			max_cnt = 15;
			ww = 20;
			hh = 20;
			iw = 14;
			ih = 15;
			break;
		case MEDIUM:
			max_cnt = 11;
			ww = 40;
			hh = 40;
			iw = 20;
			ih = 20;
			break;
		case LARGE:
			max_cnt = 11;
			ww = 60;
			hh = 60;
			iw = 37;
			ih = 37;
			break;
		}
	}

	public void start() {
		if(started) return;
		started = true;
		ptr = 0;
		tmr = new Timer(100, this);
		tmr.start();
	}
	
	public void stop() {
		if(!started) return;
		started = false;
		tmr.stop();
	}
	
	public void paint(Graphics2D g2) {
		int w = owner.getWidth();
		int h = owner.getHeight();
		int _ww = ww;
		int _hh = hh;
		boolean fillbg = false;
		if(w < ww) {
			_ww = w;
			_hh = w;
			fillbg = false;
		}
		if(h < hh && h < _ww) {
			_hh = h;
			_ww = h;
			fillbg = false;
		}
		if(fillbg) {
			g2.setColor(IOSUIHelper.setAlpha(Color.GRAY,180));
			g2.fillRect(0, 0, w, h);
		}
		int x = (w - _ww)/2;
		int y = (h - _hh)/2;
		g2.setColor(IOSUIHelper.setAlpha(Color.DARK_GRAY,220));
		g2.fillRoundRect(x, y, _ww, _hh, 10, 10);
		x = (w - iw)/2;
		y = (h - ih)/2;
		BufferedImage bi = null;
		switch(size) {
		case SMALL:
			bi = imgs_small.get(ptr);
			break;
		case MEDIUM:
			bi = imgs_medium.get(ptr);
			break;
		case LARGE:
			bi = imgs_large.get(ptr);
			break;
		}
		if(bi == null) return;
		g2.drawImage(bi, x, y, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ptr++;
		if(ptr > max_cnt) {
			ptr = 0;
		}
		owner.repaint();
	}
}
