package uguide.nankai.util;

import java.util.ArrayList;

import uguide.nankai.R;
import uguide.nankai.model.ActionItem;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Listadapter extends BaseAdapter {
	Context context;
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();


	public Listadapter(Context context, ArrayList<ActionItem> mActionItems) {
		this.context = context;
		this.mActionItems = mActionItems;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.listviewitem,
					null);
		}
		
		TextView textView = (TextView) view.findViewById(R.id.item_tv);
		ActionItem item = mActionItems.get(position);
		textView.setTextSize(14);
		// 设置文本居中
		textView.setGravity(Gravity.CENTER);
		// 设置文本域的范围
		textView.setPadding(0, 10, 0, 10);
		// 设置文本在一行内显示（不换行）
		textView.setSingleLine(true);
		// 设置文本文字
		textView.setText(item.mTitle);
		// 设置文字与图标的间隔
		textView.setCompoundDrawablePadding(10);
		return view;
	}

	@Override
	public int getCount() {
		return mActionItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mActionItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
