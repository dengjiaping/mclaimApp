package com.sinosoftyingda.fastclaim.common.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.apn.model.ApnDto;

/**
 * @author JingTuo
 *
 */
public class ApnUtils {
	public static final String TAG = ApnUtils.class.getSimpleName();

	/**
	 * APN列表资源
	 */
	public static final Uri APN_URI = Uri.parse("content://telephony/carriers");
	
	/**
	 * 默认APN
	 */
	public static final Uri PREFER_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	private static ApnDto defaultApn;
	
	private static ApnDto useApn;
	
	public static void init(Context context){
		initDefaultApn(context);
		useApn = find(context, "jingtuo");
		if(useApn==null){
			initUseApn(context);
		}
		useApn(context, useApn.getId());
	}
	
	/**
	 * 获取缺省APN
	 * @param context
	 * @return
	 */
	private static void initDefaultApn(Context context){
		Cursor cursor = null;
		ContentResolver contentResolver = null;
		try {
			contentResolver = context.getContentResolver();
			cursor = contentResolver.query(PREFER_APN_URI, null, null, null,
					null);
			while (cursor != null && cursor.moveToNext()) {
				defaultApn = toApnDto(cursor);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	/**
	 * 获取缺省APN
	 * @param context
	 * @return
	 */
	public static List<ApnDto> getAll(Context context){
		Cursor cursor = null;
		ContentResolver contentResolver = null;
		List<ApnDto> list = new ArrayList<ApnDto>();
		try {
			contentResolver = context.getContentResolver();
			cursor = contentResolver.query(APN_URI, null, null, null,
					null);
			while (cursor != null && cursor.moveToNext()) {
				list.add(toApnDto(cursor));
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	public static ApnDto find(Context context, String apn){
		Cursor cursor = null;
		ContentResolver contentResolver = null;
		ApnDto result = null;
		try {
			contentResolver = context.getContentResolver();
			cursor = contentResolver.query(APN_URI, null, "apn=?", new String[]{apn},
					null);
			while (cursor != null && cursor.moveToNext()) {
				result = toApnDto(cursor);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}
	
	private static void initUseApn(Context context){
		Cursor cursor = null;
		try {
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();
			values.put("name", context.getResources().getString(R.string.apn_name));
			values.put("apn", context.getResources().getString(R.string.apn_apn));
			values.put("type", context.getResources().getString(R.string.apn_type));
			String numeric = DeviceUtils.getSimOperator(context);
			if(numeric==null){
				numeric = "";
			}
			values.put("numeric", numeric);
			Log.i("numeric", numeric);
			if(numeric.length()>=3){
				values.put("mcc", numeric.substring(0, 3));
				Log.i("mcc", numeric.substring(0, 3));
			}
			if(numeric.length()>=4){
				values.put("mnc", numeric.substring(3, numeric.length()));
				Log.i("mnc", numeric.substring(3, numeric.length()));
			}
			values.put("proxy", "");
			values.put("port", "");
			values.put("mmsproxy", "");
			values.put("mmsport", "");
			values.put("user", "");
			values.put("server", "");
			values.put("password", "");
			values.put("mmsc", "");
			Uri newUri = resolver.insert(APN_URI, values);
			if (newUri != null) {
				cursor = resolver.query(newUri, null, null, null, null);
				while (cursor != null && cursor.moveToNext()) {
					useApn = toApnDto(cursor);
					break;
				}
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	
	
	public static ApnDto getDefaultApn() {
		return defaultApn;
	}

	public static ApnDto getUseApn() {
		return useApn;
	}

	public static boolean useApn(Context context,  int apnId){
		boolean result = false;
		Cursor cursor = null;
		try {
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();
			values.put("apn_id", apnId);
			int index = resolver.update(PREFER_APN_URI, values, null, null);
			if(index<=0){
				Log.i(TAG, "APN切换失败");
			}else{
				Log.i(TAG, "APN切换成功");
			}
			cursor = resolver.query(PREFER_APN_URI, null,
					"_id=" + apnId, null, null);
			while (cursor != null && cursor.moveToNext()) {
				result = true;
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}
	
	public static ApnDto createApn(Context context, ApnDto apnDto){
		ApnDto result = null;
		Cursor cursor = null;
		try {
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = toValues(apnDto, false);
			Uri newUri = resolver.insert(APN_URI, values);
			if (newUri != null) {
				cursor = resolver.query(newUri, null, null, null, null);
				while (cursor != null && cursor.moveToNext()) {
					result = toApnDto(cursor);
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}
	
	private static ApnDto toApnDto(Cursor cursor){
		ApnDto apnDto = new ApnDto();
		apnDto.setApn(cursor.getString(cursor.getColumnIndex("apn")));
        apnDto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        apnDto.setMcc(cursor.getString(cursor.getColumnIndex("mcc")));
        apnDto.setMmsc(cursor.getString(cursor.getColumnIndex("mcc")));
        apnDto.setMmsport(cursor.getString(cursor.getColumnIndex("mmsport")));
        apnDto.setMmsproxy(cursor.getString(cursor.getColumnIndex("mmsproxy")));
        apnDto.setMnc(cursor.getString(cursor.getColumnIndex("mnc")));
        apnDto.setName(cursor.getString(cursor.getColumnIndex("name")));
        apnDto.setNumeric(cursor.getString(cursor.getColumnIndex("numeric")));
        apnDto.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        apnDto.setPort(cursor.getString(cursor.getColumnIndex("port")));
        apnDto.setProxy(cursor.getString(cursor.getColumnIndex("proxy")));
        apnDto.setServer(cursor.getString(cursor.getColumnIndex("server")));
        apnDto.setType(cursor.getString(cursor.getColumnIndex("type")));
        apnDto.setUser(cursor.getString(cursor.getColumnIndex("user")));
        return apnDto;
	}
	
	/**
	 * 将ApnDto转换成ContentValues，如果flag为true，会设置_id这个值
	 * @param apnDto
	 * @param flag
	 * @return
	 */
	private static ContentValues toValues(ApnDto apnDto, boolean flag){
		ContentValues values = new ContentValues();
		values.put("name", apnDto.getApn());
		if(flag){
			values.put("_id", apnDto.getId());
		}
		values.put("mcc", apnDto.getMcc());
		values.put("mmsc", apnDto.getMmsc());
		values.put("mmsport", apnDto.getMmsport());
		values.put("mmsproxy", apnDto.getMmsproxy());
		values.put("mnc", apnDto.getMnc());
		values.put("name", apnDto.getName());
		values.put("numeric", apnDto.getNumeric());
		values.put("password", apnDto.getPassword());
		values.put("port", apnDto.getPort());
		values.put("proxy", apnDto.getProxy());
		values.put("server", apnDto.getServer());
		values.put("type", apnDto.getType());
		values.put("user", apnDto.getUser());
		return values;
	}

	
}
