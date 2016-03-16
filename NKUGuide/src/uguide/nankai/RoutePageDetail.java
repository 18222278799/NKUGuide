package uguide.nankai;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RoutePageDetail extends Activity {
	private ListView listView;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route_page_detail);
		listView = (ListView) findViewById(R.id.rountlist);
		textView=(TextView) findViewById(R.id.rountinfo_tv);

		if (RoutePage.distance<1000) {
			textView.setText("总计"+RoutePage.distance+"m");
		}else {
			textView.setText("总计"+(((float)(RoutePage.distance))/1000)+"km");
		}
		listView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View view, ViewGroup arg2) {
				TextView tv = new TextView(RoutePageDetail.this);
				String string = RoutePage.arrayList.get(position);
				tv.setText(string);
				view=tv;
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return RoutePage.arrayList.get(position);
			}

			@Override
			public int getCount() {
				return RoutePage.arrayList.size();
			}
		});
	}

}
