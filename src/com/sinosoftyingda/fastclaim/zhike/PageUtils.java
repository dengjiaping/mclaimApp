package com.sinosoftyingda.fastclaim.zhike;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author JingTuo
 *
 */
public class PageUtils {
	
	public static int getlength(String str) {
		int i, t = 0;
		byte[] bt = str.getBytes();
		for (i = 1; i <= bt.length; i++) {
			if (bt[i - 1] < 0) {
				t = t + 2;
				i++;
			} else
				t = t + 1;
		}
		return t;
	}
	
	/**
	 * 设置页面大小
	 * @param width
	 * @param height
	 */
	public static void SetPageSize(int width, int height) {
		byte[] CommandData = new byte[10];
		CommandData[0] = 0x1c;
		CommandData[1] = 0x4c;
		CommandData[2] = 0x70;
		CommandData[3] = (byte) (width % 256);
		CommandData[4] = (byte) (width / 256);
		CommandData[5] = (byte) (height % 256);
		CommandData[6] = (byte) (height / 256);
		CommandData[7] = 0x00;
		PrintUtils.SPPWrite(CommandData, 8);
	}

	/**
	 * 清除页面
	 */
	public static void ClearPage() {
		byte[] CommandData = new byte[8];
		CommandData[0] = 0x1c;
		CommandData[1] = 0x4c;
		CommandData[2] = 0x63;
		PrintUtils.SPPWrite(CommandData, 3);
	}

	/**
	 * 报错后是否提示继续打印<br>
	 * true 报错后继续打印<br>
	 * false 报错后不继续打印<br>
	 * @param ContinueFlag
	 */
	public static void ErrorConfig(boolean ContinueFlag) {
		byte[] CommandData = new byte[8];
		CommandData[0] = 0x1c;
		CommandData[1] = 0x4c;
		CommandData[2] = 0x72;
		if (ContinueFlag)
			CommandData[3] = 0x01;
		else
			CommandData[3] = 0x00;
		PrintUtils.SPPWrite(CommandData, 4);
	}

	/**
	 * 打印页面
	 * 
	 * @param mark	0:直接停止；1：Lable;2:left mark;3:right mark;4:any；
	 * @param maxlengthmm	maxlongthmm 最大走纸距离 （毫米）
	 * @param rotate	是否旋转页面90度 true 旋转90度
	 */
	public static void PrintPage(int mark, int maxlengthmm, boolean rotate) {
		byte[] CommandData = new byte[8];
		CommandData[0] = 0x1c;
		CommandData[1] = 0x4c;
		CommandData[2] = 0x6f;
		if (rotate)
			CommandData[3] = 0x01;
		else
			CommandData[3] = 0x00;
		CommandData[4] = (byte) (mark % 256);
		CommandData[5] = (byte) (maxlengthmm % 256);
		CommandData[6] = (byte) (maxlengthmm / 256);
		PrintUtils.SPPWrite(CommandData, 7);
	}

	/**
	 * 画线
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param linewidth
	 */
	public static void DrawLine(int x0, int y0, int x1, int y1, int linewidth) {
		byte[] CommandData = new byte[16];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x6C;
		CommandData[3] = (byte) (linewidth % 256);
		CommandData[4] = 1;// 一条线段
		CommandData[5] = (byte) (x0 % 256);
		CommandData[6] = (byte) (x0 / 256); // x
		CommandData[7] = (byte) (y0 % 256);
		CommandData[8] = (byte) (y0 / 256); // y
		CommandData[9] = (byte) (x1 % 256);
		CommandData[10] = (byte) (x1 / 256); // xx
		CommandData[11] = (byte) (y1 % 256);
		CommandData[12] = (byte) (y1 / 256); // yy
		PrintUtils.SPPWrite(CommandData, 13);
	}

	/**
	 * 添加文字
	 * @param x
	 * @param y
	 * @param str
	 * @param font
	 * @param fontsize
	 */
	public static void DrawText(int x, int y, String str, int font,
			int fontsize) {
		byte[] CommandData = new byte[512];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x74;
		CommandData[3] = (byte) (x % 256);
		CommandData[4] = (byte) (x / 256); // x
		CommandData[5] = (byte) (y % 256);
		CommandData[6] = (byte) (y / 256); // y
		CommandData[7] = (byte) (font % 256);
		CommandData[8] = (byte) (fontsize % 256);
		PrintUtils.SPPWrite(CommandData, 9);
		try {
			CommandData = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		PrintUtils.SPPWrite(CommandData);
		CommandData[0] = 0x00;
		PrintUtils.SPPWrite(CommandData, 1);
	}

	/**
	 * 添加文本框
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param str
	 * @param font
	 * @param fontsize
	 */
	public static void DrawTextBox(int x, int y, int width, int height,
			String str, int font, int fontsize) {
		byte[] CommandData = new byte[512];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x54;
		CommandData[3] = (byte) (x % 256);
		CommandData[4] = (byte) (x / 256); // x
		CommandData[5] = (byte) (y % 256);
		CommandData[6] = (byte) (y / 256); // y
		CommandData[7] = (byte) (width % 256);
		CommandData[8] = (byte) (width / 256); // x
		CommandData[9] = (byte) (height % 256);
		CommandData[10] = (byte) (height / 256); // y
		CommandData[11] = (byte) (font % 256);
		CommandData[12] = (byte) (fontsize % 256);
		PrintUtils.SPPWrite(CommandData, 13);
		try {
			CommandData = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
		}
		PrintUtils.SPPWrite(CommandData);
		CommandData[0] = 0x00;
		PrintUtils.SPPWrite(CommandData, 1);
	}

	/**
	 * 添加一维条码
	 * @param x
	 * @param y
	 * @param str
	 * @param codetype
	 * @param rotatewidth
	 * @param height
	 */
	public static void DrawCode1D(int x, int y, String str, int codetype,
			int rotatewidth, int height) {
		byte[] CommandData = new byte[256];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x62;
		CommandData[3] = (byte) (x % 256);
		CommandData[4] = (byte) (x / 256); // x
		CommandData[5] = (byte) (y % 256);
		CommandData[6] = (byte) (y / 256); // y
		CommandData[7] = (byte) (codetype % 256);
		CommandData[8] = (byte) (rotatewidth % 256);
		CommandData[9] = (byte) (height % 256);
		PrintUtils.SPPWrite(CommandData, 10);
		try {
			CommandData = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
		}
		PrintUtils.SPPWrite(CommandData);
		CommandData[0] = 0x00;
		PrintUtils.SPPWrite(CommandData, 1);
	}

	/**
	 * 添加二维条码
	 * @param x
	 * @param y
	 * @param str
	 * @param codenum
	 * @param codetype
	 * @param rotatewidth
	 * @param d
	 */
	public static void DrawCode2D(int x, int y, String str, int codenum,
			int codetype, int rotatewidth, int d) {
		byte[] CommandData = new byte[256];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x42;
		CommandData[3] = (byte) (x % 256);
		CommandData[4] = (byte) (x / 256); // x
		CommandData[5] = (byte) (y % 256);
		CommandData[6] = (byte) (y / 256); // y
		CommandData[7] = (byte) (codetype % 256);
		CommandData[8] = (byte) (rotatewidth % 256);
		CommandData[9] = (byte) (d % 256);
		CommandData[10] = (byte) (codenum % 256);
		CommandData[11] = (byte) (codenum / 256); // length
		PrintUtils.SPPWrite(CommandData, 12);
		try {
			CommandData = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
		}
		PrintUtils.SPPWrite(CommandData);
	} 
	
	/**
	 * 选择页面
	 * @param index
	 */
	public static void SelectPage(int index) {
		byte[] CommandData = new byte[8];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x50;
		CommandData[3] = (byte) (index % 256);
		PrintUtils.SPPWrite(CommandData, 4);
	}// select page

	/**
	 * 打印带增量数据
	 * @param x
	 * @param y
	 * @param str
	 * @param font
	 * @param rotatefontsize
	 * @param post
	 */
	public static void DrawTextSpike(int x, int y, String str, int font,
			int rotatefontsize, int post) {
		byte[] CommandData = new byte[256];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x61;
		CommandData[3] = (byte) (x % 256);
		CommandData[4] = (byte) (x / 256); // x
		CommandData[5] = (byte) (y % 256);
		CommandData[6] = (byte) (y / 256); // y
		CommandData[7] = (byte) (font % 256);
		CommandData[8] = (byte) (rotatefontsize % 256);
		CommandData[9] = (byte) (post % 256);
		PrintUtils.SPPWrite(CommandData, 10);
		try {
			CommandData = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
		}
		PrintUtils.SPPWrite(CommandData);
		CommandData[0] = 0x00;
		PrintUtils.SPPWrite(CommandData, 1);
	}

	// 设置增量数据
	public static void SetSpike(int dataL, int dataH, int spike) {
		byte[] CommandData = new byte[16];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x49;
		CommandData[3] = (byte) (dataL % 256);
		CommandData[4] = (byte) (dataL / 256); // x
		CommandData[5] = (byte) (dataH % 256);
		CommandData[6] = (byte) (dataH / 256); // y
		CommandData[7] = (byte) (spike % 256);
		CommandData[8] = (byte) (spike / 256); // y7
		PrintUtils.SPPWrite(CommandData, 9);
	}

	/**
	 * 打印带增量页面
	 * @param mark
	 * @param maxlength
	 * @param num
	 * @param b
	 */
	public static void PrintPageSpike(int mark, int maxlength, int num, int b) {
		byte[] CommandData = new byte[16];
		CommandData[0] = 0x1C;
		CommandData[1] = 0x4C;
		CommandData[2] = 0x4F;
		CommandData[3] = (byte) (b % 256);
		CommandData[4] = (byte) (mark % 256);
		CommandData[5] = (byte) (maxlength % 256);
		CommandData[6] = (byte) (maxlength / 256);
		CommandData[7] = (byte) (num % 256);
		CommandData[8] = (byte) (num / 256);
		PrintUtils.SPPWrite(CommandData, 9);
	}

}
