package hu.droidzone.iosui;

import hu.droidzone.iosui.icons.IOSUIIconPainter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D.Double;
import java.net.URL;

import javax.swing.Action;

public class IOSUIImageButton extends IOSUIAbstractButton {
	private static final long serialVersionUID = -4116827222099020906L;
	private static final Color darkBorderColor = new Color(29,27,27,200);
	private static final Color lightBorderColor = new Color(255,255,255,60);
	private static final Color lightShadeColor = new Color(255,255,255,100);
	private static final Color darkShadeColor = new Color(255,255,255,0);
	
	private IOSUIIconPainter painter;
	private boolean paintBorder;

	public IOSUIImageButton(URL url) {
		this(url,true);
	}
	public IOSUIImageButton(URL url, boolean paintBorder) {
		this.painter = new IOSUIIconPainter(this, 3, 3, 22, 22, url);
		this.paintBorder = paintBorder;
		
		setPreferredSize(new Dimension(HH, HH));
		
	}
	
	public IOSUIImageButton(Action a, URL url) {
		this(a,url,true);
	}
	
	public IOSUIImageButton(Action a, URL url, boolean paintBorder) {
		this(url, paintBorder);
		setAction(a);
	}
	
	@Override
	protected void paintUI(Graphics2D g2) {
		if(!paintBorder && isSelected()) {
			Paint pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
			RoundRectangle2D br2d = new RoundRectangle2D.Double(1, 3, getWidth()-3, getHeight()-4, 10, 10);
			g2.setColor(getBackground());
	        g2.setPaint(pShade);
	        g2.fill(br2d);
	        g2.setPaint(null);
	        g2.setColor(lightBorderColor);
	        g2.draw(br2d);
	        painter.paint(g2);
	        return;
		}
		if(paintBorder) {
			super.paintUI(g2);
		}
		painter.paint(g2);
	}
}
