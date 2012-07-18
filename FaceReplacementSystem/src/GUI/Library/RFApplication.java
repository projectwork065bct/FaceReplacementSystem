package GUI.Library;

import hu.droidzone.iosui.IOSUIApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.IOSUIHeader;
import hu.droidzone.iosui.IOSUIPage;
import hu.droidzone.iosui.IOSUIPagesControl;
import hu.droidzone.iosui.IOSUITextArea;
import hu.droidzone.iosui.IOSUIView;
import hu.droidzone.iosui.list.IOSUIListView;
import GUI.Pages.RFPage;
import GUI.Pages.RFPage1;
import facereplacementsystem.FaceReplacementSystem;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class RFApplication {
	public IOSUIButton btnNext;
	public FaceReplacementSystem faceReplacementSystem;
	
	IOSUIPagesControl pc;
	
	private void startup() {
		this.faceReplacementSystem = new FaceReplacementSystem();
		IOSUIView content = new IOSUIView("p:g","p,p:g");
		
		IOSUIHeader pageHeader = IOSUIHeader.createOwnedPagesViewHeader();
		IOSUIHeader navigationHeader = IOSUIHeader.createApplicationHeader("p:g,p");

		IOSUIHeader applicationHeader = IOSUIHeader.createApplicationHeader(1,1,"Replace Face");
		btnNext = new IOSUIButton(new AbstractAction("Next Step") {
			@Override
			public void actionPerformed(ActionEvent e) {
				RFPage cp = (RFPage) pc.getCurrent();
				cp.goNext();
			}
		});
		
		pc = new IOSUIPagesControl(new RFPage1(this), pageHeader);
                
		IOSUIButton btnExit = new IOSUIButton(new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		applicationHeader.addXY(btnExit,3,1);
		btnExit.setBackground(new Color(255,0,0,150));//Color.RED);
		
		navigationHeader.addXY(pageHeader,1,1);
		navigationHeader.addXY(btnNext,2,1);
		
		content.addXY(navigationHeader,1,1,"f,f");
		content.addXY(pc,1,2,"f,f");
		content.setBackground(pc.getBackground());//Color.gray);
		
		IOSUIApplication.startIOSUIApplication(800, 600,content,applicationHeader);
		
	}
	
	public static void main(String[] args) {
		new RFApplication().startup();
	}

    public FaceReplacementSystem getFaceReplacementSystem() {
        return faceReplacementSystem;
    }
        
        
}
