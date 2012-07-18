package GUI.Pages;

import GUI.Library.RFApplication;
import GUI.temp.ResizableRectangleView;
import java.awt.Color;

public class RFPage6 extends RFPage {

    
    public RFPage6(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        
        helpText.setText("Draw rectangle around the face");
    }

    @Override
    public void goNext() {
         pc.navigateTo(new RFPage3(app));
    }
}
