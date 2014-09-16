package com.sinosoftyingda.fastclaim.common.recordError;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * 上传工具
 * 
 * @author TanYanqiang
 * 
 */
public class Upload {
	private static final String END = "\r\n";
	private static final String TWOHYPHENS = "--";
	private static final String BOUNDARY = "*****";
	private static final String CHARSET = "utf-8";

	private static Upload upload = new Upload();
	private Upload() {

	}

	public static Upload getInstance() {
		return upload;
	}
	
	/**
	 * POST提交文件
	 * @param actionUrl
	 * @param params
	 * @param file
	 * @return
	 */
	public String post(String actionUrl, Map<String, String> params, File file) {
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", CHARSET);
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			dos.write(buildFormText(params).getBytes());
			dos.write(buildFileText(file).getBytes());
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, length);
			}

			fis.close();
			dos.write(END.getBytes());
			dos.writeBytes(END);
			dos.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + END);
			dos.flush();

			InputStream is = connection.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			dos.close();

			if (connection.getResponseCode() != 200) {
				connection.disconnect();
			}
			
			return "success";

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "fail";
	}

	
	/**
	 *  创建表单
	 * @param paramText
	 * @return
	 */
	private String buildFormText(Map<String, String> paramText) {
		StringBuffer sb = new StringBuffer();
		if (paramText != null) {
			for (Map.Entry<String, String> entry : paramText.entrySet()) {
				sb.append(TWOHYPHENS);
				sb.append(BOUNDARY);
				sb.append(END);

				String key = entry.getKey();
				if (key.indexOf("comments") != -1) {
					key = key.substring(0, key.indexOf("_"));
				}

				sb.append("Content-Disposition: form-data; name=\"" + key + "\"" + END);
				sb.append(END);
				sb.append(entry.getValue());
				sb.append(END);
			}
		}
		
		return sb.toString();
	}

	
	/**
	 *  创建文件
	 * @param file
	 * @return
	 */
	private String buildFileText(File file) {
		StringBuffer sb = new StringBuffer();
		if (file != null) {
			sb.append(TWOHYPHENS);
			sb.append(BOUNDARY);
			sb.append(END);
			sb.append("Content-Disposition: form-data; " + "name=\"doc\";filename=\"" + file.getName() + "\"" + END);
			sb.append("Content-Type: text/HTML" + END);
			sb.append(END);
		}

		return sb.toString();
	}
}
