/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.components;

import hu.droidzone.iosui.IOSUIComponent;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import javax.swing.JCheckBox;

/**
 *
 * @author Robik Shrestha
 */
public class IOSUICheckBox extends IOSUIComponent {

    protected JCheckBox checkBox;

    public IOSUICheckBox() {
        checkBox = new JCheckBox();
        setLayout(new BorderLayout());
        this.add(checkBox);
                
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    protected void paintUI(Graphics2D gd) {
        this.validate();
    }
}
