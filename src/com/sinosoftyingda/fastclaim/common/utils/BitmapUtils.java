package com.sinosoftyingda.fastclaim.common.utils;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/****
 * 对于图片操作的封装工具
 * 
 * @author chenjianfan
 * 
 */
public class BitmapUtils {

	private Context context;

	public BitmapUtils(Context context) {
		this.context = context;
	}

	/***
	 * 设置控件的背景图片（px转为dp）
	 * 
	 * @param drawable
	 *            drawable 图片的drawable
	 * @param view
	 *            设置背景图片的控件
	 * 
	 */
	public void setWidgetBackdrop(Drawable drawable, View view) {
		Bitmap bitmap = drawableToBitmap(drawable);
		view.setBackgroundDrawable(new BitmapDrawable(bitmap));
	}

	/***
	 * 图片宽高px转化成dp的宽高
	 * 
	 * @param
	 * @return
	 */
	public Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = DensityUtil.px2dip(context, drawable.getIntrinsicWidth());
		int h = DensityUtil.px2dip(context, drawable.getIntrinsicHeight());
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/*******
	 * 图片按照8/1的加载到内存
	 * 
	 * @param context
	 * @param res
	 * @param id
	 * @return
	 */
	public static Bitmap optimizeLoadPic(Context context, Resources res, int id) {
		BitmapFactory.Options options = new Options();
		options.inSampleSize = 8;// 按照原图的8/1进行加载
		return BitmapFactory.decodeResource(res, id, options);
	}

	/******
	 * 优化读取本地图片
	 * 
	 * @param context
	 * @param resId
	 *            图片资源R id
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);
	}

	/******
	 * 优化读取本地图片
	 * 
	 * @param context
	 * @param resId
	 *            图片资源R id
	 * @return
	 */
	public static Bitmap readBitMap(Context context, String pathName) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		return BitmapFactory.decodeFile(pathName, opt);
	}

}
