package hu.droidzone.iosui.list;

public interface ListDataItem {
	Object getValueOfField(RowFieldKind field);
	void setValueOfField(RowFieldKind field, Object newValue);
	RowFieldKind[][] getFields();
}
