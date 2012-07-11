package hu.droidzone.iosui.list;

import java.util.List;

public interface ListDataGroup {
	String getValueOfHeader();
	String getValueOfFooter();
	List<ListDataItem>getItems();
}
