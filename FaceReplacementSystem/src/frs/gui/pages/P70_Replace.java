package frs.gui.pages;

import frs.dataTypes.FeaturePoint;
import frs.gui.components.IOSUIImageView;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class P70_Replace extends RFPage {
    IOSUIImageView imgView;
    IOSUIView buttonsView;
    IOSUIButton srcSkinBtn;
    IOSUIButton srcWarpBtn;
    IOSUIButton tarWarpBtn;
    IOSUIButton replaceBtn;
    IOSUIButton warpedSrcHairBtn;

    public P70_Replace(RFApplication app) {
        super(app, "The face has been replaced!");
        //finalResultView.setImage(frs.getWarpedImage());//get replaced face
        initializeBtns();
        mainView = new IOSUIView("600px,20px,140px,20px", "400px");
        imgView = new IOSUIImageView("600px", "400px");
        mainView.addXY(imgView, 1, 1);
        buttonsView = new IOSUIView("140px", "50px,50px,50px,50px,50px,50px");
        buttonsView.addXY(srcSkinBtn, 1, 1);
        buttonsView.addXY(srcWarpBtn, 1, 2);
        buttonsView.addXY(warpedSrcHairBtn, 1, 3);
        buttonsView.addXYWH(replaceBtn, 1, 4,1,2);
        mainView.addXY(buttonsView, 3, 1, "f,f");
        addXY(mainView, 1, 1, "f,f");
        helpText.setText("Jump and dance!");
    }

    public void initializeBtns() {
        srcSkinBtn = new IOSUIButton(new AbstractAction("Skin of Source") {

            @Override
            public void actionPerformed(ActionEvent e) {
                imgView.setImage(frs.getSourceSkinImage());
            }
        });

        srcWarpBtn = new IOSUIButton(new AbstractAction("Warp Source") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.warp(3);
                imgView.setImage(frs.getWarpedImage());
            }
        });



        //Draw the replaced face on 
        replaceBtn = new IOSUIButton(new AbstractAction("Replace") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.warp(FeaturePoint.CHIN);
                frs.applyColorConsistency();
                frs.shiftReplacementPoint();
                frs.replaceFace();
                frs.blend();
                if (frs.getWhichHairStr().compareTo("source") == 0) {
                    frs.addSourceHairToReplacedFace();
                } else if (frs.getWhichHairStr().compareTo("target") == 0) {
                    frs.addTargetHairToReplacedFace();
                }
                imgView.setImage(frs.getReplacedFaceImage());
            }
        });

        warpedSrcHairBtn = new IOSUIButton(new AbstractAction("Warped Src Hair") {

            @Override
            public void actionPerformed(ActionEvent e) {
                imgView.setImage(frs.getSrcHairImgAfterWarping());
            }
        });
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
