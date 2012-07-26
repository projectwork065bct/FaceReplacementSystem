package GUI.Pages;

import GUI.Components.RFApplication;
import GUI.Components.ResizableRectangleView;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class RFPage3 extends RFPage {

    IOSUIView mainView = new IOSUIView("500px,100px", "400px");
    ResizableRectangleView rrv = new ResizableRectangleView("500px", "400px");
    IOSUIButton exptBtn;//Button for experimentation

    public RFPage3(final RFApplication app) {
        super(app, "Second Page");
        rrv.setFRS(frs);
        rrv.setImage(frs.getSourceImage());
        rrv.initializeRectangle("source");
        rrv.setBackground(Color.red);
        mainView.addXY(rrv, 1, 1, "f,f");

        exptBtn = new IOSUIButton(new AbstractAction("Experiment") {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSourceRectangle();
                pc.navigateTo(new RFPage3Expt(app));
            }
        });

        mainView.addXY(exptBtn, 2, 1);
        addXY(mainView, 1, 1);
        helpText.setText("Draw a rectangle around the face.");

    }

    @Override
    public void goNext() {
        setSourceRectangle();
        pc.navigateTo(new RFPage4(app));
    }

    //Saves the rectangle drawn around the face
    public void setSourceRectangle() {
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
        frs.setSourceFaceRectangle(resized);
    }
}
