package com.sjb.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
 
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
public class HttpsGetData {
	private static class TrustAnyTrustManager implements X509TrustManager {
 
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
 
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
 
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}
 
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
 
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
	public static String postSend(String _url,Map<String, String> _params) throws Exception {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			String urlStr = _url ;
			String  params = getParamStr(_params);
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
			URL realUrl = new URL(urlStr);
			// 打开和URL之间的连接
			HttpsURLConnection connection = (HttpsURLConnection) realUrl
					.openConnection();
			// 设置https相关属性
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			connection.setDoOutput(true);
			// 设置通用的请求属性
			connection.setRequestMethod("POST");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Length", String.valueOf(params.length()));
			// 建立实际的连接
//			connection.connect();
			OutputStream outputStream = connection.getOutputStream();
			OutputStreamWriter write = new OutputStreamWriter(outputStream); 
			write.write(params);
			write.flush();
			int responseCode = connection.getResponseCode();
			// 定义 BufferedReader输入流来读取URL的响应
			String line;
			if (responseCode != 200) {
				InputStream errorStream = connection.getErrorStream();
				in = new BufferedReader(new InputStreamReader(errorStream,
						"UTF-8"));
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			} else {
				InputStream inputStream = connection.getInputStream();
				Reader reader = new InputStreamReader(inputStream, "utf-8");
	    		in = new BufferedReader(reader);
	    		while ((line = in.readLine()) != null) {
					result.append(line);
				}
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			// e.printStackTrace();
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			if (in != null) {
				in.close();
			}
		}
		return result.toString();
	}
 
	private static String getParamStr(Map<String, String> _params) {
		String paramStr = "";
		// 获取所有响应头字段
		Map<String, String> params = _params;
		// 获取参数列表组成参数字符串
		for (String key : params.keySet()) {
			paramStr += key + "=" + params.get(key) + "&";
		}
		// 去除最后一个"&"
		paramStr = paramStr.substring(0, paramStr.length() - 1);
		return paramStr;
	}
 
}
