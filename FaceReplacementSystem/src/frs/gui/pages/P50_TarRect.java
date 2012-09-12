package frs.gui.pages;

import frs.gui.components.ResizableRectangleView;
import frs.helpers.GeometricTransformation;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextField;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class P50_TarRect extends RFPage {
    IOSUILabel thresholdLbl, iterationLbl;
    IOSUITextField thresholdTxtField, iterationTxtField;
    IOSUIButton snakePreviewBtn;
    ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");

    public P50_TarRect(RFApplication app) {
        super(app, "Draw Rectangle");
        initUI();
        helpText.setText("Adjust the rectangle around the face.");
        initSnakeUI();
        addXY(mainView,1,1);
    }

    public void initUI()
    {
        mainView = new IOSUIView("600px,10px,170px","400px");
        rrv.setImage(frs.getTargetImage());
        rrv.initializeRectangle(GeometricTransformation.getRectangleUsingFP(frs.getTarFP()));
        rrv.setBackground(Color.red);
        mainView.addXY(rrv, 1, 1, "f,f");
        
        
    }
    @Override
    public void goNext() {
        //detect skin color
        setTargetRectangle();
        frs.detectTargetSkin();
        frs.warp(3);
        pc.navigateTo(new P60_Hair(app));
    }

    //Saves the rectangle drawn around the target face
    public void setTargetRectangle() {
        Rectangle r = rrv.getRectangle();
        Rectangle resized = new Rectangle();
        int recX = r.x;
        int recY = r.y;
        int width = r.width;
        int height = r.height;
        int stopx = recX + width;
        int stopy = recY + height;
        Point a = rrv.toActualImagePoint(new Point(recX, recY));
        Point b = rrv.toActualImagePoint(new Point(stopx, stopy));
        resized.x = a.x;
        resized.y = a.y;
        resized.width = b.x - a.x;
        resized.height = b.y - a.y;
        frs.setTarFaceRect(resized);
        frs.findTargetRectImage();
        frs.findTargetRectFP();
    }
    
    protected void initSnakeUI()
    {
        
        IOSUIView snakeView=new IOSUIView("50px,50px","40px,40px,40px");
        thresholdLbl = new IOSUILabel("Threshold");
        thresholdLbl.setFont(new Font(Font.SANS_SERIF,Font.BOLD,9));
        thresholdLbl.setForeground(Color.white);
        iterationLbl = new IOSUILabel("Iteration");
        iterationLbl.setFont(new Font(Font.SANS_SERIF,Font.BOLD,9));
        iterationLbl.setForeground(Color.white);
        thresholdTxtField = new IOSUITextField(false);
        thresholdTxtField.setText("3");
        thresholdTxtField.setBackground(Color.white);
        iterationTxtField = new IOSUITextField(false);
        iterationTxtField.setText("10");
        iterationTxtField.setBackground(Color.white);
        snakePreviewBtn = new IOSUIButton(new AbstractAction("Preview") {
/**
 * Add the code here for the previe button event
 */
            @Override
            public void actionPerformed(ActionEvent e) {
                int t = Integer.parseInt(thresholdTxtField.getText());
                frs.setSrcImg(null);
            }
        });
        IOSUIView thresholdView = new IOSUIView("50px","40px");
        IOSUIView iterationView = new IOSUIView("50px","40px");
        thresholdView.setBackground(Color.WHITE);
        iterationView.setBackground(Color.WHITE);
        snakeView.addXY(thresholdLbl,1,1);
        thresholdView.addXY(thresholdTxtField,1,1);
        snakeView.addXY(thresholdView,2,1);
        snakeView.addXY(iterationLbl,1,2);
        iterationView.addXY(iterationTxtField,1,1);
        snakeView.addXY(iterationView,2,2);
        snakeView.addXYW(snakePreviewBtn,1,3,2);
        mainView.addXY(snakeView,3,1);
    }

}
