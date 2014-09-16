package com.sinosoftyingda.fastclaim.signature.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;

/**
 * 客户签名
 * 
 * @author DengGuang
 */
public class SignatureActivity extends Activity implements OnClickListener {

	private SignatureView	gSignatureView; // 手迹画板
	private Button			gBtnBack;		// 返回
	private Button			gBtnClear;		// 清空重置按钮
	private Button			gBtnSave;		// 保存按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signture);
		init();
		setView();
	}

	/**
	 * 初始化组
	 */
	private void init() {
		/* 初始化组件 */
		gBtnBack = (Button) findViewById(R.id.signture_btn_back);
		gBtnClear = (Button) findViewById(R.id.signture_btn_clear);
		gBtnSave = (Button) findViewById(R.id.signture_btn_save);
		gSignatureView = (SignatureView) findViewById(R.id.signture_sv_sign);
		// 点击时间
		gBtnBack.setOnClickListener(this);
		gBtnClear.setOnClickListener(this);
		gBtnSave.setOnClickListener(this);
	}

	/**
	 * 设置值
	 */
	private void setView() {
		if (!SystemConfig.isOperate) {// false 不可操作
			gBtnClear.setEnabled(false);
			gBtnSave.setEnabled(false);
		} else {
			gBtnClear.setEnabled(true);
			gBtnSave.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 重置清空
		case R.id.signture_btn_clear:
			gSignatureView.clear();
			break;

		// 保存
		case R.id.signture_btn_save:
			//保存照片相关信息到picadress表中
			gSignatureView.savePicInfo();
			gSignatureView.save(this);

			String signDir = SystemConfig.PHOTO_DIR + SystemConfig.PHOTO_CLAIMNO;
			FileUtils.mkDir(new File(signDir));
			signDir = signDir + SystemConfig.PHOTO_TYEP_0;
			FileUtils.mkDir(new File(signDir));
			String signtureName = SystemConfig.PHOTO_CLAIMNO + "_sign.png";
			String paintSignture = signDir + "/" + signtureName;
			String paintSigntureTemp = paintSignture.replace(SystemConfig.PHOTO_DIR, SystemConfig.PHOTO_TEMP);
			FileUtils fileUtils = new FileUtils();
			File fromFile = new File(paintSignture);
			File toFile = new File(paintSigntureTemp);
			fileUtils.copyfile(fromFile, toFile, false);
			if (!CheckSurveyValue.isSignature) {
				delSignturePic();
			}

			finish();
			break;

		// 返回
		case R.id.signture_btn_back:
			finish();
			break;
		}
	}

	/**
	 * 删除空白手写签名
	 */
	private void delSignturePic() {

		new Thread() {
			@Override
			public void run() {
				// /mnt/sdcard/cClaim/1001010010(报案号)/signture
				String signDir = SystemConfig.PHOTO_DIR + SystemConfig.PHOTO_CLAIMNO;
				FileUtils.mkDir(new File(signDir));
				String signDirTemp = SystemConfig.PHOTO_TEMP + SystemConfig.PHOTO_CLAIMNO;
				FileUtils.mkDir(new File(signDirTemp));

				signDir = signDir + SystemConfig.PHOTO_TYEP_0;
				FileUtils.mkDir(new File(signDir));
				signDirTemp = signDirTemp + SystemConfig.PHOTO_TYEP_0;
				FileUtils.mkDir(new File(signDirTemp));

				String signtureName = SystemConfig.PHOTO_CLAIMNO + "_sign.png";
				String signturePath = signDir + "/" + signtureName;
				String signturePathTemp = signDirTemp + "/" + signtureName;

				FileUtils fileUtils = new FileUtils();
				// 文件名集合
				List<String> signtureImages = new ArrayList<String>();
				// 查找当前目录里面的文件
				signtureImages = fileUtils.getFileName(new File(signDir), signDir);
				for (int i = 0; i < signtureImages.size(); i++) {
					if (signtureImages.get(i).equals(signturePath)) {
						FileUtils.deleteFile(signturePath);
						CheckSurveyValue.isSignature = false;
					}
				}

				// 文件名集合
				List<String> signtureImageTemps = new ArrayList<String>();
				// 查找当前目录里面的文件
				signtureImageTemps = fileUtils.getFileName(new File(signDirTemp), signDirTemp);
				for (int i = 0; i < signtureImageTemps.size(); i++) {
					if (signtureImageTemps.get(i).equals(signturePathTemp)) {
						FileUtils.deleteFile(signturePathTemp);
					}
				}

			};
		}.start();

	}

	@Override
	protected void onPause() {
		if (gSignatureView != null)
			gSignatureView.bitMapRecyle();

		gSignatureView = null;
		SystemConfig.isSignatureActivity = true;
		super.onPause();
	}

}
