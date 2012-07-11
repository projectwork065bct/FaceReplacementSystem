package hu.droidzone.iosui.list;

import hu.droidzone.iosui.IOSUIComponent;

import java.util.List;

public interface IOSUIListViewModel {
	ListKind getListKind();
	int getGroupCount();
	IOSUIComponent getRow(int row);
	IOSUIComponent getRow(int group, int row);
	int getRowCount(int group);
	int getRowCount();
	int getRowHeight(int row);
	int getRowHeight(int group, int row);
	IOSUIComponent getGroupHeader(int group);
	IOSUIComponent getGroupFooter(int group);
	int getGroupHeaderHeight(int group);
	int getGroupFooterHeight(int group);
	void rowClicked(int group, int row);
}
