package GUI.Pages;

import java.awt.Color;
import java.awt.Font;

import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextArea;
import GUI.Library.RFApplication;
import GUI.temp.FeaturePoints;

public class RFPage4 extends RFPage {

    FeaturePoints fp = new FeaturePoints("600px", "400px");
    
	public RFPage4(RFApplication app) {
		super(app, "Fourth Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Fourth Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
                fp.setFRS(frs);
                fp.setImage(frs.getSourceImage());
                //rrv.setBackground(Color.red);
                addXY(fp, 1, 1, "f,f");
		helpText.setText("Help for the Fourth page");
	}

	@Override
	public void goNext() {
            pc.navigateTo(new RFPage5(app));
        }

}
