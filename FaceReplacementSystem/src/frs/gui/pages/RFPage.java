package frs.gui.pages;

import frs.engine.FRSEngine;
import hu.droidzone.iosui.*;
import java.awt.Color;
import frs.main.RFApplication;
/*
 * This is the parent RFPage
 */

public abstract class RFPage extends IOSUIView implements IOSUIPage {
    
    protected RFApplication app;
    protected IOSUIPagesControl pc;
    private String title;
    protected IOSUITextArea helpText;
    protected IOSUIView mainView;
    protected FRSEngine frs;
    
    public RFPage(RFApplication app, String title) {
        super("p:g", "p:g,25dlu");
        this.title = title;
        this.app = app;
        this.frs = app.getFRS();
        mainView = new IOSUIView("750px", "400px");
        helpText = new IOSUITextArea("Help");
        helpText.setForeground(Color.WHITE);
        helpText.setEnabled(false);
        addXY(helpText, 1, 2, "f,f");
    }
    
    @Override
    public String getPageTitle() {
        return title;
    }
    
    @Override
    public IOSUIComponent getPageContent() {
        return this;
    }
    
    @Override
    public IOSUIComponent getPageControls() {
        return null;
    }
    
    @Override
    public void setShell(IOSUIPagesControl shell) {
        this.pc = shell;
    }
    
    @Override
    public boolean isImageBack() {
        return true;
    }
    
    @Override
    public void pageRemoved() {
    }
    
    @Override
    public void pageCameIn() {
    }
    
    public abstract void goNext();
}
