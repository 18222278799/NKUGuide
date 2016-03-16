package uguide.nankai;



import com.baidu.mapapi.SDKInitializer;

import uguide.nankai.view.CircleLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Start extends Activity implements
		CircleLayout.OnItemClickListener, CircleLayout.OnItemSelectedListener {
	private CircleLayout circleLayout;
	private TextView navi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.activity_start);

		navi = (TextView) findViewById(R.id.tv_navi);
		circleLayout = (CircleLayout) findViewById(R.id._cl_main);
		circleLayout.setOnItemClickListener(this);
		circleLayout.setOnItemSelectedListener(this);
		
	}
	
	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		Animation zoomin = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.zoom_in);

		switch (position) {
		// 定位
		case 0:
			navi.setText(R.string.entry_location);
			navi.startAnimation(zoomin);
			break;
		// 关于我们
		case 1:
			navi.setText(R.string.entry_about);
			navi.startAnimation(zoomin);
			break;
		// 生活服务
		case 2:
			navi.setText(R.string.entry_service);
			navi.startAnimation(zoomin);
			break;
		// 景点推荐
		case 3:
			navi.setText(R.string.entry_scenery);
			navi.startAnimation(zoomin);
			break;
		// 路径规划
		case 4:
			navi.setText(R.string.entry_route);
			navi.startAnimation(zoomin);
			break;
		}
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		Intent intent;

		switch (position) {
		// 定位
		case 0:
			 intent = new Intent(Start.this, Location.class);
			 startActivity(intent);
			break;
		// 关于我们
		case 1:
			View view2 = LayoutInflater.from(Start.this).inflate(
					R.layout.about_us, null);
			AlertDialog dia = new AlertDialog.Builder(Start.this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setView(view2)
					.create();
			dia.show();
			WindowManager.LayoutParams lp = dia.getWindow().getAttributes();
			lp.dimAmount = 0.1f;
			lp.alpha = 0.95f;
			dia.getWindow().setAttributes(lp);
			break;
		// 生活服务
		case 2:
			 intent = new Intent(Start.this, ServiceNavi.class);
			 startActivity(intent);
			break;
		// 景点推荐
		case 3:
			 intent = new Intent(Start.this, SceneryList.class);
			 startActivity(intent);
			break;
		// 路径规划
		case 4:
			 intent = new Intent(Start.this, RoutePage.class);
			 startActivity(intent);
			break;
		}
	}
}
