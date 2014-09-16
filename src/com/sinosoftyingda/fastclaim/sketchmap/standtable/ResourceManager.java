package com.sinosoftyingda.fastclaim.sketchmap.standtable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 装饰素材资源管理类，素材分两类：<br>
 * 1选中的高亮图片--位于assets/selected_icon目录下<br>
 * 2非选中的普通图片--位于assets/unselected_icon目录下<br>
 * 图片素材加载的顺序将根据文件名排序来决定：如 a.png将排在b.png之前，可以根据这个设置素材在UIToolPanel中出现的位置
 * 
 * 目前IToolPanel中分两排：其排列顺序为从左-->右，从上-->下
 * 
 * @author anychen
 * 
 */
public class ResourceManager {

	/**
	 * 精灵用到的图
	 */
	private static LinkedHashMap<Integer, Bitmap> roads_icon_elements = new LinkedHashMap<Integer, Bitmap>();
	private static LinkedHashMap<Integer, Bitmap> general_icon_elements = new LinkedHashMap<Integer, Bitmap>();
	private static LinkedHashMap<Integer, String> index_general_name_map = new LinkedHashMap<Integer, String>();
	private static LinkedHashMap<Integer, String> index_roads_name_map = new LinkedHashMap<Integer, String>();
	/**
	 * 图标文件扩展名
	 */
	private static String PNG_ICON_DAT_EXT = ".png";
	private static String JPG_ICON_DAT_EXT = ".jpg";
	/**
	 * 缺省图标文件路径
	 */
	private static final String RES_ROAD_PATH = "roads_res";
	private static final String ICON_ROAD_PATH = "roads_icon";
	private static final String RES_GENERAL_PATH = "other_res";

	public static void loadAllIcon() {
		try {
			AssetManager assetManager = AccidentSketchActivity.getInstance().getAssets();
			ArrayList<String> generalRes = getAssetsFilesBySort(assetManager,
					RES_GENERAL_PATH);
			// 加载图标普通素材数据
			int index = 0;
			for (String fileName : generalRes) {
				if (SketchConfig.isDebug) {
					Log.d("Test", "load general element fileName=" + fileName);
				}
				if (fileName.endsWith(PNG_ICON_DAT_EXT) || fileName.endsWith(JPG_ICON_DAT_EXT)) {
					index_general_name_map.put(index, fileName);
					String pName = File.separatorChar + fileName;
					load_generalsResIcons(index, assetManager.open(RES_GENERAL_PATH + pName));
					index++;
				}
			}

			// 加载道路素材资源
			ArrayList<String> roadRes = getAssetsFilesBySort(assetManager, ICON_ROAD_PATH);
			// 加载图标数据
			index = 0;
			for (String fileName : roadRes) {
				if (SketchConfig.isDebug) {
					Log.d("Test", "load road element fileName:" + fileName);
				}
				if (fileName.endsWith(PNG_ICON_DAT_EXT) || fileName.endsWith(JPG_ICON_DAT_EXT)) {
					index_roads_name_map.put(index, fileName);
					String pName = File.separatorChar + fileName;
					load_roadsResIcons(index, assetManager.open(ICON_ROAD_PATH + pName));
					index++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Resource load error!");
		}

	}

	private static ArrayList<String> getAssetsFilesBySort(
			AssetManager assetManager, String path) throws IOException {
		String[] datFilter = assetManager.list(path);
		ArrayList<String> listFileName = new ArrayList<String>();
		for (String fileName : datFilter) {
			listFileName.add(fileName);
		}
		// 排序 ：优先使用数字排序
		Collections.sort(listFileName, new Comparator<String>() {
			@Override
			public int compare(String object1, String object2) {
				try {
					int v1 = Integer.parseInt(object1.substring(0, object1.length() - 4));
					int v2 = Integer.parseInt(object2.substring(0, object2.length() - 4));
					return v1 > v2 ? 1 : -1;
				} catch (Exception e) {
				}
				return object1.compareTo(object2);
			}
		});
		return listFileName;
	}

	public static LinkedHashMap<Integer, Bitmap> getRoads_icon_elements() {
		return roads_icon_elements;
	}

	public static LinkedHashMap<Integer, Bitmap> getGeneral_icon_elements() {
		return general_icon_elements;
	}

	public static String getNameByIndex(int index, boolean isRoadElement) {
		if (isRoadElement) {
			return index_roads_name_map.get(index);
		} else {
			return index_general_name_map.get(index);
		}

	}
    public static Bitmap load_roadResImage(int index){
    	String fileName=getNameByIndex(index,true);
    	AssetManager assetManager = AccidentSketchActivity.getInstance().getAssets();
    	InputStream is;
		try {
			is = assetManager.open(RES_ROAD_PATH +File.separatorChar + fileName);
			return loadBitmapByScale(is,1);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
	/**
	 * 加载本地图标文件
	 * 
	 * @param index
	 */
	private static void load_roadsResIcons(int index, InputStream is) {
		try {
			roads_icon_elements.put(index, BitmapFactory.decodeStream(is));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Resource load error on icons!");
		}
	}

	/**
	 * 一缩放方式加载图片，一减少内存使用
	 * 
	 * @param is
	 * @param inSampleSize 缩小倍数
	 * @return
	 */
	private static Bitmap loadBitmapByScale(InputStream is, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		return BitmapFactory.decodeStream(is, null, options);
	}

	/**
	 * 加载本地图标文件
	 * 
	 * @param index
	 */
	private static void load_generalsResIcons(int index, InputStream is) {
		try {
			general_icon_elements.put(index, BitmapFactory.decodeStream(is));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Resource load error on icons!");
		}
	}

	/**
	 * 清除内存
	 */
	public static void release() {
		SketchConfig.isHasadd = false;
		SystemConfig.isAddAccident = false;
		
		Collection<Bitmap> listRoad = roads_icon_elements.values();
		for (Bitmap bmp : listRoad) {
			bmp.recycle();
		}
		roads_icon_elements.clear();
		index_roads_name_map.clear();
		Collection<Bitmap> listGen = general_icon_elements.values();
		for (Bitmap bmp : listGen) {
			bmp.recycle();
		}

		general_icon_elements.clear();
		index_roads_name_map.clear();
	}
}
