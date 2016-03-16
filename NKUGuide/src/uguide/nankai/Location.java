package uguide.nankai;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

import android.R.interpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import uguide.nankai.model.ActionItem;
import uguide.nankai.po.Poi;
import uguide.nankai.po.bus;
import uguide.nankai.view.TitlePopup;
import uguide.nankai.view.TitlePopup.OnItemOnClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡 
 */
public class Location extends Activity implements OnClickListener,
		OnItemOnClickListener {

	private LocationClient mLocClient;
	private MyLocationListenner myListener;
	private LocationMode mCurrentMode;
	private ImageButton imageButton;
	private ImageButton imageButton1;
	private TitlePopup titlePopup;
	private BitmapDescriptor mCurrentMarker;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button requestLocButton;
	private boolean isFirstLoc = true;
	
	private final Timer timer = new Timer(); 
	BitmapDescriptor bda;
	private Marker markerPlace;
	private OverlayOptions OO;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location);
		imageButton = (ImageButton) findViewById(R.id.imagebutton);
		imageButton1 = (ImageButton)findViewById(R.id.imagebutton1);
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText(R.string.mode_putong);
		
		bda = BitmapDescriptorFactory.fromResource(R.drawable.c11);
		
		//处理不同的视图，有正常，罗盘，以及跟随
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				//正常
				case NORMAL:
					requestLocButton.setText(R.string.mode_gensui);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				//罗盘
				case COMPASS:
					requestLocButton.setText(R.string.mode_putong);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				//跟随
				case FOLLOWING:
					requestLocButton.setText(R.string.mode_luopan);
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		//定位当前的位置
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000); //设置发起定位请求的间隔时间为1000ms  
		mLocClient.setLocOption(option);
		mLocClient.start();

		//添加正常，卫星，交通，热力图等功能
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, R.string.map_zhengchang));
		titlePopup.addAction(new ActionItem(this, R.string.map_weixing));
		titlePopup.addAction(new ActionItem(this, R.string.map_traffic));
		titlePopup.addAction(new ActionItem(this, R.string.map_heat));

		imageButton.setOnClickListener(this);
		imageButton1.setOnClickListener(this);
		titlePopup.setItemOnClickListener(this);
		//点击校车事件的监听
//		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//			
//			@Override
//			public boolean onMarkerClick(Marker arg0) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "响应了单击事件"+arg0.getAnchorX()+markerPlace.getPosition().latitude, Toast.LENGTH_SHORT).show();
//				new AlertDialog.Builder(Location.this).setTitle("是否导航？")
//				.setMessage("目标地址：(" + markerPlace.getPosition().longitude+","+markerPlace.getPosition().latitude+")")
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface arg0, int arg1) {
//						Intent intent2 = new Intent(Location.this,
//								RoutePage.class); 
//						intent2.putExtra("latitude",
//								markerPlace.getPosition().latitude);
//						intent2.putExtra("longitude",
//								markerPlace.getPosition().longitude);
//						intent2.putExtra("adress", "校车");
//						startActivity(intent2);
//					}
//
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//					public void onClick(DialogInterface arg0, int arg1) {
//
//					}
//
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				}).create().show();
//				return false;
//			}
//		});
	}

	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg); 
            List<bus> BusData = new ArrayList<bus>();
            BusData = (List<bus>)msg.obj;
            if(msg.obj!=null){
            	ShowSchoolCar(BusData);
            }
        }  
    };  
   
    //显示校车图标
    private void ShowSchoolCar(List<bus> busData){
    	mBaiduMap.clear();
    	for(int i=0;i<busData.size();i++){
    		Log.w(busData.get(i).getId(), "这是数据"+busData.get(i).getJingdu());
        	LatLng SCP = new LatLng(Double.parseDouble(busData.get(i).getWeidu()) ,Double.parseDouble(busData.get(i).getJingdu()));
        	OO = new MarkerOptions().position(SCP).icon(bda).zIndex(9).draggable(true);
//        	markerPlace = (Marker) mBaiduMap.addOverlay(OO);
        	mBaiduMap.addOverlay(OO);
    	}
    }
    
    TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub 
        	Thread thread = new Thread() {
    			public void run() {
    				List<bus> buses = new ArrayList<bus>();
    				buses = doRequestJSON("新开湖");
    				Message message = new Message();  
    		        message.what = 1;  
    		        message.obj = buses;
    		        handler.sendMessage(message);
    			};
    		};
        	thread.start(); 
        }  
    };
    //请求服务器
    private List<bus> doRequestJSON(String name) {
    	Poi tav = new Poi();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("option", "dingwei"));
		String uri = "http://121.42.43.36:8080/map_server/placeServlet";
		List<bus> Lbus = new ArrayList<bus>();
		// 创建连接对象
		HttpClient client = new DefaultHttpClient();
		// 创建请求对象
		HttpPost request = new HttpPost(uri);
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
				Type type = new TypeToken<List<bus>>(){}.getType();
				Lbus = gson.fromJson(strJson, type);
				
				//tav = gson.fromJson(strJson, Poi.class);
//				Log.w("获取到的信息为："+tav.getName(), "获取到的经纬度为："+tav.getJingdu()+tav.getWeidu());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Lbus;
	}
	//实时定位监听
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			//获取自己的定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}
	}
	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}
	//开始从数据库中检索数据	
	@Override
	protected void onDestroy() {
		timer.cancel(); // 关闭定时器处理
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;	
		super.onDestroy();
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imagebutton:
			titlePopup.show(view);
			break;
		case R.id.imagebutton1:
			timer.schedule(task, 3000,3000);
			break;
		default:
			break;
		}
	}
	//对按钮进行监听，分别对应着普通图，热力图，卫星图，交通图
	@Override
	public void onItemClick(ActionItem item, int position) {

		switch (position) {
		case 0:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setBaiduHeatMapEnabled(false);
			mBaiduMap.setTrafficEnabled(false);
			break;
		case 1:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			mBaiduMap.setBaiduHeatMapEnabled(false);
			mBaiduMap.setTrafficEnabled(false);
			break;
		case 2:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setBaiduHeatMapEnabled(false);
			mBaiduMap.setTrafficEnabled(true);
			break;
		case 3:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setTrafficEnabled(false);
			mBaiduMap.setBaiduHeatMapEnabled(true);
			break;
		}
//		timer.schedule(task, 3000, 3000);
	}
}
