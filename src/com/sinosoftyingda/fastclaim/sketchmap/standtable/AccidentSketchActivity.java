package com.sinosoftyingda.fastclaim.sketchmap.standtable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblGPSAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.model.PicAddress;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.sketchmap.view.StandTableView;
import com.sinosoftyingda.fastclaim.sketchmap.view.UIFloatedDragElementTool;
import com.sinosoftyingda.fastclaim.sketchmap.view.UIGeneralElementToolBar;
import com.sinosoftyingda.fastclaim.sketchmap.view.UIRoadsElementToolBar;

/**
 * 现场草图页面
 * 
 * @author DengGuang
 * 
 */
public class AccidentSketchActivity extends Activity {
	private static final String TAG = "MainActivity";

	// 屏幕大小（像素数）
	private int screenWidth;
	private int screenHeight;
	private static AccidentSketchActivity instance;

	/**
	 * 屏幕参数DPI与像素分辨率--缩放值：
	 */
	private float scaledDensity;
	private View roadsResetBtn;
	private View genResetBtn;

	/**
	 * 获取以DPI格式为参数的屏幕宽
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 获取以DPI格式为参数的屏幕高
	 * 
	 * @return
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * 获取以标准像素为参考的屏幕宽度
	 * 
	 * @return
	 */
	public int getDisplayWidth() {
		return (int) (screenWidth * scaledDensity);
	}

	/**
	 * 获取以标准像素为参考的屏幕高度
	 * 
	 * @return
	 */
	public int getDisplayHeight() {
		return (int) (screenHeight * scaledDensity);
	}

	public float getScaledDensity() {
		return scaledDensity;
	}

	public static AccidentSketchActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		// 无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (!SketchConfig.isHasadd) {
			SketchConfig.isHasadd = true;

			ResourceManager.loadAllIcon();
			getScreenSize();
			setContentView(R.layout.sitemap);

			final StandTableView standTable = (StandTableView) findViewById(R.id.StandTableView);
			loadSceneUI(standTable);
			standTable.readLocalMapData();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!SketchConfig.isHasadd) {
			SketchConfig.isHasadd = true;

			ResourceManager.loadAllIcon();
			getScreenSize();
			setContentView(R.layout.sitemap);

			final StandTableView standTable = (StandTableView) findViewById(R.id.StandTableView);
			loadSceneUI(standTable);
			standTable.readLocalMapData();
		}
	}

	/**
	 * 加载场景示意图中 工具UI
	 * 
	 * @param scene
	 */
	private void loadSceneUI(StandTableView scene) {
		// 普通素材
		LinearLayout genToolBarView = (LinearLayout) this.findViewById(R.id.general_element_tool_bar);
		final UIGeneralElementToolBar genToolBar = new UIGeneralElementToolBar(genToolBarView, scene);
		genToolBar.initElements(ResourceManager.getGeneral_icon_elements());
		View mainWindow = this.findViewById(R.id.main_window_scene);
		genResetBtn = this.findViewById(R.id.general_reset_btn);
		View parentArea = findViewById(R.id.general_bar_parent);

		genToolBar.setParentAreaView(parentArea);
		// 道路素材
		LinearLayout roadToolBarView = (LinearLayout) this.findViewById(R.id.roads_element_tool_bar);
		final UIRoadsElementToolBar roadToolBar = new UIRoadsElementToolBar(roadToolBarView, scene);
		roadToolBar.initElements(ResourceManager.getRoads_icon_elements());
		roadsResetBtn = this.findViewById(R.id.roads_reset_btn);
		parentArea = findViewById(R.id.roads_bar_parent);
		roadToolBar.setParentAreaView(parentArea);

		// 重置按钮监听器
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.general_reset_btn) {
					genToolBar.reset();
				} else if (v.getId() == R.id.roads_reset_btn) {
					roadToolBar.reset();
				}
			}
		};

		// 返回
		Button btnBack = (Button) findViewById(R.id.sitemap_btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				savePicInfo();
				finish();
			}
		});

		genResetBtn.setOnClickListener(onClickListener);
		roadsResetBtn.setOnClickListener(onClickListener);

		UIFloatedDragElementTool.newInstance((FrameLayout) findViewById(R.id.floated_drag_element_view), scene, mainWindow);
	}

	private void savePicInfo(){
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
	private void getScreenSize() {
		// 获取屏幕分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// dm.xdpi dm.ydpi 254 for g7
		scaledDensity = dm.scaledDensity;// added by cjm_20110614
		this.screenWidth = dm.widthPixels;
		this.screenHeight = dm.heightPixels;
	}

	@Override
	protected void onDestroy() {

		ResourceManager.release();

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		boolean isAddStandTable = autoSave();

		if (!isAddStandTable) {
			delSignturePic();
		}
	}

	/**
	 * 删除空白手写签名
	 */
	private void delSignturePic() {
		// /mnt/sdcard/cClaim/1001010010(报案号)/signture
		String signDir = SystemConfig.PHOTO_DIR + SystemConfig.PHOTO_CLAIMNO;
		FileUtils.mkDir(new File(signDir));
		signDir = signDir + SystemConfig.PHOTO_TYEP_6;
		FileUtils.mkDir(new File(signDir));

		String signDirTemp = signDir.replace(SystemConfig.PHOTO_DIR, SystemConfig.PHOTO_TEMP);
		FileUtils.mkDir(new File(signDirTemp));
		String signtureName = SystemConfig.PHOTO_CLAIMNO + "_Sketch.png";
		String signtureName2 = SystemConfig.PHOTO_CLAIMNO + "_Sketch.map";
		String signturePath = signDir + "/" + signtureName;
		String signturePath2 = signDir + "/" + signtureName2;
		String signturePathTemp = signDirTemp + "/" + signtureName;
		
		FileUtils fileUtils = new FileUtils();
		// 文件名集合
		List<String> signtureImages = new ArrayList<String>();
		// 查找当前目录里面的文件
		signtureImages = fileUtils.getFileName(new File(signDir), signDir);
		for (int i = 0; i < signtureImages.size(); i++) {
			if (signtureImages.get(i).equals(signturePath)) {
				FileUtils.deleteFile(signturePath);
				FileUtils.deleteFile(signturePath2);
			}
		}
		
		// 文件名集合
		List<String> signtureImagesTemp = new ArrayList<String>();
		// 查找当前目录里面的文件
		signtureImagesTemp = fileUtils.getFileName(new File(signDirTemp), signDirTemp);
		for (int i = 0; i < signtureImagesTemp.size(); i++) {
			if (signtureImagesTemp.get(i).equals(signturePathTemp)) {
				FileUtils.deleteFile(signturePathTemp);
			}
		}
	}

	/**
	 * 自动保存
	 * @return
	 */
	private boolean autoSave() {
		if (SketchConfig.isDebug) {
			Log.d(TAG, "autoSave...");
		}
		StandTableView standTable = (StandTableView) findViewById(R.id.StandTableView);
		standTable.saveMapData();
		boolean hasRoadElement = standTable.getMapSceneBackGroundId() != SketchMap.NULL_MAP_BACKGROUND;//
		boolean hasGenElement = standTable.getChildCount() > 0;

		return hasRoadElement || hasGenElement;
	}

}