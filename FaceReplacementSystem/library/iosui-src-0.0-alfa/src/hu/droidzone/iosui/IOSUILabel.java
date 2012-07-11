package hu.droidzone.iosui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class IOSUILabel extends IOSUIComponent {
	JLabel lbl;
	
	public IOSUILabel() {
		this("");
	}
	
	public IOSUILabel(String text) {
		setLayout(new BorderLayout());
		lbl = new JLabel(text);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(lbl != null) lbl.setFont(font);
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if(lbl != null) lbl.setForeground(fg);
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(lbl != null) lbl.setBackground(bg);
	}
	
	public void setTitle(String title) {
		lbl.setText(title);
		setPreferredSize(lbl.getPreferredSize());
	}
	
	public void setVerticalAlignment(int valign) {
		lbl.setVerticalAlignment(valign);
	}
	
	public void setHorizontalAlignment(int halign) {
		lbl.setHorizontalAlignment(halign);
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		lbl.setSize(getSize());
		lbl.paint(g2);
	}
}
