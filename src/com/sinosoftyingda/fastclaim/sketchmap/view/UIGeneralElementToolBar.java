package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.graphics.Bitmap;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.sketchmap.standtable.ResourceManager;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchConfig;

/**
 * 普通素材工具栏管理工具类<br>
 * 本栏存放除道路以外的其他所有素材 提供拖拽功能：可将素材栏中元素拖拽到 现场示意图主场景<br>
 * 
 * @author anychen
 * 
 */
public class UIGeneralElementToolBar extends UIElementBar {
	private static UIGeneralElementToolBar instance;

	public static UIGeneralElementToolBar getInstance() {
		return instance;
	}

	public UIGeneralElementToolBar(LinearLayout listView, StandTableView scene) {
		super(listView, scene);
		instance = this;
	}

	@Override
	public void reset() {
		this.mapScene.clearMapLay2();

	}

	/**
	 * 获取工具栏指定位置图片
	 * 
	 * @param position
	 * @return
	 */
	@Override
	public Bitmap getElementImage(int position) {
		return mElementsMap.get(position);
	}

	/**
	 * 判断某元素是否是文本对象
	 * 
	 * @param position
	 * @return
	 */
	private boolean isEditTextObj(int position) {
		return SketchConfig.getAsset_input_text_name().equalsIgnoreCase(
				ResourceManager.getNameByIndex(position, false));
	}

	@Override
	protected void onSelectedElement(int position, int x, int y, int metaState,UIElementBar parentBar) {
		UIFloatedDragElementTool.getInstance().floatedDragElement(x, y,
				position, isEditTextObj(position), false, metaState,parentBar);

	}

}
