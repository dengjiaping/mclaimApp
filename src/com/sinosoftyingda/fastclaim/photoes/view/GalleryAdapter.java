package com.sinosoftyingda.fastclaim.photoes.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

/**
 * Gallery的适配器类，主要用于加载图片
 * 
 * @author lyc
 * 
 */
public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private String currentPic;
	public static String selectPicPath;

	/* 图片素材 */
	// private int images[] ={ R.drawable.signture_bg, R.drawable.signture_bg,
	// R.drawable.signture_bg};

	private List<String> picPaths;
	private Bitmap bmp;

	public GalleryAdapter(Context context, String currentPic, List<String> picPaths) {
		this.context = context;
		this.currentPic = currentPic;
		this.picPaths = picPaths;
	}

	@Override
	public int getCount() {
		return picPaths.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 每次移动获取图片并重新加载，当图片很多时可以构造函数就把bitmap引入并放入list当中，
		// 然后在getview方法当中取来直接用
		// Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
		// images[position]);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		bmp = null;
		bmp = BitmapFactory.decodeFile(picPaths.get(position), options);

		MyImageView view = new MyImageView(context, bmp.getWidth(), bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		view.setImageBitmap(bmp);

		selectPicPath = picPaths.get(position);
		return view;
	}

	/*****
	 * 清楚数据
	 */
	public void clean() {
		if (bmp != null && bmp.isRecycled()) {
			bmp.recycle();
			System.gc();
		}
	}

}
