package GUI.Pages;

import GUI.Library.IOSUIImageView;
import GUI.Library.RFApplication;

public class RFPage6 extends RFPage {

    //DrawFinalImage finalResult = new DrawFinalImage("600px", "400px");
    IOSUIImageView finalResult = new IOSUIImageView("600px", "400px");

    public RFPage6(RFApplication app) {
        super(app, "The face has been replaced!");
        finalResult.setImage(frs.getWarpedImage());
        addXY(finalResult, 1, 1, "f,f");
        helpText.setText("Jump and dance!");
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
