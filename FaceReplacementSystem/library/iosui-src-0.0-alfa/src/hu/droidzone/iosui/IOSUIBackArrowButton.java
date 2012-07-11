package hu.droidzone.iosui;

import hu.droidzone.iosui.shapes.ArrowRoundedRectangleShape;

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
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;

public class IOSUIBackArrowButton extends IOSUIAbstractButton {
	private static final long serialVersionUID = -4116827222099020906L;
	
	private static final int EE = 20+HH/2;
	private static final int IW = 23;
	private static final int IH = 19;
	private static final int IE = 30;
	
	private BufferedImage backArrow;
	private boolean withImage;

	private String title;

	public IOSUIBackArrowButton() {
		setWithImage();
	}

	public void setWithImage() {
		this.withImage = true;
		try {
			this.backArrow = ImageIO.read(getClass().getResource("/resources/back_arrow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(IW+IE,HH));
	}

	public IOSUIBackArrowButton(String title) {
		this.title = title;
		this.withImage = false;
		setForeground(Color.WHITE);
		setFont(new Font("Verdana", Font.BOLD, 12));

		setPreferredSizeFor(title, HH, EE);
	}
	
	public IOSUIBackArrowButton(Action a) {
		this(a.getValue(Action.NAME).toString());
		setAction(a);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setPreferredSizeFor(title, HH, EE);
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		ArrowRoundedRectangleShape r2d = new ArrowRoundedRectangleShape.Double(2, 2, getWidth()-4, getHeight()-4, 10, 10);
		ArrowRoundedRectangleShape br2d = new ArrowRoundedRectangleShape.Double(2, 3, getWidth()-3, getHeight()-5, 10, 10);
//		backupClipping(g2, r2d);
		g2.setColor(getBackground());
		g2.fill(r2d);
//		restoreClipping(g2);
		
		//draw shade - will reflect the status
		Paint pShade = null;
		if(isSelected()) {
			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, darkShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
		} else {
			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
		}
		br2d = new ArrowRoundedRectangleShape.Double(2, 3, getWidth()-4, getHeight()-4, 10, 10);
//		backupClipping(g2, r2d);
        g2.setPaint(pShade);
        g2.fill(br2d);
        g2.setPaint(null);
//        restoreClipping(g2);
        ArrowRoundedRectangleShape sr2d = null;
        Rectangle sr = null;
		//draw shadow under the button
        sr2d = new ArrowRoundedRectangleShape.Double(2, 3, getWidth()-5, getHeight()-4, 10, 10);
        sr = new Rectangle(0,getHeight()-3,getWidth(),3);
        backupClipping(g2, sr);
        g2.setColor(lightBorderColor);
        g2.draw(sr2d);
        restoreClipping(g2);
        
		//draw shadow under upper border
        sr2d = new ArrowRoundedRectangleShape.Double(4, 3, getWidth()-5, getHeight()-4, 10, 10);
        sr = new Rectangle(0,0,getWidth(),5);
        g2.setColor(darkShadeColor);
        backupClipping(g2, sr);
        g2.draw(sr2d);
        restoreClipping(g2);
        
        
        //draw frame
        g2.setColor(darkBorderColor);
        g2.draw(r2d);
        
        if(withImage) {
	        int y = (getHeight() - IH)/2;
	        int x = (IE)/2;
	        g2.drawImage(backArrow, x, y, null);
        } else {
	        g2.setFont(getFont());
	        g2.setColor(getForeground());
	        FontMetrics fm = g2.getFontMetrics(getFont());
	        int hgt = fm.getHeight();
	        int adv = fm.stringWidth(title);
	        int ascent = fm.getAscent();
	        int y = (getHeight() - hgt)/2;
	        int x = (getWidth() - adv + HH/4)/2;
	        g2.drawString(title, x, y+ascent);
        }
	}
}
