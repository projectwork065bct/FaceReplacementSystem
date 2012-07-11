package hu.droidzone.iosui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Action;

public class IOSUISectionalButton extends IOSUIComponent {
	private static final long serialVersionUID = -8063630673015051480L;
	Action[] sectionActions;
	private static final Color darkBorderColor = new Color(29,27,27,(int)(2.55*70));
	private static final Color lightBorderColor = new Color(213,213,213,(int)(2.55*20));
	private static final Color lightShadeColor = new Color(255,255,255,100);
	private static final Color darkShadeColor = new Color(255,255,255,0);
	
	private int selectedSection = -1;
	
	public IOSUISectionalButton(Action ... sas) {
		this.sectionActions = sas;
		setForeground(Color.WHITE);
		setFont(new Font("Verdana", Font.BOLD, 12));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
		        int sw = getWidth() / sectionActions.length;
				selectedSection = e.getX()/sw;
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				selectedSection = -1;
				repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				Action a = null;
		        int sw = getWidth() / sectionActions.length;
				int ss = e.getX()/sw;
				if(ss != -1) a = sectionActions[ss]; 
				selectedSection = -1;
				repaint();
				if(a != null) {
					ActionEvent ae = new ActionEvent(IOSUISectionalButton.this, ss, "SECTION_CLICK");
					a.actionPerformed(ae);
				}
			}
		});
	}
	
	private int calculateWidth(FontMetrics fm) {
		int ret = -1;
        for(int i = 0;i < sectionActions.length;i++) {
        	String title = sectionActions[i].getValue(Action.NAME).toString();
        	int adv = fm.stringWidth(title);
        	ret = Math.max(adv,ret);
        }
		return ret;
	}
	
	@Override
	protected void paintUI(Graphics2D g2) {
        g2.setFont(getFont());
        g2.setColor(getBackground());
        int w = getWidth();
        int sw = w / sectionActions.length;
        
        FontMetrics fm = g2.getFontMetrics(getFont());
		RoundRectangle2D r2d = new RoundRectangle2D.Double(2, 2, getWidth()-4, getHeight()-4, 10, 10);
		RoundRectangle2D br2d = new RoundRectangle2D.Double(2, 3, getWidth()-4, getHeight()-4, 10, 10);
		backupClipping(g2, r2d);
		g2.setColor(getBackground());
		g2.fill(br2d);
		restoreClipping(g2);
		
		//draw shade - will reflect the status
		Paint pShade = null;
		if(selectedSection == -1) {
			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
			br2d = new RoundRectangle2D.Double(2, 3, getWidth()-4, getHeight()-3, 10, 10);
			backupClipping(g2, r2d);
			g2.setPaint(pShade);
			g2.fill(br2d);
			g2.setPaint(null);
			restoreClipping(g2);
		} else {
			br2d = new RoundRectangle2D.Double(2, 3, getWidth()-4, getHeight()-3, 10, 10);
	        for(int i = 0;i < sectionActions.length;i++) {
	    		if(selectedSection == i) {
	    			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, darkShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
	    		} else {
	    			pShade = new GradientPaint(getWidth()/2.0f, 0.0f, lightShadeColor,getWidth()/2.0f, getHeight(), darkShadeColor, false);
	    		}
	    		Rectangle r = new Rectangle(sw * i, 3, sw,getHeight()-3);
				backupClipping(g2, r);
				g2.setPaint(pShade);
				g2.fill(br2d);
				g2.setPaint(null);
				restoreClipping(g2);
	        }			
		}
		
 		
		//draw shadow under the button
        RoundRectangle2D sr2d = new RoundRectangle2D.Double(2, 3, getWidth()-4, getHeight()-4, 10, 10);
        Rectangle sr = new Rectangle(0,getHeight()-3,getWidth(),3);
        backupClipping(g2, sr);
        g2.setColor(lightBorderColor);
        g2.draw(sr2d);
        restoreClipping(g2);
        
		//draw shadow under upper border
        sr2d = new RoundRectangle2D.Double(4, 3, getWidth()-5, getHeight()-4, 10, 10);
        sr = new Rectangle(0,0,getWidth(),5);
        g2.setColor(darkShadeColor);
        backupClipping(g2, sr);
        g2.draw(sr2d);
        restoreClipping(g2);
        
        //draw frame
        g2.setColor(darkBorderColor);
        g2.draw(r2d);
        for(int i = 0;i < sectionActions.length;i++) {
        	String title = sectionActions[i].getValue(Action.NAME).toString();
	        //draw separators
	        if(i < (sectionActions.length - 1)) {
	        	g2.setColor(darkBorderColor);
	        	g2.drawLine((sw*i)+sw, 2, (sw*i)+sw, getHeight()-2);
	        	g2.setColor(lightBorderColor);
	        	g2.drawLine((sw*i)+sw+1, 2, (sw*i)+sw+1, getHeight()-2);
	        }
	        
	        g2.setFont(getFont());
	        if(i == selectedSection) {
//	        	g2.setColor(Color.BLUE);
	        	g2.setColor(getForeground());
	        } else {
	        	g2.setColor(getForeground());
	        }
	        
	        int hgt = fm.getHeight();
	        int adv = fm.stringWidth(title);
	        int ascent = fm.getAscent();
	        int y = (getHeight() - hgt)/2;
	        int x = (sw*i)+(sw - adv)/2;
	        g2.drawString(title, x, y+ascent);
        }
	}
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

}
