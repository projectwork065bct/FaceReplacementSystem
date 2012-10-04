package frs.gui.pages;

import frs.dataTypes.FeaturePoint;
import frs.gui.components.IOSUIImageView;
import frs.gui.components.IOSUIRadioButton;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextField;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;

public class P80_Replace extends RFPage {

    IOSUIImageView imgView;
    //chooseHairView and its components
    ButtonGroup bg;
    //Buttons
    IOSUIView panelView;
    IOSUIView colorConsistencyView;
    IOSUIRadioButton meanShiftRB, histRB;
    IOSUILabel colorConsistencyLbl, meanShiftLbl, histLbl;
    IOSUIView blendView;
    IOSUILabel blendLbl, iterationLbl;
    IOSUITextField iterationTF;
    IOSUIButton replaceBtn;
    IOSUIButton saveBtn;
    Font f;

    public P80_Replace(RFApplication app) {
        super(app, "The face has been replaced!");
        //finalResultView.setImage(frs.getWarpedImage());//get replaced face
        f = new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 14);
        mainView = new IOSUIView("600px,20px,140px,20px", "400px");
        imgView = new IOSUIImageView("600px", "400px");
        mainView.addXY(imgView, 1, 1);
        addXY(mainView, 1, 1, "f,f");
        panelView = new IOSUIView("140px", "140px,90px,60px,60px");
        panelView.setBackground(Color.white);
        initColorConsistencyView();
        initBlendView();
        replaceBtn = new IOSUIButton(new AbstractAction("Replace Face") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.warp(FeaturePoint.CHIN);
                try {
                    frs.setBlendIterations(Integer.parseInt(iterationTF.getText()));
                } catch (Exception ex) {
                    frs.setBlendIterations(10);
                }
                if (meanShiftRB.getRadio().isSelected()) {
                    frs.applyColorConsistency_meanShift();
                } else {
                    System.out.println("hist. matching");
                    frs.applyColorConsistency_HistogramMatch();
                }

                frs.shiftReplacementPoint();
                frs.replaceFace2();
                frs.blend();
                if (frs.getWhichHairStr().compareTo("source") == 0) {
                    frs.addSourceHairToReplacedFace();
                } else if (frs.getWhichHairStr().compareTo("target") == 0) {
                    frs.addTargetHairToReplacedFace();
                }
                imgView.setImage(frs.getReplacedFaceImage());
            }
        });
        Color c = new Color(7,100,15,240);
        replaceBtn.setBackground(c);
        
        panelView.addXY(replaceBtn, 1, 3);
        mainView.addXY(panelView, 3, 1);
        helpText.setText("Jump and dance!");
    }

    protected void initColorConsistencyView() {
        bg = new ButtonGroup();
        colorConsistencyView = new IOSUIView("40px,100px", "40px,5px,40px,5px,40px,5px");
        colorConsistencyLbl = new IOSUILabel("  Color Consistency");
        colorConsistencyView.addXYW(colorConsistencyLbl, 1, 1, 2);
        histRB = new IOSUIRadioButton();
        colorConsistencyView.addXY(histRB, 1, 3);
        meanShiftRB = new IOSUIRadioButton();
        colorConsistencyView.addXY(meanShiftRB, 1, 5);
        bg.add(histRB.getRadio());
        bg.add(meanShiftRB.getRadio());
        histRB.getRadio().setSelected(true);
        histLbl = new IOSUILabel("Histogram Matching");
        colorConsistencyView.addXY(histLbl, 2, 3);
        meanShiftLbl = new IOSUILabel("Mean Shifting");
        colorConsistencyView.addXY(meanShiftLbl, 2, 5);
        panelView.addXY(colorConsistencyView, 1, 1);
        saveBtn = new IOSUIButton(new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                File replacedImgFile;
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(mainView);
                String fileName = fileChooser.getSelectedFile().getAbsolutePath() + ".png";
                replacedImgFile = new File(fileName);

                try {
                    ImageIO.write(frs.getReplacedFaceImage(), "png", replacedImgFile);
                } catch (IOException ex) {
                    Logger.getLogger(P80_Replace.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        Color c = new Color(7,100,15,240);
        saveBtn.setBackground(c);
        panelView.addXY(saveBtn, 1, 4);
    }

    protected void initBlendView() {
        blendView = new IOSUIView("60px,20px,60px", "40px,5px,40px,5px");
        blendLbl = new IOSUILabel("  Blending");
        blendView.addXYW(blendLbl, 1, 1, 3);
        iterationLbl = new IOSUILabel("  Iterations");
        blendView.addXY(iterationLbl, 1, 3);
        iterationTF = new IOSUITextField(false);
        iterationTF.setText("10");
        IOSUIView iterationView = new IOSUIView("60px", "40px");
        iterationView.setBackground(Color.WHITE);
        iterationView.addXY(iterationTF, 1, 1);
        blendView.addXY(iterationView, 3, 3);
        panelView.addXY(blendView, 1, 2);
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
