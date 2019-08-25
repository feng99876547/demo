package com.sjb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class httpUtil {
	
	/**
	 * POST请求
	 * @param url
	 * @param para
	 * @return
	 * @throws ClientProtocolException 
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String para) throws ClientProtocolException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(para,"UTF-8"));
		HttpResponse response;
		response = client.execute(httpost);
		try {
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
//			System.out.println(result);
			jsonObject = JSONObject.parseObject(result);
//			System.out.println(jsonObject);
		}catch (Exception e) {
			Log.error("解析消息返回异常", e);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param para
	 * @return
	 * @throws ClientProtocolException 
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject get(String url) throws ClientProtocolException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpost = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse response;
		response = client.execute(httpost);
		try {
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
//			System.out.println(result);
			jsonObject = JSONObject.parseObject(result);
//			System.out.println(jsonObject);
		}catch (Exception e) {
			Log.error("解析消息返回异常", e);
		}
		return jsonObject;
	}
	
	/**
	 * @param url
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String sendPost(String url,NameValuePair[] params) throws HttpException, IOException{
        HttpClient httpClient = new HttpClient();
        // 设置 HTTP 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
        httpClient.getParams().setContentCharset("utf-8");
        // 2.生成 GetMethod 对象并设置参数
        PostMethod getMethod = new UTF8PostMethod(url);
        getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;");
        getMethod.setRequestBody(params);  
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
        getMethod.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        int statusCode = httpClient.executeMethod(getMethod);
        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + getMethod.getStatusLine());
        }
        BufferedReader bf = null;
		try {
			String str = null;
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			Reader reader = new InputStreamReader(inputStream, "utf-8");
    		bf = new BufferedReader(reader);
            while(null != (str = bf.readLine())){
            	System.out.println(str);
            	if(!"null".equals(str)){
            		return str;
            	}
            }
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(httpUtil.class,"send:读取返回结果异常",null, e);
		}finally{
			if(bf!=null)
				bf.close();
		}
		return null;
	}
	
	
	/**
	 * @param url
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String sendGet(String url) throws HttpException, IOException{
        HttpClient httpClient = new HttpClient();
        // 设置 HTTP 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
        httpClient.getParams().setContentCharset("utf-8");
        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;"); 
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
        
//        getMethod.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        
        int statusCode = httpClient.executeMethod(getMethod);
        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + getMethod.getStatusLine());
        }
        BufferedReader bf = null;
		try {
			String str = null;
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			Reader reader = new InputStreamReader(inputStream, "utf-8");
    		bf = new BufferedReader(reader);
            while(null != (str = bf.readLine())){
            	System.out.println(str);
            	if(!"null".equals(str)){
            		return str;
            	}
            }
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(httpUtil.class,"send:读取返回结果异常",null, e);
		}finally{
			if(bf!=null)
				bf.close();
		}
		return null;
	}
	
	/**
	 * POST请求  默认是
	 * @param url
	 * @param para
	 * @return
	 * @throws ClientProtocolException 
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostJSON(String url,String para) throws ClientProtocolException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		StringEntity entity = new StringEntity(para, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpost.setEntity(entity);
		HttpResponse response;
		response = client.execute(httpost);
		try {
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			System.out.println(result);
			jsonObject = JSONObject.parseObject(result);
		}catch (Exception e) {
//			Log.error("解析消息返回异常", e);
			e.printStackTrace();
		}
		return jsonObject;
	}
}
