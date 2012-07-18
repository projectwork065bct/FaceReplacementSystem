package GUI.Pages;

import GUI.Library.IOSUIImageView;
import GUI.Library.RFApplication;

public class RFPage6 extends RFPage {

    //DrawFinalImage finalResult = new DrawFinalImage("600px", "400px");
    IOSUIImageView finalResult = new IOSUIImageView("600px", "400px");
    public RFPage6(RFApplication app) {
        super(app, "final output image");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        //finalResult.setFRS(frs);
        finalResult.setImage(frs.getSourceImage());
        
        addXY(finalResult, 1, 1, "f,f");
        helpText.setText("Draw rectangle around the face");
    }

    @Override
    public void goNext() {
         //pc.navigateTo(new RFPage3(app));
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
