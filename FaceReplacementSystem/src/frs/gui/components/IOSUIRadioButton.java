/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.components;

import hu.droidzone.iosui.IOSUIComponent;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JRadioButton;

/**
 *
 * @author Dell
 */
public class IOSUIRadioButton extends IOSUIComponent{

    protected JRadioButton radio;
    public IOSUIRadioButton()
    {
        super();
        radio = new JRadioButton();
        setLayout(new BorderLayout());
        // CellConstraints cc = new CellConstraints();
        add(radio);
    }

    public JRadioButton getRadio() {
        return radio;
    }

    public void setRadio(JRadioButton radio) {
        this.radio = radio;
    }
    
    
    
    
    @Override
    protected void paintUI(Graphics2D gd) {
       this.validate();
    }
    
}
