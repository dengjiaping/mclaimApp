package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.content.Context;
import android.os.SystemClock;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.EditText;

import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchConfig;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchTool;

/**
 * 扩展的输入框，可自由拖动
 * 
 * @author anychen
 * 
 */
public class XEditorText extends EditText implements OnTouchListener,
		OnGestureListener, View.OnFocusChangeListener {
	private GestureDetector gestureDetector;
	private AbsoluteLayout.LayoutParams absXY_layout;
	private int x, y;
	private boolean isDeleteState;
	/**
	 * 是否处于可编辑状态
	 */
	private boolean canEditble;

	public boolean isCanEditble() {
		return canEditble;
	}

	public void setCanEditble(boolean canEditble) {
		this.canEditble = canEditble;
		if (canEditble) {
			this.setInputType(InputType.TYPE_CLASS_TEXT);
		} else {
			this.setInputType(InputType.TYPE_NULL);
		}
	}

	public XEditorText(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetector = new GestureDetector(this);

		super.setOnTouchListener(this);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnFocusChangeListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (this.equals(StandTableView.getCurrentItem())) {
			StandTableView.moveRotBar(true, getFloatMenuX(), getFloatMenuY(),
					getFloatMenuWidth(), this.getFloatMenuHeight());
		}
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	private void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.setLayoutParams(absXY_layout);
		StandTableView.moveRotBar(true, getFloatMenuX(), getFloatMenuY(),
				getFloatMenuWidth(), this.getFloatMenuHeight());
	}

	public int getFloatMenuX() {
		return absXY_layout.x;
	}

	public int getFloatMenuY() {
		int vh = this.getHeight() > 0 ? this.getHeight() : 60;
		return absXY_layout.y + vh;
	}

	public int getFloatMenuWidth() {
		return this.getWidth() == 0 ? 80 : this.getWidth();
	}

	public int getFloatMenuHeight() {
		return this.getHeight() == 0 ? 72 : this.getHeight();
	}

	@Override
	public void setSelected(boolean selected) {
		if (selected) {
			this.setCursorVisible(true);
			this.requestFocus();
		} else {
			this.setCursorVisible(false);
			this.clearFocus();
		}
		super.setSelected(selected);
	}

	@SuppressWarnings("deprecation")
	public void showLocation(int floatedX, int floatedY) {
		absXY_layout = new AbsoluteLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, floatedX,
				floatedY);
		this.setVisibility(View.VISIBLE);
		setLocation(floatedX, floatedY);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int offSetX = (int)getX(), offSetY = (int)getY();
		event.offsetLocation(offSetX, offSetY);
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		if (this.isDeleteState && event.getAction() == MotionEvent.ACTION_UP) {
			StandTableView.instance.removeCurrentItemViewFromMap();
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	/**
	 * Touch了滑动时触发。
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (!isCanEditble()) {
			return false;
		}
		if (SketchConfig.isDebug) {
			Log.v("XEditText", "onScroll");
		}
		doMoveSprite((int) e2.getX(), (int) e2.getY());
		// if (this.absXY_layout.x > StandTableView.getScreenWidth() -
		// DELETE_DX) {
		// this.isDeleteState = true;
		// this.setBackgroundColor(Color.RED);
		// } else {
		// this.setBackgroundDrawable(bgDrawable);
		// this.isDeleteState = false;
		// }
		// 否则继续
		return false;
	}

	private void doMoveSprite(int floatedX, int floatedY) {

		absXY_layout.x = floatedX;
		absXY_layout.y = floatedY;
		if (SketchConfig.isDebug) {
			Log.d("XEditText", "doMoveSprite to: lp.x=" + floatedX + " lp.y="
					+ floatedY);
		}
		setLocation(floatedX - (getWidth() >> 1), floatedY - (getHeight() >> 1));
		if (SketchConfig.isDebug) {
			Log.d("XEditText", "doMoveSprite getX=" + (int)getX() + " getY="
					+ (int)getY());
		}
	}

	@Override
	public void onLongPress(MotionEvent e) {

		SketchTool.virbrate();
		StandTableView.selectedView(this);
		setCanEditble(true);

		// 创建一个按下的onTouch事件，以便进一步触发拖拽事件
		MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(),
				e.getY(), e.getMetaState());

		int offSetX =(int) getX(), offSetY =(int) getY();
		e2.offsetLocation(-offSetX, -offSetY);
		this.onTouch(this, e2);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			Context ctx = this.getContext().getApplicationContext();
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		}
		// 1 当EidtText无焦点（focusable=false）时阻止输入法弹出
		// InputMethodManager imm =
		// (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		//
		// 2调用显示系统默认的输入法
		// //方法1
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(m_receiverView(接受软键盘输入的视图(View)),InputMethodManager.SHOW_FORCED(提供当前操作的标记，SHOW_FORCED表示强制显示));
		// //方法2
		// InputMethodManager m=(InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		// (这个方法可以实现输入法在窗口上切换显示，如果输入法在窗口上已经显示，则隐藏，如果隐藏，则显示输入法到窗口上)
		// 3调用隐藏系统默认的输入法
		// ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(WidgetSearchActivity.this.getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		// (WidgetSearchActivity是当前的Activity)
		// 4.获取输入法打开的状态
		// InputMethodManager imm =
		// (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		// boolean isOpen=imm.isActive();
		// isOpen若返回true，则表示输入法打开
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
		// 软键盘直接覆盖Activity，通常这是默认值
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		// Activity高度会变化，让出软键盘的空间。和WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
		// 为2选1的值
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
		// Activity一打开就直接显示软键盘窗口，如果该窗口需要的话（即有EditText，或有ditable的控件）
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
		// Activity打开后并不直接显示软键盘窗口，直到用户自己touch文本框
	}
}
