package com.sinosoftyingda.fastclaim.sketchmap.standtable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.sketchmap.view.ElementCons;
import com.sinosoftyingda.fastclaim.sketchmap.view.StandTableView;
import com.sinosoftyingda.fastclaim.sketchmap.view.UIGeneralElementToolBar;
import com.sinosoftyingda.fastclaim.sketchmap.view.UIRoadsElementToolBar;
import com.sinosoftyingda.fastclaim.sketchmap.view.XEditorText;
import com.sinosoftyingda.fastclaim.sketchmap.view.XImgView;

public class SketchMap {
	private static final String TAG = "Map";
	public static final String MAP_HEAD = "map_head";
	public static final String VIEW_NUM = "view_count=";
	public static final String VIEW_CONTENT = "content=";
	public static final String VIEW_TYPE = "type=";
	public static final String VIEW_X_Y_W_H = "X_Y_W_H=";

	public static final int VIEW_TYPE_XImgView = 0x0;
	public static final int VIEW_TYPE_XEditorText = 0x1;
	public static final int NULL_MAP_BACKGROUND=-1;

	static StandTableView standtable;

	public static void save(StandTableView mapView) {
		String path = getSavePath();
		//String path = SystemConfig.PHOTO_DIR+SystemConfig.PHOTO_CLAIMNO+"/"+getSavePath();
		Log.i("Accident", "path="+path);
		
		File savePathFile = new File(path);
		if (!savePathFile.exists()) {
			savePathFile.mkdir();
		}
		try {
			saveToImage(savePathFile, mapView.getViewMirror());
			saveMap(savePathFile, mapView);
			showSaveOkTips(path);

			String picFormat = SketchConfig.getImageFormat();
			String picName = SketchConfig.getSaveFileName()+"."+picFormat;
			String pathTemp = path.replace(SystemConfig.PHOTO_DIR, SystemConfig.PHOTO_TEMP);
			File fromFile = new File(path+"/"+picName);
			File toFile = new File(pathTemp+"/"+picName);
			FileUtils fileUtils = new FileUtils();
			fileUtils.copyfile(fromFile, toFile, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			showSaveOkTips(SketchConfig.getString(R.string.tip_error_path) + e.toString());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			showSaveOkTips(SketchConfig.getString(R.string.tip_error_create_image) + e.toString());
		}

	}

	/**
	 * 保存为图片
	 * 
	 * @throws IOException
	 */
	private static void saveToImage(File pathFile, Bitmap img)
			throws IOException {
		try {
			String fileName = getSaveFileImageName();
			File f = new File(pathFile, fileName);
			FileOutputStream fos = new FileOutputStream(f);
			img.compress(getConfigImageFormat(), SketchConfig.getImageQuality(), fos);
		} finally {
			img.recycle();// 注意回收内存
		}

	}

	/**
	 * 保存地图数据文件： 文件格式如下： <br>
	 * 文件头:String:map <br>
	 * 组件个数：int <br>
	 * 组件对象类型 byte <br>
	 * 组件内容：如果是XImgView 存储 组件ID,缩放参数,如果是文本存储文本内容<br>
	 * 组件对象位置 Int X,int Y,w,h<br>
	 * 
	 * @param pathFile
	 * @throws IOException
	 */
	private static void saveMap(File pathFile, StandTableView mapView)
			throws IOException {
		File f = new File(pathFile, getSaveFileMapName());
		FileOutputStream fos = new FileOutputStream(f);
		DataOutputStream dos = new DataOutputStream(fos);
		if (SketchConfig.isDebug) {
			Log.d(TAG, "write MAP_HEAD " + SketchMap.MAP_HEAD);
		}
		dos.writeUTF(SketchMap.MAP_HEAD);

		// 地图背景
		dos.writeInt(mapView.getMapSceneBackGroundId());

		int childSize = mapView.getChildCount();
		if (SketchConfig.isDebug) {
			Log.d(TAG, "write childSize=" + childSize);
		}

		dos.writeInt(childSize);
		for (int i = 0; i < childSize; i++) {
			View child = mapView.getChildAt(i);
			if (child instanceof AbsoluteLayout) {
				AbsoluteLayout absLayout = (AbsoluteLayout) child;
				int grandsonSize = absLayout.getChildCount();
				View grandson = absLayout.getChildAt(0);
				AbsoluteLayout.LayoutParams absLP = (AbsoluteLayout.LayoutParams) grandson
						.getLayoutParams();
				int x = absLP.x;
				int y = absLP.y;
				int width = absLP.width;
				int height = absLP.height;
				Log.d(TAG, "write  Map.VIEW_X_Y_W_H:" + grandson.toString()
						+ " absLP:" + "x=" + x + " y=" + y + " width=" + width
						+ " height=" + height);
				dos.writeUTF(SketchMap.VIEW_X_Y_W_H);
				dos.writeInt(x);
				dos.writeInt(y);
				dos.writeInt(width);
				dos.writeInt(height);

				Log.d(TAG, "write VIEW_TYPE=" + VIEW_TYPE);
				dos.writeUTF(SketchMap.VIEW_TYPE);
				if (grandson instanceof XImgView) {// 图片类
					// 组件对象类型 byte
					Log.d(TAG, "write byte VIEW_TYPE_XImgView-"
							+ SketchMap.VIEW_TYPE_XImgView);
					dos.writeByte(SketchMap.VIEW_TYPE_XImgView);
					XImgView xview = ((XImgView) grandson);
					// 组件内容
					Log.d(TAG,
							"write byte xview.getImageId-" + xview.getImageId());
					dos.writeInt(xview.getImageId());
					Log.d(TAG, "write float xview.scale-" + xview.getScale());
					dos.writeFloat(xview.getScale());
					// 保存旋转角度
					dos.writeInt(xview.getRot());
					// 元素类型
					dos.writeInt(xview.getElementType());
				} else if (grandson instanceof XEditorText) {// 文本输入类
					dos.writeByte(SketchMap.VIEW_TYPE_XEditorText);
					// 组件内容
					dos.writeUTF(((XEditorText) grandson).getText().toString());
					// 元素类型
					dos.writeInt(ElementCons.TYPE_GENERAL_ELEMENT);
				}
			}
		}

	}

	public static void readMap(StandTableView mapView) throws IOException {

		File pathFile = new File(getMapFilePath());
		if (!pathFile.exists()) {
			// 没有保存的问件
			return;
		}
		if (SketchConfig.isDebug) {
			Log.d(TAG, "readMap inLocal");
		}
		FileInputStream fis = new FileInputStream(pathFile);
		DataInputStream dis = new DataInputStream(fis);
		String head = dis.readUTF();
		if (SketchConfig.isDebug) {
			Log.d(TAG,
					"head " + head + " is valid=" + (SketchMap.MAP_HEAD.equals(head)));
		}
		if (!SketchMap.MAP_HEAD.equals(head)) {
			return;
		}
		// 地图背景
		int MapSceneBackGroundId = dis.readInt();
		if (MapSceneBackGroundId != NULL_MAP_BACKGROUND) {
			Bitmap bmp = UIRoadsElementToolBar.getInstance().getElementImage(
					MapSceneBackGroundId);
			UIRoadsElementToolBar.getInstance().unLockedSelectItem(
					MapSceneBackGroundId);
			mapView.setMapSceneBackGround(new BitmapDrawable(bmp),
					MapSceneBackGroundId);
		} else {
			mapView.clearMapLay1();
		}


		int childSize = dis.readInt();
		Log.d(TAG, "read childSize " + childSize);
		for (int i = 0; i < childSize; i++) {
			// 读取布局参数：
			String parmFlag = dis.readUTF();
			Log.d(TAG, "read parmFlag-" + parmFlag);
			int lpx = 0, lpy = 0, lpw = 0, lph = 0;
			if (parmFlag.equals(SketchMap.VIEW_X_Y_W_H)) {
				lpx = dis.readInt();
				lpy = dis.readInt();
				lpw = dis.readInt();
				lph = dis.readInt();
				Log.d(TAG, "read  Map.VIEW_X_Y_W_H:" + "x=" + lpx + " y=" + lpy
						+ " width=" + lpw + " height=" + lph);
			}

			// 读取类型
			parmFlag = dis.readUTF();
			if (parmFlag.equals(SketchMap.VIEW_TYPE)) {
				byte type = dis.readByte();

				if (type == VIEW_TYPE_XImgView) {
					int imageId = dis.readInt();
					Log.d(TAG, "read int xview.imageId=" + imageId);
					float scale = dis.readFloat();
					int rot = dis.readInt();
					Log.d(TAG, "read float xview.scale=" + scale
							+ "  xview.rot=" + rot);
					View itemView = View.inflate(AccidentSketchActivity.getInstance(),
							R.layout.sitemap_item_xylayout, null);
					XImgView newView = (XImgView) itemView
							.findViewById(R.id.item_icon);
					int elementType = dis.readInt();

					if (elementType == ElementCons.TYPE_GENERAL_ELEMENT) {
						Bitmap bmp1 = UIGeneralElementToolBar.getInstance()
								.getElementImage(imageId);
						newView.initilaze(lpx, lpy, lpw, lph, scale, imageId,
								rot, bmp1, elementType);
						mapView.addItemViewToMap(itemView);
					}

				} else {
					String text = dis.readUTF();
					View itemView = View.inflate(AccidentSketchActivity.getInstance(),
							R.layout.sitemap_item_text_xylayout, null);
					XEditorText newView = (XEditorText) itemView
							.findViewById(R.id.item_icon);
					newView.setText(text);
					newView.showLocation(lpx, lpy);
					mapView.addItemViewToMap(itemView);
				}

			}
		}
	}

	private static String getSaveFileImageName() {
		Log.i("Accident", "name="+SketchConfig.getSaveFileName());
		return SketchConfig.getSaveFileName() + "." + SketchConfig.getImageFormat();
	}

	private static String getSaveFileMapName() {
		Log.i("Accident", "map="+SketchConfig.getSaveFileName());
		return SketchConfig.getSaveFileName() + ".map";
	}

	private static String getMapFilePath() {
		return getSavePath() + File.separator + getSaveFileMapName();
	}

	private static CompressFormat getConfigImageFormat() {
		if (SketchConfig.getImageFormat().equalsIgnoreCase("jpeg")) {
			return CompressFormat.JPEG;
		} else {
			return CompressFormat.PNG;
		}
	}

	private static void showSaveOkTips(String msg) {
		// 使用Toast提示下存储路径

		String tip = SketchConfig.getString(R.string.tip_save_image_ok) + ":" + msg;
		showTips(tip);
	}

	private static void showTips(String msg) {
		// 使用Toast提示下存储路径
		Toast toast = Toast.makeText(AccidentSketchActivity.getInstance()
				.getApplicationContext(), msg, Toast.LENGTH_SHORT);
	}

	private static void showTip(int resId) {
		showTips(SketchConfig.getString(resId));
	}

	private static String getSavePath() {
		return SketchConfig.getImageDir();
	}

	public static boolean isAvaiableSDcard() {
		String sdCardState = android.os.Environment.getExternalStorageState();
		if (SketchConfig.isDebug) {
			Log.d("Test", "sdCardState=" + sdCardState);
		}
		return android.os.Environment.MEDIA_MOUNTED.equals(sdCardState);
	}

//	public static String getSDcardPath() {
//		try {
//			File SDFile = android.os.Environment.getExternalStorageDirectory();
//			return SDFile.getCanonicalPath();
//		} catch (IOException e) {
//			return null;
//		}
//	}
}
