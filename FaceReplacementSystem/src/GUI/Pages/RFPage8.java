package GUI.Pages;

import GUI.Components.RFApplication;
import GUI.Components.ResizableRectangleView;
import java.awt.Color;

public class RFPage8 extends RFPage {

    
    public RFPage8(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        
        helpText.setText("Draw rectangle around the face");
    }

	@Override
	public void goNext() {
	}

	@Override
	public void pageCameIn() {
		app.btnNext.setVisible(false);
	}
	
	@Override
	public void pageRemoved() {
		app.btnNext.setVisible(true);
	}
}