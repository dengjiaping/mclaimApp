package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchConfig;

/**
 * 手势触摸旋转实现 参考API ScaleGestureDetector实现
 * 
 * @author anychen
 */
public class RotateGestureDetector {

	public interface OnRotateGestureListener {
		public boolean onRotate(RotateGestureDetector detector);
		public boolean onRotateBegin(RotateGestureDetector detector);
		public void onRotateEnd(RotateGestureDetector detector);
	}

	private static final float PRESSURE_THRESHOLD = 0.67f;
	private final Context mContext;
	private final OnRotateGestureListener mListener;
	private boolean mGestureInProgress;

	private MotionEvent mPrevEvent;
	private MotionEvent mCurrEvent;
	private float mFocusX;
	private float mFocusY;
	private float mPrevFingerDiffX;
	private float mPrevFingerDiffY;
	private float mCurrFingerDiffX;
	private float mCurrFingerDiffY;
	private float mCurrRot;
	private float mPrevRot;
	private float mCurrPressure;
	private float mPrevPressure;
	private float mRotateFactor;

	private long mTimeDelta;

	private final float mEdgeSlop;
	private float mRightSlopEdge;
	private float mBottomSlopEdge;
	private boolean mSloppyGesture;

	public RotateGestureDetector(Context context,
			OnRotateGestureListener listener) {
		ViewConfiguration config = ViewConfiguration.get(context);
		mContext = context;
		mListener = listener;
		mEdgeSlop = config.getScaledEdgeSlop();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() <= 1) {
			return false;
		}
		final int action = event.getAction();
		boolean handled = true;
		if (!mGestureInProgress) {
			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_POINTER_DOWN: {
				DisplayMetrics metrics = mContext.getResources()
						.getDisplayMetrics();
				mRightSlopEdge = metrics.widthPixels - mEdgeSlop;
				mBottomSlopEdge = metrics.heightPixels - mEdgeSlop;

				// Be paranoid in case we missed an event
				reset();

				mPrevEvent = MotionEvent.obtain(event);
				mTimeDelta = 0;

				setContext(event);

				// Check if we have a sloppy gesture. If so, delay
				// the beginning of the gesture until we're sure that's
				// what the user wanted. Sloppy gestures can happen if the
				// edge of the user's hand is touching the screen, for example.
				final float edgeSlop = mEdgeSlop;
				final float rightSlop = mRightSlopEdge;
				final float bottomSlop = mBottomSlopEdge;
				final float x0 = event.getRawX();
				final float y0 = event.getRawY();
				final float x1 = getRawX(event, 1);
				final float y1 = getRawY(event, 1);

				boolean p0sloppy = x0 < edgeSlop || y0 < edgeSlop
						|| x0 > rightSlop || y0 > bottomSlop;
				boolean p1sloppy = x1 < edgeSlop || y1 < edgeSlop
						|| x1 > rightSlop || y1 > bottomSlop;

				if (p0sloppy && p1sloppy) {
					mFocusX = -1;
					mFocusY = -1;
					mSloppyGesture = true;
				} else if (p0sloppy) {
					mFocusX = event.getX(1);
					mFocusY = event.getY(1);
					mSloppyGesture = true;
				} else if (p1sloppy) {
					mFocusX = event.getX(0);
					mFocusY = event.getY(0);
					mSloppyGesture = true;
				} else {
					onRotateBegin();
				}
			}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mSloppyGesture) {
					// Initiate sloppy gestures if we've moved outside of the
					// slop area.
					final float edgeSlop = mEdgeSlop;
					final float rightSlop = mRightSlopEdge;
					final float bottomSlop = mBottomSlopEdge;
					final float x0 = event.getRawX();
					final float y0 = event.getRawY();
					final float x1 = getRawX(event, 1);
					final float y1 = getRawY(event, 1);

					boolean p0sloppy = x0 < edgeSlop || y0 < edgeSlop
							|| x0 > rightSlop || y0 > bottomSlop;
					boolean p1sloppy = x1 < edgeSlop || y1 < edgeSlop
							|| x1 > rightSlop || y1 > bottomSlop;

					if (p0sloppy && p1sloppy) {
						mFocusX = -1;
						mFocusY = -1;
					} else if (p0sloppy) {
						mFocusX = event.getX(1);
						mFocusY = event.getY(1);
					} else if (p1sloppy) {
						mFocusX = event.getX(0);
						mFocusY = event.getY(0);
					} else {
						mSloppyGesture = false;
						onRotateBegin();
					}
				}
				break;

			case MotionEvent.ACTION_POINTER_UP:
				if (mSloppyGesture) {
					// Set focus point to the remaining finger
					int id = (((action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT) == 0) ? 1
							: 0;
					mFocusX = event.getX(id);
					mFocusY = event.getY(id);
				}
				break;
			}
		} else {
			// Transform gesture in progress - attempt to handle it
			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_POINTER_UP:
				setContext(event);
				int id = (((action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT) == 0) ? 1
						: 0;
				mFocusX = event.getX(id);
				mFocusY = event.getY(id);
				if (!mSloppyGesture) {
					onRotateEnd();
				}
				reset();
				break;
			case MotionEvent.ACTION_CANCEL:
				if (!mSloppyGesture) {
					onRotateEnd();

				}
				reset();
				break;
			case MotionEvent.ACTION_MOVE:
				setContext(event);

				// Only accept the event if our relative pressure is within
				// a certain limit - this can help filter shaky data as a
				// finger is lifted.
				if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
					final boolean updatePrevious = mListener.onRotate(this);
					if (updatePrevious) {
						mPrevEvent.recycle();
						mPrevEvent = MotionEvent.obtain(event);
					}
				}
				break;
			}
		}

		return handled;
	}

	private void onRotateBegin() {
		if (SketchConfig.isDebug) {
			Log.v("Rotate", "onRotateBegin");
		}

		initBeginRot();
		mGestureInProgress = mListener.onRotateBegin(this);
	}

	private void onRotateEnd() {
		if (SketchConfig.isDebug) {
			Log.v("Rotate", "onRotateEnd");
		}

		mListener.onRotateEnd(this);
	}

	/**
	 * MotionEvent has no getRawX(int) method; simulate it pending future API
	 * approval.
	 */
	private static float getRawX(MotionEvent event, int pointerIndex) {
		float offset = event.getRawX() - event.getX();
		return event.getX(pointerIndex) + offset;
	}

	/**
	 * MotionEvent has no getRawY(int) method; simulate it pending future API
	 * approval.
	 */
	private static float getRawY(MotionEvent event, int pointerIndex) {
		float offset = event.getRawY() - event.getY();
		return event.getY(pointerIndex) + offset;
	}

	private void setContext(MotionEvent curr) {
		if (mCurrEvent != null) {
			mCurrEvent.recycle();
		}
		mCurrEvent = MotionEvent.obtain(curr);

		mCurrRot = -1;
		mPrevRot = -1;
		mRotateFactor = -1;

		final MotionEvent prev = mPrevEvent;

		final float px0 = prev.getX(0);
		final float py0 = prev.getY(0);
		final float px1 = prev.getX(1);
		final float py1 = prev.getY(1);
		final float cx0 = curr.getX(0);
		final float cy0 = curr.getY(0);
		final float cx1 = curr.getX(1);
		final float cy1 = curr.getY(1);

		final float pvx = px1 - px0;
		final float pvy = py1 - py0;
		final float cvx = cx1 - cx0;
		final float cvy = cy1 - cy0;
		mPrevFingerDiffX = pvx;
		mPrevFingerDiffY = pvy;
		mCurrFingerDiffX = cvx;
		mCurrFingerDiffY = cvy;

		mFocusX = cx0 + cvx * 0.5f;
		mFocusY = cy0 + cvy * 0.5f;
		mTimeDelta = curr.getEventTime() - prev.getEventTime();
		mCurrPressure = curr.getPressure(0) + curr.getPressure(1);
		mPrevPressure = prev.getPressure(0) + prev.getPressure(1);
	}

	private void reset() {
		if (mPrevEvent != null) {
			mPrevEvent.recycle();
			mPrevEvent = null;
		}
		if (mCurrEvent != null) {
			mCurrEvent.recycle();
			mCurrEvent = null;
		}
		mSloppyGesture = false;
		mGestureInProgress = false;
	}

	/**
	 * Returns {@code true} if a two-finger scale gesture is in progress.
	 * 
	 * @return {@code true} if a scale gesture is in progress, {@code false}
	 *         otherwise.
	 */
	public boolean isInProgress() {
		return mGestureInProgress;
	}

	/**
	 * Get the X coordinate of the current gesture's focal point. If a gesture
	 * is in progress, the focal point is directly between the two pointers
	 * forming the gesture. If a gesture is ending, the focal point is the
	 * location of the remaining pointer on the screen. If
	 * {@link #isInProgress()} would return false, the result of this function
	 * is undefined.
	 * 
	 * @return X coordinate of the focal point in pixels.
	 */
	public float getFocusX() {
		return mFocusX;
	}

	/**
	 * Get the Y coordinate of the current gesture's focal point. If a gesture
	 * is in progress, the focal point is directly between the two pointers
	 * forming the gesture. If a gesture is ending, the focal point is the
	 * location of the remaining pointer on the screen. If
	 * {@link #isInProgress()} would return false, the result of this function
	 * is undefined.
	 * 
	 * @return Y coordinate of the focal point in pixels.
	 */
	public float getFocusY() {
		return mFocusY;
	}

	/**
	 * 获取旋转的角度
	 * 
	 * @return
	 */
	public float getCurrentRot() {
		if (mCurrRot == -1) {
			double angrad = Math.atan2(mCurrFingerDiffY, mCurrFingerDiffX);
			mCurrRot = (float) Math.toDegrees(angrad);
		}
		return mCurrRot;
	}

	/**
	 * 获取旋转前的角度
	 * 
	 * @return
	 */
	public float getPreviousRot() {
		if (mPrevRot == -1) {
			double angrad = Math.atan2(mPrevFingerDiffX, mPrevFingerDiffY);
			mPrevRot = (float) Math.toDegrees(angrad);
		}
		return mPrevRot;
	}

	private int mPrevRotValue;

	/**
	 * 最近一次旋转变化绝度
	 * 
	 * @return
	 */
	public float getRotateValue() {
//		Log.v("Rotate", " mCurRot="
//				+ mCurrRot + " mPrevRot=" + mPrevRot+" getCurRot=" + getCurrentRot()
//				+ " getPrevRot=" + getPreviousRot());
		int newRotValue = (int) (getCurrentRot() - getPreviousRot());
		
		int rotValue = newRotValue - mPrevRotValue;
		if(!isClockwise){
			rotValue=-rotValue;
		}
		mPrevRotValue = newRotValue;
		Log.v("Rotate", "isClockwise="+isClockwise+" rotValue="+rotValue);
		return rotValue;
	}

	/**
	 * Return the scaling factor from the previous scale event to the current
	 * event. This value is defined as ({@link #getCurrentRot()} /
	 * {@link #getPreviousRot()}).
	 * 
	 * @return The current scaling factor.
	 */
	public float geRotateFactor() {
		if (mRotateFactor == -1) {
			mRotateFactor = getCurrentRot() / getPreviousRot();
		}
		return mRotateFactor;
	}

	/**
	 * Return the time difference in milliseconds between the previous accepted
	 * scaling event and the current scaling event.
	 * 
	 * @return Time difference since the last scaling event in milliseconds.
	 */
	public long getTimeDelta() {
		return mTimeDelta;
	}

	/**
	 * Return the event time of the current event being processed.
	 * 
	 * @return Current event time in milliseconds.
	 */
	public long getEventTime() {
		return mCurrEvent.getEventTime();
	}

	/**
	 * 第一次按下时两点夹角
	 */
	private void initBeginRot() {
		mPrevRotValue = (int) (getCurrentRot() - getPreviousRot());
		//isClockwise=(getCurrentRot()>=0 && getPreviousRot()>=0)||(getCurrentRot()<0 && getPreviousRot()<0);
		Log.d("Rotate", "Begin:mPrevRot=" + mPrevRot + " mCurrRot=" + mCurrRot
				+" pdiffX="+mPrevFingerDiffX+" pdiffY="+mPrevFingerDiffY
				+" diffX="+mCurrFingerDiffX+" diffY="+mCurrFingerDiffY);
	}
	
	private boolean isClockwise=true;//顺时针旋转
}
