package com.sinosoftyingda.fastclaim.splash.page;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.UpdateVersionRequest;
import com.sinosoftyingda.fastclaim.common.model.UpdateVersionResponse;
import com.sinosoftyingda.fastclaim.common.service.UpdateVersionHttpService;
import com.sinosoftyingda.fastclaim.common.utils.BitmapUtils;
import com.sinosoftyingda.fastclaim.common.utils.DensityUtil;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.login.page.LoginView;
import com.sinosoftyingda.fastclaim.more.service.MoreServlet;

/**
 * 欢迎界面
 * 
 * @author chenjianfan
 * 
 */
public class SplashView extends BaseView {
	/********** 布局 **************/
	private LinearLayout container;
	/********** 控件 **************/
	private BitmapUtils bitmapUtils;
	private LinearLayout splashlayout;

	private MoreServlet moreServlet;
	private ProgressDialog mProgressDialog;
	private ProgressDialog progressDialog;
	private PromptManager promptManager;

	private Handler svHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantValue.Splash_VERSION_UPDATE:
				// 更新完之后跳转到登录页面
				UiManager.getInstance().changeView(LoginView.class, null, false, false);
				break;
			case ConstantValue.Splash_Skip:
				// 跳转到登录页面
				UiManager.getInstance().changeView(LoginView.class, null, false, false);
			}
		};
	};
	private Drawable drawable;
	private Bitmap bitmap;

	public SplashView(Context context, Bundle bundle) {
		super(context, bundle);

	}

	@Override
	public View getView() {

		return container;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_Login;
	}

	@Override
	protected void init() {
		// 加载布局文件
		container = (LinearLayout) inflater.inflate(R.layout.splash, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		container.setLayoutParams(params);
		progressDialog = new ProgressDialog(context);
		// sd判断
		if (!HttpUtils.hasSdcard()) {
			Toast.showToast(context, "请正确安装sd卡");
		} else {
			createFileDir();
		}

		// 初始化控件
		splashlayout = (LinearLayout) container.findViewById(R.id.LinearLayout01);
		// 代码定义背景图片
		// bitmap = BitmapUtils.optimizeLoadPic(context, context.getResources(),
		// R.drawable.welcome_bg);
		bitmap = BitmapUtils.readBitMap(context, R.drawable.welcome_bg);
		splashlayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
		// 初始化控件的显示
		setView();
	}


	/**
	 * 创建文件目录
	 */
	private void createFileDir() {
		// 获取Sdcard路径
		File sdcardDir = Environment.getExternalStorageDirectory();
		String sdDir = sdcardDir.getParent() + "/" + sdcardDir.getName();
		// 本地相册目录
		SystemConfig.PHOTO_DIR = sdDir + SystemConfig.PHOTO_DIR;
		FileUtils.mkDir(new File(SystemConfig.PHOTO_DIR));
		// 上传目录
		SystemConfig.PHOTO_TEMP = sdDir + SystemConfig.PHOTO_TEMP;
		FileUtils.mkDir(new File(SystemConfig.PHOTO_TEMP));
		
		Log.i("PhotoDir", "claim[创建目录]-------------->"+SystemConfig.PHOTO_DIR);
		Log.d("PhotoDir", "cTemp[创建目录]-------------->"+SystemConfig.PHOTO_TEMP);
	}

	private void setView() {
		
	}

	@Override
	public void onResume() {
		super.onResume();
		updatVersion();
	}

	@Override
	protected void setListener() {

	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = DensityUtil.px2dip(context, drawable.getIntrinsicWidth());
		int h = DensityUtil.px2dip(context, drawable.getIntrinsicHeight());
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	@Override
	public Integer getExit() {
		return null;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	/****
	 * 版本更新 Message message = new Message(); message.what =
	 * ConstantValue.Splash_VERSION_UPDATE; svHandler.sendMessage(message);
	 */
	private void updatVersion() {
		if (!HttpUtils.isNetworkConnected(context)) {
			Toast.showToast(context, R.string.error_not_connection);
			UiManager.getInstance().changeView(LoginView.class, null, false, false);
			return;
		}

		new AsyncTask<String, Void, UpdateVersionResponse>() {

			@Override
			protected void onPreExecute() {
				PromptManager.showSimpleProgressDialog(progressDialog, context.getString(R.string.is_loading));
			};

			@Override
			protected void onPostExecute(UpdateVersionResponse result) {
				PromptManager.closeProgressDialog(progressDialog);
				final UpdateVersionResponse response = result;
				if ("YES".equals(result.getResponseCode())) {
					if (TextUtils.isEmpty(result.getUpdateUrl())) {
						// 跳转到登录页面
						Message message = new Message();
						message.what = ConstantValue.Splash_VERSION_UPDATE;
						svHandler.sendMessage(message);
					} else {
						promptManager = new PromptManager();
						PromptManager.showUpdateDialog(context, "有新版本，请更新版本!", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								download(response.getUpdateUrl());
							}
						});
					}

				} else {
					UiManager.getInstance().changeView(LoginView.class, null, false, false);
				}

			};

			@Override
			protected UpdateVersionResponse doInBackground(String... params) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				UpdateVersionRequest updateVersionRequest = new UpdateVersionRequest();
				moreServlet = new MoreServlet(context);
				updateVersionRequest.setVersion(moreServlet.getVersion());
				return UpdateVersionHttpService.updateVersionService(
						updateVersionRequest, params[0]);
			}

		}.execute(context.getString(R.string.http_url));

	}

	/***
	 * 下载安装
	 */
	protected void download(String urlPath) {

		new AsyncTask<String, Void, File>() {
			@Override
			protected void onPreExecute() {
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setTitle("下载进度");
				mProgressDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setProgress(100);
				// 设置ProgressDialog 是否可以按退回键取消
				mProgressDialog.setCancelable(false);
				mProgressDialog.incrementProgressBy(1);
				mProgressDialog.show();
			};

			@Override
			protected void onPostExecute(File result) {
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				if (result != null) {
					moreServlet.installApp(result);
				} else {
					UiManager.getInstance().changeView(LoginView.class, null,
							false, false);
				}
			};

			@Override
			protected File doInBackground(String... params) {
				return moreServlet.download(params[0], mProgressDialog);
			}

		}.execute(urlPath);
	}

	@Override
	public void onPause() {
		if (bitmap != null)
			bitmap.recycle();
		if (bitmapUtils != null)
			bitmapUtils = null;
		if (moreServlet != null)
			moreServlet = null;
		if (mProgressDialog != null)
			mProgressDialog = null;
		if (progressDialog != null)
			progressDialog = null;
		if (promptManager != null)
			promptManager = null;
		System.gc();
		super.onPause();
	}
}
