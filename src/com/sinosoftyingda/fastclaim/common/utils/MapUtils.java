package com.sinosoftyingda.fastclaim.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.route.Route;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.GpsUploadRequest;
import com.sinosoftyingda.fastclaim.common.service.GpsUploadHttpService;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 经度Longitude、纬度Latitude
 * @author JingTuo
 *
 */
public class MapUtils {
	
	private static LocationManager locationManager;
	private static String locationProvider;
	private static LocationListener locationListener;
	private static Context mapContext;
	
	/**
	 * 初始化位置代理、定位提供器
	 * @param context
	 */
	private synchronized static void init(Context context){
		if(context!=null){
			mapContext = context;
			locationManager = (LocationManager)mapContext.getSystemService(Context.LOCATION_SERVICE);
		}
		if(locationManager!=null&&locationProvider==null){
			List<String> providers = locationManager.getProviders(true);
			if(providers==null||providers.size()<=0){
				locationProvider = null;
			}else{
				//判断可用定位工具是否包含GPS定位
				int index = -1;
				for (int i = 0; i < providers.size(); i++) {
					if(LocationManager.GPS_PROVIDER.equals(providers.get(i))||LocationManager.NETWORK_PROVIDER.equals(providers.get(i))){
						index = i;
						break;
					}
				}
				if(index==-1){
					locationProvider = null;
				}else{
					locationProvider = providers.get(index);
				}
			}
		}
	}

	public synchronized static void addListener(Context context, LocationListener listener, long updatetime, float updatedistance){
		init(context);
		if(locationManager!=null&&locationProvider!=null){
			if(locationListener!=null){
				locationManager.removeUpdates(locationListener);	
			}
			locationListener = listener;
			locationManager.requestLocationUpdates(locationProvider, updatetime, updatedistance, locationListener);
		}
	}
	
	public synchronized static void addListener(Context context, long updatetime, float updatedistance){
		init(context);
		if(locationManager!=null&&locationProvider!=null){
			if(locationListener!=null){
				locationManager.removeUpdates(locationListener);	
			}
			locationListener = new Listener(context);
			locationManager.requestLocationUpdates(locationProvider, updatetime, updatedistance, locationListener);
		}
	}
	
	public synchronized static void removeListener(){
		if(locationManager!=null&&locationProvider!=null){
			locationManager.removeUpdates(locationListener);
		}
	}
	
	
	/**
	 * 获得开启的定位工具（优先考虑GPS定位），如果没有开启返回null<br>
	 * Intent intent = new Intent();<br>
	 * intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);<br>
	 * startActivity(intent);<br>
	 * @param context
	 * @return
	 */
	public synchronized static String getProvider(Context context){
		init(context);
		return locationProvider;
	}
	
	/**
	 * 获取当前位置
	 * @param context
	 * @param provider
	 * @return
	 */
	public synchronized static Location getCurrentLocation(Context context){
		Location location = null;
		if(context!=null){
			init(context);
			if(locationManager!=null&&locationProvider!=null){
				location = locationManager.getLastKnownLocation(locationProvider);
			}
		}
		return location;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	public static GeoPoint toPoint(Location location){
		if(location==null){
			return new GeoPoint();
		}else{
			int lat = (int)(location.getLatitude()*1E6);
			int lng = (int)(location.getLongitude()*1E6);
			return new GeoPoint(lat, lng);
		}
	}
	
	/**
	 * 获取路线信息
	 * @param route
	 * @return
	 */
	public static List<String> getRouteInfo(Route route){
		List<String> list = new ArrayList<String>();
		if(route!=null){
			for (int i = 0; i <= route.getStepCount(); i++) {
				list.add(route.getStepedDescription(i));
			}
		}
		return list;
	}
	
	
	
	/**
	 * 根据geopoint获取位置信息
	 * @param context
	 * @param location
	 * @return
	 */
	public static Address getAddress(Context context, GeoPoint geopoint){
		Geocoder geocoder = new Geocoder(context);
		Address address = null;
		try {
			double lat = (geopoint.getLatitudeE6()) / 1E6;
			double lng = (geopoint.getLongitudeE6()) / 1E6;
			List<Address> list = geocoder.getFromLocation(lat, lng, 1);
			if(list.size()>=1){
				address = list.get(0);
			}
		} catch (AMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return address;
	}
	static class Listener implements LocationListener{

		private Context context;
		
		private Location mLocation;
		
		public Listener(Context context){
			this.context = context;
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(location!=null&&mapContext!=null){
				System.out.println("获取当前位置，开始上传...");
				this.mLocation = location;
				int lat = (int)(this.mLocation.getLatitude() * 1E6); 
				int lng = (int)(this.mLocation.getLongitude() * 1E6); 
				SystemConfig.currentGeoPoint = new GeoPoint(lat, lng);
				new Thread(){
					@Override
					public void run() {
						try {
							Address address = getAddress(mapContext, SystemConfig.currentGeoPoint);
							if(address!=null&&address.getThoroughfare()!=null){
								SystemConfig.currentThoroughfare = address.getThoroughfare();
							}
							GpsUploadRequest gpsUploadRequest = new GpsUploadRequest();
							gpsUploadRequest.setUserCode(SystemConfig.USERLOGINNAME);
							gpsUploadRequest.setxCoordinateDegree(mLocation.getLatitude() + "");
							gpsUploadRequest.setyCoordinateDegree(mLocation.getLongitude() + "");
							gpsUploadRequest.setSpeed(mLocation.getSpeed() + "");
							gpsUploadRequest.setDirectionsdegree(mLocation.getBearing() + "");
							
							GpsUploadHttpService.gpsUploadService(gpsUploadRequest, context.getResources().getString(R.string.http_url));
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle bundle) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
