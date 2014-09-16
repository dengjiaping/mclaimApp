package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.sketchmap.standtable.ResourceManager;


/**
 * 场景中：存放所有道路元素的工具栏<br>
 * 提供拖拽操作：可将素材栏中元素拖拽到 现场示意图主场景<br>
 * 本栏中所有元素只可同时操作一个类似RadioButon功能<br>
 * 
 * @author anychen
 * 
 */
public class UIRoadsElementToolBar extends UIElementBar {
	ColorMatrix colorMatrix = new ColorMatrix();
	/*
	 * 当前正在使用道路类图片资源
	 */
	private Bitmap mCurrentElementBitmap;
	private int mCurrentElementIndex;
	private static UIRoadsElementToolBar instance;

	public static UIRoadsElementToolBar getInstance() {
		return instance;
	}

	public UIRoadsElementToolBar(LinearLayout listView, StandTableView scene) {
		super(listView, scene);
		instance = this;
		colorMatrix.setSaturation(0);// 灰度处理
		openAllLocked();
	}

	@Override
	public void reset() {
		this.mapScene.clearMapLay1();
		if (mCurrentElementBitmap != null &&!mCurrentElementBitmap.isRecycled()) {
			mCurrentElementBitmap.recycle();
		}
		mCurrentElementBitmap=null;
		mCurrentElementIndex = -1;
		openAllLocked();
	}

	@Override
	protected void onSelectedElement(int position, int x, int y, int metaState,UIElementBar parentBar) {
		UIFloatedDragElementTool.getInstance().floatedDragElement(x, y,
				position, false, true, metaState,parentBar);
		unLockedSelectItem(position);
	}

	@Override
	public Bitmap getElementImage(int position) {
		//同一个素材，且改素材已经被加载了
		if (mCurrentElementIndex == position && mCurrentElementBitmap != null) {
			return mCurrentElementBitmap;
		}
		mCurrentElementBitmap = ResourceManager.load_roadResImage(position);
		return mCurrentElementBitmap;
	}

	/**
	 * *解锁选中的素材<br>
	 * 1、道路永远是在最底层，其他素材摆放不影响道路的位置。<br>
	 * 2、如果②现场事故示意图框没有道路，则③道路框内全部素材都是可拖拽状态。<br>
	 * 3、如果有道路，则③道路框只显示选择了的道路，非选中的道路显示为不可拖拽状态。<br>
	 * 4、③道路框内按钮只清空②现场事故示意图框内道路素材。<br>
	 * 5、道路素材不用显示在①记录框内。<br>
	 */
	public void unLockedSelectItem(int position) {
		int childCount = this.mListView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			ImageView childView = (ImageView) mListView.getChildAt(i);
			if (i == position) {
				childView.getDrawable().clearColorFilter();
				childView.setEnabled(true);
			} else {
				childView.getDrawable().setColorFilter(
						new ColorMatrixColorFilter(colorMatrix));
				childView.setEnabled(false);
			}
		}
	}

	/**
	 * 打开所有的锁
	 */
	private void openAllLocked() {
		int childCount = this.mListView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			ImageView childView = (ImageView) mListView.getChildAt(i);
			childView.getDrawable().clearColorFilter();
			childView.setEnabled(true);
		}
	}
}
