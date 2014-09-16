package com.sinosoftyingda.fastclaim.sketchmap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout.LayoutParams;

import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchConfig;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.SketchTool;
import com.sinosoftyingda.fastclaim.sketchmap.view.RotateGestureDetector.OnRotateGestureListener;

/**
 * 自然装饰物体视图，可实现自由拖动，缩放，依赖于AbsoluteLayout
 * 
 * @author anychen
 * 
 */
public class XImgView extends View implements OnTouchListener,
		OnGestureListener, OnScaleGestureListener, OnRotateGestureListener {
	private static final String namespace = "http://com.standTable.cn";

	/**
	 * 单点双击缩放参数
	 */
	private static final float MAX_SCALE = SketchConfig.getMaxScale();
	private static final float MIN_SCALE = SketchConfig.getMinScale();
	private static final float middleScale = SketchConfig.getMidScale();
	/**
	 * 最小缩放尺寸
	 */
	private static final int MIN_SCALE_SIZE = SketchConfig.getMinScaleDisplaySize();

	private static final int frameCount = 6;
	// 手势识别器
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;
	private RotateGestureDetector rotGestureDetector;

	// 视图实际显示范围：其半径=[矩形对角线长度] 即以实际图片中心点为原点，旋转360所占的最大位置
	private int mDisplayWidth, mDisplayHeight;
	// 背景图id
	private int mBackGroudDrawableId;
	private float mScaleTarget = 1.0f;// 单点缩放目标值
	private float mCurrentScale = mScaleTarget;

	private float mScaleStep;// 每帧缩放变化值
	private int mRotateDegrees;// 旋转角度
	private AbsoluteLayout.LayoutParams absXY_layout;
	private int x, y;

	private int elementType;
	/**
	 * 未选中的普通图片
	 */
	private Bitmap mElementBitmap;

	/**
	 * 图形ID
	 */
	private int imageId;

	/**
	 * 
	 * 原始图片所需宽、高
	 */
	private int mBackGroundWidth;
	private int mBackGroundHeight;
	/*
	 * 当拖动时：浮起的坐标超过底部边界时删除
	 */
	private boolean isDeleteState;
	private static final int MIN_DELETE_DY = 30;
	/**
	 * 是否处于可编辑状态
	 */
	private boolean canEditble;

	public boolean isCanEditble() {
		return canEditble;
	}

	public void setCanEditble(boolean canEditble) {
		this.canEditble = canEditble;
	}

	public Bitmap getElementBitmap() {
		return mElementBitmap;
	}

	public XImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDrawingCacheEnabled(true);
		mBackGroudDrawableId = attrs.getAttributeResourceValue(namespace,
				"background", 0);
		if (mBackGroudDrawableId > 0) {
			mElementBitmap = getBitmapById(mBackGroudDrawableId);
		}

		mRotateDegrees = attrs.getAttributeIntValue(namespace, "rot", 0);
		mCurrentScale = attrs.getAttributeFloatValue(namespace, "scale", 1.0f);
		// 手势识别
		gestureDetector = new GestureDetector(this);

		final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		if (sdkVersion >= Build.VERSION_CODES.ECLAIR) { // 版本大于等于2.0才可以使用双指缩放
			scaleGestureDetector = new ScaleGestureDetector(context, this);
			// 旋转手势识别器
			rotGestureDetector = new RotateGestureDetector(context, this);
		}
		super.setOnTouchListener(this);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setLongClickable(true);
	}

	Paint paint = new Paint();

	@SuppressWarnings("deprecation")
	public void showLocation(int floatedX, int floatedY) {
		absXY_layout = new AbsoluteLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, floatedX,
				floatedY);
		this.setVisibility(View.VISIBLE);
		setLocation(floatedX, floatedY);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// nextFrame();
		if (SketchConfig.isDebug && canEditble) {
			canvas.save();
			paint.setColor(Color.argb(0xf0, 0xff, 0x00, 0x00));
			canvas.drawRect(0, 0, this.mDisplayWidth, mDisplayHeight, paint);
		}

		int imgWidth = (int) (mBackGroundWidth * mCurrentScale);
		int imgHeight = (int) (mBackGroundHeight * mCurrentScale);
		canvas.translate(mDisplayWidth - imgWidth >> 1, mDisplayHeight
				- imgHeight >> 1);
		canvas.rotate(mRotateDegrees, imgWidth >> 1, imgHeight >> 1);
		if (mCurrentScale != 1.0f) {
			canvas.scale(mCurrentScale, mCurrentScale);
		}
		if (this.isSelected()) {
			canvas.drawBitmap(mElementBitmap, 0, 0, paint);
			if(!isDeleteState){
				nextFrameMode();
			}
		} else {
			canvas.drawBitmap(mElementBitmap, 0, 0, null);
		}

		if (SketchConfig.isDebug && canEditble) {
			// Log.d("Test", " time=" + (System.currentTimeMillis() -
			// lastTime));
			lastTime = System.currentTimeMillis();
			canvas.restore();
			drawCoordinateXY(canvas, paint);
		}
		if (isDeleteState) {
			canvas.restore();
			paint.setColorFilter(null);
			paint.setColor(Color.argb(0x60, 0xff, 0x00, 0x00));
			canvas.drawRect(0, 0, this.mDisplayWidth, mDisplayHeight, paint);
		}
	}

	private long lastTime;
	private boolean isAdding;
	private int mIncrement = 255;

	ColorMatrix colorMatrix = new ColorMatrix();

	private static void setContrastTranslateOnly(ColorMatrix cm, float contrast) {
		float scale = contrast + 1.f;
		float translate = (-.5f * scale + .5f) * 255.f;
		cm.set(new float[] { 1, 0, 0, 0, translate, 0, 1, 0, 0, translate, 0,
				0, 1, 0, translate, 0, 0, 0, 1, 0 });
	}

	/**
	 * 按颜色过滤 变化
	 */
	public void nextFrameByColor() {
		mIncrement += 2;
		if (mIncrement > 60) {
			mIncrement = 0;
		}
		float contrast = mIncrement / 180.f;
		setContrastTranslateOnly(colorMatrix, contrast);

		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		this.invalidate();
	}

	/**
	 * 按透明度 渐变
	 */
	public void nextFrameByAlph() {

		paint.setAlpha(mIncrement);
		if (isAdding) {
			if (mIncrement < 250) {
				mIncrement += 5;
			} else {
				isAdding = false;
			}
		} else {
			if (mIncrement > 200) {
				mIncrement -= 5;
			} else {
				isAdding = true;
			}
		}
		this.invalidate();

	}

	/**
	 * 光照颜色 类似蒙板
	 */
	private void nextFrameByLightingColorFilter() {
		if (isAdding) {
			if (mIncrement < 0xa0a0a0) {
				mIncrement += 0x010101;
			} else {
				isAdding = false;
			}
		} else {
			if (mIncrement > 0x010101) {
				mIncrement -= 0x010101;
			} else {
				isAdding = true;
			}
		}
		// 效果：类似某种颜色 蒙在图片上
		paint.setColorFilter(new LightingColorFilter(Color.WHITE, mIncrement));
		this.invalidate();
	}

	private long mLastTime;

	private void nextFrameByPorterDuffColorFilter() {
		int mods = PorterDuff.Mode.values().length;
		if (mLastTime == 0 || mIncrement >= mods - 1) {
			mLastTime = System.currentTimeMillis();
			mIncrement = 0;
			Log.d("ColorMode", mIncrement + "-"
					+ PorterDuff.Mode.values()[mIncrement]);
		} else if (System.currentTimeMillis() - mLastTime > 10000) {
			mLastTime = System.currentTimeMillis();
			mIncrement++;
			Log.d("ColorMode", mIncrement + "-"
					+ PorterDuff.Mode.values()[mIncrement]);
		}
		paint.setColorFilter(new PorterDuffColorFilter(Color.WHITE,
				PorterDuff.Mode.values()[mIncrement]));
		this.invalidate();
		// 10-11 22:21:27.206: DEBUG/ColorMode(9373): 0- CLEAR-CLEAR 无图像
		// 10-11 22:21:37.226: DEBUG/ColorMode(9373): 1-SRC 全部单色--红色
		// 10-11 22:21:47.256: DEBUG/ColorMode(9373): 2-DST 近似原图
		// 10-11 22:21:57.296: DEBUG/ColorMode(9373): 3-SRC_OVER 全部单色--红色
		// 10-11 22:22:07.326: DEBUG/ColorMode(9373): 4-DST_OVER 透明色变红色 实体不变
		// 10-11 22:22:17.365: DEBUG/ColorMode(9373): 5-SRC_IN 实体图形全红
		// 10-11 22:22:27.385: DEBUG/ColorMode(9373): 6-DST_IN 近似原图
		// 10-11 22:22:37.395: DEBUG/ColorMode(9373): 7-SRC_OUT 透明色变红色 实体图无
		// 10-11 22:22:47.395: DEBUG/ColorMode(9373): 8-DST_OUT 无图
		// 10-11 22:22:57.414: DEBUG/ColorMode(9373): 9-SRC_ATOP 实体图形变红
		// 10-11 22:23:07.454: DEBUG/ColorMode(9373): 10-DST_ATOP 透明色变红色 实体不变
		// 10-11 22:23:17.494: DEBUG/ColorMode(9373): 11-XOR 透明色变红色 实体图无
		// 10-11 22:23:27.534: DEBUG/ColorMode(9373): 12-DARKEN 透明色变红色 实体变暗
		// 10-11 22:23:37.574: DEBUG/ColorMode(9373): 13-LIGHTEN 背景及实体图都带红色
		// 10-11 22:23:47.583: DEBUG/ColorMode(9373): 14-MULTIPLY 实体图变暗
		// 10-11 22:23:57.603: DEBUG/ColorMode(9373): 15-SCREEN 背景及实体图都红色
	}

	float lightIncrement = 0;

	private void nextFrameByEmbossMaskFilter() {
		lightIncrement += 0.1;
		if (lightIncrement > 1) {
			lightIncrement = 0;
		}
		mIncrement += 4;
		if (mIncrement > 40) {
			mIncrement = -40;
		}
		// 设置光源的方向
		float[] direction = new float[] { 1f, 1, 1 };// [x, y, z]
		// 设置环境光亮度
		float light = 1;// 0...1 amount of ambient light 亮度变化
		// 选择要应用的反射等级
		float specular = mIncrement;// 数值为1较明显
		// 向mask应用一定级别的模糊
		float blur = 3;// amount to blur before applying lighting (e.g. 3) 基本无变化
		EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light,
				specular, blur);

		paint.setMaskFilter(emboss);
		this.invalidate();
	}

	private void nextFrameByBlurMaskFilter() {
		lightIncrement += 10;
		if (lightIncrement > 500) {
			lightIncrement = 1f;
		}
		int mods = BlurMaskFilter.Blur.values().length;
		if (mLastTime == 0 || mIncrement >= mods - 1) {
			mLastTime = System.currentTimeMillis();
			mIncrement = 0;
			Log.d("BlurMaskFilter",
					mIncrement + "-" + BlurMaskFilter.Blur.values()[mIncrement]);
		} else if (System.currentTimeMillis() - mLastTime > 10000) {
			mLastTime = System.currentTimeMillis();
			mIncrement++;
			Log.d("BlurMaskFilter",
					mIncrement + "-" + BlurMaskFilter.Blur.values()[mIncrement]);
		}

		float radius = lightIncrement;// radius The radius to extend the blur
										// from the original mask. Must be > 0.

		BlurMaskFilter mBlur = new BlurMaskFilter(radius,
				BlurMaskFilter.Blur.values()[mIncrement]);
		paint.setMaskFilter(mBlur);
		this.invalidate();
		// 10-11 23:17:01.219: DEBUG/BlurMaskFilter(10524): 0-NORMAL 有变化
		// 10-11 23:17:11.289: DEBUG/BlurMaskFilter(10524): 1-SOLID 无变化
		// 10-11 23:17:21.409: DEBUG/BlurMaskFilter(10524): 2-OUTER 图消失
		// 10-11 23:17:31.429: DEBUG/BlurMaskFilter(10524): 3-INNER 有变化

	}

	private void nextFrameMode() {
		this.nextFrameByLightingColorFilter();
	}

	private void drawCoordinateXY(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, mDisplayHeight / 2 - 1, this.mDisplayWidth,
				mDisplayHeight / 2 + 1, paint);
		canvas.drawRect(mDisplayWidth / 2 - 1, 0, mDisplayWidth / 2 + 1,
				mDisplayHeight, paint);
	}

	/**
	 * 
	 * 设定View显示区域
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mDisplayWidth, mDisplayHeight);

	}

	/**
	 * 设置图片源，设置后需要重新计算图片所占用的视图大小
	 */
	public void setImageBitmap(int position, Bitmap bmp) {
		this.imageId = position;

		this.mElementBitmap = bmp;
		mBackGroundWidth = mElementBitmap.getWidth();
		mBackGroundHeight = mElementBitmap.getHeight();
		int[] size = calculateDisplaySize();

		if (SketchConfig.isDebug) {
			Log.d("updateView", "setImageBitmap mDisplayWidth=" + mDisplayWidth
					+ " mDisplayHeight=" + mDisplayHeight + " newDisplayW="
					+ size[0] + " newDisplayH=" + size[1]);
		}
		mDisplayWidth = size[0];
		mDisplayHeight = size[1];

	}

	public int getImageId() {
		return this.imageId;
	}

	public int getBackGroundId() {
		return mBackGroudDrawableId;
	}

	public void setBackGroundId1(int resId) {
		this.mBackGroudDrawableId = resId;
	}

	public int getImageWidth() {
		return mBackGroundWidth;
	}

	public int getImageHeight() {
		return mBackGroundHeight;
	}

	public float getScale() {
		return mCurrentScale;
	}

	public void setScale(float scale) {
		this.mCurrentScale = scale;
	}

	public int getRot() {
		return mRotateDegrees;
	}

	public void setRot(int rot) {
		this.mRotateDegrees = rot;
		updateViewSize();
	}

	/**
	 * 更新视图大小
	 */
	private void updateViewSize() {
		int[] size = calculateDisplaySize();
		int offSetX = size[0] - mDisplayWidth >> 1;
		int offSetY = size[1] - mDisplayHeight >> 1;
		if (SketchConfig.isDebug) {
			Log.d("updateView", "mDisplayWidth=" + mDisplayWidth
					+ " mDisplayHeight=" + mDisplayHeight + " newDisplayW="
					+ size[0] + " newDisplayH=" + size[1]);
		}
		mDisplayWidth = size[0];
		mDisplayHeight = size[1];
		moveOffset(-offSetX, -offSetY);
	}

	/**
	 * 计算旋转缩放后：视图的显示尺寸
	 * 
	 * @return
	 */
	private int[] calculateDisplaySize() {
		int[] size = new int[2];
		int width = (int) (mBackGroundWidth * mCurrentScale);
		int height = (int) (mBackGroundHeight * mCurrentScale);
		double angrad = Math.toRadians(mRotateDegrees);
		double sinAngrad = Math.abs(Math.sin(angrad));
		double cosAngrad = Math.abs(Math.cos(angrad));
		// new Displaywidth
		size[0] = (int) (height * sinAngrad + width * cosAngrad) + 1;
		// new DisplayHeight
		size[1] = (int) (width * sinAngrad + height * cosAngrad) + 1;

		return size;
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
	}

	public void showInCenter() {
		int width = (int) (mBackGroundWidth * mCurrentScale);
		int height = (int) (mBackGroundHeight * mCurrentScale);

		int mDisplaySize = (int) Math.sqrt(width * width + height * height);
		int screenWidth = StandTableView.instance.getWidth();
		int screenHeight = StandTableView.instance.getHeight();

		int floatedX = screenWidth - mDisplaySize >> 1;
		int floatedY = screenHeight - mDisplaySize >> 1;
		if (absXY_layout == null)
			absXY_layout = new AbsoluteLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					floatedX, floatedY);
		this.setVisibility(View.VISIBLE);
		setLocation(floatedX, floatedY);
	}

	/**
	 * 初始化一个组件
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param scale
	 * @param imageId
	 * @param rot
	 * @param bmp
	 * @param elementType
	 */
	public void initilaze(int x, int y, int w, int h, float scale, int imageId,
			int rot, Bitmap bmp, int elementType) {
		if (absXY_layout == null)
			absXY_layout = new AbsoluteLayout.LayoutParams(w, h, x, y);
		this.mCurrentScale = scale;
		this.mRotateDegrees = rot;
		setElementType(elementType);
		this.setImageBitmap(imageId, bmp);
		this.setVisibility(View.VISIBLE);
		this.x = x;
		this.y = y;
		this.setLayoutParams(absXY_layout);
	}

	public int getElementType() {
		return elementType;
	}

	public void setElementType(int elementType) {
		this.elementType = elementType;
	}

	public final Bitmap getBitmapById(int id) {
		return BitmapFactory.decodeStream(this.getResources().openRawResource(
				id));

	}

	private boolean isValidScale(float scaleTarget) {
		int width = (int) (mBackGroundWidth * scaleTarget);
		int height = (int) (mBackGroundHeight * scaleTarget);
		if (mCurrentScale == scaleTarget || scaleTarget > MAX_SCALE
				|| scaleTarget < MIN_SCALE || scaleTarget < 1
				&& (width < MIN_SCALE_SIZE || height < MIN_SCALE_SIZE)) {
			Log.d("Test", "Waring--inValid   scaleTarget-->" + scaleTarget);
			return false;
		}
		return true;
	}

	private boolean doScale(float scaleTarget) {
		if (!isValidScale(scaleTarget)) {
			return false;
		}
		if (Math.abs(scaleTarget - MAX_SCALE) <= 0.05) {
			scaleTarget = MAX_SCALE;
		} else if (Math.abs(scaleTarget - MIN_SCALE) <= 0.05) {
			scaleTarget = MIN_SCALE;
		}
		// 实现双指地图缩放
		mCurrentScale = scaleTarget;
		// 根据缩放后的的尺寸，移动坐标 以保持中心点不变
		int[] size = calculateDisplaySize();
		int offSetX = size[0] - mDisplayWidth >> 1;
		int offSetY = size[1] - mDisplayHeight >> 1;
		mDisplayWidth = size[0];
		mDisplayHeight = size[1];
		if (SketchConfig.isDebug) {
			Log.d("Test", "getX=" + (int)getX() + " getY=" + (int)getY() + " offSetX="
					+ offSetX + " offSetY=" + offSetY);
		}
		moveOffset(-offSetX, -offSetY);
		int targetX = absXY_layout.x - offSetX;
		int targetY = absXY_layout.y - offSetY;
		int screenWidth = StandTableView.instance.getWidth();
		int screenHeight = StandTableView.instance.getHeight();

		int rectX = screenWidth / 4;
		int rectY = screenHeight / 4;

		// 如果缩放时 坐标偏移到显示区域之外时，直接剧中
		boolean isShowCenter = (targetX + this.mDisplayWidth <= rectX || targetX >= screenWidth - 30)
				|| (targetY + this.mDisplayWidth <= rectY || targetY >= screenHeight - 30);
		if (SketchConfig.isDebug) {
			Log.d("Test", isShowCenter + " targetX=" + targetX + " targetY="
					+ targetY + " rectX=" + rectX + " rectY=" + rectY);
		}
		if (isShowCenter) {
			if (SketchConfig.isDebug) {
				Log.d("Test", "showInCenter()");
			}
			this.showInCenter();
		}

		return true;
	}

	/**
	 * 将当前点击的点坐标转换为 图片上某点的坐标值
	 */
	private int getX_inBitmap(int pointX) {
		return pointX;
	}

	/**
	 * 将当前点击的点坐标转换为 图片上某点的坐标值
	 */
	private int getY_inBitmap(int pointY) {
		return pointY;
	}

	/**
	 * 用户按下屏幕（Touch down）时触发
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	/**
	 * onDown 之后，抬起或移动之前，用于提示给用户按钮已被按下。（Touch了还没有滑动时触发）
	 */
	@Override
	public void onShowPress(MotionEvent e) {

	}

	/**
	 * 单击事件的一部分，抬起时调用
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (!isCanEditble()) {
			return false;
		}
		if (SketchConfig.isDebug) {
			Log.v("Test", "onSingleTapUp");
		}
		// 否则继续
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
			Log.v("Test", "onScroll distanceX=" + distanceX + " distanceY="
					+ distanceY);
		}
		// doMoveSprite((int) e2.getX(), (int) e2.getY());
		moveOffset(-(int) distanceX, -(int) distanceY);
		// 否则继续
		// 判断是否移到右边的删除区域
		int deleteAreaY = this.getHeight() / 2;
		if (deleteAreaY < MIN_DELETE_DY) {
			deleteAreaY = MIN_DELETE_DY;
		}
		int deleteY = StandTableView.instance.getHeight()
				- StandTableView.instance.getPaddingBottom();
		if (this.absXY_layout.y > deleteY - deleteAreaY) {
			this.isDeleteState = true;
		} else {
			this.isDeleteState = false;
		}

		return false;
	}

	private void moveOffset(int offsetX, int offsetY) {
		absXY_layout.x += offsetX;
		absXY_layout.y += offsetY;
		setLocation((int)getX() + offsetX, (int)getY() + offsetY);
		if (SketchConfig.isDebug) {
			Log.d("Test", "moveOffset getX=" + getX() + " getY=" + getY()
					+ " lp.x=" + absXY_layout.x + " lp.y" + absXY_layout.y);
		}
	}

	/**
	 * 用户按下并保持一段时间后调用（Touch了不移动一直Touch down时触发）
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		if(this.isCanEditble()){
			return ;
		}
		Log.v("Test", "onLongPress");
		SketchTool.virbrate();
		StandTableView.selectedView(this);
		setCanEditble(true);

		// 创建一个按下的onTouch事件，以便进一步触发拖拽事件
		MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
				SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(),
				e.getY(), e.getMetaState());

		int offSetX = (int)getX(), offSetY = (int)getY();
		e2.offsetLocation(-offSetX, -offSetY);
		this.onTouch(this, e2);

	}

	/**
	 * 用户的轻拂手势，Touch了滑动一点距离后，up时触发。
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (SketchConfig.isDebug) {
			Log.v("Test", "onFling");
		}

		// 否则继续
		return false;
	}

	private void animationScale(float targetScale) {
		if (!isValidScale(targetScale)) {
			if (targetScale > MAX_SCALE) {
				targetScale = MAX_SCALE;
			} else if (targetScale < MIN_SCALE) {
				targetScale = MIN_SCALE;
			}
			int width = (int) (mBackGroundWidth * targetScale);
			int height = (int) (mBackGroundHeight * targetScale);
			if (targetScale < 1
					&& (width < MIN_SCALE_SIZE || height < MIN_SCALE_SIZE)) {
				if (width > height) {
					targetScale = MIN_SCALE_SIZE * 1.0f / mBackGroundHeight;// old=150
																			// min=100
				} else {
					targetScale = MIN_SCALE_SIZE * 1.0f / mBackGroundWidth;
				}
			}
		}
		this.mScaleTarget = targetScale;
		this.mScaleStep = (this.mScaleTarget - this.mCurrentScale)
				/ XImgView.frameCount;
		nextFrame();
	}

	/**
	 * 推进缩放动画的下一帧计算
	 */
	public void nextFrame() {
		if (this.mCurrentScale != this.mScaleTarget) {
			if (SketchConfig.isDebug) {
				Log.d("Test", "nextFrame() mCurrentScale=" + mCurrentScale
						+ " mScaleTarget" + mScaleTarget);
			}
			float newScale = this.mCurrentScale + this.mScaleStep;
			if (Math.abs((this.mScaleTarget - newScale)) < Math
					.abs(this.mScaleStep / 100)) {
				// 避免浮点运算误差，可以认为相等
				newScale = this.mScaleTarget;
			}
			newScale = newScale < MIN_SCALE ? MIN_SCALE
					: (newScale > MAX_SCALE ? MAX_SCALE : newScale);
			if (doScale(newScale)) {
				this.postInvalidateDelayed(100);
			}
		}
	}

	// 缩放相关

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		if (!isCanEditble()) {
			return false;
		}
		if (SketchConfig.isDebug) {
			Log.v("Test", "onScaleBegin");
		}

		return true;
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {

		float scaleTarget = detector.getScaleFactor() * this.mCurrentScale;
		if (!isValidScale(scaleTarget)) {
			return false;
		}
		if (SketchConfig.isDebug) {
			Log.v("Test", "onScale-->" + mScaleTarget);
		}
		mScaleTarget = scaleTarget;
		return doScale(mScaleTarget);
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		if (SketchConfig.isDebug) {
			Log.v("Test", "onScaleEnd");
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (SketchConfig.isDebug) {
			Log.v("Test", "event.getPointerCount()=" + event.getPointerCount());
		}
		if (isCanEditble()) {
			scaleGestureDetector.onTouchEvent(event);
			rotGestureDetector.onTouchEvent(event);
			if(rotGestureDetector.isInProgress()){
				return true;
			}
		}

		if (!scaleGestureDetector.isInProgress()) {
			int offSetX =(int) getX(), offSetY =(int) getY();
			event.offsetLocation(offSetX, offSetY);
			if (gestureDetector.onTouchEvent(event)) {
				return false;
			} else {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					onTouchUp(event);
				}

			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 松开坐标时，检测当前素材元素是否处于删除区域，如果是删除
	 * 
	 * @param event
	 * @return
	 */
	private boolean onTouchUp(MotionEvent event) {

		if (this.isDeleteState) {
			StandTableView.instance.removeCurrentItemViewFromMap();
		}
		return true;
	}

	public void rotate(int degrees) {
		setRot(degrees%360);
	}

	public void rotateRight() {
		int rot = getRot();
		if (rot < 360) {
			rot += 10;
		} else {
			rot = 0;
		}
		rotate(rot);
	}

	public void rotateLeft() {
		int rot = getRot();
		if (rot > 10) {
			rot -= 10;
		} else {
			rot = 360;
		}
		rotate(rot);
	}

	@Override
	public boolean onRotate(RotateGestureDetector detector) {
		int rotValue = (int) detector.getRotateValue();
		 
		Log.v("Rot", "onRotate rotValue=" + rotValue + " curRot="
				+ (getRot() + rotValue));
		
		rotate(getRot()+rotValue);
		return false;
	}

	@Override
	public boolean onRotateBegin(RotateGestureDetector detector) {
		Log.v("Rot", "onRotateBegin");
		return true;
	}

	@Override
	public void onRotateEnd(RotateGestureDetector detector) {
		Log.v("Rot", "onRotateEnd");
	}

}
