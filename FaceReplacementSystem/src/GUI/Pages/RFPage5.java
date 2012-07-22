package GUI.Pages;

import java.awt.Color;
import java.awt.Font;

import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextArea;
import GUI.Library.RFApplication;
import GUI.temp.ResizableRectangleView;
import java.awt.Point;
import java.awt.Rectangle;

public class RFPage5 extends RFPage {

    ResizableRectangleView rrv = new ResizableRectangleView("600px", "400px");

    public RFPage5(RFApplication app) {
        super(app, "Third Page");
        rrv.setFRS(frs);
        rrv.setImage(frs.getTargetImage());
        rrv.initializeRectangle("target");
        rrv.setBackground(Color.red);
        addXY(rrv, 1, 1, "f,f");
        helpText.setText("Draw a rectangle around the face.");
    }

    @Override
    public void goNext() {
        //detect skin color
        setTargetRectangle();
        frs.detectSkin();
        frs.warp(3);
        pc.navigateTo(new RFPage6(app));
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
        frs.setTargetFaceRectangle(resized);
    }
}
