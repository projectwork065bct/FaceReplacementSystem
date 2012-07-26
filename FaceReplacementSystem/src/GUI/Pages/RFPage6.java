package GUI.Pages;

import CoreAlgorithms.Shifter;
import GUI.Components.IOSUIImageView;
import GUI.Components.RFApplication;
import Helpers.DeepCopier;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RFPage6 extends RFPage {

    //DrawFinalImage finalResult = new DrawFinalImage("600px", "400px");
    IOSUIImageView finalResultView = new IOSUIImageView("600px", "400px");

    public RFPage6(RFApplication app) {
        super(app, "The face has been replaced!");
        //finalResultView.setImage(frs.getWarpedImage());//get replaced face
        frs.replaceFace();
        finalResultView.setImage(frs.getReplacedFaceImage());
        addXY(finalResultView, 1, 1, "f,f");
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
