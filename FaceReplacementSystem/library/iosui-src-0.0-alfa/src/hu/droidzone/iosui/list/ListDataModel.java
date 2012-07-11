package hu.droidzone.iosui.list;

import java.util.List;

public interface ListDataModel {
	public static RowFieldKind[][]ICON_TITLE_SWITCH = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.Switch}};
	public static RowFieldKind[][]ICON_TITLE_VALUE_SWITCH = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.Value},{RowFieldKind.Switch}};
	public static RowFieldKind[][]ICON_TITLE_DETAIL_VALUE_SWITCH = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title,RowFieldKind.Detail},{RowFieldKind.Value},{RowFieldKind.Switch}};

	public static RowFieldKind[][]ICON_TITLE_MORE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.More}};
	public static RowFieldKind[][]ICON_TITLE_VALUE_MORE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.Value},{RowFieldKind.More}};
	public static RowFieldKind[][]ICON_TITLE_DETAIL_VALUE_MORE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title,RowFieldKind.Detail},{RowFieldKind.Empty},{RowFieldKind.Value},{RowFieldKind.More}};
	public static RowFieldKind[][]TITLE_SWITCH = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.Switch}};
	public static RowFieldKind[][]TITLE_MORE = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.More}};
	public static RowFieldKind[][]TITLE_CHECK = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.Check}};
	public static RowFieldKind[][]TITLE_DETAIL_CHECK = new RowFieldKind[][] {{RowFieldKind.Title, RowFieldKind.Detail},{RowFieldKind.Empty},{RowFieldKind.Check}};
	public static RowFieldKind[][]TITLE_VALUE_MORE = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.Value},{RowFieldKind.More}};
	public static RowFieldKind[][]TITLE_VALUE_CHECK = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.Value},{RowFieldKind.Check}};
	public static RowFieldKind[][]TITLE_EDITABLE_VALUE = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.EditableValue}};
	public static RowFieldKind[][]TITLE_PASSWORD_VALUE = new RowFieldKind[][] {{RowFieldKind.Title},{RowFieldKind.PasswordValue}};
	
	public static RowFieldKind[][]ICON_TITLE_CHECK = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.Check}};
	public static RowFieldKind[][]ICON_TITLE_DETAIL_CHECK = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title, RowFieldKind.Detail},{RowFieldKind.Empty},{RowFieldKind.Check}};
	public static RowFieldKind[][]ICON_TITLE_VALUE_CHECK = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.Empty},{RowFieldKind.Value},{RowFieldKind.Check}};
	public static RowFieldKind[][]ICON_TITLE_DETAIL_VALUE_CHECK = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title,RowFieldKind.Detail},{RowFieldKind.Empty},{RowFieldKind.Value},{RowFieldKind.Check}};

	public static RowFieldKind[][]ICON_TITLE_EDITABLE_VALUE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.EditableValue}};
	public static RowFieldKind[][]ICON_EDITABLE_VALUE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.EditableValue}};
	public static RowFieldKind[][]ICON_TITLE_PASSWORD_VALUE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.Title},{RowFieldKind.PasswordValue}};
	public static RowFieldKind[][]ICON_PASSWORD_VALUE = new RowFieldKind[][] {{RowFieldKind.Icon}, {RowFieldKind.PasswordValue}};
	
	List<ListDataGroup>getGroups();
}
