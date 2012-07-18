package GUI.Pages;

import hu.droidzone.iosui.*;
import GUI.Library.RFApplication;
import java.awt.Color;

public abstract class RFPage extends IOSUIView implements IOSUIPage {
	protected RFApplication app;
	protected IOSUIPagesControl pc;
	private String title;
	protected  IOSUITextArea helpText;
        protected facereplacementsystem.FaceReplacementSystem frs;
	public RFPage(RFApplication app, String title) {
		super("p:g","p:g,25dlu");
		this.title = title;
		this.app = app;
                this.frs = app.getFaceReplacementSystem();
		helpText = new IOSUITextArea("HelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpTextHelpText");
		helpText.setForeground(Color.WHITE);
		addXY(helpText,1,2,"f,f");
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
