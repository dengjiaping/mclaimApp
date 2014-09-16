package com.sinosoftyingda.fastclaim.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;

/**
 * 文件创建，查找
 * @author DengGuang
 *
 */
public class FileUtils {
	
	public static final String SINOSOFT_CACHE_DIR = "cache";
	public static final String CDS_PHOTO_SAVE_DIR = "photos";
	 
	/**
	 * 缓存数据到SDCard
	 * 
	 * @param result
	 * @param fileName
	 */
	public static void cacheResultToSDCard(String result, String fileName ) {

		//加密数据
		//	result = StringUtils.encrypt(result);
		File f = new File(SINOSOFT_CACHE_DIR + "/" + fileName);
		if (f.exists()) {
			f.delete();
		}

		try {
			FileOutputStream utStream = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(utStream, "UTF-8");
			writer.write(result);
			writer.write("\n");
			writer.flush();
			writer.close();// 记得关闭
			utStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得文件大小
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			//f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	/**
	 * 读取数据
	 * @param fileName
	 * @return
	 */
	public static String readCacheFromSDCard(String fileName){
		
		String result="";
		File file = new File(SINOSOFT_CACHE_DIR + "/" + fileName);
		try{
		if(file.exists()){
			FileInputStream inStream = new FileInputStream(file);
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			  byte[] buffer = new byte[1024];
			  int len = -1;
			  //=-1已读到文件末尾
			  while ((len = inStream.read(buffer)) != -1) {
			   outStream.write(buffer, 0, len);
			  }
			  byte[] data = outStream.toByteArray();  
			  outStream.close();
			  inStream.close();
			  
			  result = new String(data);
			//  return new String (data);
			//解密
		//	result = StringUtils.decrypt(result);
		}else{
			System.out.println("文件不存在");
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	public static String readCacheFromSDCard(String fileName, boolean bool){
		
		String result="";
		File file = new File(CDS_PHOTO_SAVE_DIR + "/" + fileName);
		try{
		if(file.exists()){
			FileInputStream inStream = new FileInputStream(file);
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			  byte[] buffer = new byte[1024];
			  int len = -1;
			  //=-1已读到文件末尾
			  while ((len = inStream.read(buffer)) != -1) {
			   outStream.write(buffer, 0, len);
			  }
			  byte[] data = outStream.toByteArray();  
			  outStream.close();
			  inStream.close();
			  
			  result = new String(data);
			//  return new String (data);
			//解密
		//	result = StringUtils.decrypt(result);
		}else{
			System.out.println("文件不存在");
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	
	/**
	 * 缓存数据到SDCard
	 * @param result
	 * @param fileName
	 */
	public static void cacheResultToSDCard(String result, String path, String fileName ) {
		//加密数据
		File f = new File(path + "/" + fileName);
		if (f.exists()) 
			f.delete();
		
		try {
			FileOutputStream utStream = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(utStream, "UTF-8");
			writer.write(result);
			writer.write("\n");
			writer.flush();
			writer.close();// 记得关闭
			utStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 读取数据
	 * @param fileName
	 * @return
	 */
	public static String readCacheFromSDCard(String path,String fileName){
		
		String result="";
		File file = new File(path + "/" + fileName);
		try{
		if(file.exists()){
			FileInputStream inStream = new FileInputStream(file);
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			  byte[] buffer = new byte[1024];
			  int len = -1;
			  //=-1已读到文件末尾
			  while ((len = inStream.read(buffer)) != -1) {
			   outStream.write(buffer, 0, len);
			  }
			  byte[] data = outStream.toByteArray();  
			  outStream.close();
			  inStream.close();
			  
			  result = new String(data);
			//  return new String (data);
			//解密
		//	result = StringUtils.decrypt(result);
		}else{
			System.out.println("文件不存在");
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	

	/**
	 * 递归求取目录文件个数
	 * @param f
	 * @return
	 */
	 public long getlist(File f){
	        long size = 0;
	        File flist[] = f.listFiles();
	        size=flist.length;
	        for (int i = 0; i < flist.length; i++) {
	            if (flist[i].isDirectory()) {
	                size = size + getlist(flist[i]);
	                size--;
	            }
	        }
	        return size;
	   }

	 
	 public String getFileContents(File f){
		 String str = "";
		 try 
         {
             BufferedReader in = new BufferedReader(new FileReader(f));
             while ((str = in.readLine()) != null) 
             {
                   System.out.println(str);
             }
             in.close();
         } 
         catch (IOException e) 
         {
             e.getStackTrace();
         }
         return str;
	 }
	 
	 /**
	  * 取得文件夹大小
	  * @param f
	  * @return
	  * @throws Exception
	  */
	   public long getFileSize(File f) throws Exception
	   {
	       long size = 0;
	       File flist[] = f.listFiles();
	       for (int i = 0; i < flist.length; i++)
	       {
	           if (flist[i].isDirectory())
	           {
	               size = size + getFileSize(flist[i]);
	           } else
	           {
	               size = size + flist[i].length();
	           }
	       }
	       return size;  
	   }
	   
	   /**
	    * 递归获取目录下文件名称
	    * @param f
	    * @return
	    */
	   public List<String> getFileName(File f, String fileDir){  
	    	List<String> names = new ArrayList<String>();
	    	
	        File flist[] = f.listFiles();
	        if(flist != null){
		        for (int i = 0; i < flist.length; i++) {
		            if (flist[i].isDirectory()) {
		                getlist(flist[i]);
		            }else{
		            	String fName = flist[i].getName();
		            	boolean isImage = ImageUtils.getImageFile(fName);
		            	if(isImage){
		            		names.add(fileDir+"/"+flist[i].getName());
		            	}
		            }
		        }
	        }
	        return names;
	    }
	   
	   public List<String> getFileName2(File f, String fileDir){  
	    	List<String> names = new ArrayList<String>();
	    	
	        File flist[] = f.listFiles();
	        if(flist != null){
		        for (int i = 0; i < flist.length; i++) {
		            if (flist[i].isDirectory()) {
		                getlist(flist[i]);
		                List<String> ms = getFileName(flist[i], flist[i].getName());
		                for(int n=0; n<ms.size(); n++){
		                	 names.add(fileDir+ms.get(n));
		                }
		            }else{
	            		names.add(fileDir+flist[i].getName());
		            }
		        }
	        }
	        return names;
	    }
	   
	   /**
	    * 移动文件测试
	    * @author codeif.com
	    */
	   public void RemoveFile(String from, String to) {
	   		File source = new File(from);
	   		File dest = new File(to);
	   		source.renameTo(dest);
	   }
	   
	   /**
	    * 删除图片
	    * @param filePath
	    * @return
	    */
	   public static boolean deleteFile(String filePath) {
		
			File file = new File(filePath);
			if (file.exists()) {
				return file.delete();
			}
			return false;
	   }

	   /**
	     * 创建文件夹
	     * @param file
	     */
	    public static void mkDir(File file){
	    	// 文件是目录， 并且不存在
			if(!file.exists()){   
				file.mkdir();   //创建目录
			}
		}
	    
	    /**
	     * 刪除文件或文件夹
	     *
	     */
	    public static void deleteFileByFile(File file) {

	    	if (file.exists()) { // 判断文件是否存在
	    		if (file.isFile()) { // 判断是否是文件
	    			file.delete(); // delete()方法 你应该知道 是删除的意思;
	    		} else if (file.isDirectory()) { // 否则如果它是一个目录
	    			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
	    			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
	    				deleteFileByFile(files[i]); // 把每个文件 用这个方法进行迭代
	    			}
	    		}
	    		file.delete();
	    	} 
	    }
	
	/**
	 * 文件的复制操作方法  
	 * @param fromFile 被复制的文件
	 * @param toFile   复制的目录文件
	 * @param rewrite  是否重新创建文件
	 * <p>
	 *    文件的复制操作方法
	 */
	public void copyfile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return;
		}

		if (!fromFile.isFile()) {
			return;
		}
		
		if (!fromFile.canRead()) {
			return;
		}
		
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}

		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);

			byte[] bt = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			// 关闭输入、输出流
			fosfrom.close();
			fosto.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 图片打包并添加或更新图片上传记录
	 * 
	 * @param claimNo
	 * @param lossNo
	 */
	public static void saveZIPMSG(String dir, String claimNo, String lossNo) {
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
