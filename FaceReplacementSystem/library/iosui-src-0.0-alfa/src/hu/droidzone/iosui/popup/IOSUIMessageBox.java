package hu.droidzone.iosui.popup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUIHeader;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUIView;
import hu.droidzone.iosui.i18n.IOSI18N;
import hu.droidzone.iosui.utils.IOSUIHelper;

public class IOSUIMessageBox {
	private IOSUIDialog dlg;
	private IOSUIMessageButton res = IOSUIMessageButton.OK;
	private IOSUIMessageBox() {
	}
	
	private IOSUIMessageButton _showMessageDialog(String title, String message, IOSUIMessageButton ... buttons) {
		IOSUIHeader hdr = IOSUIHeader.createApplicationHeader(0, 0, title);
		String cstr = "p:g";
		for(IOSUIMessageButton btn : buttons) {
			cstr += ",2dlu,75px";
		}
		IOSUIView ftr = new IOSUIView(cstr, "28px");
	
		int col = 3;
		for(IOSUIMessageButton btn : buttons) {
			IOSUIButton btnctrl = null;
			switch(btn) {
			case CANCEL:
				btnctrl = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.cancel")) {
					@Override
					public void actionPerformed(ActionEvent e) {
						res = IOSUIMessageButton.CANCEL;
						dlg.setVisible(false);
					}
				});
				btnctrl.setBackground(IOSUIHelper.setAlpha(Color.DARK_GRAY,180));
				break;
			case MORE:
				btnctrl = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.more")) {
					@Override
					public void actionPerformed(ActionEvent e) {
						res = IOSUIMessageButton.MORE;
						dlg.setVisible(false);
					}
				});
				btnctrl.setBackground(IOSUIHelper.setAlpha(Color.BLUE,180));
				break;
			case NO:
				btnctrl = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.no")) {
					@Override
					public void actionPerformed(ActionEvent e) {
						res = IOSUIMessageButton.NO;
						dlg.setVisible(false);
					}
				});
				btnctrl.setBackground(IOSUIHelper.setAlpha(Color.RED,180));
				break;
			case OK:
				btnctrl = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.ok")) {
					@Override
					public void actionPerformed(ActionEvent e) {
						res = IOSUIMessageButton.OK;
						dlg.setVisible(false);
					}
				});
				btnctrl.setBackground(IOSUIHelper.setAlpha(Color.GREEN,180));
				break;
			case YES:
				btnctrl = new IOSUIButton(new AbstractAction(IOSI18N.get("msg.btn.yes")) {
					@Override
					public void actionPerformed(ActionEvent e) {
						res = IOSUIMessageButton.YES;
						dlg.setVisible(false);
					}
				});
				btnctrl.setBackground(IOSUIHelper.setAlpha(Color.GREEN,180));
				break;
			}
			ftr.addXY(btnctrl,col,1);
			col += 2;
		}
		ftr.setPreferredSize(new Dimension(290, 32));
		IOSUILabel cnt = new IOSUILabel(message);
		cnt.setForeground(Color.WHITE);
		cnt.setBackground(Color.GRAY);
		dlg = new IOSUIDialog(225 + (buttons.length * 75), 120, cnt, hdr, ftr);
		dlg.setVisible(true);
		return res;
	}
	
	public static void displayMessage(String message) {
		new IOSUIMessageBox()._showMessageDialog(IOSI18N.get("msg.message"), message, IOSUIMessageButton.OK);
	}
	public static void displayWarning(String message) {
		new IOSUIMessageBox()._showMessageDialog(IOSI18N.get("msg.warning"), message, IOSUIMessageButton.OK);
	}
	public static void displayError(String message) {
		new IOSUIMessageBox()._showMessageDialog(IOSI18N.get("msg.error"), message, IOSUIMessageButton.OK);
	}

	public static IOSUIMessageButton showMessageDialog(String title, String message, IOSUIMessageButton ... buttons) {
		return new IOSUIMessageBox()._showMessageDialog(title, message, buttons);
	}
	
}
