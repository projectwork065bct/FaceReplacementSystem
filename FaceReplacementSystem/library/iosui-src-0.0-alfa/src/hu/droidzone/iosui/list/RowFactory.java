package hu.droidzone.iosui.list;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingConstants;

import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.IOSUIIcon;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUISwitch;
import hu.droidzone.iosui.IOSUITextField;
import hu.droidzone.iosui.IOSUIView;
import hu.droidzone.iosui.icons.IconMode;
import hu.droidzone.iosui.icons.IconSize;

public class RowFactory {
	private static final Object RowFieldValueListenerKey = "RowFieldValueListenerKey";

	private RowFactory() {}
	
	public static IOSUIView createRowView(ListDataItem ldi) {
		RowFieldKind[][]fields = ldi.getFields();
		int cols = fields.length;
		int rows = 1;
		String colsSpec = "3dlu,2dlu,";
		String rowsSpec = "";
		for(int c = 0;c < cols;c++) {
			RowFieldKind[]col = fields[c];
			if(col.length > rows) rows = col.length;
//			colsSpec += "p";
			String ncs = "p";
			for(int r = 0;r < col.length;r++) {
				if(col[r] == RowFieldKind.More) {
					ncs = "16px";
					break;
				}
			}
			for(int r = 0;r < col.length;r++) {
				if(col[r] == RowFieldKind.Value) {
					ncs = "50px";
					break;
				}
			}
			colsSpec += ncs;
			for(int r = 0;r < col.length;r++) {
				if(col[r] == RowFieldKind.Empty || col[r] == RowFieldKind.EditableValue || col[r] == RowFieldKind.PasswordValue || col[r] == RowFieldKind.Value) {
					colsSpec += ":g";
					break;
				}
			}
			colsSpec += ",2dlu,";
		}
		
		for(int r = 0; r < rows;r++) rowsSpec += "p:g,";
		colsSpec = colsSpec.substring(0,colsSpec.length() - 1);
		rowsSpec = rowsSpec.substring(0,rowsSpec.length() - 1);
		colsSpec += ",3dlu";
		System.out.println(colsSpec+" -- "+rowsSpec);
		IOSUIView ret = new IOSUIView(colsSpec, rowsSpec);
		for(int x = 0;x < cols;x++) {
			RowFieldKind[]col = fields[x];
			if(col.length > 1) {
				for(int r = 0;r < col.length;r++) {
					RowFieldKind rfk = col[r];
					if(rfk == null) continue;
					IOSUIComponent fc = getComponentForFieldKind(rfk);
					loadValueFrom(ldi,rfk,fc);
					if(fc == null) continue;
					ret.addXY(fc, (x*2)+3, r+1, "f,f");
				}
			} else {
				RowFieldKind rfk = col[0];
				if(rfk == null) continue;
				IOSUIComponent fc = getComponentForFieldKind(rfk);
				loadValueFrom(ldi,rfk,fc);
				if(fc == null) continue;
				if(rows > 1) {
					ret.addXYWH(fc, (x*2)+3, 1, 1, rows, "c,f");
				} else {
					ret.addXY(fc, (x*2)+3, 1, "c,c");
				}
			}
		}
		return ret;
	}
	private static class RowFieldValueListener implements PropertyChangeListener {
		private ListDataItem ldi;
		private RowFieldKind rfk;
		public RowFieldValueListener(ListDataItem ldi, RowFieldKind rfk) {
			this.ldi = ldi;
			this.rfk = rfk;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ldi.setValueOfField(rfk, evt.getNewValue());
		}
		
	}
	
	public static void releaseRowView(IOSUIView rv) {
		for(Component c : rv.getComponents()) {
			if(c instanceof IOSUIComponent) {
				IOSUIComponent ic = (IOSUIComponent) c;
				Object o = ic.getClientProperty(RowFieldValueListenerKey);
				if(o != null) {
					ic.removeValueListener((PropertyChangeListener) o);
					ic.putClientProperty(RowFieldValueListenerKey, null);
				}
			}
		}
	}
	
	private static void loadValueFrom(ListDataItem ldi, RowFieldKind rfk,IOSUIComponent fc) {
		Object val = ldi.getValueOfField(rfk);
		if((val == null) || (fc == null)) return;
		switch (rfk) {
		case Check: 
		case Empty:
		case More:
			break;
		case Icon:
			break;
		case Detail: {
			IOSUILabel cl = (IOSUILabel) fc;
			cl.setTitle(val.toString());
			}
			break;
		case EditableValue: {
			IOSUITextField tf = (IOSUITextField) fc;
			tf.setText(val.toString());
			RowFieldValueListener rfvl = new RowFieldValueListener(ldi, rfk);
			tf.addValueListener(rfvl);
			tf.putClientProperty(RowFieldValueListenerKey, rfvl);
			}
			break;
		case PasswordValue: {
			IOSUITextField tf = (IOSUITextField) fc;
			tf.setText(val.toString());
			RowFieldValueListener rfvl = new RowFieldValueListener(ldi, rfk);
			tf.addValueListener(rfvl);
			tf.putClientProperty(RowFieldValueListenerKey, rfvl);
			}
			break;
		case Switch: {
			IOSUISwitch sw = (IOSUISwitch) fc;
			String vs = val.toString().toLowerCase();
			if("true".equals(vs) || "yes".equals(vs) || "t".equals(vs) || "y".equals(vs) || "1".equals(vs)) {
				sw.setOn(true);
			} else {
				sw.setOn(false);
			}
			RowFieldValueListener rfvl = new RowFieldValueListener(ldi, rfk);
			sw.addValueListener(rfvl);
			sw.putClientProperty(RowFieldValueListenerKey, rfvl);
			}
			break;
		case Title: {
			IOSUILabel cl = (IOSUILabel) fc;
			cl.setTitle(val.toString());
			}
			break;
		case Value:{
			IOSUILabel cl = (IOSUILabel) fc;
			cl.setTitle(val.toString());
			}
			break;
		}
		
	}

	private static IOSUIComponent getComponentForFieldKind(RowFieldKind rfk) {
		IOSUIComponent ret = null;
		switch (rfk) {
		case Check: {
			ret =  new IOSUIIcon(RowFactory.class.getResource("/resources/checkmark.png"), IconSize.M);
			}
			break;
		case Detail: {
			IOSUILabel cl = new IOSUILabel();
			cl.setFont(new Font("Verdana", Font.PLAIN, 10));
			cl.setForeground(Color.BLUE);
			cl.setVerticalAlignment(SwingConstants.TOP);
			ret = cl;
			}
			break;
		case EditableValue: {
			IOSUITextField tf = new IOSUITextField(new Font("Verdana", Font.PLAIN, 16),false);
			ret = tf;
			}
			break;
		case PasswordValue: {
			IOSUITextField tf = new IOSUITextField(new Font("Verdana", Font.PLAIN, 16),true);
			ret = tf;
			}
			break;
		case Empty:
			break;
		case Icon:
			IOSUIIcon icn = new IOSUIIcon(IconSize.L,IconMode.ROUND);
			ret = icn;
			break;
		case More: {
			ret = new IOSUIIcon(RowFactory.class.getResource("/resources/more.png"), IconSize.M);
			}
			break;
		case Switch: {
			IOSUISwitch sw = new IOSUISwitch();
			ret = sw;
			}
			break;
		case Title: {
			IOSUILabel cl = new IOSUILabel();
			cl.setFont(new Font("Verdana", Font.BOLD, 16));
			ret = cl;
			}
			break;
		case Value:{
			IOSUILabel cl = new IOSUILabel();
			cl.setFont(new Font("Verdana", Font.PLAIN, 16));
			cl.setBackground(Color.yellow);
			ret = cl;
			}
			break;
		}
		return ret;
	}

	public static void main(String[] args) {
//		createRowView(new ListDataItem() {
//			@Override
//			public Object getValueOfField(RowFieldKind field) {
//				return null;
//			}
//			
//			@Override
//			public RowFieldKind[][] getFields() {
//				return ListDataModel.ICON_TITLE_DETAIL_VALUE_CHECK;
//			}
//		});
		System.out.println(Boolean.TRUE.toString());
	}
}
