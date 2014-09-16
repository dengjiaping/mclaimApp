package com.sinosoftyingda.fastclaim.common.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/**
 * 网络连接工具（get, post, uploadImage）
 * 
 * @author DengGuang
 */
public class HttpUtils {
	public DefaultHttpClient httpClient;
	private static final int TIME_OUT_DELAY = 1000 * 40; // 响应时间
	public static final int SOCKET_BUFFER_SIZE = 1024 * 1024 * 1024;
	public static final int MAX_LINE_LENGTH = 1000;

	/**
	 * 初始化HttpClient
	 */
	public HttpUtils() {

		// HttpParams httpParams = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParams,
		// TIME_OUT_DELAY);
		// HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT_DELAY);
		// HttpConnectionParams.setSocketBufferSize(httpParams,
		// SOCKET_BUFFER_SIZE);
		// 设置连接超时
		// httpClient = new DefaultHttpClient();
		// httpClient.getParams().setIntParameter(HttpConnectionParams.MAX_LINE_LENGTH,
		// MAX_LINE_LENGTH);
		// httpClient.getParams().setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE,
		// SOCKET_BUFFER_SIZE);
		// httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
		// TIME_OUT_DELAY);
		// httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
		// TIME_OUT_DELAY);
	}

	/***
	 * 判断网络连接是否可用 这个可以用SystemConfig类中isExistNet来代替判断
	 * 
	 * @param context
	 * @return
	 * @author chenjianfan
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				SystemConfig.isExistNet = mNetworkInfo.isAvailable();
				return mNetworkInfo.isAvailable();
			}
		}
		return false;

	}

	/***
	 * 检测sd卡是否存在
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get 方式从服务器获取数据
	 * 
	 * @param url
	 * @param params
	 *            参数
	 * @return 服务器返回的数据
	 */
	public String doGet(String url, Map<String, Object> params) {
		httpClient = new DefaultHttpClient();

		if (SystemConfig.isExistNet) {
			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT_DELAY);
			httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_DELAY);
		} else {
			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 5);
			httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_DELAY);
		}

		// 参数
		String paramsStr = "";
		Iterator<?> iterator = params.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			paramsStr += "&" + key + "=" + value;
		}
		if (!paramsStr.equals("")) {
			paramsStr = paramsStr.replaceFirst("&", "?");
			url += paramsStr;
			Log.i(getClass().getSimpleName(), "url=" + url);
		}

		// 创建HttpGet 连接，发送Http 请求
		HttpGet httpGet = new HttpGet(url);
		try {
			// 若状态码为200 OK
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出答应字符串
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				return strResult;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timeout";
		} catch (ConnectTimeoutException e) {
			return "Timeout";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "Get";
		} catch (IOException e) {
			e.printStackTrace();
			return "Get";
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return "Get";
	}

	/**
	 * Post 数据到服务器端
	 * 
	 * @param url
	 * @param params
	 *            参数
	 * @return 服务器返回的信息
	 */
	public String doPost(String url, Map<String, Object> params) {
		httpClient = new DefaultHttpClient();
		if (SystemConfig.isExistNet) {
			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT_DELAY);
			httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_DELAY);
		} else {
			httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 5);
			httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_DELAY);
		}

		/** Post 运行传递参数必须用 NameValuePair[] 数组存储 */
		List<NameValuePair> _params = new ArrayList<NameValuePair>();
		Iterator<?> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_params.add(new BasicNameValuePair("" + key, "" + value));
		}

		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(_params,
			// HTTP.UTF_8);
			// 若状态码为200 OK
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出答应字符串
				String result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				// HttpEntity entity = httpResponse.getEntity();
				// InputStream input = entity.getContent();
				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(input, Charset.defaultCharset()));
				// String result = "";
				// String line = null;
				// while((line = br.readLine()) != null){
				// result = result + line;
				// }
				return result;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timeout";
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			return "Timeout";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Post";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "Post";
		} catch (IOException e) {
			e.printStackTrace();
			return "Post";
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return "Post";
	}

	/**
	 * 上传文件(或图片)至Server
	 * 
	 * @param actionUrl
	 *            图片上传地址
	 * @param files
	 *            待上传文件
	 * @param params
	 *            文本类型参数
	 */
	public String uploadFile(String actionUrl, Map<String, File> files, Map<String, String> params) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);

			/* 设置传送的method=POST */
			con.setRequestMethod("POST");

			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(twoHyphens);
				sb.append(boundary);
				sb.append(end);

				String key = entry.getKey();
				if (key.indexOf("comments") != -1) {
					key = key.substring(0, key.indexOf("_"));
				}

				sb.append("Content-Disposition: form-data; name=\"" + key + "\"" + end);
				sb.append(end);
				sb.append(entry.getValue());
				sb.append(end);
			}

			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.write(sb.toString().getBytes());

			// 发送文件数据
			if (files != null) {
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb2 = new StringBuilder();
					sb2.append(twoHyphens);
					sb2.append(boundary);
					sb2.append(end);
					sb2.append("Content-Disposition: form-data; " + "name=\"uploadFile\";filename=\"" + file.getKey() + "\"" + end);
					sb2.append("Content-Type: image/pjpeg" + end);
					sb2.append(end);
					ds.write(sb2.toString().getBytes());

					/* 取得文件的FileInputStream */
					FileInputStream fStream = new FileInputStream(file.getValue());

					/* 设置每次写入1024bytes */
					int bufferSize = 1024 * 1024;
					byte[] buffer = new byte[bufferSize];
					int length = -1;

					/* 从文件读取数据至缓冲区 */
					while ((length = fStream.read(buffer)) != -1) {
						/* 将资料写入DataOutputStream中 */
						ds.write(buffer, 0, length);
					}
					fStream.close();
					ds.write(end.getBytes());
				}
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();

			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}

			/* 关闭DataOutputStream */
			ds.close();
			con.disconnect();

			return b.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}

	/**
	 * 发送一个POST请求
	 * 
	 * @param url
	 * @param xml
	 */
	public InputStream sendPostRequest(String url, String xml) {
		StringEntity entity;
		try {
			entity = new StringEntity(xml, "utf-8");
			// 1、明确Post、Get（URL）
			HttpPost httpPost = new HttpPost(url);
			// 2、使用到的是Post，设置发送内容
			httpPost.setEntity(entity);
			// 3、HttpClient 发送请求
			httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// 4、服务器会送状态码判断（200）
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 5、把服务器回送的输入流返回给调用者
				// String result =
				// EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				return httpResponse.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
