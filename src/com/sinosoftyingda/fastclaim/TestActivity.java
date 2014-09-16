package com.sinosoftyingda.fastclaim;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.ZipUtil;

/**
 * 测试activity
 * 
 * @author DengGuang
 */
public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 图片打包文件夹
		String reDir = SystemConfig.PHOTO_TEMP;

		// 查找cTemp的文件
		File f = new File(reDir);
		Map<String, List<String>> mapFiles = researchfile(f);
		List<String> unZipDirs = mapFiles.get("dir");
		List<String> zipFiles = mapFiles.get("file");

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
					saveZIPMSG(reDir, policyNo, lossNo);
				}
			}

			// 遍历打包好的文件
			for (int z = 0; z < zipFiles.size(); z++) {

			}
		}
	}

	/**
	 * 图片打包并添加或更新图片上传记录
	 * 
	 * @param claimNo
	 * @param lossNo
	 */
	private void saveZIPMSG(String dir, String claimNo, String lossNo) {
		Random random = new Random(System.currentTimeMillis());
		String postfix = random.nextInt(10000) + 100001 + "";// zip包名后缀，5位随机整数100000~200000
		ZipUtil zipUtil = new ZipUtil();
		String fileName = claimNo + "_0_" + postfix;
		try {
			zipUtil.doZip(dir + claimNo,
					dir + fileName + ".zip", claimNo);
			UploadInfo ui = new UploadInfo();
			// private String plateNo; // 损失编号
			ui.setPlateNo(lossNo);
			// private String policyNo; // 报案号
			ui.setPolicyNo(claimNo);
			// private String parcent; // 上传进度
			ui.setParcent("0");
			// private String filesize; // 文件大小
			ui.setFilesize("0");
			// private String action; // 文件名称
			ui.setAction(fileName + ".zip");
			// private String fileUrl;//本地路径
			ui.setFileUrl(dir + fileName + ".zip");
			TblUploadInfo.addUploadInfo(ui);

			// 删除上传目录图片
			FileUtils.deleteFileByFile(new File(dir + claimNo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找目录文件
	 * 
	 * @param file
	 */
	public static Map<String, List<String>> researchfile(File file) {
		Map<String, List<String>> mapFiles = new HashMap<String, List<String>>();
		List<String> unZipDirs = new ArrayList<String>();
		List<String> zipFiles = new ArrayList<String>();

		if (file.isDirectory()) {
			File[] filearry = file.listFiles();
			for (File f : filearry) {
				// 文件是未打包的文件目录
				if (f.isDirectory()) {
					String unZipDir = f.getAbsoluteFile().toString();
					unZipDirs.add(unZipDir);
				} else {
					String zipFile = f.getAbsoluteFile().toString();
					zipFiles.add(zipFile);
				}
			}
		}
		mapFiles.put("dir", unZipDirs);
		mapFiles.put("file", zipFiles);
		return mapFiles;
	}

}
