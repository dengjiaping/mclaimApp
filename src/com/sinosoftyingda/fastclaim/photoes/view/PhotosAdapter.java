package com.sinosoftyingda.fastclaim.photoes.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.photoes.model.ESImageView;
import com.sinosoftyingda.fastclaim.photoes.page.ShowPhotoActivity;

/**
 * 加载图片
 * @author DengGuang 20130306
 *
 */
public class PhotosAdapter extends BaseAdapter implements OnItemClickListener{

	private Context context;	
	private LayoutInflater gInflater;
	private List<String> gListPics;
	private String picDir;
	private String typeDir;
	private String gType;

	public PhotosAdapter(Context context, List<String> gListPics, String picDir, String typeDir, String gType) {
		super();
		this.context = context;
		this.gInflater = LayoutInflater.from(context);
		this.gListPics = gListPics;
		this.picDir = picDir;
		this.typeDir = typeDir;
		this.gType = gType;
	}

	@Override
	public int getCount() {
		if(gListPics!=null)
			return gListPics.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		if(gListPics!=null)
			return gListPics.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/* 设置单张图片 */
		convertView = gInflater.inflate(R.layout.photos_image, null);
		ESImageView imageView = (ESImageView)convertView.findViewById(R.id.ivshow);
		imageView.setImageUrl(getItem(position).toString());
		
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		// 查看单张图片
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("selectposition_key", position);	// 默认选中起始位置
		//bundle.putString("photodir_key", picDir);
		bundle.putString("typedir_key", typeDir);
		bundle.putString("gtype_key", gType);
		intent.putExtras(bundle);
		intent.setClass(context, ShowPhotoActivity.class);
		context.startActivity(intent);
		
//		String photoDir = SystemConfig.PHOTO_CLAIM_DIR + typeDir;
//		Intent it = new Intent(Intent.ACTION_VIEW); 
//		it.setDataAndType(Uri.fromFile(new File(gListPics.get(position))), "image/*"); 
//		context.startActivity(it); 

	}
}
