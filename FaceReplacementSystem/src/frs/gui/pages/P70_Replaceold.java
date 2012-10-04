package frs.gui.pages;

import frs.dataTypes.FeaturePoint;
import frs.gui.components.IOSUIImageView;
import frs.gui.components.IOSUIRadioButton;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;

public class P70_Replaceold extends RFPage {

    IOSUIImageView imgView;
    //chooseHairView and its components
    IOSUIView chooseHairView;
    IOSUIRadioButton srcHairRB, tarHairRB;
    ButtonGroup bg;
    IOSUILabel hairLbl, srcHairLbl, tarHairLbl;
    //Buttons
    IOSUIView buttonsView;
    IOSUIButton srcSkinBtn;
    IOSUIButton srcWarpBtn;
    IOSUIButton tarWarpBtn;
    IOSUIButton replaceBtn;
    IOSUIButton warpedSrcHairBtn;
    Font f;

    public P70_Replaceold(RFApplication app) {
        super(app, "The face has been replaced!");
        //finalResultView.setImage(frs.getWarpedImage());//get replaced face
        f = new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 14);
        initializeBtns();
        mainView = new IOSUIView("600px,20px,140px,20px", "200px,200px");
        imgView = new IOSUIImageView("600px", "400px");
        mainView.addXYWH(imgView, 1, 1, 1, 2);
        buttonsView = new IOSUIView("140px", "30px,30px,30px,30px,30px,30px");
        buttonsView.addXY(srcSkinBtn, 1, 1);
        buttonsView.addXY(srcWarpBtn, 1, 2);
        buttonsView.addXY(warpedSrcHairBtn, 1, 3);
        buttonsView.addXYWH(replaceBtn, 1, 4, 1, 2);
        mainView.addXY(buttonsView, 3, 2, "f,f");
        initChooseHairView();
        addXY(mainView, 1, 1, "f,f");
        helpText.setText("Jump and dance!");
    }

    public void initChooseHairView() {
        chooseHairView = new IOSUIView("20px,10px,110px", "30px,5px,30px,5px,30px,5px");
        initSrcHairRB();
        initTarHairRB();
        bg = new ButtonGroup();
        bg.add(srcHairRB.getRadio());
        bg.add(tarHairRB.getRadio());
        hairLbl = new IOSUILabel("Choose Hair:");
        hairLbl.setFont(f);
        hairLbl.setForeground(Color.white);

        srcHairLbl = new IOSUILabel("Source Hair");
        srcHairLbl.setFont(f);
        srcHairLbl.setForeground(Color.white);
        tarHairLbl = new IOSUILabel("Target Hair");
        tarHairLbl.setFont(f);
        tarHairLbl.setForeground(Color.white);
        chooseHairView.addXYW(hairLbl, 1, 1, 3);
        chooseHairView.addXY(srcHairRB, 1, 3);
        chooseHairView.addXY(srcHairLbl, 3, 3);
        chooseHairView.addXY(tarHairRB, 1, 5);
        chooseHairView.addXY(tarHairLbl, 3, 5);
        mainView.addXY(chooseHairView, 3, 1);
    }

    protected void initSrcHairRB() {
        srcHairRB = new IOSUIRadioButton();
        srcHairRB.getRadio().addActionListener(new AbstractAction("") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.setWhichHairStr("source");
            }
        });
    }

    protected void initTarHairRB() {
        tarHairRB = new IOSUIRadioButton();
        tarHairRB.getRadio().addActionListener(new AbstractAction("") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.setWhichHairStr("target");
            }
        });
        tarHairRB.getRadio().setSelected(true);
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
                frs.applyColorConsistency_meanShift();
                frs.shiftReplacementPoint();
                frs.replaceFace2();
                frs.blend();
                //frs.overlayReplace();
                //frs.replaceUsingTanBlending();
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
