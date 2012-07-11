package hu.droidzone.iosui.icons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.utils.IOSUIHelper;

public class IOSUIIconPainter {
	private IOSUIComponent owner;
	private int x,y,w,h;
	private BufferedImage img;
	private URL url;
	private IconMode[] modes;
	private Color fillColor;
	private Color borderColor;

	public IOSUIIconPainter(IOSUIComponent owner, int x, int y, int w, int h, URL url, IconMode ... modes) {
		this(owner, x, y, w, h, url, Color.GRAY, null, modes);
	}

	public IOSUIIconPainter(IOSUIComponent owner, int x, int y, int w, int h, URL url, Color borderColor, Color fillColor, IconMode ... modes) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.url = url;
		this.modes = modes;
		this.fillColor = fillColor;
		this.borderColor = borderColor;
		prepareImage();
	}

	private static final Color lightSubShadeColor = new Color(255,255,255,50);
	private static final Color darkSubShadeColor = new Color(255,255,255,80);
	boolean mirrored = false;
	boolean round = false;
	boolean shine = false;

	private void prepareImage() {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(url);
		} catch (IOException e) {
		}
		//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//		GraphicsDevice gs = ge.getDefaultScreenDevice();
		//		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		for(IconMode m : modes) {
			if(m == IconMode.MIRROR) {
				mirrored = true;
			}
			if(m == IconMode.ROUND) {
				round = true;
			}
			if(m == IconMode.SHINE) {
				shine = true;
			}
		}
		if(mirrored) img = new BufferedImage(w, h*2,BufferedImage.TYPE_INT_ARGB);
		else img = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
		if(modes.length == 0) {
			Graphics2D g2 = img.createGraphics();
			drawScaled(g2, bi);
			//			g2.drawImage(bi, 0, 0, w, h, 0, 0, w, h, null);//, bgcolor, observer)
			g2.dispose();
		} else {
			BufferedImage mask = createGradientMask(w,h);
			BufferedImage avatar = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = avatar.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(255,255,255,0));
			g2.fillRect(0, 0, w, h);
			RoundRectangle2D r2d = new RoundRectangle2D.Double(0,0,w-1,h-1,10,10);
			if(round) {
				g2.setClip(r2d);
			}
			if(fillColor != null) {
				g2.setColor(fillColor);
				g2.fillRect(0, 0, w, h);
			}
			drawScaled(g2, bi);
			//			g2.drawImage(bi, 0, 0, w, h, 0, 0, w, h, null);
			if(shine) {
				int sh = 15;
				Paint pSubShade = new GradientPaint(w/2.0f, -sh, lightSubShadeColor,w/2.0f, (h/2.0f) + sh, darkSubShadeColor, true);
				g2.setPaint(pSubShade);
				RoundRectangle2D sr2d = new RoundRectangle2D.Double(0,-sh,w,(h/2) + sh,w/2,sh);
				g2.fill(sr2d);
				g2.setPaint(null);
			}
			if(round) {
				if(shine) {
					IOSUIHelper.drawEtchedRoundRect(g2, w-1,h-1, 10, 10, borderColor);
				} else {
					g2.setClip(null);
					g2.setColor(borderColor);
					g2.drawRoundRect(0,0,w-1,h-1,10,10);
				}
			}
			g2.dispose();

			if(mirrored) {
				avatar = createReflectedPicture(avatar, mask);
			}

			g2 = img.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int hh = h;
			if(mirrored) hh *= 2;
			g2.drawImage(avatar, 0, 0, w, hh, 0, 0, w, hh, null);
			g2.dispose();

		}
	}
	
	private Image getScaledImage(BufferedImage bi) {
		float iw = bi.getWidth();
		float ih = bi.getHeight();
		float pw = w;
		float ph = h;

		if ( pw < iw || ph < ih ) {
			if ( (pw / ph) > (iw / ih) ) {
				iw = -1;
				ih = ph;
			} else {
				iw = pw;
				ih = -1;
			}

			if (iw == 0) {
				iw = -1;
			}
			if (ih == 0) {
				ih = -1;
			}

			return bi.getScaledInstance(new Float(iw).intValue(), new Float(ih).intValue(), Image.SCALE_SMOOTH);

		} else {
			return bi;
		}
	}
	private void drawScaled(Graphics2D g2, BufferedImage bi) {
		Image si = getScaledImage(bi);
		int sw = si.getWidth(null);
		int sh = si.getHeight(null);
		int xx = 0;
		int yy = 0;
		if(sw < w) xx = (w - sw) / 2;
		if(sh < h) yy = (h - sh) / 2;
		g2.drawImage(getScaledImage(bi), xx, yy, null);

//		int srcw = bi.getWidth();
//		int srch = bi.getHeight();
//		g2.drawImage(bi, 0, 0, w, h, 0, 0, srcw, srch, null);
	}

	public void paint(Graphics2D g2) {
		int ow = owner.getWidth();
		int oh = owner.getHeight();
		int hh = h;
		if(mirrored) hh *= 2;
		int xx = (ow - w)/2;
		int yy = (oh - h)/2;
		g2.drawImage(img, xx, yy+1, xx+w, yy+hh+1, 0, 0, w, hh, null);
	}

	public BufferedImage createReflectedPicture(BufferedImage avatar,BufferedImage alphaMask) {
		int avatarWidth = avatar.getWidth();
		int avatarHeight = avatar.getHeight();

		BufferedImage buffer = createReflection(avatar,
				avatarWidth, avatarHeight);

		applyAlphaMask(buffer, alphaMask, avatarWidth, avatarHeight);

		return buffer;
	}

	private BufferedImage createReflection(BufferedImage avatar,int avatarWidth,int avatarHeight) {

		BufferedImage buffer = new BufferedImage(avatarWidth, avatarHeight << 1,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buffer.createGraphics();

		g.drawImage(avatar, null, null);
		g.translate(0, avatarHeight << 1);

		AffineTransform reflectTransform = AffineTransform.getScaleInstance(1.0, -1.0);
		g.drawImage(avatar, reflectTransform, null);
		g.translate(0, -(avatarHeight << 1));

		g.dispose();

		return buffer;
	}

	private void applyAlphaMask(BufferedImage buffer,BufferedImage alphaMask,int avatarWidth, int avatarHeight) {

		Graphics2D g2 = buffer.createGraphics();
		g2.setComposite(AlphaComposite.DstOut);
		g2.drawImage(alphaMask, null, 0, avatarHeight);
		g2.dispose();
	}

	public BufferedImage createGradientMask(int avatarWidth, int avatarHeight) {
		BufferedImage gradient = new BufferedImage(avatarWidth, avatarHeight,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = gradient.createGraphics();
		GradientPaint painter = new GradientPaint(0.0f, 0.0f,new Color(1.0f, 1.0f, 1.0f, 0.5f),	0.0f, avatarHeight / 2.0f,new Color(1.0f, 1.0f, 1.0f, 1.0f));
		g.setPaint(painter);
		g.fill(new Rectangle2D.Double(0, 0, avatarWidth, avatarHeight));

		g.dispose();

		return gradient;
	}

}
