package hu.droidzone.iosui;

import hu.droidzone.iosui.i18n.IOSI18N;
import hu.droidzone.iosui.utils.IOSUIHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.JTextComponent;

public class IOSUITextField extends IOSUIComponent {
	private static final long serialVersionUID = -8999812514676301963L;
	private JTextComponent tf;
	private String placeholder = IOSI18N.get("textfield.default.placeholder");;
	private DocumentListener tfdl;
	private boolean withBorder = true;
	private int leftSpace = 3;
	private int rightSpace = 3;
	
	public IOSUITextField(boolean isPassword) {
		tfdl = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateValue();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateValue();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		if(isPassword) {
			JPasswordField ptf = new JPasswordField();
			ptf.setEchoChar('*');
			tf = ptf;
		} else {
			tf = new JTextField();
		}
		tf.setFont(new Font("Verdana", Font.PLAIN, 14));
		tf.setText("");
		
//		MetalTextFieldUI mtf = new MetalTextFieldUI();
//
//		tf.setUI(mtf);
		tf.setBorder(BorderFactory.createEmptyBorder());
		tf.setOpaque(false);
		tf.getDocument().addDocumentListener(tfdl);
		add(tf);
		int h = tf.getPreferredSize().height;
		leftSpace = h/2;
		rightSpace = h/2;
		if(withBorder) h += 4;
		setPreferredSize(new Dimension(50, h));
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(withBorder) {
					tf.setBounds(leftSpace,1,getWidth()-leftSpace-rightSpace,getHeight()-2);
				} else {
					tf.setBounds(0,0,getWidth(),getHeight());
				}
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	
		if(isPassword) {
			JPasswordField ptf = (JPasswordField) tf;
			ptf.setEchoChar('*');
		}
	}
	
	public IOSUITextField(Font font, boolean isPassword) {
		this(isPassword);
		tf.setFont(font);
	}

	protected void updateValue() {
		fireValueChanged(null, tf.getText());
	}

	@Override
	protected void paintUI(Graphics2D g2) {
		if(withBorder) IOSUIHelper.drawEtchedRoundRect(g2, this, getHeight(), getHeight(), Color.GRAY);
		if(placeholder != null && (tf.getText().length() == 0)) {
	        g2.setFont(tf.getFont());
	        g2.setColor(Color.LIGHT_GRAY);
	        FontMetrics fm = g2.getFontMetrics(getFont());
	        int hgt = fm.getHeight();
	//        int adv = fm.stringWidth(placeholder);
	        int ascent = fm.getAscent();
	        int y = (getHeight() - hgt)/2;
	        int x = tf.getLocation().x;//(getWidth() - adv)/2;
	        backupClipping(g2, tf.getBounds());
	        g2.drawString(placeholder, x, y+ascent);
	        restoreClipping(g2);
		}
	}

	public void setText(String text) {
		if(tf != null) {
			tf.getDocument().removeDocumentListener(tfdl);
			tf.setText(text); 
			tf.getDocument().addDocumentListener(tfdl);
		}
	}

	public String getText() {
		if(tf != null) return tf.getText();
		else return null;
	}
}
