package hu.droidzone.iosui;

public interface IOSUIPage {
	String getPageTitle();
	IOSUIComponent getPageContent();
	IOSUIComponent getPageControls();
	void setShell(IOSUIPagesControl shell);
	boolean isImageBack();
	void pageRemoved();
}
