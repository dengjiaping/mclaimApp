package com.sinosoftyingda.fastclaim.upload.page;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.BottomManager;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.upload.config.HomeConfig;
import com.sinosoftyingda.fastclaim.upload.util.UploadListviewAdapter;

/**
 * 资料上传
 * 
 * @author haoyun
 */
public class UploadActivity extends BaseView {

	public UploadActivity(Context context, Bundle bundle) {
		super(context, bundle);

	}

	private View container;
	private ListView gLvUploadlist;
	private List<UploadInfo> testData;
	private Bitmap bitmap;

	@Override
	public View getView() {
		return container;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_second;
	}

	@Override
	protected void init() {
		// 第一步：加载layout
		container = inflater.inflate(R.layout.upload_cds, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		container.setLayoutParams(params);
		// 设置顶部标题
		TopManager.getInstance().setHeadTitle("上传队列", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
		gLvUploadlist = (ListView) container.findViewById(R.id.upload_lv_info);
		
		//   -------------------------------->>>>>>>>>>>>>>>>   查找上传队列表
		List<UploadInfo> uploadInfoList = TblUploadInfo.queryUploadInfolist();
		UploadListviewAdapter uploadAdapter = new UploadListviewAdapter(context, uploadInfoList, gLvUploadlist);
		HomeConfig.uploadListViewAdapter = uploadAdapter;
		gLvUploadlist.setAdapter(uploadAdapter);
		// 底部导航
	}
	
	/**
	 * 对未进行打包的图片进行再次打包上传
	 * @author DengGuang
	 * @param reDir
	 */
	private void reZipDir_____(String tempDir){
		// 查找cTemp的文件
		File f = new File(tempDir);
		Map<String, List<String>> mapFiles = FileUtils.researchfile(f);
		List<String> unZipDirs = mapFiles.get("dir");

		// 遍历将数据插入表结构的数据
		List<UploadInfo> uploadinfoLists = TblUploadInfo.queryUploadInfolist();
		for (int i = 0; i < uploadinfoLists.size(); i++) {
			UploadInfo info = uploadinfoLists.get(i);
			String policyNo = info.getPolicyNo();
			String lossNo = info.getPlateNo();
			// 遍历未打包的文件目录
			for (int u = 0; u < unZipDirs.size(); u++) {
				String unDir = unZipDirs.get(u);
				String unPolicy = unDir.substring(unDir.lastIndexOf("/") + 1, unDir.length());
				// 如果此条任务表里面存在，则将未打包的目录进行打包
				if (policyNo.equals(unPolicy)) {
					FileUtils.saveZIPMSG(tempDir, policyNo, lossNo);
				}
			}
		}
	}
	
	

	@Override
	protected void setListener() {

	}

	@Override
	public Integer getExit() {
		return ConstantValue.Page_Title_Upload;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	@Override
	public void onPause() {
		HomeConfig.isStart = false;

		super.onPause();
	}

	@Override
	public void onResume() {
		// start Add DengGuang
		// 对没有打包的文件进行再次打包
		// reZipDir(SystemConfig.PHOTO_TEMP);
		Log.i("PhotoDir", "cTemp[重新打zip包]-------------->"+SystemConfig.PHOTO_TEMP);
		// end 
		
		// 底部导航错位
        BottomManager.getInstance().daoHang(R.id.activity_main_bottom_upload);
		super.onResume();
	}
}
