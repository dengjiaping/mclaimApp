package com.sinosoftyingda.fastclaim.photoes.model;

import java.util.WeakHashMap;

import android.graphics.Bitmap;

/**
 * @author Lukasz Wisniewski
 */
public class ImageCache extends WeakHashMap<String, Bitmap> {

	public static final long serialVersionUID = 1L;
	
	public boolean isCached(String url){
		return containsKey(url) && get(url) != null;
	}

}
