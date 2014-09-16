package com.sinosoftyingda.fastclaim.amap.util;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.location.LocationManagerProxy;
import com.amap.mapapi.route.Route;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblGPSAddress;
import com.sinosoftyingda.fastclaim.common.definetime.TimeService;
import com.sinosoftyingda.fastclaim.common.model.GPSUploadResponse;
import com.sinosoftyingda.fastclaim.common.model.GpsUploadRequest;
import com.sinosoftyingda.fastclaim.common.service.GpsUploadHttpService;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * 经度Longitude、纬度Latitude
 * 
 * @author JingTuo
 * 
 */
public class AmapUtils {

	private static LocationManagerProxy	locationManagerProxy;
	private static String				locationProvider;
	private static LocationListener		locationListener;
	private static Context				amapContext;

	public static final int				GPS_NO_OPEN	= 0;

	/**
	 * 初始化位置代理、定位提供器
	 * 
	 * @param context
	 */
	private synchronized static void init(Context context) {
		if (context != null) {
			amapContext = context;
			locationManagerProxy = LocationManagerProxy.getInstance(amapContext);
		}

		if (locationManagerProxy != null && locationProvider == null) {
			if (locationManagerProxy.isProviderEnabled(LocationManagerProxy.GPS_PROVIDER)) {
				locationProvider = LocationManagerProxy.GPS_PROVIDER;
			} else {
				// Message.obtain(handler, GPS_NO_OPEN);
			}
		}
	}

	public synchronized static void addListener(Context context, LocationListener listener, long updatetime, float updatedistance) {
		init(context);
		if (locationManagerProxy != null && locationProvider != null) {
			if (locationListener != null) {
				locationManagerProxy.removeUpdates(locationListener);
			}
			locationListener = listener;
			locationManagerProxy.requestLocationUpdates(locationProvider, updatetime, updatedistance, locationListener);
		}
	}

	public synchronized static void addListener(Context context, long updatetime, float updatedistance) {
		init(context);
		if (locationManagerProxy != null && locationProvider != null) {
			if (locationListener != null) {
				locationManagerProxy.removeUpdates(locationListener);
			}
			locationListener = new Listener(context);
			locationManagerProxy.requestLocationUpdates(locationProvider, updatetime, updatedistance, locationListener);
		}
	}

	public synchronized static void removeListener() {
		if (locationManagerProxy != null && locationProvider != null) {
			locationManagerProxy.removeUpdates(locationListener);
		}
	}

	/**
	 * 获得开启的定位工具（优先考虑GPS定位），如果没有开启返回null<br>
	 * Intent intent = new Intent();<br>
	 * intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);<br>
	 * startActivity(intent);<br>
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static String getProvider(Context context) {
		init(context);
		return locationProvider;
	}

	/**
	 * 获取当前位置
	 * 
	 * @param context
	 * @param provider
	 * @return
	 */
	public synchronized static Location getCurrentLocation(Context context) {
		Location location = null;
		if (context != null) {
			init(context);
			if (locationManagerProxy != null && locationProvider != null) {
				location = locationManagerProxy.getLastKnownLocation(locationProvider);
			}
		}
		return location;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public static GeoPoint toPoint(Location location) {
		if (location == null) {
			return new GeoPoint();
		} else {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			return new GeoPoint(lat, lng);
		}
	}

	/**
	 * 获取路线信息
	 * 
	 * @param route
	 * @return
	 */
	public static ArrayList<String> getRouteInfo(Route route) {
		ArrayList<String> list = new ArrayList<String>();
		if (route != null) {
			for (int i = 0; i <= route.getStepCount(); i++) {
				list.add(route.getStepedDescription(i));
			}
		}
		return list;
	}

	/**
	 * 根据location获取位置信息
	 * 
	 * @param context
	 * @param location
	 * @return
	 */
	public static Address getAddress(Context context, Location location) {
		Geocoder geocoder = new Geocoder(context);
		Address address = null;
		if (location != null) {
			try {
				List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				address = list.get(0);
			} catch (AMapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return address;
	}

	/**
	 * 根据geopoint获取位置信息
	 * 
	 * @param context
	 * @param location
	 * @return
	 */
	public static Address getAddress(Context context, GeoPoint geopoint) {
		Geocoder geocoder = new Geocoder(context);
		Address address = null;
		try {
			double lat = (geopoint.getLatitudeE6()) / 1E6;
			double lng = (geopoint.getLongitudeE6()) / 1E6;
			List<Address> list = geocoder.getFromLocation(lat, lng, 1);
			if (list.size() >= 1) {
				address = list.get(0);
			}
		} catch (AMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return address;
	}

	/**
	 * 获得地址的描述
	 * 
	 * @param address
	 * @return
	 */
	public static String getDescription(Address address) {
		String result = null;
		if (address != null) {
			int max = address.getMaxAddressLineIndex();
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i <= max; i++) {
				stringBuffer.append(address.getAddressLine(i));
			}
			result = stringBuffer.toString();
		}
		return result;
	}

	/**
	 * 调用高德地图apk
	 */
	public static void connectMap(Context context, String latStr, String lonStr) {
		Intent intent = new Intent("android.intent.action.VIEW",
		// android.net.Uri.parse("androidamap://showTraffic?sourceApplication=sinaweibo&poiid=BGVIS1&lat="+latStr+"&lon="+lonStr+"&level=12&dev=0"));
		// navi?sourceApplication=sinaweibo&poiname=fangheng&poiid=BGVIS&lat=36.547901&lon=104.258354&dev=1&style=2
				android.net.Uri.parse("androidamap://navi?sourceApplication=sinaweibo&poiname=fangheng&poiid=BGVIS&lat=" + latStr + "&lon=" + lonStr + "&dev=1&style=2"));

		intent.setPackage("com.autonavi.minimap");
		context.startActivity(intent);
	}

	static class Listener implements LocationListener {
		private Context				context;
		private Location			mLocation;
		public static long			lastUpdate	= 0;
		public static final long	PERIOD		= 5 * 60 * 1000;

		public Listener(Context context) {
			this.context = context;
		}

		@Override
		public void onLocationChanged(Location location) {
			this.mLocation = location;
			int lat = (int) (this.mLocation.getLatitude() * 1E6);
			int lng = (int) (this.mLocation.getLongitude() * 1E6);
			SystemConfig.currentGeoPoint = new GeoPoint(lat, lng);
			new Thread() {
				@Override
				public void run() {
					try {
						GpsUploadRequest gpsUploadRequest = new GpsUploadRequest();
						gpsUploadRequest.setUserCode(SystemConfig.USERLOGINNAME);
						gpsUploadRequest.setIMEI(DeviceUtils.getDeviceId(amapContext));
						if (mLocation != null) {
							gpsUploadRequest.setxCoordinateDegree(mLocation.getLongitude() + "");
							gpsUploadRequest.setyCoordinateDegree(mLocation.getLatitude() + "");
							gpsUploadRequest.setSpeed(mLocation.getSpeed() + "");
							gpsUploadRequest.setDirectionsdegree(mLocation.getBearing() + "");
						} else {
							gpsUploadRequest.setxCoordinateDegree("");
							gpsUploadRequest.setyCoordinateDegree("");
							gpsUploadRequest.setSpeed("");
							gpsUploadRequest.setDirectionsdegree("");
						}
						
						/* 上传坐标服务 */
						GPSUploadResponse gpsResponse = GpsUploadHttpService.gpsUploadService(gpsUploadRequest, context.getResources().getString(R.string.http_url));
						if(gpsResponse.getLocation() != null && !gpsResponse.getLocation().equals("")){
							// 检查时间服务是否还在,停止后，重启server
							if(!SystemConfig.serverTimeIsRunning && !gpsResponse.getServerTime().equals("")){
								SystemConfig.serverTime = gpsResponse.getServerTime();
								
								// 启动时间服务
								Intent defineTimeIntent = new Intent();
								defineTimeIntent.setClass(context, TimeService.class);
								context.startService(defineTimeIntent);
							}
							
							// jingtuo每隔指定时间将地址更新到数据库
							SystemConfig.currentThoroughfare = gpsResponse.getLocation();
							if (lastUpdate == 0) {
								lastUpdate = System.currentTimeMillis();
								TblGPSAddress.insert(SystemConfig.currentThoroughfare);
							} else {
								if (System.currentTimeMillis() - lastUpdate >= PERIOD) {
									lastUpdate = System.currentTimeMillis();
									TblGPSAddress.insert(SystemConfig.currentThoroughfare);
								}
							}
						}
						System.gc();
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle bundle) {

		}

	}

	private static Handler	handler	= new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GPS_NO_OPEN:
				Toast.makeText(null, "GPS定位未开启", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

}
