package com.sinosoftyingda.fastclaim.sketchmap.standtable;

import java.io.File;

import android.content.res.Resources;
import android.util.Log;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;


/**
 * 程序中配置参数类，用来读取res/values/config.xml下配置参数，供程序使用
 * @author anychen
 * 
 */
public class SketchConfig {
	
	public static boolean isDebug = false;
	/**
	 * 原始缩放参数
	 */
	private static float originalSize = 100.0f;
	/**
	 * 保存沙盘快照图片的图片格式
	 */
	private static String imageFormat = "png";
	/**
	 * 沙盘快照图片存储目录位于:SDCARD/imageDir
	 */
	private static String imageDir = "/Accident";
	/**
	 * 沙盘快照图片质量【值越大越清晰，对应文件容量也越大】
	 */
	private static int imageQuality = 60;
	 /**
	  * 沙盘中素材最大放大倍数
	  */
	private static float maxScale = 4.0f;
	/**
	 * 沙盘中素材最小缩放倍数
	 */
	private static float minScale = 0.5f;
	/**
	 * 沙盘中素材 缩放基准参数【双击缩放时用，目前舍弃】
	 */
	private static float midScale = 1.0f;
	/**
	 * 文本输入框对应的素材图标，如果素材中图标文件名是此值将被认为是特定文本输入框组件
	 */
	private static String asset_input_text_name = "text_icon.png";
	/**
	 * 沙盘中素材：最小缩放尺寸，为了保证缩放的可操作性-缩放时将
	 */
	private static int minScaleDisplaySize = 100;

	/**
	 * 素材工具栏面板UIToolPanel中每个素材图标的显示的参考尺寸
	 */
	private static int grid_item_view_size = 90;
	
	
	private static int element_view_width=48;
	
	private static int element_view_height=80;
	/**
	 * 沙盘快照图片存储文件名，位于：SDCARD/imageDir/saveFileName
	 */
	private static String saveFileName = SystemConfig.PHOTO_CLAIMNO+"_Sketch";
	
	/**
	 * 振动时间
	 */
	private static int virbrate_time = 10;
	public static boolean isHasadd = false;
	
	static {
		Resources res = AccidentSketchActivity.getInstance().getResources();
		imageQuality = res.getInteger(R.integer.save_img_quality);
		imageFormat = res.getString(R.string.save_img_format);
		
		maxScale = res.getInteger(R.integer.image_max_scale) / originalSize;
		minScale = res.getInteger(R.integer.image_min_scale) / originalSize;
		virbrate_time= res.getInteger(R.integer.virbrate_time);
		minScaleDisplaySize = res.getInteger(R.integer.image_min_scale_display_size);
		grid_item_view_size = (int) res.getDimension(R.dimen.grid_item_view_size);
		asset_input_text_name = res.getString(R.string.asset_input_text_name);
		saveFileName = SystemConfig.PHOTO_CLAIMNO+"_Sketch";
		
		element_view_width=(int) res.getDimension(R.dimen.elements_width);
		element_view_height=(int) res.getDimension(R.dimen.elements_height);
	}
	public static int getVirbrate_time() {
		return virbrate_time;
	}
	public static int getElement_view_width() {
		return element_view_width;
	}
	public static int getElement_view_height() {
		return element_view_height;
	}
	public static String getSaveFileName() {
		saveFileName = SystemConfig.PHOTO_CLAIMNO+"_Sketch";
		Log.i("PhotoDir", "saveFileName2="+saveFileName);
		return saveFileName;
	}

	public static String getAsset_input_text_name() {
		return asset_input_text_name;
	}

	public static int getGrid_item_view_size() {
		return grid_item_view_size;
	}

	public static float getOriginalSize() {
		return originalSize;
	}

	public static float getMaxScale() {
		return maxScale;
	}

	public static float getMinScale() {
		return minScale;
	}

	public static float getMidScale() {
		return midScale;
	}

	public static int getMinScaleDisplaySize() {
		return minScaleDisplaySize;
	}

	public static String getString(int id) {
		return AccidentSketchActivity.getInstance().getResources().getString(id);
	}

	public static String getImageFormat() {
		return imageFormat;
	}

	public static String getImageDir() {
		imageDir = SystemConfig.PHOTO_DIR+SystemConfig.PHOTO_CLAIMNO;
		FileUtils.mkDir(new File(imageDir));
		imageDir = imageDir+SystemConfig.PHOTO_TYEP_6;
		FileUtils.mkDir(new File(imageDir));
		
		return imageDir;
	}

	public static int getImageQuality() {
		return imageQuality;
	}

}
