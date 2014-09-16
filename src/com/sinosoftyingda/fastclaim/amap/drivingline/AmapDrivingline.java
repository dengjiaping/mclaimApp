package com.sinosoftyingda.fastclaim.amap.drivingline;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.amap.mapapi.map.RouteMessageHandler;
import com.amap.mapapi.map.RouteOverlay;
import com.amap.mapapi.offlinemap.City;
import com.amap.mapapi.offlinemap.OfflineMapManager;
import com.amap.mapapi.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
import com.amap.mapapi.route.Route;
import com.sinosoftyingda.fastclaim.amap.util.AmapUtils;
import com.sinosoftyingda.fastclaim.amap.util.Constants;

/**
 * 导航地图
 * 
 * @author DengGuang
 * 
 *         目前支持实时路况的城市有：北京 上海 广州 深圳 成都 南京 沈阳 武汉 宁波 重庆 青岛 杭州
 *         实时路况的图符块不会被缓存，且每5分钟左右更新
 */
public class AmapDrivingline extends MapActivity implements RouteMessageHandler, OfflineMapDownloadListener {

	private MapView mMapView;
	
	private MapController mMapController;
		
	private GeoPoint currentPoint, startPoint, endPoint;

	private Location currentLocation;

	private RouteOverlay routeOverlay;

	private MyLocationOverlay myLocationOverlay;

	private ProgressDialog progDialog;
	private List<Route> routeResult; // 道路信息
	
	private Intent intent;

	private ListView lvRouteInfo;
	
	private OfflineMapManager offlineMapManager;
	
	private List<City> offlineCityList, downloadingCityList;

	private ImageView ivRouteInfo;
	private ImageView ivDaohangInfo;	// 导航按钮
	
	private Address start, end;
	
	private Button btnHome;
	private TextView tvTitle;
	
	private String latitude, longitude; 
	
	@Override
	/**
	 *显示栅格地图，启用内置缩放控件，并用MapController控制地图的中心点及Zoom级别
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.amap_drivingline);

		offlineMapManager = new OfflineMapManager(this, this);
		/*
		mMapView = (MapView) findViewById(R.id.traffic_mapView);
		myLocationOverlay = new MyLocationOverlay(this, mMapView);
		ivRouteInfo = (ImageView)findViewById(R.id.amap_drivingline_imgbtn_routeinfo);
		ivDaohangInfo = (ImageView)findViewById(R.id.amap_drivingline_imgbtn_daohang);
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController = mMapView.getController();
		intent = getIntent();
		btnHome = (Button)findViewById(R.id.btn_home);
		tvTitle = (TextView)findViewById(R.id.tv_title);
		
		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		tvTitle.setText(R.string.nav_route);
		mMapView.setBuiltInZoomControls(false); // 启用内置的缩放
		mMapView.setTraffic(false);
		mMapView.getOverlays().add(myLocationOverlay);
		mMapController.setZoom(12); // 设置地图zoom级别
		
		//终止位置
		latitude = intent.getStringExtra("Latitude");
		longitude = intent.getStringExtra("Longitude");
		if(latitude!=null&&longitude!=null&&!latitude.equals("")&&!longitude.equals("")){
			double lat = Double.parseDouble(intent.getStringExtra("Latitude"));
			double lng = Double.parseDouble(intent.getStringExtra("Longitude"));
			endPoint = new GeoPoint((int)(lat*1E6), (int)(lng*1E6));
		}else{
			endPoint = null;
		}
		// 地图路线
		ivRouteInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(routeResult==null||routeResult.size()<=0){
					Toast.makeText(AmapDrivingline.this, "没有路线信息", Toast.LENGTH_SHORT).show();
				}else{
					progDialog = ProgressDialog.show(AmapDrivingline.this, null, "正在获取路线信息", true, true);
					new Thread(){
						@Override
						public void run() {
							super.run();
							try {
								start = AmapUtils.getAddress(AmapDrivingline.this, currentLocation);
								end = AmapUtils.getAddress(AmapDrivingline.this, endPoint);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.what = Constants.POISEARCH;
							address.sendMessage(msg);
						}
					}.start();
				}
			}
		});
		// 导航功能
		ivDaohangInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					connectMap(latitude, longitude);
				} catch (Exception e) {
					Toast.makeText(AmapDrivingline.this, "未安装高德导航软件！", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			
			}
		});
		
		myLocationOverlay.runOnFirstFix(new Runnable() {//此线程会一直循环
			@Override
			public void run() {
            	handler.sendMessage(Message.obtain(handler, Constants.FIRST_LOCATION));
			}
		});
		*/
		
		//终止位置
		intent = getIntent();
		latitude = intent.getStringExtra("Latitude");
		longitude = intent.getStringExtra("Longitude");
		if(latitude!=null&&longitude!=null&&!latitude.equals("")&&!longitude.equals("")){
			double lat = Double.parseDouble(intent.getStringExtra("Latitude"));
			double lng = Double.parseDouble(intent.getStringExtra("Longitude"));
			endPoint = new GeoPoint((int)(lat*1E6), (int)(lng*1E6));
		}else{
			endPoint = null;
		}
		
		
		try {
			connectMap(latitude, longitude);
		} catch (Exception e) {
			Toast.makeText(AmapDrivingline.this, "未安装高德导航软件！", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}
	
	/**
     * 调用高德地图apk
     */
    private void connectMap(String latStr, String lonStr){
    	Intent intent = new Intent("android.intent.action.VIEW",  
    	android.net.Uri.parse("androidamap://navi?sourceApplication=sinaweibo&poiname=fangheng&poiid=BGVIS&lat="+latStr+"&lon="+lonStr+"&dev=1&style=2"));  
    	intent.setPackage("com.autonavi.minimap");  
		startActivity(intent);  
    }
    
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if(progDialog!=null){
//			progDialog.dismiss();
//			progDialog.cancel();
//		}
	}
		
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.FIRST_LOCATION) {
				currentPoint = myLocationOverlay.getMyLocation();
				currentLocation = myLocationOverlay.getLastFix();
				startPoint = currentPoint;
				mMapController.setCenter(currentPoint);// 设置地图中心
				mMapController.animateTo(currentPoint);
				if(endPoint!=null){
					searchRouteResult(startPoint, endPoint);
				}
			}
		}
    };
    
    private Handler address = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.POISEARCH) {
				progDialog.dismiss();
				Route route = routeResult.get(0);
				ArrayList<String> list = AmapUtils.getRouteInfo(route);
				Intent intent = new Intent();
				if(start!=null){
					intent.putExtra("StartPlace", start.getThoroughfare());
				}else{
					intent.putExtra("StartPlace", route.getStartPlace());
				}
				if(end!=null){
					intent.putExtra("TargetPlace", end.getThoroughfare());
				}else{
					intent.putExtra("TargetPlace", route.getTargetPlace());
				}
				intent.putStringArrayListExtra("routeinfos", list);
				intent.setClass(AmapDrivingline.this, RouteInfoActivity.class);
				startActivity(intent);
			}
		}
    };
	
	@Override
	protected void onResume() {
		super.onResume();
//		myLocationOverlay.enableMyLocation();
//		myLocationOverlay.enableCompass();
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		myLocationOverlay.disableMyLocation();
//		myLocationOverlay.disableMyLocation();
	}
	
	
	/**
	 * 查找导航路线
	 * 
	 * @param startPoint
	 * @param endPoint
	 */
	public void searchRouteResult(GeoPoint startPoint, GeoPoint endPoint) {
		progDialog = ProgressDialog.show(AmapDrivingline.this, null, "正在获取线路", true, true);
		final Route.FromAndTo fromAndTo = new Route.FromAndTo(startPoint, endPoint);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					routeResult = Route.calculateRoute(AmapDrivingline.this, fromAndTo, Route.DrivingDefault);
						if (routeResult != null || routeResult.size() > 0){
							routeHandler.sendMessage(Message.obtain(routeHandler, Constants.ROUTE_SEARCH_RESULT));
						}
				} catch (AMapException e) {
					Message msg = new Message();
					msg.what = Constants.ROUTE_SEARCH_ERROR;
					msg.obj = e.getErrorMessage();
					routeHandler.sendMessage(msg);
				}
			}
		});
		t.start();

	}

	private Handler routeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.ROUTE_SEARCH_RESULT) {
				progDialog.dismiss();
				if (routeResult != null && routeResult.size() > 0) {
					Route route = routeResult.get(0);
					if (route != null) {
						if (routeOverlay != null) {
							routeOverlay.removeFromMap(mMapView);
						}
						routeOverlay = new RouteOverlay(AmapDrivingline.this, route);
						routeOverlay.registerRouteMessage(AmapDrivingline.this); // 注册消息处理函数
						routeOverlay.enableDrag(false);//关闭起点终点拖动功能
						Paint paint = new Paint();
						paint.setAntiAlias(true);
						paint.setColor(Color.BLUE);
						paint.setStrokeMiter(5.0f);
						paint.setStrokeWidth(10.0f);
						routeOverlay.setCarLinePaint(paint);
						routeOverlay.addToMap(mMapView); //
						ArrayList<GeoPoint> pts = new ArrayList<GeoPoint>();
						pts.add(route.getLowerLeftPoint());
						pts.add(route.getUpperRightPoint());
						mMapView.getController().setFitView(pts);// 调整地图显示范围
						mMapView.invalidate();
					}
				}
			} else if (msg.what == Constants.ROUTE_SEARCH_ERROR) {
				progDialog.dismiss();
				showToast((String) msg.obj);
			}
		}
	};

	public void showToast(String showString) {
		Toast.makeText(getApplicationContext(), showString, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDrag(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {

	}

	@Override
	public void onDragBegin(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {

	}

	@Override
	public void onDragEnd(MapView mapView, RouteOverlay overlay, int index, GeoPoint pos) {
		try {
			startPoint = overlay.getStartPos();
			endPoint = overlay.getEndPos();
			searchRouteResult(startPoint, endPoint);
		} catch (IllegalArgumentException e) {
			routeOverlay.restoreOverlay(mMapView);
			overlayToBack(routeOverlay, mMapView);
		} catch (Exception e1) {
			overlay.restoreOverlay(mMapView);
			overlayToBack(routeOverlay, mMapView);
		}
	}

	private void overlayToBack(RouteOverlay overlay, MapView mapView) {
		startPoint = overlay.getStartPos();
		endPoint = overlay.getEndPos();
	}

	@Override
	public boolean onRouteEvent(MapView arg0, RouteOverlay arg1, int arg2, int arg3) {
		return false;
	}	

	@Override
	public void onDownload(int status, int progress) {
		
	}
}
