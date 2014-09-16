package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class XHorizontalScrollView extends HorizontalScrollView {
	private View mTouchDelegateView;

	public XHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public View getTouchDelegateView() {
		return mTouchDelegateView;
	}
   /**
    * 触摸事件代理器
    * @param mTouchDelegateView
    */
	public void setTouchDelegateView(View mTouchDelegateView) {
		this.mTouchDelegateView = mTouchDelegateView;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//优先代理视图需要事件处理
		if (mTouchDelegateView != null) {
			// 改变相对坐标
			View parent = (View) this.getParent();
			int px = parent.getLeft();
			int py = parent.getTop();
			ev.offsetLocation(px, py);
			/*
			if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				Log.d("TouchDelegate",
						"px=" + px + " py=" + py + " x=" + ev.getX() + " y="
								+ ev.getY());
			}
			*/
			mTouchDelegateView.dispatchTouchEvent(ev);
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
