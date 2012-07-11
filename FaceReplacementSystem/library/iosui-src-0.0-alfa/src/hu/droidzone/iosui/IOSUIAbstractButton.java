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

public class IOSUIAbstractButton extends IOSUIComponent {
	private static final long serialVersionUID = -4116827222099020906L;
	protected static final Color darkBorderColor = new Color(29,27,27,200);
	protected static final Color lightBorderColor = new Color(255,255,255,60);
	protected static final Color lightShadeColor = new Color(255,255,255,100);
	protected static final Color darkShadeColor = new Color(255,255,255,0);
	
	protected static final int HH = 28;

	private Action action;
	private boolean selected = false;
	
	@Override
	public boolean isEventConsumer(MouseEvent e) {
		switch(e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
		case MouseEvent.MOUSE_RELEASED:
		case MouseEvent.MOUSE_CLICKED:
		case MouseEvent.MOUSE_DRAGGED:
			return true;
		}
		return false;
	}
	
	public IOSUIAbstractButton() {
		setForeground(Color.WHITE);
		setFont(new Font("Verdana", Font.BOLD, 12));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!isEnabled()) return;
				setSelected(true);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!isEnabled()) return;
				setSelected(false);
				Rectangle r = new Rectangle(getWidth(), getHeight());
				if(r.contains(e.getPoint())) {
					if(action != null) {
						ActionEvent ae = new ActionEvent(IOSUIAbstractButton.this, 100, "CLICK");
						action.actionPerformed(ae);
					}
				}
				repaint();
			}
		});
	}
	
	protected void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	protected boolean isSelected() {
		return selected;
	}
	
	public IOSUIAbstractButton(Action a) {
		this.action = a;
	}
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		RoundRectangle2D r2d = new RoundRectangle2D.Double(0, 2, getWidth()-1, getHeight()-4, 10, 10);
		RoundRectangle2D br2d = new RoundRectangle2D.Double(0, 3, getWidth()-1, getHeight()-4, 10, 10);
		g2.setColor(getBackground());
		g2.fill(r2d);
		
		//draw shade - will reflect the status
		Paint pShade = null;
		if(selected) {
			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, darkShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
		} else {
			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
		}
		br2d = new RoundRectangle2D.Double(0, 3, getWidth()-1, getHeight()-4, 10, 10);
        g2.setPaint(pShade);
        g2.fill(br2d);
        g2.setPaint(null);
        RoundRectangle2D sr2d = null;
        Rectangle sr = null;
		//draw shadow under the button
        sr2d = new RoundRectangle2D.Double(0, 2, getWidth()-1, getHeight()-3, 10, 10);
        sr = new Rectangle(0,getHeight()-5,getWidth(),6);
        backupClipping(g2, sr);
        g2.setColor(lightBorderColor);
        g2.draw(sr2d);
        restoreClipping(g2);
        
		//draw shadow under upper border
        sr2d = new RoundRectangle2D.Double(0, 3, getWidth()-1, getHeight()-1, 10, 10);
        sr = new Rectangle(0,0,getWidth(),6);
        g2.setColor(darkShadeColor);
        backupClipping(g2, sr);
        g2.draw(sr2d);
        restoreClipping(g2);
        
        
        //draw frame
        g2.setColor(darkBorderColor);
        g2.draw(r2d);
        
	}
}
