package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.AccidentSketchActivity;

/**
 * 用于包装一个可浮起在主场景内的 并可拖动的视图
 * 
 * @author anychen
 * 
 */
public class UIFloatedDragElementTool implements OnTouchListener {
	private static UIFloatedDragElementTool instance;
	private static final String TAG = "FloatedAction";
	/**
	 * 窗口全景视图
	 */
	private View mWindowScene;
	/**
	 * 浮起的视图拖拽坐标布局参数
	 */
	private AbsoluteLayout.LayoutParams mAbsXY_layout;
	/**
	 * 浮起的可拖拽视图布局容器
	 */
	private FrameLayout mFloatDragElementView;
	/**
	 * 浮起的可拖拽视图的蒙板
	 */
	private ImageView mFloatDragMatteView;
	/**
	 * 浮起的可拖拽视图的图标视图
	 */
	private ImageView mFloatDragIocnView;
	 
	/**
	 * 拼图沙盘主场景
	 */
	protected StandTableView mapScene;

	/**
	 * 浮起拖动状态中
	 */
	private boolean isFloatDraging;

	private FloatElement floatedElement;
	/**
	 * 当前浮起的可拖拽视图所属的工具栏【即从mParentBar拖出来的视图，拖拽完后需要消除拖拽状态】
	 */
	private UIElementBar mParentBar;
	public static UIFloatedDragElementTool getInstance() {
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	public static void newInstance(FrameLayout floatedDragView,
			StandTableView scene, View windowScene) {
		instance = new UIFloatedDragElementTool(floatedDragView, scene,
				windowScene);

	}
	public View getFloatDragElementView() {
		return mFloatDragElementView;
	}
	private UIFloatedDragElementTool(FrameLayout floatedDragView,
			StandTableView scene, View windowScene) {
		this.mapScene = scene;
		 
		mFloatDragElementView = floatedDragView;
		mAbsXY_layout = (android.widget.AbsoluteLayout.LayoutParams) floatedDragView
				.getLayoutParams();
		mFloatDragElementView.setOnTouchListener(this);
		mFloatDragMatteView=(ImageView) mFloatDragElementView.findViewById(R.id.floated_drag_element_matte_view);
		mFloatDragIocnView=(ImageView) mFloatDragElementView.findViewById(R.id.floated_drag_element_icon_view);
	}

	/**
	 * 在全景视图上浮起一个 可拖拽的视图
	 * 
	 * @param x
	 * @param y
	 * @param imageId
	 * @param isRoadElement
	 */
	protected void floatedDragElement(int x, int y, int imageId,
			boolean isEditorText, boolean isRoadElement, int metaState,UIElementBar parentBar) {
		this.mParentBar=parentBar;
		if (isRoadElement) {
			Bitmap bmp = UIRoadsElementToolBar.getInstance().getElementImage(
					imageId);
			BitmapDrawable bgDrawable = new BitmapDrawable(bmp);
			mFloatDragIocnView.setImageBitmap(bmp);
			floatedElement = new FloatElement(bgDrawable,
					ElementCons.TYPE_ROAD_ELEMENT, imageId);
		} else {
			Bitmap bmp = UIGeneralElementToolBar.getInstance().getElementImage(
					imageId);
			View view = null;
			if (isEditorText) {
				view = View.inflate(AccidentSketchActivity.getInstance(),
						R.layout.sitemap_item_text_xylayout, null);
			} else {
				view = View.inflate(AccidentSketchActivity.getInstance(),
						R.layout.sitemap_item_xylayout, null);
				XImgView newView = (XImgView) view.findViewById(R.id.item_icon);
				newView.setElementType(ElementCons.TYPE_GENERAL_ELEMENT);
				newView.setImageBitmap(imageId, bmp);
			}
			floatedElement = new FloatElement(view,
					ElementCons.TYPE_GENERAL_ELEMENT, isEditorText, imageId);
			mFloatDragIocnView.setImageBitmap(bmp);
		}
		isFloatDraging = true;
		mFloatDragElementView.setVisibility(View.VISIBLE);
		
		mFloatDragMatteView.setImageResource(R.color.red2);
		mAbsXY_layout.x = x;
		mAbsXY_layout.y = y;
		mFloatDragElementView.setLayoutParams(mAbsXY_layout);
		mFloatDragElementView.requestFocus();
	}

	 
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 将局部区域坐标，转换为全局视图坐标
		Log.d(TAG, "onTouch " + event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG, "ACTION_MOVE " + event.getAction() + " PointerCount= "
					+ event.getPointerCount());
			mAbsXY_layout.x = (int) event.getX()
					- mFloatDragElementView.getWidth() / 2;
			mAbsXY_layout.y = (int) event.getY()
					- mFloatDragElementView.getHeight() / 2;
			mFloatDragElementView.setLayoutParams(mAbsXY_layout);
			// 如果处于有效区域，背景变录
			if (isInMapSceneArea(mAbsXY_layout.x, mAbsXY_layout.y)) {
				mFloatDragMatteView.setImageResource(R.color.green);
			} else {// 背景红，不可放置区域
				mFloatDragMatteView.setImageResource(R.color.red2);
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "ACTION_UP " + event.getAction());
			onTouchUp(event);
			break;

		}
		 
		return true;
	}
    
	/**
	 * 弹起事件，将对应拖拽视图放入沙盘场景中
	 * 
	 * @param event
	 * @return
	 */
	private boolean onTouchUp(MotionEvent event) {

		Log.d(TAG, "onTouchUp isFloatDraging=" + isFloatDraging);
		mParentBar.clearTouchDelegateView();
		// 检测当前拖拽的视图是否在沙盘工作区域，如果是则放入沙盘，否则丢弃
		if (isFloatDraging) {
			if (isInMapSceneArea((int) event.getX(), (int) event.getY())) {
				isFloatDraging = false;
				if (floatedElement.mElementType == ElementCons.TYPE_ROAD_ELEMENT) {
					mapScene.setMapSceneBackGround((BitmapDrawable) floatedElement.mObj,
							floatedElement.mImageId);
				} else {
					View itemView = (View) floatedElement.mObj;
					if (floatedElement.isTextEditor) {
						XEditorText textView = (XEditorText) itemView.findViewById(R.id.item_icon);
						textView.showLocation(mAbsXY_layout.x, mAbsXY_layout.y);
					} else {
						XImgView imgView = (XImgView) itemView.findViewById(R.id.item_icon);
						imgView.showLocation(mAbsXY_layout.x, mAbsXY_layout.y);
					}
					mapScene.addItemViewToMap(itemView);
				}
			} else {
				this.floatedElement = null;
			}
			mFloatDragElementView.setVisibility(View.INVISIBLE);
		}
		return true;
	}

	private boolean isInMapSceneArea(int x, int y) {
		return mapScene.isInMapSceneArea(x, y);
	}

	class FloatElement {
		public FloatElement(Object obj, int type, int imgeId) {
			this(obj, type, false, imgeId);
		}

		public FloatElement(Object obj, int type, boolean isTextEditor,
				int imageId) {
			this.mObj = obj;
			this.mElementType = type;
			this.isTextEditor = isTextEditor;
			this.mImageId = imageId;
		}

		/**
		 * 当前选中的待放入沙盘场景中的素材视图
		 */
		private Object mObj;
		/**
		 * 素材类型
		 */
		private int mElementType;
		private boolean isTextEditor;
		private int mImageId;
	}

}
