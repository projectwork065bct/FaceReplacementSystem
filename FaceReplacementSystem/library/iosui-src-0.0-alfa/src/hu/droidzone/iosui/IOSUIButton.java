package hu.droidzone.iosui;

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

import javax.swing.Action;

public class IOSUIButton extends IOSUIAbstractButton {
	private static final long serialVersionUID = -4116827222099020906L;
	
	private static final int EE = 20;

	private String title;
	public IOSUIButton() {
		this("Click!");
	}

	public IOSUIButton(String title) {
		this.title = title;
		setForeground(Color.WHITE);
		setFont(new Font("Verdana", Font.BOLD, 12));

		setPreferredSizeFor(title, HH, EE);
	}
	
	public IOSUIButton(Action a) {
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
		super.paintUI(g2);
		
        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics(getFont());
        int hgt = fm.getHeight();
        int adv = fm.stringWidth(title);
        int ascent = fm.getAscent();
        int y = (getHeight() - hgt)/2;
        int x = (getWidth() - adv)/2;
        g2.drawString(title, x, y+ascent);
	}
}
