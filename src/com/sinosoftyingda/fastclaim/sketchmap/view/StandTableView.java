package com.sinosoftyingda.fastclaim.sketchmap.view;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.AccidentSketchActivity;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchConfig;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchMap;


/**
 * 沙盘页面布局容器主类，沙盘中所有的物体对象都放置于此处
 * 
 * @author anychen
 * 
 */
public class StandTableView extends FrameLayout implements OnTouchListener,OnGestureListener {
	
	private static final String TAG = "StandTableView";
	public static StandTableView instance;
	private static View currentEditingView;
	private GestureDetector mGestureDetector;
	private int mSceneBackGroundId;
	Paint mPaint = new Paint();

	/**
	 * 构造函数，选择实现这个两个参数的构造函数主要是可以支持eclipseADT中可视化设计布局。
	 */
	public StandTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		instance = this;
		this.setFocusable(true); // 让此view能够获得各类事件
		this.setFocusableInTouchMode(true); // 让此view能够获得各类事件
		mGestureDetector = new GestureDetector(this);
		this.setOnTouchListener(this);
	}

	private void rotate(int degree) {
		if (currentEditingView instanceof XImgView) {
			((XImgView) currentEditingView).rotate(degree);
		}
	}

	private void rotateRight() {
		if (currentEditingView instanceof XImgView) {
			((XImgView) currentEditingView).rotateRight();
		}
	}

	private void rotateLeft() {
		if (currentEditingView instanceof XImgView) {
			((XImgView) currentEditingView).rotateLeft();
		}
	}

	/**
	 * 沙盘场景背景
	 * 
	 * @param bgDrawable
	 */
	public void setMapSceneBackGround(BitmapDrawable bgDrawable, int id) {
		this.mSceneBackGroundId = id;
		this.setBackgroundDrawable(bgDrawable);
	}

	public int getMapSceneBackGroundId() {
		return mSceneBackGroundId;
	}

	public void addItemViewToMap(View selectView) {
		this.addView(selectView);
		selectedView(selectView.findViewById(R.id.item_icon));
	}

	public void clearMapLay1() {
		this.mSceneBackGroundId = SketchMap.NULL_MAP_BACKGROUND;
		this.setBackgroundResource(R.color.white);
	}

	public void clearMapLay2() {
		this.removeAllViews();
		currentEditingView = null;
	}

	public void removeCurrentItemViewFromMap() {
		this.removeView((View) currentEditingView.getParent());
		currentEditingView.setSelected(false);
		currentEditingView = null;
		// itemRotBar.setVisibility(View.INVISIBLE);
	}

	// private static void showItemMenu() {
	// if (currentItemView instanceof XImgView) {
	// itemRotBar.setVisibility(View.VISIBLE);
	// rot_left_btn.setVisibility(View.VISIBLE);
	// rot_right_btn.setVisibility(View.VISIBLE);
	// XImgView view = (XImgView) currentItemView;
	// moveRotBar(false, view.getFloatMenuX(), view.getFloatMenuY(),
	// view.getFloatMenuWidth(), view.getFloatMenuHeight());
	// } else {
	// rot_left_btn.setVisibility(View.INVISIBLE);
	// rot_right_btn.setVisibility(View.INVISIBLE);
	// itemRotBar.setVisibility(View.VISIBLE);
	// XEditorText view = (XEditorText) currentItemView;
	// moveRotBar(true, view.getFloatMenuX(), view.getFloatMenuY(),
	// view.getFloatMenuWidth(), view.getFloatMenuHeight());
	// }
	// }

	public static View getCurrentItem() {
		return currentEditingView;
	}

	/**
	 * 
	 * @param isText
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void moveRotBar(boolean isText, int x, int y, int width,
			int height) {

		// if (!isText) {// 非文本组件
		// int rotLeftWidth = itemRotBar.findViewById(R.id.rot_left_btn)
		// .getWidth();
		// int rotRighttWidth = itemRotBar.findViewById(R.id.rot_right_btn)
		// .getWidth();
		// width += rotLeftWidth + rotRighttWidth;
		// x -= rotLeftWidth;
		// height += itemRotBar.findViewById(R.id.delete_btn).getHeight();
		// }
		//
		// AbsoluteLayout.LayoutParams view_para = null;
		// if (itemRotBar.getLayoutParams() == null) {
		// view_para = new AbsoluteLayout.LayoutParams(width, height, x, y);
		// } else {
		// view_para = (android.widget.AbsoluteLayout.LayoutParams) itemRotBar
		// .getLayoutParams();
		// view_para.width = width;
		// view_para.height = height;
		// ((AbsoluteLayout.LayoutParams) view_para).x = x;
		// ((AbsoluteLayout.LayoutParams) view_para).y = y;
		// }
		// itemRotBar.setLayoutParams(view_para);
		// // itemRotBar.setBackgroundColor(color.ltblue);
		// if (Config.isDebug) {
		// Log.v("Test_moveRotBar", "moveRotBar view_para.x=" + view_para.x
		// + " view_para.y=" + view_para.y);
		// }

	}

	public static boolean hasSelectedMe(View me) {
		return me.equals(currentEditingView);
	}

	public static void selectedView(View focusedView) {
		if (currentEditingView != null) {
			currentEditingView.setSelected(false);
			if (currentEditingView instanceof XImgView) {
				((XImgView) currentEditingView).setCanEditble(false);
			} else if (currentEditingView instanceof XEditorText) {
				((XEditorText) currentEditingView).setCanEditble(false);
			}
		}
		if (focusedView instanceof XImgView) {
			((XImgView) focusedView).setCanEditble(true);
		}
		focusedView.setSelected(true);
		currentEditingView = focusedView;
	}

	public void reset() {
		this.removeAllViews();
		currentEditingView = null;
		// itemRotBar.setVisibility(View.INVISIBLE);
	}

	/**
	 * 保存为图片
	 */
	public void saveMapData() {
		if (!isAvaiableSDcard()) {
			this.showTip(R.string.tip_error_sdcard_unavailable);
			return;
		}
		SketchMap.save(this);
	}

	public void readLocalMapData() {
		try {
			SketchMap.readMap(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showTips(String msg) {
		// 使用Toast提示下存储路径
		Toast toast = Toast.makeText(AccidentSketchActivity.getInstance()
				.getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	private void showTip(int resId) {
		showTips(SketchConfig.getString(resId));
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

	public Bitmap getViewMirror() {
		  if (currentEditingView != null) {
		   currentEditingView.setSelected(false);//取消选中状态
		   currentEditingView.clearFocus();
		  }
		  this.destroyDrawingCache();//先销毁，避免使用已经被回收的对象
		  this.setDrawingCacheEnabled(true);
		  this.invalidate();
		  this.buildDrawingCache();
		  Bitmap bitmap = this.getDrawingCache();
		  return bitmap;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		if (SketchConfig.isDebug) {
			Log.v(TAG, "onDown");
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		if (SketchConfig.isDebug) {
			Log.v(TAG, "onShowPress");
		}

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (SketchConfig.isDebug) {
			Log.v(TAG, "onSingleTapUp");
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (SketchConfig.isDebug) {
			Log.v(TAG, "onScroll");
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if (SketchConfig.isDebug) {
			Log.v(TAG, "onLongPress");
		}

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (SketchConfig.isDebug) {
			Log.v(TAG, "onFling");
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	public boolean isInMapSceneArea(int x, int y) {
		int lx = this.getLeft();
		int ly = this.getTop();
		int width = this.getWidth();
		int high = this.getHeight();
		Rect scenRect = new Rect(lx, ly, lx + width, ly + high);
		return scenRect.contains(x, y);
	}
}
