package hu.droidzone.iosui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.SwingConstants;

public class IOSUIHeader extends IOSUIView {
	private static final long serialVersionUID = 2606678738623357847L;
	private Color darkColor = new Color(255,255,255,20);
	private Color lightColor = new Color(255,255,255,180);
	private Color separatorColor = new Color(255,255,255,0);
	
	private static final Color listViewHeaderBgColor = new Color(166,171,184);
	private static final Color listViewHeaderSeparatorColor = new Color(120,128,144);
	private static final boolean debugPainting = false;
	private boolean halfShadedMode = false;
	private boolean transparentMode = false;
	public static IOSUIHeader createApplicationHeader(String columnSpecs) {
		IOSUIHeader ret = new IOSUIHeader(columnSpecs, "p");
		ret.setPreferredSize(new Dimension(100, 38));
		ret.transparentMode = true;
		return ret;
	}
	
	public static IOSUIHeader createApplicationHeader(int left, int right, String title) {
		String rowSpecs = "p";
		String columnSpecs = "";
		for(int i = 0;i < left;i++) columnSpecs += "p,";
		columnSpecs += "p:g,";
		for(int i = 0;i < right;i++) columnSpecs += "p,";
		columnSpecs = columnSpecs.substring(0,columnSpecs.length() - 1);
//		rowSpecs = rowSpecs.substring(0,rowSpecs.length() - 1);
		IOSUIHeader ret = new IOSUIHeader(columnSpecs, rowSpecs);
		ret.setPreferredSize(new Dimension(100, 38));
		IOSUILabel tl = new IOSUILabel();
		tl.setFont(new Font("Verdana", Font.BOLD, 18));
		tl.setForeground(Color.WHITE);
		tl.setHorizontalAlignment(SwingConstants.CENTER);
		tl.setVerticalAlignment(SwingConstants.TOP);
		tl.setTitle(title);
//		tl.setBackground(Color.YELLOW);
		ret.transparentMode = true;
		ret.addXY(tl,left+1,1,"f,c");
		return ret;
	}
	
	public static IOSUIHeader createOwnedPagesViewHeader() {
		IOSUIHeader ret = new IOSUIHeader(1,1);
		ret.setLayout(null);
		ret.transparentMode = true;
		ret.setPreferredSize(new Dimension(100, IOSUIPagesControl.HEADER_HEIGHT));
		return ret;
	}
	
	public static IOSUIHeader createPagesViewHeader() {
		IOSUIHeader ret = new IOSUIHeader(1,1);
		ret.setLayout(null);
		ret.setBackground(listViewHeaderBgColor);
		ret.separatorColor = listViewHeaderSeparatorColor;
//		ret.halfShadedMode = true;
		return ret;
	}
	
	public IOSUIHeader(int colCount, int rowCount) {
		super(colCount, rowCount);
	}
	private IOSUIHeader(String columnSpecs, String rowSpecs) {
		super(columnSpecs, rowSpecs);
	}
	
	@Override
	protected void paintUI(Graphics2D g2) {
		if(transparentMode) return;
		RoundRectangle2D r2d = new RoundRectangle2D.Double(0,0,getWidth(),getHeight()+20,10,10);
		g2.setColor(getBackground());
		g2.fill(r2d);
		int sh = getHeight();
		if(halfShadedMode) {
			sh /= 2;
			Rectangle r = new Rectangle(0,0,getWidth(),sh);
			backupClipping(g2, r);
		}
		Paint pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightColor,getWidth()/2.0f, sh, darkColor, false);
		g2.setPaint(pShade);
		g2.fill(r2d);
		g2.setPaint(null);
		if(halfShadedMode) {
			restoreClipping(g2);
		}
		g2.setColor(separatorColor);
		g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		if(debugPainting) {
			g2.setColor(Color.RED);
			g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}
	}

}
