package com.sinosoftyingda.fastclaim.upload.util; 

import java.io.File; 
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.PicUploadRequest;
import com.sinosoftyingda.fastclaim.common.service.PicUploadHttpService;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.upload.config.HomeConfig;

/**
 * 文件断点续传
 * @author HaoYun
 */
public class FTPBp implements Runnable {
	private UploadInfo	gUploadInfo;
	private Context		context;

	/**
	 * 枚举类UploadStatus代码
	 * 
	 * @author jingtuo
	 */
	public enum UploadStatus {
		Create_Directory_Fail, // 远程服务器相应目录创建失败
		Create_Directory_Success, // 远程服务器创建目录成功
		Upload_New_File_Success, // 上传新文件成功
		Upload_New_File_Failed, // 上传新文件失败
		File_Exits, // 文件已经存在
		Remote_Bigger_Local, // 远程文件大于本地文件
		Upload_From_Break_Success, // 断点续传成功
		Upload_From_Break_Failed, // 断点续传失败
		Delete_Remote_Faild // 删除远程文件失败
	}

	public FTPClient			ftpClient;
	private String				ftpURL, username, pwd, remotePath, localPath, ftpPort;
	private static int			MAX_BTYE_SIZE		= 10 * 1024; // modify by gl 修改文件上传缓冲区,将100k调整为10k,因为现在每张照片平均在20k左右,所以缓冲区设置太大了,进度条显示比例不协调.
	private static String		DEFAULT_ENCODING	= "UTF-8";
	public boolean				isStart				= false;
	private Map<String, Object>	map					= new HashMap<String, Object>();

	/**
	 * 断点续传的构造函数
	 * 
	 * @param ftpURL
	 * @param username
	 * @param pwd
	 * @param ftpport
	 * @param remotePath
	 *            如果ftp服务器的根目录是ftp.jingtuo,remotePath可以写成"/music"或者"music"或者
	 *            "music/",都表示根目录ftp.jingtuo下的music文件夹
	 * @param localPath
	 */

	public void init(String remotePath, String localPath, View v, TextView tv, Button btn, String infoId, List<UploadInfo> uploadInfos) {
		this.ftpURL = SystemConfig.loginResponse.getTuoBaoFtpUrl();
		this.username = SystemConfig.loginResponse.getTuoBaoFtpUserCode();
		this.pwd = SystemConfig.loginResponse.getTuoBaoFtpPassWord();
		this.ftpPort = SystemConfig.loginResponse.getTuoBaoFtpPort();
		map.put("infoId", infoId);
		map.put("pro", v);
		map.put("tv", tv);
		map.put("btn", btn);
		map.put("uploadInfos", uploadInfos);

		/**
		 * 服务器路径处理,将路径修改成"/"或者"/abc/def"或者"/adb/def.txt"这种规范格式
		 */
		if (remotePath.equals("")) {
			remotePath = "/";
		} else {
			remotePath = replaceAll(remotePath, '\\', '/');
			if (!remotePath.equals("/")) {
				if (!remotePath.startsWith("/")) {
					remotePath = "/" + remotePath;
				}
				if (remotePath.endsWith("/")) {
					remotePath = remotePath.substring(0, remotePath.length() - 1);
				}
			}
		}
		this.remotePath = remotePath;

		/**
		 * 本地路径
		 */
		localPath = replaceAll(localPath, '\\', '/');
		if (localPath.endsWith("/")) {
			localPath = localPath.substring(0, localPath.length() - 1);
		}
		this.localPath = localPath;

		ftpClient = new FTPClient();
		/**
		 * 设置将过程中使用到的命令输出到控制台
		 */
		ftpClient.setControlEncoding(DEFAULT_ENCODING);
	}

	public FTPBp(UploadInfo uploadInfo, Context context) {
		this.gUploadInfo = uploadInfo;
		this.context = context;
	}

	/**
	 * 连接到FTP服务器,
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws Exception
	 */
	public boolean connect() throws Exception {
		Log.d("Tag", "1");
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		ftpClient.connect(this.ftpURL, Integer.parseInt(this.ftpPort));
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(this.username, this.pwd)) {
				return true;
			}
		}
		disconnect();
		return false;
	}

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws Exception
	 */
	public void disconnect() throws Exception {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remotePath
	 * @param ftpClient
	 * @return
	 * @throws Exception
	 */
	public boolean createDirecroty(String remotePath, FTPClient ftpClient) throws Exception {
		/**
		 * printWorkingDirectory输出当前目录 "/" is current directory.表示当前位于ftp服务器根目录
		 * "/荆拓" is current directory.表示当前位于根目录下的"荆拓"文件夹下
		 * changeWorkingDirectory(folderName)进入文件夹
		 * 如果folderName是以"/"开头,比如："/abc/def"
		 * ,如果这个目录存在,则进入根目录下的abc文件夹中的def文件夹中,并返回true,否则返回false
		 * 如果folderName不以"/"开头
		 * ,比如："abc/def",如果这个目录存在,则进入当前目录下的abc文件夹中的def文件夹中,并返回true,否则返回false
		 * makeDirectory(folderName)创建文件夹
		 * 如果folderName是以"/"开头,比如："/abc",在根目录下创建文件夹folderName
		 * ,如果创建成功,则返回true,否则返回false
		 * 如果folderName不以"/"开头,比如："abc",在当前目录下创建文件夹folderName
		 * ,如果创建成功,则返回true,否则返回false
		 * 创建文件夹时必须保证父文件夹存在,比如："/abc/def",如果文件夹abc不存在,就会创建失败
		 */
		ftpClient.printWorkingDirectory();

		if (!remotePath.equals("")) {// 远程路径不为""
			if (!remotePath.equals("/")) {// 远程路径不为"/"
				if (remotePath.contains("/")) {// 远程路径为"/abc"、"/abc/def"、"abc/def"
					if (remotePath.lastIndexOf("/") == 0) {// 远程路径为"/abc"
						if (!ftpClient.changeWorkingDirectory(remotePath)) {
							if (ftpClient.makeDirectory(remotePath)) {
								ftpClient.changeWorkingDirectory(remotePath);
							} else {
								System.out.println("创建目录失败");
								return false;
							}
						}
					} else {// 远程路径为"/abc/def"、"abc/def"
						int start = 0;
						int end = -1;
						if (remotePath.startsWith("/")) {
							start = 1;
						}
						end = remotePath.indexOf("/", start);
						int last = remotePath.lastIndexOf("/");
						String folderName = null;
						while (true) {
							folderName = remotePath.substring(start, end);
							if (!ftpClient.changeWorkingDirectory(folderName)) {
								if (ftpClient.makeDirectory(folderName)) {
									ftpClient.changeWorkingDirectory(folderName);
								} else {
									System.out.println("创建目录失败");
									return false;
								}
							}
							start = end + 1;
							if (end < last && end > 0) {
								end = remotePath.indexOf("/", start);
							} else if (end == last) {
								end = remotePath.length();
							} else {
								break;
							}
						}
					}
				} else {// 远程路径为"abc"
					if (!ftpClient.changeWorkingDirectory(remotePath)) {
						if (ftpClient.makeDirectory(remotePath)) {
							ftpClient.changeWorkingDirectory(remotePath);
						} else {
							System.out.println("创建目录失败");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 替换原字符串中所有oldChar
	 * 
	 * @param str
	 * @param oldChar
	 * @param newChar
	 * @return
	 */
	public static String replaceAll(String str, char oldChar, char newChar) {
		char[] temp = str.toCharArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == oldChar) {
				temp[i] = newChar;
			}
		}
		return new String(temp);
	}

	/*********************************************** 上传upload ******************************************************/

	/**
	 * 上传文件或者文件夹至服务器
	 * 
	 * @throws Exception
	 */
	public void upload() throws Exception {
		Log.d("Tag", "2");
		/**
		 * 设置被动模式
		 */
		ftpClient.enterLocalPassiveMode();
		/**
		 * 设置文件传输模式
		 */
		ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
		/**
		 * 设置以二进制方式传输
		 */
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		localPath = new String(localPath.getBytes(DEFAULT_ENCODING), DEFAULT_ENCODING);
		remotePath = new String(remotePath.getBytes(DEFAULT_ENCODING), DEFAULT_ENCODING);
		String localName = localPath.substring(localPath.lastIndexOf("/") + 1);
		String remoteName = null;
		/**
		 * 判断要上传的目录是一个文件夹还是一个文件,并给出布尔标识
		 */
		boolean isFile = false;
		File file = new File(localPath);
		if (file.isDirectory()) {
			isFile = false;
		} else {
			isFile = true;
		}

		/**
		 * 对远程目录的处理
		 */
		if (remotePath.equals("")) {
			System.out.println("The constructor of " + FTPBp.class.getSimpleName() + " have faults");
			return;
		} else {
			if (remotePath.equals("/")) {// 上传文件或文件夹至服务器根目录
				if (isFile) {
					uploadFile(localPath, remotePath + localName);
				} else {
					uploadFolder(localPath, remotePath);
				}
			} else {
				if (remotePath.contains("/")) {// 上传文件或文件夹至服务器的指定目录
					remoteName = remotePath.substring(remotePath.lastIndexOf("/") + 1);
					if (isFile) {
						if (remoteName.equals(localName)) {
							uploadFile(localPath, remotePath);
						} else {
							createDirecroty(remotePath, ftpClient);
							uploadFile(localPath, remotePath + "/" + localName);
						}
					} else {
						createDirecroty(remotePath, ftpClient);
						uploadFolder(localPath, remotePath);
					}
				} else {// 服务器路径为"abc"
					remoteName = remotePath;
					if (isFile) {
						if (remoteName.equals(localName)) {
							uploadFile(localPath, "/" + remotePath);
						} else {
							createDirecroty(remotePath, ftpClient);
							uploadFile(localPath, "/" + remotePath + "/" + localName);
						}
					} else {
						createDirecroty(remotePath, ftpClient);
						uploadFolder(localPath, remotePath);
					}
				}
			}
		}
	}

	/**
	 * 上传文件夹到服务器上
	 * 
	 * @param localFolder
	 * @param remoteFolder
	 * @throws Exception
	 */
	public void uploadFolder(String localFolder, String remoteFolder) throws Exception {
		String localName = localFolder.substring(localFolder.lastIndexOf("/") + 1);
		FTPFile[] ftpFileArray = null;
		String remoteCurrentPath;
		if (remoteFolder.equals("/")) {// 上传至服务器的根目录
			remoteCurrentPath = remoteFolder + localName;
			if (!ftpClient.changeWorkingDirectory(remoteCurrentPath)) {
				ftpClient.makeDirectory(remoteCurrentPath);
			}
		} else {// 上传至服务器的指定目录
			remoteCurrentPath = remoteFolder + "/" + localName;
			if (!ftpClient.changeWorkingDirectory(remoteCurrentPath)) {
				ftpClient.makeDirectory(remoteCurrentPath);
			}
		}
		ftpFileArray = ftpClient.listFiles(remoteCurrentPath);
		if (ftpFileArray.length >= 2) {
			File folder = new File(localFolder);
			File[] fileArray = folder.listFiles();
			String path = null;
			String name = null;
			for (int i = 0; i < fileArray.length; i++) {
				path = fileArray[i].getAbsolutePath();
				path = replaceAll(path, '\\', '/');
				name = path.substring(path.lastIndexOf("/") + 1);
				if (fileArray[i].isDirectory()) {
					uploadFolder(path, remoteCurrentPath);
				} else {
					uploadFile(path, remoteCurrentPath + "/" + name);
				}
			}
		} else {
			System.out.println("FTP服务端与FTP客户端不一致");
		}
	}

	// modify by gl 20131204 修改上传进度显示问题 begin
	/**
	 * 上传文件到服务器上
	 * 
	 * @param localFile
	 * @param remoteFile
	 * @throws Exception
	 */
	public void uploadFile(String localFile, String remoteFile) throws Exception {
		// ---》 start Add Dengguang
		// 解决数据库保存路径重复
		// 获取Sdcard路径
		File sdcardDir = Environment.getExternalStorageDirectory();
		String reDir = sdcardDir.getParent() + "/" + sdcardDir.getName();
		// 查找是否有重复的路径
		int find = localFile.indexOf(reDir + reDir);
		if (find == 0) {
			localFile = localFile.replaceAll(reDir + reDir, reDir);
		}
		File file = new File(localFile);
		// 判断此文件是否存在，存在即上传，否在就删除表中的数据
		if (!file.exists()) {
			TblUploadInfo.delUploadItem(localFile);
		} else {
			// ---》 end
			FileInputStream fis = new FileInputStream(file);
			int fileLen = fis.available(); // 本地文件大小
			Log.i("TAG", "File size   " + localFile.length() + "===" + fileLen);
			long localFileSize = file.length();
			long remoteFileSize = 0;
			FTPFile[] ftpFileArray = ftpClient.listFiles(remoteFile);
			if (ftpFileArray.length == 1) {// 要上传的文件已经存在
				Log.i("TAG", "文件已存在");
				remoteFileSize = ftpFileArray[0].getSize();
				if (remoteFileSize >= localFileSize) {
					map.put("fileLen", fileLen);
					sendProcess(100);
				}
			} else {
				remoteFileSize = 0;
			}
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			OutputStream out = ftpClient.appendFileStream(remoteFile);

			long step = localFileSize / 100;
			long process = 0;
			long localreadbytes = 0L;
			Log.i("TAG", "remoteFileSize---" + remoteFileSize);
			if (remoteFileSize > 0) {
				ftpClient.setRestartOffset(remoteFileSize);
				if (step == 0)
					process = 100;
				else
					process = remoteFileSize / step;
				randomAccessFile.seek(remoteFileSize);
				localreadbytes = remoteFileSize;
			}
			byte[] bytes = new byte[MAX_BTYE_SIZE];
			int c;

			while ((c = randomAccessFile.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localreadbytes += c;
				if (step != 0) {
					if (localreadbytes / step != process) {
						process = localreadbytes / step;
//						if (localreadbytes != localFileSize) {
							if (HomeConfig.isStart) {
								map.put("fileLen", fileLen);
								sendProcess((int) process);
							} else {
								break;
							}
//						}
					}
				} else {
					/** 上传完成 */
					if (HomeConfig.isStart) {
						map.put("fileLen", fileLen);
						sendProcess(100);
					} else {
						break;
					}
				}
			}
			out.flush();
			randomAccessFile.close();
			out.close();
			ftpClient.completePendingCommand();
		}
	}

	/**
	 * 向页面发送上传进度
	 * 
	 * @param process 0~100进度值
	 */
	private void sendProcess(int process) {
		Log.d("Tag", "上传进度:" + process);
		Message msg = new Message();
		msg.arg1 = (int) process;
		msg.obj = map;
		handler.sendMessage(msg);
	}
	// modify by gl 20131204 修改上传进度显示问题 end
	
	@Override
	public void run() {
		System.gc();
		try {
			Log.i("TAG", "-------上传方法-------" + HomeConfig.isStart);
			this.connect();
			this.upload();
			this.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Handler	handler	= new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Map<String, Object> m = (Map<String, Object>) msg.obj;
			ProgressBar pb = (ProgressBar) m.get("pro");
			TextView tv = (TextView) m.get("tv");
			TextView fileSize = (TextView) m.get("fileSize");
			Button btn = (Button)m.get("btn");
			float fileSizeValue = Integer.parseInt(m.get("fileLen").toString()) / (1024 * 1024);

			BigDecimal b = new BigDecimal(fileSizeValue);
			fileSizeValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			pb.setProgress(msg.arg1);
			tv.setText(msg.arg1 + "%"); // add by gl 20131204 进度百分比与进度条显示同步,没必要分逻辑显示
			
			if (msg.arg1 >= 100) {
				// 与拓保交互接口
				picUploadMutual();
						
				if (HomeConfig.ftpBPRList.size() > 0)
					HomeConfig.ftpBPRList.get(0).run();
			}
		}
	};
	
	Handler	uploadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.arg1 == 1){
				List<UploadInfo> uploadInfos = (List<UploadInfo>) map.get("uploadInfos");
				if (uploadInfos.size() > 0){
					uploadInfos.remove(0);// 删除 listview 第一条记录
				}
				HomeConfig.ftpBPRList.remove(0);
				HomeConfig.uploadListViewAdapter.notifyDataSetChanged();
				TblUploadInfo.updateUploadInfo(gUploadInfo);
			}else{
				ProgressBar pb = (ProgressBar) map.get("pro");
				TextView tv = (TextView) map.get("tv");
				Button btn = (Button)map.get("btn");
				
				tv.setText("0%");
				pb.setProgress(0);
				btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.update_start));
			}
		}
	};
	
	
	/**
	 * 与拓宝影像系统进行数据交互
	 */
	private void picUploadMutual(){
		
		new AsyncTask<String, Void, Map<String, Object>>() {
			@Override
			protected void onPreExecute() {
//				uploadHandler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN); // delete by gl 20131204 调用拓保接口时,不应该给uploadHandler发送消息,该uploadHandler只处理调用拓保影像接口成功和失败的处理逻辑,没有处理开始调用拓保影像系统时的处理逻辑
			};

			@Override
			protected void onPostExecute(Map<String, Object> result) {
				uploadHandler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				Message msg = Message.obtain();
				
				// 取返回过来的结果
				CommonResponse commonResponse = (CommonResponse) result.get("CommonResponse");
				// 上传成功
				if(commonResponse.getResponseCode().equals("YES") || commonResponse.getResponseCode().equals("Success")){
					Toast.makeText(context, "资料上传成功！", Toast.LENGTH_SHORT).show();
					PicUploadRequest pic = (PicUploadRequest) result.get("PicUploadRequest");
					
					// 删除上传记录
					TblUploadInfo.delUploadInfoItem(gUploadInfo.getAction());
					FileUtils.deleteFile(gUploadInfo.getFileUrl());
					// 删除表记录
					TblPicAddress.delPicAddress(pic.getRegistNo(), pic.getLossNo());
					//TblPicAddress.updatePicAddress(pic.getRegistNo(), pic.getLossNo());
					msg.arg1 = 1;
					
					/* msg.arg1 = 0; */
				}else{
					Toast.makeText(context, "资料上传失败！"+commonResponse.getResponseMessage(), Toast.LENGTH_LONG).show();
					msg.arg2 = 0;
				}
				uploadHandler.sendMessage(msg);
			}

			@Override
			protected Map<String, Object> doInBackground(String... params) {
				// 请求报文数据组织
				PicUploadRequest pic = new PicUploadRequest();
				pic.setRegistNo(gUploadInfo.getPolicyNo());
				pic.setFileName(context.getString(R.string.remote_path)
						.substring(1, context.getString(R.string.remote_path).length())+ gUploadInfo.getAction());

				if (gUploadInfo.getPlateNo() != null && !gUploadInfo.getPlateNo().equals("")) {
					pic.setModelCode("certainLoss");
					pic.setLossNo(gUploadInfo.getPlateNo());
				} else {
					pic.setModelCode("check");
					pic.setLossNo("-2");
				}
				pic.setImageItem(TblPicAddress.getPicAddress(pic.getRegistNo(), pic.getLossNo(), "0"));

				// 上传接口
				CommonResponse commonResponse = PicUploadHttpService.picUploadService(pic, context.getString(R.string.http_url));
				
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("PicUploadRequest", pic);
				result.put("CommonResponse", commonResponse);
				
				return result;
			}
		}.execute();
	}
}
