package uguide.nankai;


import uguide.nankai.po.Scenery;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SceneryListItem extends Activity {
	Scenery scenery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scenery_list_item);
		
		Intent intent = getIntent();
		scenery = (Scenery) intent.getSerializableExtra("theItem");
		ImageView ivPic = (ImageView) findViewById(R.id.ASI_ivPic);
		Button gothere=(Button)findViewById(R.id.btn_gothere);
		gothere.setEnabled(scenery.hasLocation);
		ivPic.setImageResource(scenery.getPic());
		ivPic.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) this.getWindowManager().getDefaultDisplay().getWidth()
						/ getResources().getDrawable(scenery.getPic())
								.getIntrinsicWidth() * getResources()
						.getDrawable(scenery.getPic()).getMinimumHeight())));
		((TextView) findViewById(R.id.ASI_tvName)).setText(scenery.getName());
		((TextView) findViewById(R.id.ASI_tvDetail)).setText(scenery.getDetail());
	}
	public void gothereOnClick(View view){
		Intent intent = new Intent();
		intent.putExtra("latitude",
				scenery.getLatitude());
		intent.putExtra("longitude",
				scenery.getLongitude());
		intent.putExtra("address", scenery.getName());
		intent.setClass(SceneryListItem.this, RoutePage.class);
		startActivity(intent);
	}
}
