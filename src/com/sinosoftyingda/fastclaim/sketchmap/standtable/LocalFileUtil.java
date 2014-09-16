package com.sinosoftyingda.fastclaim.sketchmap.standtable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * 游戏数据本地存储工具类提供： 1-简单文本数据本地存储、读取：存储于DATA/FINGER.MOBEASY/**db.xml
 * 2-二进制数据文件存储、读取，主要用来存储从网络上下载的文件
 * 
 * @author anychen
 * 
 */
public class LocalFileUtil {
	static Context context;

	/**
	 * 注册一个Context
	 * 
	 * @param ctx
	 */
	public static void registerContext(Context ctx) {
		context = ctx;
	}

	/**
	 * 存储文件到本地
	 * 
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void writeFile(String fileName, byte[] content, int mode) throws IOException {
		File file=new File(fileName);
		FileOutputStream fos_db = new FileOutputStream(file);
fos_db.write(content);
		fos_db.flush();
		fos_db.close();
	}

	/**
	 * 读取文件：返回原始字节数值
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(String name) throws IOException {
		FileInputStream fis_db = context.openFileInput(name);
		byte[] buffer = new byte[fis_db.available()];
		fis_db.read(buffer);
		fis_db.close();
		return buffer;
	}
	public static File getBillingDir(){
		return getDir("billing");
	}
	public static File getDir(String dirName){
		return context.getDir(dirName, Context.MODE_PRIVATE);
	}
	/**
	 * 读取文本文件
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String readTxtFile(String name) throws IOException {
		FileInputStream fis_db = context.openFileInput(name);
		byte[] buffer = new byte[fis_db.available()];
		fis_db.read(buffer);
		fis_db.close();
		String content = new String(buffer);
		return content;
	}

	/**
	 * 返回文件输入流
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static FileInputStream getFileInputStream(String name) throws IOException {
		return context.openFileInput(name);
	}

	/**
	 * 返回文件描述，比如在线
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static FileDescriptor getFileDescriptor(String name) throws IOException {
		return context.openFileInput(name).getFD();
	}

	/**
	 * 返回当前游戏中所有的本地存储文件名数组
	 * 
	 * @return
	 */
	public static String[] getFileList() {
		return context.fileList();
	}

	/**
	 * 删除指定文件
	 * 
	 * @param name
	 * @return 是否删除成功
	 */
	public static boolean deleteFile(String fileName) {
		File file=new File(fileName);
		return file.delete();
	}
}
