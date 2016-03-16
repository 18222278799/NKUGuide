package uguide.nankai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import java.util.Map;









import uguide.nankai.po.Scenery;
import uguide.nankai.util.MyDataFiller;
import uguide.nankai.util.Util;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TextView;

public class SceneryList extends Activity {
	TabHost tabhost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scenery_list);
		
		tabhost=(TabHost)findViewById(R.id.th_scenery);
		initTabHost(tabhost);
	}
	
	private void initTabHost(TabHost tabhost){
		tabhost.setup();
		tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("南开校区")
				.setContent(R.id.tab1));
		tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("津南校区")
				.setContent(R.id.tab2));
		tabhost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_widget_bg);
		tabhost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_widget_bg);
		final List<Scenery> data=MyDataFiller.fillSceneryData();
		
		List<Map<String, Object>> data1=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> data2=new ArrayList<Map<String, Object>>();
		for (int i = 0; i < data.size(); i++) {
			Scenery sc = data.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", i + "");
			map.put("name", sc.getName());
			map.put("detail", sc.getDetail());
			Bitmap bm=Util.decodeSampledBitmapFromResource(getResources(), sc.getPic(), 100, 100);
			map.put("pic", bm);
			if (sc.getRegion() == Scenery.JINNAN) {
				data2.add(map);
			} else {
				data1.add(map);
			}
		}
		
		ListView lv1=(ListView)findViewById(R.id.lv_tab1);
		ListView lv2=(ListView)findViewById(R.id.lv_tab2);
		OnItemClickListener l=new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = Integer
						.parseInt((String) ((TextView) ((ViewGroup) view)
								.getChildAt(0)).getText());
				Scenery value = data.get(index);
				Intent intent = new Intent();
				intent.putExtra("theItem", value);
				intent.setClass(SceneryList.this, SceneryListItem.class);
				startActivity(intent);
			}
		};
		lv1.setOnItemClickListener(l);
		lv2.setOnItemClickListener(l);
		SimpleAdapter sa1=new SimpleAdapter(this, data1,
				R.layout.scenery_list_item, new String[] { "id", "name",
				"detail", "pic" }, new int[] { R.id.SLI_tvId,
				R.id.SLI_tvName, R.id.SLI_tvDetail, R.id.SLI_ivPic });
		SimpleAdapter sa2=new SimpleAdapter(this, data2,
				R.layout.scenery_list_item, new String[] { "id", "name",
				"detail", "pic" }, new int[] { R.id.SLI_tvId,
				R.id.SLI_tvName, R.id.SLI_tvDetail, R.id.SLI_ivPic });
		ViewBinder vb=new ViewBinder() {			
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if(view instanceof ImageView  && data instanceof Bitmap){    
                    ImageView iv = (ImageView) view;    
                    iv.setImageBitmap((Bitmap) data);    
                    return true;    
                }else return false;
			}
		};
		sa1.setViewBinder(vb);
		sa2.setViewBinder(vb);
		lv1.setAdapter(sa1);
		lv2.setAdapter(sa2);
	}
}
