package com.sinosoftyingda.fastclaim.photoes.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Gallery;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.photoes.view.GalleryAdapter;
import com.sinosoftyingda.fastclaim.photoes.view.PhotoGallery;

/**
 * 查看单张图片
 * 
 * @author DengGuang
 */
public class ShowPhotoActivity extends Activity implements OnTouchListener {

	public static int		screenWidth;					// 屏幕的宽度
	public static int		screenHeight;					// 屏幕的高度

	private PhotoGallery	gallery;
	private boolean			isScale				= false;	// 是否缩放
	private float			beforeLenght		= 0.0f;		// 两触点距离
	private float			afterLenght			= 0.0f;
	private float			currentScale		= 1.0f;

	private GalleryAdapter	galleryAdapter		= null;
	private String			selectPicPath		= "";		// 选中的图片
	private String			selectPicPathTemp	= "";		// 上传目录

	private int				selectPosition;					// 当前下标
	private String			photoDir			= "";
	private String			typeDir				= "";
	private FileUtils		fileUtils			= null;		// 文件工具类
	private String			gType				= "0";
	String[] subs = new String[] { "单证照片", "复堪照片", " 标的车 ", " 三者车 ", "  物损  ", "  人伤  ", "现场照片" };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置窗体无标题 全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.photos_show);

		// 获取屏幕的大小
		screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();

		// 获取点击该张照片的路径
		Bundle bundle = getIntent().getExtras();
		selectPosition = bundle.getInt("selectposition_key");
		typeDir = bundle.getString("typedir_key");
		gType = bundle.getString("gtype_key");
		photoDir = SystemConfig.PHOTO_CLAIM_DIR + typeDir;
		FileUtils.mkDir(new File(photoDir));

		loadPhotos(photoDir, selectPosition);
	}

	/**
	 * 加载图片
	 * 
	 * @param currentPic
	 * @param picDir
	 * @param currentIndex
	 */
	private void loadPhotos(String picDir, int currentIndex) {
		// 文件夹内图片目录
		List<String> photosPaths = new ArrayList<String>();
		FileUtils fileUtils = new FileUtils();
		photosPaths = fileUtils.getFileName(new File(picDir), picDir);

		// 将该目录下所有的图片都删除
		if (photosPaths.size() != 0) {
			// 预防是最后一张图片
			if (currentIndex >= photosPaths.size()) {
				currentIndex = 0;
			}
			// 显示图片
			String currentPic = photosPaths.get(currentIndex);
			galleryAdapter = new GalleryAdapter(this, currentPic, photosPaths);
			gallery = (PhotoGallery) findViewById(R.id.photos_gallery);
			gallery.setVerticalFadingEdgeEnabled(false);
			gallery.setHorizontalFadingEdgeEnabled(false);// 设置view在水平滚动时，水平边不淡出。
			gallery.setAdapter(galleryAdapter);
			gallery.setSelection(currentIndex);
		} else {
			finish();
		}
	}

	/**
	 * 删除单张图片
	 */
	private void delPhoto() {
		FileUtils.deleteFile(selectPicPath);
		FileUtils.deleteFile(selectPicPathTemp);
		loadPhotos(photoDir, selectPosition);
	}

	/**
	 * 计算两点间的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			beforeLenght = spacing(event);
			if (beforeLenght > 5f) {
				isScale = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			/* 处理拖动 */
			if (isScale) {
				afterLenght = spacing(event);
				if (afterLenght < 5f)
					break;
				float gapLenght = afterLenght - beforeLenght;
				if (gapLenght == 0) {
					break;
				} else if (Math.abs(gapLenght) > 5f) {
					float scaleRate = gapLenght / 854;
					Animation myAnimation_Scale = new ScaleAnimation(currentScale, currentScale + scaleRate, currentScale, currentScale + scaleRate, Animation.RELATIVE_TO_SELF,
							0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					myAnimation_Scale.setDuration(100);
					myAnimation_Scale.setFillAfter(true);
					myAnimation_Scale.setFillEnabled(true);
					currentScale = currentScale + scaleRate;
					gallery.getSelectedView().setLayoutParams(new Gallery.LayoutParams((int) (480 * (currentScale)), (int) (854 * (currentScale))));
					beforeLenght = afterLenght;
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			isScale = false;
			break;
		}

		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 控制控件是否可以操作
		if (SystemConfig.isOperate) {
			MenuItem menuItem1 = menu.add(1, 1, 0, subs[0]);
			MenuItem menuItem2 = menu.add(1, 1, 1, subs[1]);
			MenuItem menuItem3 = menu.add(1, 1, 2, subs[2]);
			MenuItem menuItem4 = menu.add(1, 1, 3, subs[3]);
			MenuItem menuItem5 = menu.add(1, 1, 4, subs[4]);
			MenuItem menuItem6 = menu.add(1, 1, 5, subs[5]);
			MenuItem menuItem7 = menu.add(1, 1, 6, subs[6]);

			MenuItem menuItemDel = menu.add(1, 2, 8, "删除？");
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		// 移动/删除图片
		selectPicPath = GalleryAdapter.selectPicPath;
		selectPicPathTemp = selectPicPath.replace(SystemConfig.PHOTO_CLAIM_DIR, SystemConfig.PHOTO_CLAIM_TEMP);

		String picName = selectPicPath.substring(selectPicPath.lastIndexOf("/"), selectPicPath.length());
		String picNameTemp = selectPicPathTemp.substring(selectPicPathTemp.lastIndexOf("/"), selectPicPathTemp.length());
		/**
		 * 判断如果图片已经被上传过则不允许修改位置
		 */
		if (TblPicAddress.isExist(picNameTemp.substring(1, picNameTemp.length()))) {
			fileUtils = new FileUtils();
			boolean isFind = false;
			
			switch (item.getOrder()) {
			case 0:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_0)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[0] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[0], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[0] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[0] + picNameTemp), false);
					}
					delPhoto();
					/**
					 * 将此张照片状态修改为未上传
					 */
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[0]+"】,请移动到其它分类！");
				}
				break;
			case 1:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_1)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[1] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[1], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[1] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[1] + picNameTemp), false);
					}
					delPhoto();
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[1]+"】,请移动到其它分类！");
				}
				break;
			case 2:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_2)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[2] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[2], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[2] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[2] + picNameTemp), false);
					}
					delPhoto();
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[2]+"】,请移动到其它分类！");
				}
				break;
			case 3:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_3)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[3] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[3], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[3] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[3] + picNameTemp), false);
					}
					delPhoto();
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[3]+"】,请移动到其它分类！");
				}
				break;
			case 4:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_4)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[4] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[4], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[4] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[4] + picNameTemp), false);
					}
					delPhoto();
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[4]+"】,请移动到其它分类！");
				}
				break;
			case 5:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_5)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[5] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[5], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[5] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[5] + picNameTemp), false);
					}
					delPhoto();
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[5]+"】,请移动到其它分类！");
				}
				break;
			case 6:
				if(!typeDir.equals(SystemConfig.PHOTO_TYEP_6)){
					fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_DIR + SystemConfig.PHOTO_TYPES[6] + picName), false);
					// cTemp目录已上传照片移动
					isFind = isFindTempPhoto(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[6], selectPicPath);
					if (isFind) {
						fileUtils.copyfile(new File(selectPicPathTemp), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[6] + picNameTemp), false);
					} else {
						fileUtils.copyfile(new File(selectPicPath), new File(SystemConfig.PHOTO_CLAIM_TEMP + SystemConfig.PHOTO_TYPES[6] + picNameTemp), false);
					}
					delPhoto();
					TblPicAddress.updatePicAddressByPicName(picNameTemp.substring(1, picNameTemp.length()));
				}else{
					Toast.showToast(ShowPhotoActivity.this, "该照片为【"+subs[6]+"】,请移动到其它分类！");
				}
				break;
			// 删除该张图片
			case 8:
				PromptManager promptManager = new PromptManager();
				promptManager.showDialog(ShowPhotoActivity.this, "是否删除该张图片", R.string.yes, R.string.no, new ShowDialogPositiveButton() {
					@Override
					public void setPositiveButton() {
						delPhoto();
						/**
						 * 删除对应数据库里数据
						 */
						String picName = selectPicPath.substring(selectPicPath.lastIndexOf("/")+1, selectPicPath.length());
						TblPicAddress.delPicAddress(picName);
					}

					@Override
					public void setNegativeButton() {

					}
				});
				break;
			}
		} else {
			Toast.showToast(ShowPhotoActivity.this, "此照片已经上传过理赔,不允许修改!");
		}
		return false;
	}

	/**
	 * 查找是否存在该文件下有此张图片
	 * 
	 * @param photoDir
	 * @param photoPath
	 * @return
	 */
	private boolean isFindTempPhoto(String photoDir, String photoPath) {
		boolean isFind = false;
		// 文件名集合
		List<String> photos = new ArrayList<String>();
		FileUtils fileUtils = new FileUtils();
		// 查找当前目录里面的文件
		photos = fileUtils.getFileName(new File(photoDir), photoDir);
		for (int i = 0; i < photos.size(); i++) {
			if (photoPath.equals(photos.get(i))) {
				isFind = true;
			}
		}

		return isFind;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (gType.equals("0")) {
			UiManager.getInstance().changeView(PhotosView.class, false, false, null, false);
		} else {
			UiManager.getInstance().changeView(DeflossPhotosView.class, false, false, null, false);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		if (galleryAdapter != null) {
			galleryAdapter.clean();
			galleryAdapter = null;
			System.gc();
		}
		super.onDestroy();
	}
}