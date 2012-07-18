package GUI.Pages;

import java.awt.Color;
import java.awt.Font;

import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextArea;
import GUI.Library.RFApplication;
import GUI.temp.ResizableRectangleView;

public class RFPage3 extends RFPage {
ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");
	public RFPage3(RFApplication app) {
            super(app, "Third Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        rrv.setFRS(frs);
        rrv.setImage(frs.getTargetImage());
        rrv.setBackground(Color.red);
        addXY(rrv, 1, 1, "f,f");
        helpText.setText("Draw rectangle around the face");
	}
        
	@Override
	public void goNext() {
		pc.navigateTo(new RFPage4(app));
	}
}
