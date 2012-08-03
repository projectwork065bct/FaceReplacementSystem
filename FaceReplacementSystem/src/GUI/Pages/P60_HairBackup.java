package GUI.Pages;

import GUI.Components.DrawableRectangleView;
import GUI.Components.IOSUIImageView;
import GUI.Components.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Stack;
import javax.swing.AbstractAction;

public class P60_HairBackup extends RFPage {

    DrawableRectangleView drv = new DrawableRectangleView("300px", "400px");
    IOSUIImageView thv = new IOSUIImageView("300px", "400px");//Target hair view
    IOSUIView buttonsView = new IOSUIView("120px", "50px,50px,50px");
    IOSUIButton growBtn;
    Stack<Point> seedPoints;

    public P60_HairBackup(RFApplication app) {
        super(app, "Specify hair region");
        mainView = new IOSUIView("300px,20px,300px,20px,120px,20px", "400px");
        mainView.addXY(drv, 1, 1);
        mainView.addXY(thv, 3, 1);
        initializeBtns();
        mainView.addXY(buttonsView, 5, 1);
        drv.setImage(frs.getTargetImage());
        addXY(mainView, 1, 1);
        helpText.setText("Place seeds for hair region extraction.");
        seedPoints = new Stack();
    }

    @Override
    public void goNext() {
        setHairSeeds();
        pc.navigateTo(new P70_Replace(app));
    }

    protected void initializeBtns() {
        growBtn = new IOSUIButton(new AbstractAction("Grow") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHairSeeds();
                thv.setImage(frs.getTargetHairImage());
            }
        });
        buttonsView.addXY(growBtn,1,1);
    }

    public void setHairSeeds() {
        Rectangle seedRect = drv.getRectangle();
        for (int x = seedRect.x; x < seedRect.width + seedRect.x; x++) {
            for (int y = seedRect.y; y < seedRect.height + seedRect.y; y++) {
                seedPoints.push(drv.toActualImagePoint(new Point(x, y)));
            }
        }
        frs.setTargetHairSeeds(seedPoints);
        frs.detectTargetHair();
    }
}
