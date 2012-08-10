/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.components;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import hu.droidzone.iosui.IOSUIComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JSlider;
import javax.swing.JTextArea;

/**
 *
 * @author Dell
 */
public class IOSUISlider extends IOSUIComponent {

    JSlider slider;
    JTextArea txt = new JTextArea("asdf");

    public JSlider getSlider() {
        return slider;
    }

    public void setSlider(JSlider slider) {
        //this.slider = slider;
        //add(this.slider);
        //  add(txt);
    }

    public IOSUISlider(int orientation, int min, int max, int value) {
        super();
        slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        setLayout(new BorderLayout());
        // CellConstraints cc = new CellConstraints();
        add(slider);
        //slider.setOpaque(false);
        //add(slider);
        //slider.setForeground(Color.red);
        //slider.setBackground(Color.white);
        //CellConstraints cc = new CellConstraints();
        ///add(slider,cc.xy(1, 1));
        //txt.setForeground(Color.red);
        //dd(txt);
    }

    @Override
    protected void paintUI(Graphics2D gd) {
        this.validate();
        //slider.setVisible(true);
        //IOSUIHelper.drawEtchedRoundRect(gd, this, getWidth(), getHeight(), Color.GRAY);
        //super.paintComponent(gd);
        //slider.repaint();
        //gd.drawString("hello", 0, 0);
        //slider.setForeground(Color.red);
        //slider.paint(gd);
        //setBackground(Color.white);
        //slider.setOpaque(false);
        //add(slider);
        //slider.setForeground(Color.red);
        //slider.setBackground(Color.white);

    }
}
