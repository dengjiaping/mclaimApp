package com.sinosoftyingda.fastclaim.photoes.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sinosoftyingda.fastclaim.R;

/**
 * @author bboxx
 * 
 */
public class ESImageView extends ImageView {

	private static Context mContext;

	public ESImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ESImageView(Context context) {
		super(context);
		mContext = context;
	}

	public void setImageUrl(String url) {
		new AsyncSetImageUrl().execute(url);
	}

	/**
	 * 异步读取网络图片
	 * 
	 * @author hellogv
	 */
	class AsyncSetImageUrl extends AsyncTask<Object, Object, Bitmap> {

		@Override
		protected Bitmap doInBackground(Object... params) {
			String imagePath = (String) params[0];
			//查看当前程序中是否有缓存
			Bitmap bitmap = MyApplication.getInstance().getImageCache().get(imagePath);
			if (bitmap == null) {
				bitmap = getThumbnail(imagePath);
			}
			
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap map) {
			// 切图
			Paint paint = new Paint();  
//			Bitmap bitmap1 = Bitmap.createBitmap(240, 240, Bitmap.Config.ARGB_8888);  
//			// 使用bitmap1创建一个画布
//			Canvas canvas = new Canvas(bitmap1);   
//			canvas.drawColor(Color.WHITE);

//			Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.load_image_bg).copy(Bitmap.Config.ARGB_8888, true); 
//			Canvas canvas = new Canvas(bitmap);  
//
//			int x = 60;
//			int y = 60;
//			int w = 300;
//			int h = 300;
//			int line = 1;
//			int row = 0;
//			canvas.clipRect(x, y, x + w, h + y);
//			canvas.drawBitmap(map, x - line * w, y - row * h, paint);
//			canvas.restore();
			
			ESImageView.this.setImageBitmap(map);
		}
	}
	

	/**
	 * 返回本地图片的缩略图
	 * 
	 * @param imagePath
	 *            SDCARD本地图片路径
	 * @return
	 */
	public static Bitmap getThumbnail(String imagePath) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagePath, opts);

			opts.inSampleSize = computeSampleSize(opts, -1, 80 * 80);
			opts.inJustDecodeBounds = false;

			bitmap = BitmapFactory.decodeFile(imagePath, opts);
			MyApplication.getInstance().getImageCache().put(imagePath, bitmap);
		} catch (Exception ex) {
			bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 80 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
