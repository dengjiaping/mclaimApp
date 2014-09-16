package com.sinosoftyingda.fastclaim.upload.util;

import java.io.File;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;
import com.sinosoftyingda.fastclaim.common.model.ImageItem;
import com.sinosoftyingda.fastclaim.common.model.PicAddress;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.utils.ZipUtil;

/**
 * 给上传图片打包
 * @author DengGuang
 */
public class CreateZipFile {

	/**
	 * 图片打包并添加或更新图片上传记录
	 * @param context
	 * @param tempDir
	 * @param claimNo
	 * @param lossNo
	 * @return
	 */
	public static boolean saveZIPMSG(Context context, String tempDir, String claimNo, String lossNo) {
		// 查找是否有上传的图片
		File f = new File(tempDir+"/"+claimNo);
		FileUtils fileUtils = new FileUtils();
		// 图片压缩包<FILE>
		List<String> files = fileUtils.getFileName2(f, tempDir);
		
		// 拍照信息表<DB>
		List<ImageItem> uploadpics = TblPicAddress.getPicAddress(claimNo, lossNo, "");
		boolean isInsert = false;
		// 先判断db中是否有上传图片信息
		if(uploadpics.size() <= 0){
			for(int i=0; i<files.size(); i++){
				String tempFileStr = files.get(i);
				tempFileStr = tempFileStr.substring(tempFileStr.lastIndexOf("/")+1, tempFileStr.length());
				isInsert = true;
				
				// 遍历是否有上传图片信息是否存在
				for(int p=0; p<uploadpics.size(); p++){
					String tempUploadStr = uploadpics.get(p).getImageName();
					if(tempFileStr.equals(tempUploadStr)){
						isInsert = false;
					}
				}
				
				// 将没有将文件插入的图片插入到db中
				if(isInsert){
					insertPicaddress(claimNo, lossNo, tempFileStr);
				}
			}
		}
		// 是否上传图片标注
		boolean isUpload = false;
		if(files.size() > 0){
			isUpload = true;
			
			Random random = new Random(System.currentTimeMillis());
			String postfix = random.nextInt(10000) + 100001 + "";// zip包名后缀，5位随机整数100000~200000
			ZipUtil zipUtil = new ZipUtil();
			String fileName = claimNo + "_0_" + postfix;
			try {
				UploadInfo ui = new UploadInfo();
				// private String plateNo; // 损失编号
				ui.setPlateNo(SystemConfig.LOSSNO);
				// private String policyNo; // 报案号
				ui.setPolicyNo(claimNo);
				// private String parcent; // 上传进度
				ui.setParcent("0");
				// private String filesize; // 文件大小
				ui.setFilesize("0");
				// private String action; // 文件名称
				ui.setAction(fileName + ".zip");
				// private String fileUrl;//本地路径
				ui.setFileUrl(tempDir + fileName + ".zip");
				TblUploadInfo.addUploadInfo(ui);
	
				Log.i("PhotoDir", "cTemp[打上传zip包]-------------->" + tempDir + fileName + ".zip");
				zipUtil.doZip(tempDir + claimNo, tempDir + fileName + ".zip", claimNo);
	
				// 删除上传目录图片
				FileUtils.deleteFileByFile(new File(tempDir + claimNo));
			} catch (Exception e) {
				isUpload = false;
				Toast.showToast(context, "程序打包失败");
				e.printStackTrace();
			}
		}
		
		return isUpload;
	}
	
	/**
	 * 插入未插入的照片信息
	 * @param claimNo
	 * @param lossNo
	 * @param picName
	 */
	public static void insertPicaddress(String claimNo, String lossNo, String picName){
		String time = DateTimeUtils.getCurrentTime2();
		PicAddress pic = new PicAddress();
		pic.setRegistNo(claimNo);
		pic.setLossNo(lossNo.equals("") ? "-2" : lossNo);
		pic.setPicAddress("地址暂无");
		pic.setPicName(picName);
		pic.setRemark(time);
		pic.setStarte("0");
		TblPicAddress.insertPicAddress(pic);
	}
}
