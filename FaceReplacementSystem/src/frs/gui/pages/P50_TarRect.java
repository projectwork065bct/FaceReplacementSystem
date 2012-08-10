package frs.gui.pages;

import frs.gui.components.ResizableRectangleView;
import frs.helpers.GeometricTransformation;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import frs.main.RFApplication;

public class P50_TarRect extends RFPage {

    ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");

    public P50_TarRect(RFApplication app) {
        super(app, "Draw Rectangle");
        rrv.setImage(frs.getTargetImage());
        rrv.initializeRectangle(GeometricTransformation.getRectangleUsingFP(frs.getTarFP()));
        rrv.setBackground(Color.red);
        addXY(rrv, 1, 1, "f,f");
        helpText.setText("Adjust the rectangle around the face.");
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
}
