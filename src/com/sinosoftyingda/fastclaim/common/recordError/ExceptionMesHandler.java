package com.sinosoftyingda.fastclaim.common.recordError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 异常代理
 * @author TanYanqiang
 */
public class ExceptionMesHandler implements UncaughtExceptionHandler {

	public static final String TAG = "ExceptionMesHandler";
	private static final String addressURl = "http://192.168.1.110:8080/Microll/man/FileUploadAction.action";

	private Context context;
	private Map<String, String> infos = new HashMap<String, String>();
	private DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	private String filepath;

	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static ExceptionMesHandler exceptionMesHandler = new ExceptionMesHandler();
	private ExceptionMesHandler() {
	
	}

	public static ExceptionMesHandler getInstance() {
		return exceptionMesHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				Log.e(TAG, "error : ", e);
			}
			
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	public void init(Context context) {
		this.context = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		new Thread() {
			@Override
			public void run() {
				super.run();
				Looper.prepare();
				Toast.makeText(context, "程序异常退出", 5000).show();
				Looper.loop();
			}
		}.start();
		
		collectDeviceInfo(context);
		saveCrashInfo2File(ex);
		
		return true;
	}

	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误日志
	 * @param ex
	 * @return
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			// 文件存储名
			String claimNoStr = "claim";
			if(SystemConfig.PHOTO_CLAIMNO.trim().equals("")){
				claimNoStr = SystemConfig.PHOTO_CLAIMNO;
			}
			String fileName = claimNoStr +"_crash_"+time+".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 文件存储路径
				// 获取Sdcard路径
				File sdcardDir = Environment.getExternalStorageDirectory();
				String sdDir = sdcardDir.getParent() + "/" + sdcardDir.getName();
				String path =sdDir+"/claimError/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				filepath = path + fileName;
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			upload();
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file", e);
		}
		return null;
	}

	/**
	 * 上传
	 */
	private void upload() {
		final File file = new File(filepath);
		Log.d("file", filepath);
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("doc", "doc");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String log = Upload.getInstance().post(addressURl, params, file);
				Log.i(TAG, log);
			}
		});
		
		thread.start();
	}
}
