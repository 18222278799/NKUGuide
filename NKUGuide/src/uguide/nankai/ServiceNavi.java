package uguide.nankai;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import uguide.nankai.po.Poi;
import uguide.nankai.util.GridViewAdapter;
import uguide.nankai.util.MyDataFiller;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ServiceNavi extends Activity {
	private GridView gridview;
	private String choice = "";
	private Handler handler;
	private Poi poi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_service_navi);
		
		gridview=(GridView) findViewById(R.id.gv_service_navi);
		gridview.setAdapter(new GridViewAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showSubList(position);
			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				poi = (Poi)msg.obj;
				if (msg.obj ==null){
					Toast.makeText(ServiceNavi.this, "此时数据无法直接加载", Toast.LENGTH_SHORT).show();
//					Route123(view, null);
				}
				else if (msg.obj!=null){
					Toast.makeText(ServiceNavi.this, "数据加载成功", Toast.LENGTH_SHORT).show();
				GoToRoute(poi);}
			}
		};
	}
	
	private void GoToRoute(Poi p){
		Intent intent = new Intent(ServiceNavi.this,
				RoutePage.class); 
		intent.putExtra("latitude",
				p.getWeidu());
		intent.putExtra("longitude",
				p.getJingdu());
		intent.putExtra("address", choice);
		startActivity(intent);
	}

	
	private void showSubList(final int index){
		final List<List<String>> str=MyDataFiller.fillServiceList();
		AlertDialog dia = new AlertDialog.Builder(ServiceNavi.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setSingleChoiceItems(str.get(index).toArray(new String[str.get(index).size()]), -1, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						choice=str.get(index).get(which);
					}
				})
				.setPositiveButton("搜索", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(!choice.equals("")){
							System.out.println(choice);
							SendPoiMsg(choice);
						}else {
							Toast.makeText(getApplicationContext(), "您未选中任何选项", Toast.LENGTH_SHORT).show();
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();
		dia.show();
	}
	
	private void SendPoiMsg(final String str){
		new Thread() {
			@Override
			public void run() {
				Poi p = new Poi(); 
				List<Poi> lists = new ArrayList<Poi>();
				lists = doRequestJSON(str);
				p = lists.get(0);
				Message msg = new Message();
				msg.obj = p;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	private List<Poi> doRequestJSON(String name) {
		Log.w("在此时字符串为",name );
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("option", "daohang"));
		String uri = "http://121.42.43.36:8080/map_server/placeServlet";
		// 创建连接对象
		HttpClient client = new DefaultHttpClient();
		// 创建请求对象
		HttpPost request = new HttpPost(uri);
		Poi tav = new Poi();
		List<Poi> Ltav = new ArrayList<Poi>();
		try {
			// 给请求设置参数
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 发送请求
			HttpResponse response = client.execute(request);
			// 判断请求状态 200
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 提取响应内容
				String strJson = EntityUtils.toString(response.getEntity(), "utf-8");
				Gson gson = new Gson();
				Type type = new TypeToken<List<Poi>>(){}.getType();
				//tavern tav = gson.fromJson(strJson,tavern.class);
				Ltav = gson.fromJson(strJson, type);
				Log.w("此时从数据库中取出的数据为", Ltav.get(0).getName()+Ltav.get(0).getJingdu());
				if(Ltav.isEmpty()){
					return null;
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(-1);
		}
		return Ltav;
	}
	
}
