package com.sinosoftyingda.fastclaim.signature.view;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblGPSAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.model.PicAddress;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;

/**
 * 客户签名绘图工具
 * 
 * @author DengGuang
 */
public class SignatureView extends View {

	// 创建画布
	private Canvas canvas;
	private Bitmap bitmap1, bitmap2;
	public Paint paint, bitmapPaint;
	private Context context;

	private float mX, mY;
	private Path path;

	public String paintSignture = "";
	private int bitmapHeight;
	private int bitmapWidth;

	/**
	 * SignatureView的构造函数
	 * 
	 * @param context
	 */
	public SignatureView(Context context) {

		super(context);
		this.context = context;
		// 创建一个可在图像上绘制图形的Bitmap对象
		loadBitmap(false);
		// 创建一个Paint对象，也就是drawBitmap方法中的最后一个参数值
		bitmapPaint = new Paint(Paint.DITHER_FLAG);
		// 创建用于绘制图形的Path对象
		path = new Path();
	}

	public SignatureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 创建一个可在图像上绘制图形的Bitmap对象
		loadBitmap(false);
		invalidate();
		// 创建一个Paint对象，也就是drawBitmap方法中的最后一个参数值
		bitmapPaint = new Paint(Paint.DITHER_FLAG);
		// 创建用于绘制图形的Path对象
		path = new Path();
		paint = new Paint(Paint.DITHER_FLAG);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制图像
	    canvas.drawBitmap(bitmap1, 0, 0, bitmapPaint);
		// 绘制图形
		paint = new Paint(Paint.DITHER_FLAG);
		// 设置画笔的Style 设置为空心
		paint.setStyle(Style.STROKE);
		// 设置空心外框的 宽度
		paint.setStrokeWidth(5);
		// 设置画笔的颜色
		paint.setColor(Color.RED);
		canvas.drawPath(path, paint);
	}

	/**
	 * 创建一个可在图像上绘制图形的Bitmap对象
	 */
	public void loadBitmap(boolean isClean) {
		try {
			// 清空手写签名
			if (!isClean) {
				// true
				paintSignture = findSigntureImages(false);
			} else {
				// false
				paintSignture = "";
			}

			// 加载画布
			if (paintSignture == null || paintSignture.equals("")) {
				// 无手写签名图片
				CheckSurveyValue.isSignature = false;
				// InputStream is =
				// getResources().openRawResource(R.drawable.signture_bg);
				// // 从res\drawable目录中装载图像资源，并生成Bitmap对象
				// chenjianfan
				// bitmap2 = BitmapUtils.readBitMap(context,
				// R.drawable.signture_bg);
				BitmapFactory.Options options = new Options();
				// 下面这个设置是将图片边界不可调节变为可调节
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeResource(context.getResources(), R.drawable.signture_bg, options);
				int outWidth = options.outWidth;
				int outHeight = options.outHeight;
				// 获取水平和垂直方向的缩放比
				int yRatio = (int) Math.ceil(outHeight / 600);
				int xRatio = (int) Math.ceil(outWidth / 600);
				if (xRatio > yRatio && yRatio > 1) {
					options.inSampleSize = xRatio;
				}
				if (yRatio > xRatio && xRatio > 1) {
					options.inSampleSize = yRatio;
				}
				// 由于已经得到了缩放比例 ,让位图工厂真正的解析这个位图
				options.inJustDecodeBounds = false;
				bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.signture_bg, options);

			} else {
				try {
					// 有手写签名图片
					CheckSurveyValue.isSignature = true;
					// bitmap2 = BitmapUtils.readBitMap(context, paintSignture);
					BitmapFactory.Options options = new Options();
					// 下面这个设置是将图片边界不可调节变为可调节
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(paintSignture, options);
					int outWidth = options.outWidth;
					int outHeight = options.outHeight;
					// 获取水平和垂直方向的缩放比
					int yRatio = (int) Math.ceil(outHeight / 600);
					int xRatio = (int) Math.ceil(outWidth / 600);
					if (xRatio > yRatio && yRatio > 1) {
						options.inSampleSize = xRatio;
					}
					if (yRatio > xRatio && xRatio > 1) {
						options.inSampleSize = yRatio;
					}
					// 由于已经得到了缩放比例 ,让位图工厂真正的解析这个位图
					options.inJustDecodeBounds = false;
					bitmap2 = BitmapFactory.decodeFile(paintSignture, options);

				} catch (Exception e) {
					// InputStream is =
					// getResources().openRawResource(R.drawable.signture_bg);
					// // 从res\drawable目录中装载图像资源，并生成Bitmap对象
					// bitmap2 = BitmapFactory.decodeStream(is);
					// bitmap2 = BitmapUtils.readBitMap(context,
					// R.drawable.signture_bg);
					BitmapFactory.Options options = new Options();
					// 下面这个设置是将图片边界不可调节变为可调节
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeResource(context.getResources(), R.drawable.signture_bg, options);
					int outWidth = options.outWidth;
					int outHeight = options.outHeight;
					// 获取水平和垂直方向的缩放比
					int yRatio = (int) Math.ceil(outHeight / 600);
					int xRatio = (int) Math.ceil(outWidth / 600);
					if (xRatio > yRatio && yRatio > 1) {
						options.inSampleSize = xRatio;
					}
					if (yRatio > xRatio && xRatio > 1) {
						options.inSampleSize = yRatio;
					}
					// 由于已经得到了缩放比例 ,让位图工厂真正的解析这个位图
					options.inJustDecodeBounds = false;
					bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.signture_bg, options);
				}

			}
			// 修改为圆角样式
			// bitmap2 = ImageUtils.toRoundCorner(bitmap2, 60);
			// 使用createBitmap方法创建一个可绘制图形的Bitmap对象

			bitmap1 = Bitmap.createBitmap(720, 900, Bitmap.Config.ARGB_8888);  
			// 使用bitmap1创建一个画布
			canvas = new Canvas(bitmap1);
			// 在bitmap1的画布上绘制bitmap2
			canvas.drawBitmap(bitmap2, 0, 0, null);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获得当前触摸的横坐标和纵坐标
		float x = event.getX();
		float y = event.getY();
		// 手写签名
		CheckSurveyValue.isSignature = true;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 手指按下时的动作
			touch_start(x, y);
			invalidate();

			break;
		case MotionEvent.ACTION_MOVE: // 手指移动时的动作
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP: // 手指抬起时的动作
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	/**
	 * 手指按下动作发生
	 * 
	 * @param x
	 * @param y
	 */
	private void touch_start(float x, float y) {
		path.moveTo(x, y);
		mX = x;
		mY = y;
	}

	/**
	 * 手指移动动作发生
	 * 
	 * @param x
	 * @param y
	 */
	private void touch_move(float x, float y) {
		// 从手指触摸点到最新点的曲线
		path.quadTo(mX, mY, x, y);
		mX = x;
		mY = y;
		try {
			canvas.drawPath(path, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 手指抬起动作发生
	 */
	private void touch_up() {
		// 绘制路径。 canvas是和bitmap1相连的画布
		// canvas.drawPath(path, paint);
		// 清空Path对象中的所有绘制的图形
		path.reset();
	}

	/**
	 * 清除绘制图形
	 */
	public void clear() {
		// 重新装载图像资源
		loadBitmap(true);
		invalidate();

		CheckSurveyValue.isSignature = false;
	}

	/**
	 * 查找手写签名图片
	 * 
	 * @param isDelfile
	 *            ture
	 */
	private String findSigntureImages(boolean isDelfile) {
		// /mnt/sdcard/cClaim/6001060010(报案号)/signture
		String signDir = SystemConfig.PHOTO_DIR + SystemConfig.PHOTO_CLAIMNO;
		FileUtils.mkDir(new File(signDir));
		signDir = signDir + SystemConfig.PHOTO_TYEP_0;
		FileUtils.mkDir(new File(signDir));
		String signtureName = SystemConfig.PHOTO_CLAIMNO + "_sign.png";
		String signturePath = signDir + "/" + signtureName;

		// 文件名集合
		List<String> signtureImages = new ArrayList<String>();
		FileUtils fileUtils = new FileUtils();
		// 查找当前目录里面的文件
		signtureImages = fileUtils.getFileName(new File(signDir), signDir);

		// 是否删除手写签名图片
		if (isDelfile) {
			for (int i = 0; i < signtureImages.size(); i++) {
				if (signtureImages.get(i).equals(signtureName)) {
					FileUtils.deleteFile(signtureName);
				}
			}
		} else {
			if (signtureImages.size() == 0) {
				signturePath = "";
			}
		}

		return signturePath;
	}

	/**
	 * 将图片保存到SDCard 中
	 */
	public void save(Context context) {
		try {
			paintSignture = findSigntureImages(true);

			// 压缩图片
			File f = new File(paintSignture);
			FileOutputStream fos = new FileOutputStream(f);
			bitmap1.compress(CompressFormat.PNG, 80, fos);
			fos.close();
			if (bitmap1 != null && bitmap1.isRecycled()) {
				bitmap1.recycle();
				bitmap1=null;
			}
			if (bitmap2 != null && bitmap2.isRecycled()) {
				bitmap2.recycle();
				bitmap2=null;
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存相关信息到表Picadress，保证手写签名上传成功
	 */
	public void savePicInfo(){
		String address = "";
		if (!TextUtils.isEmpty(SystemConfig.currentThoroughfare)) {
		address = SystemConfig.currentThoroughfare;
		} else if (TblGPSAddress.getAddress() != null && !TextUtils.isEmpty(TblGPSAddress.getAddress())) {
		address = TblGPSAddress.getAddress();
		}
		PicAddress pic = new PicAddress();
		pic.setRegistNo(SystemConfig.PHOTO_CLAIMNO);
		pic.setLossNo("-2");
		pic.setPicAddress(address);
		pic.setPicName(SystemConfig.PHOTO_CLAIMNO + "_sign.png");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sf.format(new Date());
		pic.setRemark(date);
		pic.setStarte("0");
		TblPicAddress.insertPicAddress(pic);
	}

	/****
	 * chenjianfan 释放图片资源
	 */
	public void bitMapRecyle() {
		// 回收资源
		if (bitmap2 != null && bitmap2.isRecycled()) {
			bitmap2.recycle();
			bitmap2=null;
		}
		if (bitmap1 != null && bitmap1.isRecycled()) {
			bitmap1.recycle();
			bitmap1=null;
		}
		System.gc();
	}
}