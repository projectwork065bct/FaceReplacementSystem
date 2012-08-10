package frs.gui.pages;

import frs.gui.components.IOSUISlider;
import frs.gui.components.SRGView;
import frs.helpers.DeepCopier;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextField;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import frs.main.RFApplication;

public class P60_Hair extends RFPage {

    SRGView srgView = new SRGView("550px", "400px");
    IOSUIView leftBtnsView = new IOSUIView("95px", "50px,50px,50px,50px,50px,50px,50px,50px");
    IOSUILabel imgLbl;
    IOSUIButton chooseSrcBtn;
    IOSUIButton chooseTarBtn;
    IOSUILabel modeLbl;
    IOSUIButton addBtn, removeBtn;
    IOSUIView rightBtnsView = new IOSUIView("95px", "50px,50px,50px,50px,50px,50px,50px,50px");
    IOSUILabel thresholdLbl;
    IOSUITextField thresholdTxtField;
    IOSUILabel similarityLbl;
    IOSUIButton stdDevBtn, rangeBtn;
    Stack<Point> seedPoints;
    IOSUISlider slider;

    public P60_Hair(RFApplication app) {
        super(app, "Specify hair region");
        initializeData();
        initializeComponents();
    }

    private void initializeData() {
        seedPoints = new Stack();
        frs.setTarHairImgShowingRegion(DeepCopier.getBufferedImage(frs.getTargetImage(), BufferedImage.TYPE_INT_ARGB));
        frs.setSrcHairImgShowingRegion(DeepCopier.getBufferedImage(frs.getSrcImg(), BufferedImage.TYPE_INT_ARGB));
        srgView.setFRSEngine(frs);
        srgView.defineImage("target");
        helpText.setText("Place seeds for hair region extraction.");
    }

    private void initializeComponents() {
        mainView = new IOSUIView("10px,550px,10px,95px,10px,95px,10px", "400px");
        mainView.addXY(srgView, 2, 1);
        createLeftBtnsView();
        createRightBtnsView();
        mainView.addXY(leftBtnsView, 4, 1);
        mainView.addXY(rightBtnsView, 6, 1);
        srgView.setImage(frs.getTargetImage());
        addXY(mainView, 1, 1);
    }

    @Override
    public void goNext() {
        //pc.navigateTo(new P10_LoadImage(app));
        pc.navigateTo(new P70_Replace(app));
    }

    protected void createLeftBtnsView() {
        createImgSection();
        createModeSection();
    }

    protected void createImgSection() {
        imgLbl = new IOSUILabel("Image:");
        imgLbl.setForeground(Color.WHITE);
        leftBtnsView.addXY(imgLbl, 1, 1);
        chooseSrcBtn = new IOSUIButton(new AbstractAction("Source") {

            @Override
            public void actionPerformed(ActionEvent e) {
                srgView.defineImage("source");
            }
        });
        leftBtnsView.addXY(chooseSrcBtn, 1, 2);

        chooseTarBtn = new IOSUIButton(new AbstractAction("Target") {

            @Override
            public void actionPerformed(ActionEvent e) {
                srgView.defineImage("target");
            }
        });
        leftBtnsView.addXY(chooseTarBtn, 1, 3);
    }

    protected void createModeSection() {
        modeLbl = new IOSUILabel("Mode:");
        modeLbl.setForeground(Color.white);
        leftBtnsView.addXY(modeLbl, 1, 5);
        addBtn = new IOSUIButton(new AbstractAction("Add") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.setSrgMode("add");
            }
        });
        leftBtnsView.addXY(addBtn, 1, 6);

        removeBtn = new IOSUIButton(new AbstractAction("Remove") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.setSrgMode("remove");
            }
        });
        leftBtnsView.addXY(removeBtn, 1, 7);
    }

    protected void createRightBtnsView() {
        thresholdLbl = new IOSUILabel("Threshold (0-1):");
        thresholdLbl.setForeground(Color.white);
        rightBtnsView.addXY(thresholdLbl, 1, 1);
        //thresholdTxtField = new IOSUITextField(false);
        //thresholdTxtField.setText(".5");
        slider = new IOSUISlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.getSlider().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                frs.setSrgThreshold(getThreshold());

            }
        });
        IOSUIView thresholdTxtFieldView = new IOSUIView("95px", "50px");
        thresholdTxtFieldView.setBackground(Color.white);
        thresholdTxtFieldView.addXY(slider, 1, 1);


        rightBtnsView.addXY(thresholdTxtFieldView, 1, 2);
        //rightBtnsView.addXY(slider, 1, 2);
        similarityLbl = new IOSUILabel("Similarity:");
        similarityLbl.setForeground(Color.white);
        rightBtnsView.addXY(similarityLbl, 1, 5);
        stdDevBtn = new IOSUIButton(new AbstractAction("Std. Dev.") {

            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        rightBtnsView.addXY(stdDevBtn, 1, 6);
        rangeBtn = new IOSUIButton(new AbstractAction("Range") {

            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        rightBtnsView.addXY(rangeBtn, 1, 7);
    }

    public float getThreshold() {
        //return Float.parseFloat(thresholdTxtField.getText());
        return (((float)slider.getSlider().getValue() / 100));
    }
}
