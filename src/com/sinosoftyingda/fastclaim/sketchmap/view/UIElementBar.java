package com.sinosoftyingda.fastclaim.sketchmap.view;

import java.util.Collection;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.AccidentSketchActivity;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchConfig;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchTool;

/**
 * 元素工具栏抽象父类，封装工具栏中公共属性及方法接口
 * 
 * @author anychen
 * 
 */
public abstract class UIElementBar implements View.OnTouchListener,
		OnGestureListener {
	private static final String TAG = "UIElementBar";
	/**
	 * 拼图沙盘主场景
	 */
	protected StandTableView mapScene;
	/**
	 * 素材工具栏视图
	 */
	protected LinearLayout mListView;
	private XHorizontalScrollView hScrollView;
	/**
	 * 素材元素
	 */
	protected LinkedHashMap<Integer, Bitmap> mElementsMap;
	/**
	 * 手势监听器
	 */
	private GestureDetector mGestureDetector;
	/**
	 * 当前工具栏中被选中的图标
	 */
	private View mSelectedView;
	private View mParentAreaView;

	/**
	 * 
	 * @param listView
	 * @param scene
	 */
	public UIElementBar(LinearLayout listView, StandTableView scene) {
		this.mListView = listView;
		this.mapScene = scene;
		hScrollView = (XHorizontalScrollView) mListView.getParent();
		mGestureDetector = new GestureDetector(this);
	}

	public View getSelectedView() {
		return mSelectedView;
	}

	public void setParentAreaView(View v) {
		mParentAreaView = v;
	}

	/**
	 * 重置场景中：录属于本工具栏中的所有元素
	 */
	public abstract void reset();

	/**
	 * 选中一个元素，准备拖拽到主场景中
	 * 
	 * @param position
	 *            素材索引ID
	 * @param x
	 * @param y
	 * @param metaState
	 *            触摸事件metaState
	 * @param parentBar
	 *            素材工具栏[拖拽完后需要消除拖拽时的触摸事件代理状态]
	 */
	protected abstract void onSelectedElement(int position, int x, int y,
			int metaState, UIElementBar parentBar);

	/**
	 * 获取工具栏指定位置图片
	 * 
	 * @param position
	 * @return
	 */
	public abstract Bitmap getElementImage(int position);

	/**
	 * 初始化素材工具条
	 * 
	 * @param elements
	 */
	public void initElements(LinkedHashMap<Integer, Bitmap> elements) {
		this.mElementsMap = elements;
		buildElementsBarUI();
	}

	/**
	 * 将场景中元素编译成UI
	 */
	private void buildElementsBarUI() {
		Context context = AccidentSketchActivity.getInstance().getApplicationContext();
		Collection<Bitmap> elements = mElementsMap.values();
		int width = SketchConfig.getElement_view_width();
		int high = SketchConfig.getElement_view_height();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				high);
		int positionIndex = 0;
		for (Bitmap img : elements) {
			ImageView item = (ImageView) View.inflate(context,
					R.layout.sitemap_element_view, null);
			item.setOnTouchListener(this);
			item.setImageBitmap(img);
			item.setId(positionIndex);// 将位置索引设为ID

			positionIndex++;
			lp.leftMargin = 2;
			lp.rightMargin = 2;
			mListView.addView(item, lp);
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		clearTouchDelegateView();
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		int mBarLpX = mParentAreaView.getLeft();
		int mBarLpY = mParentAreaView.getTop();
		HorizontalScrollView hs = (HorizontalScrollView) mParentAreaView
				.findViewById(R.id.HorizontalScrollView);
		int absX = mBarLpX + mSelectedView.getLeft() - hs.getScrollX();
		int absY = mBarLpY + mSelectedView.getTop();
		Log.d(TAG, "onLongPress absX=" + absX + " absY=" + absY);
		int index = mSelectedView.getId();
		SketchTool.virbrate();

		// mSelectedView.cancelLongPress();
		// mSelectedView.clearFocus();
		hScrollView.setTouchDelegateView(UIFloatedDragElementTool.getInstance().getFloatDragElementView());
		onSelectedElement(index, absX, absY, e.getMetaState(), this);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// 注意如果还需要连续处理其他onTouch事件，则 不能在此方法中处理，一旦触发了此事件，则无法触发其他触摸事件
		// 当长按某素材图标时，将此素材浮起并放到可拖拽的视图区容器，开始拖拽操作
		// 工具条在全景布局中相对坐标
		/*
		int mBarLpX = mParentAreaView.getLeft();
		int mBarLpY = mParentAreaView.getTop();
		HorizontalScrollView hs = (HorizontalScrollView) mParentAreaView
				.findViewById(R.id.HorizontalScrollView);
		int absX = mBarLpX + mSelectedView.getLeft() - hs.getScrollX();
		int absY = mBarLpY + mSelectedView.getTop();
		Log.d(TAG, "onLongPress absX=" + absX + " absY=" + absY);
		int index = mSelectedView.getId();
		Tool.virbrate();

		// mSelectedView.cancelLongPress();
		// mSelectedView.clearFocus();
		hScrollView.setTouchDelegateView(UIFloatedDragElementTool.getInstance()
				.getFloatDragElementView());
		onSelectedElement(index, absX, absY, e.getMetaState(), this);
		*/
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.mSelectedView = v;
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		return true;// 注意需要吃掉事件才能在滚动时不响应长按事件
	}

	/**
	 * 清除事件代理
	 */
	protected void clearTouchDelegateView() {
		this.hScrollView.setTouchDelegateView(null);
	}
}
