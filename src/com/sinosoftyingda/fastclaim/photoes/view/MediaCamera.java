package com.sinosoftyingda.fastclaim.photoes.view; 

import java.io.ByteArrayInputStream; 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.dao.TblGPSAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskinfo;
import com.sinosoftyingda.fastclaim.common.model.PicAddress;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.photoes.page.CameraUtils;
import com.sinosoftyingda.fastclaim.photoes.page.DeflossPhotosView;
import com.sinosoftyingda.fastclaim.photoes.page.PhotosView;
//import com.sinosoftyingda.fastclaim.common.utils.FileUtils;

/**
 * 拍照
 * @author DengGuang 20130306
 */
public class MediaCamera extends Activity {
	private static String POType = "";
	private static final String TAG = "PhotoDir";
	String picName = "";
	// /mnt/sdcard/cClaim/1001010010(报案号)/danzheng(单证)
	private String photoDir = "";
	// /mnt/sdcard/cClaim/1001010010(报案号)/danzheng(单证)/20130314112221.jpg
	private String photoPath = "";
	private Bitmap b1;
	private int type = 0;
	private boolean isTrue = true;
	private Bitmap picBitmap;
	private int starCamera = 0;
	
	// add by gl 20131203 优化拍照功能,减少内存消耗 begin
	
	/** 日期格式化工具对象 */
	private SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/** 存储本地DB中存储的地址 */
    private String tblGPSAddress = null;
    
    /** 设置画笔 */
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
    
    /** 文件工具类 */
    private FileUtils fileUtils = new FileUtils();
	// add by gl 20131203 优化拍照功能,减少内存消耗 end
	
	// 拍照地址
	private String strImgPath = "";
    private static final int RESULT_CAPTURE_IMAGE = 1;// 照相的requestCode
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("TAG", "onCreate");
		super.onCreate(savedInstanceState);
		if (isTrue) {
			// 拍照传递过来的值
			Bundle bundle = getIntent().getExtras();
			int typeIndex = bundle.getInt("typeindex_key"); // 照片类别
			// 查勘定损类别（0查勘、1定损）
			type = bundle.getInt("type_key"); 

			// /mnt/sdcard/cClaim/1001010010(报案号)/danzheng(单证)
			photoDir = SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[typeIndex];			
			String cTempDir = SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[typeIndex];
			FileUtils.mkDir(new File(photoDir));
			FileUtils.mkDir(new File(cTempDir));
			
			// add by gl 20131203 初始化变量 begin
			tblGPSAddress = TblGPSAddress.getAddress();
			
			textPaint.setTextSize(40.0f); // 字体大小
			textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
			textPaint.setColor(Color.RED); // 采用的颜色
			textPaint.setAlpha(100); // 透明度0
			// add by gl 20131203 初始化变量 end
			
			/*
			// 解决数据库保存路径重复
			File sdcardDir = Environment.getExternalStorageDirectory();
			String reDir = sdcardDir.getParent() + "/" + sdcardDir.getName();
			// 查找是否有重复的路径
			int find = photoDir.indexOf(reDir + reDir);
			if (find == 0) {
				photoDir = photoDir.replaceAll(reDir + reDir, reDir);
			}
			*/
			String[] subs = this.getResources().getStringArray(R.array.photos_type);
			POType = subs[typeIndex];
			if (savedInstanceState != null) {
				starCamera = savedInstanceState.getInt("startCMA");
				System.out.println("调用相机" + starCamera);
			}
			if (starCamera == 0){
				mCamera();
			}
			isTrue = false;
		}
	}
	/**
	 * 拍照
	 */
	private void mCamera() {
		starCamera++;

		// 调用系统照相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
		startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
	}
	
	/**
	 * 照相功能
	 * @param photoDir
	 */
	private void cameraMethod(String photoDir) {
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		strImgPath = photoDir;// 存放照片的文件夹
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";// 照片命名
		File out = new File(strImgPath);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(strImgPath, fileName);
		strImgPath = strImgPath + fileName;// 该照片的绝对路径
		Uri uri = Uri.fromFile(out);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		picName = sf2.format(new Date()) + ".jpg";
		photoPath = photoDir + "/" + picName;
		Log.i(TAG, photoPath);
		
		// 继续调用照相机
		if (resultCode == -1) {
			// if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
			String strPicPath = "";
			String strPicName = "";
			Uri uriVideo = data.getData();
			Cursor cursor = this.getContentResolver().query(uriVideo, null, null, null, null);
			if (cursor!=null && cursor.moveToNext()) {
				/*_data：文件的绝对路径 ，_display_name：文件名*/
				strPicPath = cursor.getString(cursor.getColumnIndex("_data"));
				strPicName = cursor.getString(cursor.getColumnIndex("_display_name"));
		
				Log.i("Tag", "图片路径=" + strPicPath);
				Log.i("Tag", "图片名字=" + strPicName);
			}

			if (!TextUtils.isEmpty(strPicPath) && !TextUtils.isEmpty(strPicName)) {
				try {
					// createNewFlie(); // delete by gl 20131203 在onCreate方法中已经创建了存放照片的目录,因此每次保存照片时,没有必要检测文件夹是否存在并创建.
					addPicinfo(strPicPath, strPicName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mCamera();
			}
		}
		// 取消
		else if (resultCode == 0) {
			// 返回图片类别
			Bundle bundle = new Bundle();
			bundle.putString("PType", POType);
			bundle.putString("registNo", SystemConfig.PHOTO_CLAIMNO);
			if (type == 0) {
				UiManager.getInstance().changeView(PhotosView.class, false, false, bundle, false);
			} else {
				UiManager.getInstance().changeView(DeflossPhotosView.class, false, false, bundle, false);
			}
			finish();
		}
	}
	
	
	/**
	 * 添加照片信息，添加照片水印
	 * @param strPicName 
	 * @param strPicPath 
	 * @throws Exception
	 */
	private void addPicinfo(String strPicPath, String strPicName) throws Exception{
		File oldFile = new File(strPicPath);
		File newFile = new File(photoPath);
		// 复制一张照片到指定的目录，判断原图片是否保存成功
		long fileSize = oldFile.length(); // modify by gl 20131203 使用文件对象自带的属性取文件大小,没有必要通过流方式读取进来取大小
		if(fileSize > 0){
			// modify by gl 20131203 修改文件复制功能,先使用文件移动功能,如果失败了,再使用流方式进行对拷 begin
			if (!oldFile.renameTo(newFile)) {
				fileUtils.copyfile(oldFile, newFile, false);
				//CameraUtils.copyFile(oldFile, newFile);
				oldFile.delete();// 把原来的图片删除掉
			}
			// modify by gl 20131203 修改文件复制功能,先使用文件移动功能,如果失败了,再使用流方式进行对拷 end
		
			// 获取街道地址 
			String address = "";
			if (!TextUtils.isEmpty(SystemConfig.currentThoroughfare)) {
				address = SystemConfig.currentThoroughfare;
			} else if (tblGPSAddress != null && !TextUtils.isEmpty(tblGPSAddress)) { // modify by gl 20131203 使用成员变量没有必要每次取值都去本地db中进行取数据
				address = tblGPSAddress; // modify by gl 20131203 使用成员变量没有必要每次取值都去本地db中进行取数据
			} else{
				address = "地址暂无";
			}
			// 获取当前时间
			String date = SystemConfig.serverTime;
			
			// 获取照片添加水印 
			Bitmap bitmap = null;
			bitmap = createBitmap(getimage(photoPath), null, date, address);
			
			// 删除原图 
			// modify by gl 20131204 修改删除原图逻辑 begin
			/*File file = new File(photoPath);
			file.delete();*/
			newFile.delete();
			// modify by gl 20131204 修改删除原图逻辑 end
			
			// 保存添加水印后的照片,判断是否保存成功
			boolean isSave = storeInSD(bitmap, photoDir, picName);
			if(isSave){
				PicAddress pic = new PicAddress();
				pic.setRegistNo(SystemConfig.PHOTO_CLAIMNO);
				pic.setLossNo(SystemConfig.LOSSNO.equals("") ? "-2" : SystemConfig.LOSSNO);
				pic.setPicAddress(address);
				pic.setPicName(picName);
				pic.setRemark(date);
				pic.setStarte("0");
				TblPicAddress.insertPicAddress(pic);
			}
			System.gc();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}else{
			Toast.showToast(MediaCamera.this, "图片保存失败，请返回重新拍取照片!!!");
		}
	}

	/**
	 * 照片添加水印 add by haoyun
	 * 
	 * @param src
	 * @param watermark
	 * @return
	 */
	private Bitmap createBitmap(Bitmap src, Bitmap watermark, String date, String address) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src

		String str = date + "  " + address;
		cv.drawText(str, 5, h - 15, textPaint);// 绘制上去 字，开始未知x,y采用那只笔绘
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储

		insertFirstCamerTime(type, date);
		if (src != null && !src.isRecycled()) {
			src.recycle();
			src = null;
			cv = null;
		}
		System.gc();
		return newb;
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param bitmap
	 * @param url
	 * @param fileName
	 *            add by haoyun
	 */
	private boolean storeInSD(Bitmap bitmap, String url, String fileName) {
		boolean isSave = false;
		File file = new File(url);
		if (!file.exists()) {
			file.mkdir();
		}
		
		try {
			File imageFile = new File(file, fileName);
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			
			/**
			 * author DengGuang copy一份到temp目录，
			 */
			long fileSize = imageFile.length(); // modify by gl 20131203 修改去文件大小
			if(fileSize > 0){
				String tempUrl = url.replace(SystemConfig.PHOTO_CLAIM_DIR, SystemConfig.PHOTO_CLAIM_TEMP);
				fileUtils.copyfile(imageFile, new File(tempUrl + "/" + picName), false);
				//CameraUtils.copyFile(file, new File(tempUrl + "/" + picName));
				isSave = true;
			}else{
				imageFile.delete();
				isSave = false;
				Toast.showToast(MediaCamera.this, "图片保存失败，请返回重新拍取照片!!!");
			}
		} catch (Exception e) {
			isSave = false;
			e.printStackTrace();
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			System.gc();
		}
		
		return isSave;
	}
	

	/**
	 * 第一张拍照时间
	 * @param type
	 *            【0=查勘 1=定损】
	 */
	private void insertFirstCamerTime(int type, String firstPhoto) {
		if (type == 0) {
			if (DataConfig.tblTaskQuery != null) {
				// 查询查勘到达现场时间
				String checkArriveTime = TblTaskinfo.queryCheckArriveTime(DataConfig.tblTaskQuery.getRegistNo());
				if (checkArriveTime == null || checkArriveTime.equals("")) {
					checkArriveTime = firstPhoto;
					
					// 将达到现场时间赋到拍照时间
					ContentValues values = new ContentValues();
					values.put("ARRIVESCENETIME", checkArriveTime);
					DBUtils.update("TASKQUERY", values, "REGISTNO=?", new String[] { DataConfig.tblTaskQuery.getRegistNo() });
					DataConfig.tblTaskQuery.setArricesceneTime(checkArriveTime);
				}
				DataConfig.tblTaskQuery.setArricesceneTime(checkArriveTime);
			}
		} else {
			if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getTaskInfo() != null) {
				ContentValues checkValues = DBUtils.find("ClTaskInfo", new String[] { "ARRIVESCENETIME" }, "REGISTNO=? and LOSSNO=?", 
						new String[] {DataConfig.defLossInfoQueryData.getRegist().getRegistNo(), DataConfig.defLossInfoQueryData.getRegist().getLossNo() });
				
				String arrivetime = checkValues.get("ARRIVESCENETIME").toString();
				if (arrivetime == null || arrivetime.equals("")) {	
					arrivetime = firstPhoto;
					
					checkValues = new ContentValues();
					checkValues.put("ARRIVESCENETIME", arrivetime);
					DBUtils.update("ClTaskInfo", checkValues, "REGISTNO=? and LOSSNO=?", new String[] { DataConfig.defLossInfoQueryData.getRegist().getRegistNo(),
							DataConfig.defLossInfoQueryData.getRegist().getLossNo() });
					
					DataConfig.defLossInfoQueryData.getTaskInfo().setArrivesceneTime(arrivetime);
				}
				DataConfig.defLossInfoQueryData.getTaskInfo().setArrivesceneTime(arrivetime);
			}
		}
	}



	/**
	 * 压缩图片分辨率 add haoyun 20130407
	 * 
	 * @param inSampleSize
	 * @param imageFile
	 * @return
	 */
	private Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是1920f*1080f分辨率，所以高和宽我们设置为
		float hh = 1920f;// 这里设置高度为1920f
		float ww = 1080f;// 这里设置宽度为1080f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be + 2;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 压缩图片质量 add haoyun
	 * 
	 * @param image
	 * @return
	 */
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap=null;
		bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 创建文件夹
	 */
	public void createNewFlie() {
		File file = new File(photoDir);
		if (!file.exists()) {
			// 不存在,创建目录
			file.mkdirs();
		}
	}

	@Override
	protected void onDestroy() {
		Log.i("TAG", "销毁了");
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("startCMA", starCamera);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		starCamera = savedInstanceState.getInt("startCMA");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
