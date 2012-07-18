package GUI.Pages;

import GUI.Library.RFApplication;
import GUI.temp.ResizableRectangleView;
import java.awt.Color;

public class RFPage2 extends RFPage {

    ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");
    
    public RFPage2(RFApplication app) {
        super(app, "Second Page");
//		IOSUITextArea ta = new IOSUITextArea(new Font("Verdana", Font.BOLD, 22));
//		ta.setText("Second Page Content");
//		ta.setForeground(Color.WHITE);
//		addXY(ta,1,1,"f,f");
        rrv.setFRS(frs);
        rrv.setImage(frs.getSourceImage());
        rrv.setBackground(Color.red);
        addXY(rrv, 1, 1, "f,f");
        helpText.setText("Draw rectangle around the face");
    }

    @Override
    public void goNext() {
        frs.setSourceFaceRectangle(rrv.getRectangle());
        System.out.println("rect height = "+frs.getSourceFaceRectangle().height);
        pc.navigateTo(new RFPage3(app));
    }
}
