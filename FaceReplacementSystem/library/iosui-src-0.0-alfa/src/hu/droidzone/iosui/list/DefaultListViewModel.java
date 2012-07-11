package hu.droidzone.iosui.list;

import java.awt.Color;

import hu.droidzone.iosui.IOSUIComponent;
import hu.droidzone.iosui.IOSUILabel;

public class DefaultListViewModel implements IOSUIListViewModel {

	private ListDataModel ldm;
	private ListKind lk;

	public DefaultListViewModel(ListKind lk, ListDataModel ldm) {
		this.ldm = ldm;
		this.lk = lk;
	}
	
	@Override
	public ListKind getListKind() {
		return lk;
	}

	@Override
	public int getGroupCount() {
		return ldm.getGroups().size();
	}

	@Override
	public IOSUIComponent getRow(int row) {
		return getRow(0, row);
	}

	@Override
	public IOSUIComponent getRow(int group, int row) {
		if(group == -1) group = 0;
		ListDataItem ldi = ldm.getGroups().get(group).getItems().get(row);
		return RowFactory.createRowView(ldi);
	}

	@Override
	public int getRowCount(int group) {
		return ldm.getGroups().get(group).getItems().size();
	}

	@Override
	public int getRowCount() {
		return getRowCount(0);
	}

	@Override
	public IOSUIComponent getGroupHeader(int group) {
		String hdr = ldm.getGroups().get(group).getValueOfHeader();
		if(hdr == null) return null;
		IOSUILabel l = new IOSUILabel(hdr);
//		l.setForeground(Color.WHITE);
		return l;
	}
	@Override
	public IOSUIComponent getGroupFooter(int group) {
		String ftr = ldm.getGroups().get(group).getValueOfFooter();
		if(ftr == null) return null;
		IOSUILabel l = new IOSUILabel(ftr);
//		l.setForeground(Color.WHITE);
		return l;
	}
	@Override
	public int getGroupHeaderHeight(int group) {
		String hdr = ldm.getGroups().get(group).getValueOfHeader();
		return hdr == null ? 0 : 30;
	}
	@Override
	public int getGroupFooterHeight(int group) {
		String ftr = ldm.getGroups().get(group).getValueOfFooter();
		return ftr == null ? 0 : 20;
	}
	@Override
	public int getRowHeight(int row) {
		return 45;
	}
	@Override
	public int getRowHeight(int group, int row) {
		return 45;
	}

	@Override
	public void rowClicked(int group, int row) {
	}

}
