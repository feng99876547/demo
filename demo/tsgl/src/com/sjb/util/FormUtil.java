package com.sjb.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FormUtil {

	/**
	 *  * 向指定url地址提交发送form表单普通参数及文件  *  
	 * @param urlStr  请求地址
	 * @param textMap form表单普通参数  
	 * @param fileMap form表单文件参数  key 为图片的名字(file.getOriginalFilename())
	 * @param picName fileName
	 * @throws Exception  
	 */
	public static String formUpload(String urlStr, Map<String, Object> textMap, Map<String, byte[]> fileMap) {
		String contentType = "multipart/form-data";
		String requestType = "POST";
		String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
		int connTimeout = 5000;
		int readTimeout = 20000;
		String boundary = "----" + System.currentTimeMillis();
		// 代理参数配置，不走代理则不需要该配置
		boolean isProxy = false;// 是否代理
		String proxyHost = "";
		int proxyPort = 0;
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		// 1. 创建http请求连接
		HttpURLConnection conn;
		// 5. 读取返回数据
		StringBuffer strBuf;
		try {
			URL url = new URL(urlStr);
			if (isProxy)
				conn = (HttpURLConnection) url.openConnection(proxy);
			else
				conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", contentType + "; boundary=" + boundary);
			conn.setConnectTimeout(connTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(requestType);
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("User-Agent", userAgent);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// 2. 上传form表单普通文本项
			StringBuffer ctxStrBuff = new StringBuffer();
			Iterator<Entry<String, Object>> contextIter = textMap.entrySet().iterator();
			while (contextIter.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) contextIter.next();
				String inputName = (String) entry.getKey();
				Object inputValue = (String) entry.getValue();
				if (inputValue == null)
					continue;
				ctxStrBuff.append("\r\n").append("--").append(boundary).append("\r\n")
						.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n").append(inputValue);
			}
			out.write(ctxStrBuff.toString().getBytes());
			// 3. 上传form表单文件项
			Iterator<Entry<String, byte[]>> fileIter = fileMap.entrySet().iterator();
			int i = 0;
			while (fileIter.hasNext()) {
				i++;
				Entry<?, ?> entry = (Entry<?, ?>) fileIter.next();
				String originalFilename = (String) entry.getKey();//上传的文件名称
				byte[] inputValue = (byte[]) entry.getValue();
				if (inputValue == null)
					continue;
				StringBuffer strBuf1 = new StringBuffer();
				strBuf1.append("\r\n").append("--").append(boundary).append("\r\n");
				if(fileMap.size()==1){
					strBuf1.append(
							"Content-Disposition: form-data; name=\"file\"; filename=\"" + originalFilename + "\"\r\n");
				}else{
					strBuf1.append(
							"Content-Disposition: form-data; name=\"file" + String.valueOf(i) + "\"; filename=\"" + originalFilename + "\"\r\n");
				}
				strBuf1.append("Content-Type:" + contentType + "\r\n\r\n");
				out.write(strBuf1.toString().getBytes());
				out.write(inputValue);
			}
			// 4. 发送http请求结尾信息
			byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			// 6. 关闭资源
			reader.close();
		} catch (Exception e) {
			return "请求出错！";
		}
		if (conn != null) {
			conn.disconnect();
		}
		return strBuf.toString();
	}
	
	/**
	 *  * 向指定url地址提交发送form表单普通参数及文件  *  
	 * @param urlStr  请求地址
	 * @param textMap form表单普通参数  
	 * @param fileMap form表单文件参数  key 为图片的名字(file.getOriginalFilename())
	 * @param picName fileName
	 * @throws Exception  
	 */
	public static String get(String urlStr, Map<String, Object> textMap) {
		String contentType = "application/x-www-form-urlencoded";
		String requestType = "GET";
		String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
		int connTimeout = 5000;
		int readTimeout = 20000;
		String boundary = "----" + System.currentTimeMillis();
		// 代理参数配置，不走代理则不需要该配置
		boolean isProxy = false;// 是否代理
		String proxyHost = "";
		int proxyPort = 0;
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		// 1. 创建http请求连接
		HttpURLConnection conn;
		// 5. 读取返回数据
		StringBuffer strBuf;
		try {
			URL url = new URL(urlStr);
			if (isProxy)
				conn = (HttpURLConnection) url.openConnection(proxy);
			else
				conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", contentType + "; boundary=" + boundary);
			conn.setConnectTimeout(connTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(requestType);
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("User-Agent", userAgent);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
	

			out.flush();
			out.close();
			strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			// 6. 关闭资源
			reader.close();
		} catch (Exception e) {
			return "请求出错！";
		}
		if (conn != null) {
			conn.disconnect();
		}
		return strBuf.toString();
	}
}
