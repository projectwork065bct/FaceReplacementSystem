package hu.droidzone.iosui.utils;

import hu.droidzone.iosui.IOSUIComponent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class IOSUIHelper {
	private IOSUIHelper() {
	}

	public static boolean isOverlap(long a, long b, long c, long d) {
		return ((b >= c) && (a <= d));
	}

	public static Color setAlpha(Color c, int alpha) {
		Color ret = new Color(c.getRed(), c.getGreen(), c.getBlue(),alpha);
		return ret;
	}
	
	public static void drawEtchedRoundRect(Graphics2D g2, int w, int h, int arcw, int arch, Color bc) {

		//bottom light shade
		RoundRectangle2D rr2d = new RoundRectangle2D.Double(0, 0, w, h, arcw, arch);
		Rectangle cr = new Rectangle(0, h - arch - 1, w, arch+1);
//		comp.backupClipping(g2, cr);
//		g2.setColor(bc.brighter());
//		g2.draw(rr2d);
//		comp.restoreClipping(g2);
		
		//upper dark shade
		cr = new Rectangle(0, 0, w, (arch/2)+1);
		
		g2.setClip(cr);
//		rr2d = new RoundRectangle2D.Double(0, 1, w, h-1, arcw, arch);
//		g2.setColor(setAlpha(bc.darker().darker(),180));
//		g2.draw(rr2d);

//		rr2d = new RoundRectangle2D.Double(0, 2, w, h-2, arcw, arch);
//		g2.setColor(setAlpha(bc.darker(),180));
//		g2.draw(rr2d);
	
		rr2d = new RoundRectangle2D.Double(0, 1, w, h-1, arcw, arch);
		g2.setColor(setAlpha(bc.darker(),180));
		g2.draw(rr2d);
		
		g2.setClip(null);
		
		//the border
		rr2d = new RoundRectangle2D.Double(0, 0, w, h-1, arcw, arch);
		g2.setColor(bc);
		g2.draw(rr2d);
	}
	
	public static void drawEtchedRoundRect(Graphics2D g2, IOSUIComponent comp, int arcw, int arch, Color bc) {
		int w = comp.getWidth()-1;
		int h = comp.getHeight()-1;

		//bottom light shade
		RoundRectangle2D rr2d = new RoundRectangle2D.Double(0, 0, w, h, arcw, arch);
		Rectangle cr = new Rectangle(0, h - arch - 1, w, arch+1);
//		comp.backupClipping(g2, cr);
//		g2.setColor(bc.brighter());
//		g2.draw(rr2d);
//		comp.restoreClipping(g2);
		
		//upper dark shade
		cr = new Rectangle(0, 0, w, (arch/2)+1);
		
		comp.backupClipping(g2, cr);
//		rr2d = new RoundRectangle2D.Double(0, 1, w, h-1, arcw, arch);
//		g2.setColor(setAlpha(bc.darker().darker(),180));
//		g2.draw(rr2d);

//		rr2d = new RoundRectangle2D.Double(0, 2, w, h-2, arcw, arch);
//		g2.setColor(setAlpha(bc.darker(),180));
//		g2.draw(rr2d);
	
		rr2d = new RoundRectangle2D.Double(0, 1, w, h-1, arcw, arch);
		g2.setColor(setAlpha(bc.darker(),180));
		g2.draw(rr2d);
		
		comp.restoreClipping(g2);
		
		//the border
		rr2d = new RoundRectangle2D.Double(0, 0, w, h-1, arcw, arch);
		g2.setColor(bc);
		g2.draw(rr2d);
		
	}
}
