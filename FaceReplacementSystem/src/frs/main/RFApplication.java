package frs.main;

import frs.engine.FRSEngine;
import frs.gui.pages.P10_LoadImage;
import frs.gui.pages.P40_Face;
import frs.gui.pages.RFPage;
import hu.droidzone.iosui.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class RFApplication {

    public IOSUIButton btnNext;
    public FRSEngine frs;
    IOSUIPagesControl pc;

    public FRSEngine getFRS() {
        return frs;
    }

    private void startup() {
        this.frs = new FRSEngine();
        IOSUIView content = new IOSUIView("p:g", "p,p:g");//layout preferred Grow

        IOSUIHeader pageHeader = IOSUIHeader.createOwnedPagesViewHeader();
        IOSUIHeader navigationHeader = IOSUIHeader.createApplicationHeader("p:g,p");

        IOSUIHeader applicationHeader = IOSUIHeader.createApplicationHeader(1, 1, "Replace Face");
        btnNext = new IOSUIButton(new AbstractAction("Next Step") {

            @Override
            public void actionPerformed(ActionEvent e) {
                RFPage cp = (RFPage) pc.getCurrent();
                cp.goNext();
            }
        });

        //pc = new IOSUIPagesControl(new P10_LoadImage(this), pageHeader);
        pc = new IOSUIPagesControl(new P10_LoadImage(this), pageHeader);
        IOSUIButton btnExit = new IOSUIButton(new AbstractAction("Exit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        applicationHeader.addXY(btnExit, 3, 1);
        btnExit.setBackground(new Color(255, 0, 0, 150));//Color.RED);

        navigationHeader.addXY(pageHeader, 1, 1);
        navigationHeader.addXY(btnNext, 2, 1);

        content.addXY(navigationHeader, 1, 1, "f,f");
        content.addXY(pc, 1, 2, "f,f");
        content.setBackground(pc.getBackground());//Color.gray);

        IOSUIApplication.startIOSUIApplication(800, 600, content, applicationHeader);

    }

    public static void main(String[] args) {
        new RFApplication().startup();
    }
}
