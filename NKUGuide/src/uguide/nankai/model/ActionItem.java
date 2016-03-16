package uguide.nankai.model;

import android.content.Context;

public class ActionItem {
	public CharSequence mTitle;

	public ActionItem(CharSequence title) {
		this.mTitle = title;
	}

	public ActionItem(Context context, int titleId) {
		this.mTitle = context.getResources().getText(titleId);
	}

	public ActionItem(Context context, CharSequence title) {
		this.mTitle = title;
	}

}
