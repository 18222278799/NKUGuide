package uguide.nankai.util;

import uguide.nankai.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter implements ListAdapter {
	private int imgRecouse[];

	private String title[];

	// -------------------

	LayoutInflater inflater;

	Context context;

	public GridViewAdapter(Context context) {

		this.context = context;

		inflater = LayoutInflater.from(context);

		imgRecouse = new int[] { R.drawable.repast, R.drawable.shopping, R.drawable.bank,
				R.drawable.sport, R.drawable.stay, R.drawable.life, R.drawable.study,
				R.drawable.service };
		//, R.drawable.car_service,R.drawable.government

		title = new String[] { "餐饮", "购物", "ATM", "体育", "宿舍", "生活",
				"学习", "物业" };
//		,"汽车服务","政府机关"
	}


	@Override
	public int getCount() {
		return imgRecouse.length;
	}

	@Override
	public Object getItem(int position) {
		return imgRecouse[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv=new ImageView(context);
		iv.setImageResource(imgRecouse[position]);
		iv.setMinimumHeight(Util.px2dp(context, 125));
		convertView=iv;
		return convertView;
	}

}
