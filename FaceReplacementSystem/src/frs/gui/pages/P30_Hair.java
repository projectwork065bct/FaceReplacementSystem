/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.pages;

import frs.gui.components.IOSUIRadioButton;
import frs.gui.components.IOSUISlider;
import frs.gui.components.SRGView;
import frs.helpers.DeepCopier;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextField;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Robik Shrestha
 */
public class P30_Hair extends RFPage {

    //Variables
    protected Stack<Point> seedPoints;
    protected SRGView srgView;
    protected IOSUIView toolBox;
    public static int SOURCE = 1, TARGET = 2;
    protected int identity = SOURCE;
    //All about mode view
    protected IOSUIView modeView;
    protected IOSUIRadioButton growRB, eraseRB;
    protected ButtonGroup btnGroup;
    protected IOSUILabel modeLbl, growLbl, eraseLbl;
    //For threshold view
    protected IOSUIView thresholdView;
    protected IOSUILabel thresholdLbl;
    protected IOSUISlider slider;
    //for rectSelectView
    protected IOSUIView rectSelectView;//views inside the toolbox
    protected IOSUILabel rectSelectLbl;
    protected IOSUITextField rectSelectTF;
    protected IOSUIButton rectSelectBtn;
    //font
    Font f;

    public P30_Hair(RFApplication app, int identity) {
        super(app, "Specify the hair region");
        this.identity = identity;
        initData();
        initComponents();
        addXY(mainView, 1, 1);
    }

    public void initData() {
        seedPoints = new Stack();

        if (this.identity == SOURCE) {
            frs.setSrcHairImgShowingRegion(DeepCopier.getBufferedImage(frs.getSrcImg(), BufferedImage.TYPE_INT_ARGB));
            frs.createSourceHairMatrix();
        } else {
            frs.setTarHairImgShowingRegion(DeepCopier.getBufferedImage(frs.getTargetImage(), BufferedImage.TYPE_INT_ARGB));
            frs.createTargetHairMatrix();
        }
        helpText.setText("Place seeds for hair region extraction.");
        f = new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 14);
        frs.setSrgMode("add");
    }

    public void initComponents() {
        mainView = new IOSUIView("560px,10px,180px", "400px");
        initSRGView();
        initToolBox();
    }

    public void initSRGView() {
        srgView = new SRGView("560px", "400px");
        srgView.setCursorSize(5, 5);
        srgView.setFRSEngine(frs);
        if (this.identity == SOURCE) {
            srgView.defineImage("source");
        } else {
            srgView.defineImage("target");
        }
        mainView.addXY(srgView, 1, 1);
    }

    public void initToolBox() {
        toolBox = new IOSUIView("180px", "105px,5px,70px,5px,110px");
        initModeView();
        initThresholdView();
        initRectSelectView();
        mainView.addXY(toolBox, 3, 1);
    }

    //functions for Mode View
    protected void initModeView() {
        btnGroup = new ButtonGroup();
        modeView = new IOSUIView("20px,5px,150px,5px", "30px,5px,30px,5px,30px,5px");
        initGrowRB();
        initEraseRB();
        btnGroup.add(growRB.getRadio());
        btnGroup.add(eraseRB.getRadio());
        modeLbl = new IOSUILabel("Mode:");
        modeLbl.setFont(f);
        modeLbl.setForeground(Color.white);
        growLbl = new IOSUILabel("Grow");
        growLbl.setFont(f);
        growLbl.setForeground(Color.white);
        eraseLbl = new IOSUILabel("Erase");
        eraseLbl.setFont(f);
        eraseLbl.setForeground(Color.white);
        modeView.addXY(modeLbl, 3, 1);
        modeView.addXY(growLbl, 3, 3);
        modeView.addXY(eraseLbl, 3, 5);
        toolBox.addXY(modeView, 1, 1);
    }

    protected void initGrowRB() {
        growRB = new IOSUIRadioButton();
        growRB.getRadio().addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.setSrgMode("add");
            }
        });
        growRB.getRadio().setSelected(true);
        modeView.addXY(growRB, 1, 3);
    }

    protected void initEraseRB() {
        eraseRB = new IOSUIRadioButton();
        eraseRB.getRadio().addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frs.setSrgMode("remove");
            }
        });
        modeView.addXY(eraseRB, 1, 5);
    }

    //functions for threshold view
    protected void initThresholdView() {
        thresholdView = new IOSUIView("180px", "30px,5px,30px,5px");
        thresholdLbl = new IOSUILabel("Threshold (0-1):");
        thresholdLbl.setForeground(Color.white);
        thresholdView.addXY(thresholdLbl, 1, 1);
        slider = new IOSUISlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.getSlider().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                frs.setSrgThreshold(getThreshold());

            }
        });
        IOSUIView thresholdSliderView = new IOSUIView("180px", "30px");
        thresholdSliderView.setBackground(Color.white);
        thresholdSliderView.addXY(slider, 1, 1);
        thresholdView.addXY(thresholdSliderView, 1, 3);
        toolBox.addXY(thresholdView, 1, 3);
    }

    public float getThreshold() {
        //return Float.parseFloat(thresholdTxtField.getText());
        return (((float) slider.getSlider().getValue() / 100));
    }

    //functions for rect select view
    protected void initRectSelectView() {
        rectSelectView = new IOSUIView("180px", "30px,5px,30px,5px,30px,5px");
        rectSelectLbl = new IOSUILabel("Enter cursor size:");
        rectSelectLbl.setFont(f);
        rectSelectLbl.setForeground(Color.white);
        rectSelectView.addXY(rectSelectLbl, 1, 1);
        rectSelectTF = new IOSUITextField(f, false);
        rectSelectTF.setText("5");
        rectSelectView.addXY(rectSelectTF, 1, 3);
        rectSelectBtn = new IOSUIButton(new AbstractAction("Set Size") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int val = Integer.parseInt(rectSelectTF.getText());
                srgView.setCursorSize(val, val);
            }
        });
        rectSelectView.addXY(rectSelectBtn, 1, 5);
        toolBox.addXY(rectSelectView, 1, 5);
    }

    @Override
    public void goNext() {
        if (identity == SOURCE) {
            pc.navigateTo(new P40_Face(app, P40_Face.SOURCE));
        } else {
            pc.navigateTo(new P40_Face(app, P40_Face.TARGET));
        }

    }
}
