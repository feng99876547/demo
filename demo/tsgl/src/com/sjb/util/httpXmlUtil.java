package com.sjb.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;


public class httpXmlUtil {

	/**
	 * 不带证书
	 * @param url
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String xmlpost(String url, String data) throws ClientProtocolException, IOException {
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(RegistryBuilder
				.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build(), null, null, null);

		connManager = new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build(), null, null, null);
		HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(data, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity, "UTF-8");
	}


        
	/**
	 * 
	 * @param data
	 * @param PKCS12PKCS12的密码
	 * @param fileRoute证书地址
	 * @return
	 * @throws Exception
	 */
	public static String getpublickey(String data,String url,String PKCS12,String fileRoute) throws Exception {
		
		// 指定读取证书格式为PKCS12
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 读取本机存放的PKCS12证书文件
		FileInputStream instream = new FileInputStream(new File(fileRoute));
		try {
			// 指定PKCS12的密码
			keyStore.load(instream, PKCS12.toCharArray());
		} finally {
			instream.close();
		}
		// 指定TLS版本
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, PKCS12.toCharArray()).build();
		// 设置httpclient的SSLSocketFactory
		@SuppressWarnings("deprecation")
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(RegistryBuilder
				.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslsf).build(), null, null, null);

		HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(data, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		return EntityUtils.toString(httpEntity, "UTF-8");
	}
}
