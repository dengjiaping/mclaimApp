package com.sinosoftyingda.fastclaim.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图片工具
 * 
 * @author DengGuang
 * 
 */
public class ImageUtils {

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 */
	public static void getFileByte(String filePath) {

		// 从本地读取图片
		Drawable drawable = Drawable.createFromPath(filePath);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		bitmap.recycle();

		// File f = new File(filePath);
		try {
			// FileInputStream fis = new FileInputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片格式转换
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 判断文件是否 图片
	 * 
	 * @param fName
	 * @return
	 */
	public static boolean getImageFile(String fName) {
		boolean re;
		/* 取的扩展名 */
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

		/* 依附件名的类型决定 MimeType */
		if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")) {
			re = true;
		} else {
			re = false;
		}

		return re;
	}

	/**
	 * 删除图片文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteImageFile(String filePath, Context context) {
		File file = new File(filePath);
		if (file.exists()) {
			// context.deleteFile(filePath);
			return file.delete();
		}
		return false;
	}

	/**
	 * 按照等比例缩放
	 * 
	 * @param bm
	 * @param scaleW
	 * @param scaleH
	 * @return
	 */
	public static Bitmap createScaleImage(Bitmap bm, float scaleW, float scaleH) {
		Matrix mMatrix = new Matrix();
		mMatrix.reset();
		mMatrix.setScale(scaleW / bm.getWidth(), scaleH / bm.getHeight());
		Bitmap scaleBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mMatrix, true);
		return scaleBm;
	}

	/**
	 * 将图片设置成圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.WHITE);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return bmp;
	}

	/**
	 * 根据Uri得到图片并在得到是进行缩小图片
	 * 
	 * @author 郝运
	 * @param uri
	 * @return
	 */
	public static Bitmap imgCompress(String uri) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(uri, options);
		options.inJustDecodeBounds = false;

		int be = (int) (options.outHeight / (float) 300);
		be = be <= 0 ? 1 : be;
		options.inSampleSize = be;
		return BitmapFactory.decodeFile(uri, options);
	}

	/**
	 * 保存为图片
	 * 
	 * @param imageName
	 * @param path
	 * @param img
	 * @throws IOException
	 */
	public static void saveToImage(String imageName, String path, Bitmap img) throws IOException {
		try {
			String picName = imageName + ".jpg";
			// String path = Environment.getExternalStorageDirectory() + "/" +
			// h;
			File f = new File(path, picName);
			FileOutputStream fos = new FileOutputStream(f);
			img.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// img.recycle();// 注意回收内存
		}

	}



}
