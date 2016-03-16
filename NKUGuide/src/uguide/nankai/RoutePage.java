package uguide.nankai;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
//import org.lxh.demo.MySlidingDrawerDemo;
//import org.lxh.demo.R;
//import org.lxh.demo.MySlidingDrawerDemo.OnDrawerCloseListenerImpl;
//import org.lxh.demo.MySlidingDrawerDemo.OnDrawerOpenListenerImpl;
//import org.lxh.demo.MySlidingDrawerDemo.OnDrawerScrollListenerImpl;
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
import uguide.nankai.po.Poi;
import android.R.anim;
import android.R.bool;
import android.R.interpolator;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class RoutePage extends Activity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener, TextWatcher,
		OnGetSuggestionResultListener,OnClickListener {
	private boolean data_online=true;
	
	private boolean isFirstLoc = true;
	private ArrayAdapter<String> sugAdapter = null;
	private String city;// 当前城市名
	// private String street;// 当前街道名
	// private String district;// 当前县
	private LocationClient mLocClient;
	private MyLocationListenner myListener;
	// 浏览路线节点相关
	private Button mBtnPre = null;// 上一个节点
	private Button mBtnNext = null;// 下一个节点
	private Button bt_plan;
	private int nodeIndex = -1;// 节点索引,供浏览节点时使用
	@SuppressWarnings("rawtypes")
	private RouteLine route = null;
	@SuppressWarnings("unused")
	private OverlayManager routeOverlay = null;
	private boolean useDefaultIcon = false;
	private TextView popupText = null;// 泡泡view
	AutoCompleteTextView start;
	AutoCompleteTextView end;
	SuggestionSearch suggestionSearch;
	private LatLng latLng;// 当前经纬度信息
	public static int distance;
	
	private Handler handler;
	private ProgressDialog requestNetDialog;
	
	private SlidingDrawer mDrawer;
	private ImageView handle;
	private LinearLayout content;
	private boolean result = true;
	
	public double lon,lat;

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	private MapView mMapView = null; // 地图View
	private BaiduMap mBaidumap = null;
	private Marker mMarkerPlace;
	// 搜索相关
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	public LatLng desAdd = null;
	
	public Poi dBTavern ;
	public Poi dBTavern1;
	public List<Poi> datas;
	
	public View view;
	public String startPlace = "";
	public static ArrayList<String> arrayList;

	//标注当前位置
	BitmapDescriptor bdA;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route_page);
		mLocClient = new LocationClient(getApplicationContext());
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		start = (AutoCompleteTextView) findViewById(R.id.start);
		end = (AutoCompleteTextView) findViewById(R.id.end);
		this.mDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		mDrawer.animateOpen();
		this.handle = (ImageView) findViewById(R.id.handle);
		this.content = (LinearLayout) findViewById(R.id.content);
		this.mDrawer.setOnDrawerOpenListener(new OnDrawerOpenListenerImpl());
		this.mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListenerImpl()) ;	// 	
		this.mDrawer.setOnDrawerScrollListener(new OnDrawerScrollListenerImpl()) ;
		
		dealWithIntent();
		
		//连接数据库
		//requestNetDialog = new ProgressDialog(this);
		//requestNetDialog.setMessage("正在请求服务器……");
		
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(data_online){
					datas = (List<Poi>)msg.obj;
					if (msg.obj ==null){
						Toast.makeText(RoutePage.this, "调用外部搜索", Toast.LENGTH_SHORT).show();
						Route123(view, null);
					}
					else if (msg.obj!=null)
					{
						Toast.makeText(RoutePage.this, "调用数据库数据", Toast.LENGTH_SHORT).show();
						showDBDialog(datas);
					}
					//Toast.makeText(RoutePage.this, dBTavern.getJingdu()+"这是经度", Toast.LENGTH_SHORT).show();
				}
				else {
					Route123(view,dBTavern1);
				}
//				showDBDialog(datas);
			}
		};
		
		CharSequence titleLable = "路线规划";
		setTitle(titleLable);

		suggestionSearch = SuggestionSearch.newInstance();
		suggestionSearch.setOnGetSuggestionResultListener(this);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		end.setAdapter(sugAdapter);
		start.setAdapter(sugAdapter);

		// 初始化地图
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		mMapView.showZoomControls(false);
		// 定位至南开大学

		LatLng center = new LatLng(38.991828,117.355231);
		MapStatus mMapStatus = new MapStatus.Builder()
		.target(center).zoom(18).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaidumap.setMapStatus(mMapStatusUpdate);
		
		//标注起点的图标
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		
		//获取起始位置
			InitialMyplace(38.991828, 117.355231);
//			设置定位条件
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型为百度经纬度坐标系
			option.setScanSpan(1000);//设置扫描间隔
			option.setIsNeedAddress(true);
			mLocClient.setLocOption(option);//把地图移到当前的位置
			mLocClient.start();
			Log.w("黄鑫","1");
//			设置位置监听器
			mLocClient.registerLocationListener(new BDLocationListener() {
				@Override
				public void onReceiveLocation(BDLocation location) {
					if(location == null){
						return;
					}
					lon = location.getLongitude();//获取经度
					lat = location.getLatitude();//获取纬度
				}
			});
			

		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		bt_plan = (Button) findViewById(R.id.bt_plan);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		bt_plan.setVisibility(View.INVISIBLE);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		// 存储路线信息
		arrayList = new ArrayList<String>();
		end.addTextChangedListener(this);
		start.addTextChangedListener(this);   
	}
	
	//判断是否是由别的界面跳转
	private void dealWithIntent(){
		Intent intent = getIntent();
		{
			
		}

		double latitude = intent.getDoubleExtra("latitude", 0.0);
		double longitude = intent.getDoubleExtra("longitude", 0.0);
		String address = intent.getStringExtra("address");
//		Toast.makeText(getApplicationContext(),
//				latitude + " " + longitude + " " + adress, Toast.LENGTH_SHORT)
//				.show();
		
		end.setText(address);
		if(intent.hasExtra("address"))
			data_online=false;
		end.setEnabled(data_online);
		dBTavern1=new Poi();
		dBTavern1.setName(address);
		dBTavern1.setJingdu(longitude);
		dBTavern1.setWeidu(latitude);
	}
	
	//抽屉的监听事件
	private class OnDrawerOpenListenerImpl implements OnDrawerOpenListener {
		@Override
		public void onDrawerOpened() {
			handle.setImageResource(R.drawable.down) ;			
		}
	}
	private class OnDrawerCloseListenerImpl implements OnDrawerCloseListener {
		@Override
		public void onDrawerClosed() {
			handle.setImageResource(R.drawable.up) ;			
		}
	}
	private class OnDrawerScrollListenerImpl implements OnDrawerScrollListener {
		@Override
		public void onScrollEnded() {								
//			Toast.makeText(RoutePage.this, "2", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onScrollStarted() {								
//			Toast.makeText(RoutePage.this, "1", Toast.LENGTH_SHORT).show();
		}
	}

	//初始化位置信息，把相应的图标显示出来，也即是给指定的经纬度做标记
	public void InitialMyplace(double lat,double lon )
	{
		LatLng MyPlace = new LatLng(lat, lon);
		
		OverlayOptions oo = new MarkerOptions().position(MyPlace).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerPlace = (Marker)(mBaidumap.addOverlay(oo));
	}
	
	//响应搜索按钮，对应着三种示例（驾车，公交，步行）
	public void SearchButtonProcess(View v) {
		view = v;
		String string = end.getText().toString().trim();
		Log.w("此时按钮是", string);
		startPlace = start.getText().toString();
		CheckIfDataBase(string);
	}
	
	public void Route123(View v,Poi tav1){
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		mBaidumap.clear();
		PlanNode stNode = null;
		if (startPlace.trim().equals("我的位置")||startPlace.trim().equals("")) {
			stNode = PlanNode.withLocation(latLng);//此时latLng存的应该是现在我的位置的信息
		} else {
			stNode = PlanNode.withCityNameAndPlaceName(city, startPlace.trim());
		}
		PlanNode enNode = null;
		if(tav1 != null)//判断查找数据库是否有数据,括号里判断是否能找到对应string的结果 latLng12!=null
		{
			//说明转换数据时没转换成功
//			showDBDialog();
			enNode = PlanNode.withLocation(new LatLng(tav1.getWeidu(), tav1.getJingdu()));
		}
		else {
			//调用百度的方法
			enNode = PlanNode.withCityNameAndPlaceName(city, end.getText()
					.toString().trim());	
		}
		// 实际使用中请对起点终点城市进行正确的设定
				if (v.getId() == R.id.drive) {
					mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
							.to(enNode));
				} else if (v.getId() == R.id.transit) {
					mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
							.city(city).to(enNode));
				} else if (v.getId() == R.id.walk) {
					mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
							.to(enNode));
				}
	}
	
	//判断数据库中是否有数据
	private void CheckIfDataBase(final String string) {
		new Thread() {
			public void run() {
				List<Poi> tavs = new ArrayList<Poi>(); 
				tavs = doRequestJSON(string);
				Message msg = new Message();
				msg.obj = tavs;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	//访问数据库获取经纬度信息
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
				if(strJson.equals("[]")){
					return null;
				}
				else {
					Gson gson = new Gson();
					Type type = new TypeToken<List<Poi>>(){}.getType();
					//tavern tav = gson.fromJson(strJson,tavern.class);
					Ltav = gson.fromJson(strJson, type);
					Log.w("此时从数据库中取出的数据为", Ltav.get(0).getName()+Ltav.get(0).getJingdu());
					if(Ltav.isEmpty()){
						return null;
					}
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
	
	//显示搜索框的内容（数据库中的数据）
	private void showDBDialog(final List<Poi> lists) {
		int n = lists.size();//n为所遍历到的结果的个数
		final Poi[] array = new Poi[n];//
		final String[] names = new String[n];
		for(int i =0;i<lists.size();i++){
//			Log.w(lists.get(i).getName(), lists.get(i).getJingdu()+lists.get(i).getWeidu()+"这是数据");
			names[i] = lists.get(i).getName();
		}
		//对其进行初始化
		AlertDialog.Builder builder = new AlertDialog.Builder(RoutePage.this);
		builder.setTitle("请选择");
		builder.setItems(names, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(RoutePage.this, "选择的地方为：" + names[which], Toast.LENGTH_SHORT).show();
				Poi selectPoi = new Poi();
				selectPoi.setJingdu(lists.get(which).getJingdu());
				selectPoi.setWeidu(lists.get(which).getWeidu());
				selectPoi.setName(names[which]);
				end.setText(names[which]);
				Route123(view, selectPoi);
//				Log.w(lists.get(which).getName(), lists.get(which).getJingdu()+lists.get(which).getWeidu()+"这是数据");
			}
		});
		builder.show();
	}
	//弹出消息对话框来判断输入的哪一个
	private void showDialog (){
		AlertDialog.Builder builder = new AlertDialog.Builder(RoutePage.this);
		builder.setTitle("请选择");
		//    指定下拉列表的显示数据
		final String[] cities = {"广州", "上海", "北京", "香港", "澳门"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(RoutePage.this, "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
	}
	
	//节点浏览示例
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}

		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// 设置节点索引
		if (v.getId() == R.id.next) {
			if (nodeIndex < route.getAllStep().size() - 1) {
				nodeIndex++;
			} else {
				return;
			}
		} else if (v.getId() == R.id.pre) {
			if (nodeIndex > 0) {
				nodeIndex--;
			} else {
				return;
			}
		}

		// 获取节结果信息
		LatLng nodeLocation = null;
		String nodeTitle = null;

		Object step = route.getAllStep().get(nodeIndex);
		if (step instanceof DrivingRouteLine.DrivingStep) {
			nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace()
					.getLocation();// 节点经纬度
			nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();// 节点行驶路线
		} else if (step instanceof WalkingRouteLine.WalkingStep) {
			nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
		} else if (step instanceof TransitRouteLine.TransitStep) {
			nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
		}

		if (nodeLocation == null || nodeTitle == null) {
			return;
		}

		// 移动节点至中心
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(RoutePage.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

	}
	//获取沿途路径信息 
	public void routeinfo(View view) {
		arrayList.removeAll(arrayList);// 清空之前路线信息
		for (int i = 0; i < route.getAllStep().size(); i++) {
			String nodeTitle = null;
			Object step = route.getAllStep().get(i);
			distance = route.getDistance();
			if (step instanceof DrivingRouteLine.DrivingStep) {
				nodeTitle = ((DrivingRouteLine.DrivingStep) step)
						.getInstructions();// 节点行驶路线信息
			} else if (step instanceof WalkingRouteLine.WalkingStep) {
				nodeTitle = ((WalkingRouteLine.WalkingStep) step)
						.getInstructions();
			} else if (step instanceof TransitRouteLine.TransitStep) {
				nodeTitle = ((TransitRouteLine.TransitStep) step)
						.getInstructions();
			}
			arrayList.add(nodeTitle);
		}
		Intent intent = new Intent(RoutePage.this, RoutePageDetail.class);
		startActivity(intent);
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePage.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			bt_plan.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
//
	}
	//控制抽屉的节点操作
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.handle:
			if(result == true){
				mDrawer.animateOpen();
//				content.setVisibility(1);
			}
			else {
				mDrawer.animateClose();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePage.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePage.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息

			List<CityInfo> list = result.getSuggestAddrInfo()
					.getSuggestEndCity();

			Log.i("info", list.toString());
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			bt_plan.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	//定位函数
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			city = location.getCity();//
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaidumap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
				mBaidumap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mMapView.onDestroy();
		suggestionSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		if (cs.length() <= 0) {
			return;
		}
		String cityname = city;
		suggestionSearch.requestSuggestion((new SuggestionSearchOption())
				.keyword(cs.toString()).city(cityname));

	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();

		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null) {
				sugAdapter.add(info.key);

			}
		}
		sugAdapter.notifyDataSetChanged();

	}

}
