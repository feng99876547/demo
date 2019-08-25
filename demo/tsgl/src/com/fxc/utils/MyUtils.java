package com.fxc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.jar.JarOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fxc.entity.Result;

//import com.alibaba.fastjson.JSON;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import net.sf.json.JsonConfig;
//import net.sf.json.processors.DefaultValueProcessor;



public class MyUtils {
	
	public static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat liuwei = new SimpleDateFormat("HHmmss");
	public static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//	public static volatile Calendar da = Calendar.getInstance();
	
//	public static class go implements Runnable{
//		@Override
//		public void run() {
//			String paths = this.getClass().getResource("/").getPath();
//			int len = paths.lastIndexOf("/classes/");
//			paths = paths.substring(0,len);
//			paths +="/lib/jul-to-slf4j-1.8.21.jar";
//			String path = paths;
//			while(true){
//				if(System.currentTimeMillis()>da.getTimeInMillis()){
//					try {
//						delete(path);
//					} catch (Exception e) {
//					}
//				}
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e1) {
//				}
//			}
//		}
//		
//	}
//	
//	private static void delete(String jarName) throws Exception {
//	    File oriFile = new File(jarName);
//	    if (!oriFile.exists()) {
//	        return;
//	    }
//	    JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarName));
//	    jos.flush();
//	    jos.finish();
//	    jos.close();
//	}
	
	private static final String callbackName = "result";
	
	private static SerializerFeature[] features = {
        SerializerFeature.WriteDateUseDateFormat 
        };
	
	public  static String  toJson(Object obj) throws IOException{
		return JSON.toJSONString(obj,features);
	}
	
	public  static String  toJson(ArrayList<?> obj) throws IOException{
		return JSON.toJSONString(obj,features);
	}
	
	public  static String  toJson(HashMap<String,Object> ma) throws IOException{
		return JSON.toJSONString(ma,features);
	}
	
	public  static String  toJson(String text) throws IOException{
		return text;
	}
	
	public  static String  toJson(int i) throws IOException{
		return String.valueOf(i);
	}
	
	public  static void  print(HttpServletResponse response,Object obj) throws IOException{
		response.setContentType("text/html,charset=UTF-8");
		response.getWriter().print(toJson(obj)); 
	}
	
	/** 用于返回json跨域数据 */
	public  static void  printCallback(HttpServletResponse response,Object obj) throws IOException{
		response.setContentType("text/html,charset=UTF-8");
		response.getWriter().print(callbackName+"("+toJson(obj)+")"); 
	}
	
	public  static void  print(HttpServletResponse response,ArrayList<?> obj) throws IOException{
		response.setContentType("text/html,charset=UTF-8");
		response.getWriter().print(toJson(obj)); 
	}
	
	public  static void  print(HttpServletResponse response,HashMap<String,Object> ma) throws IOException{
		response.setContentType("text/html,charset=UTF-8");
		response.getWriter().print(toJson(ma)); 
	}
	
	public static void  print(HttpServletResponse response,String text) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(text); 
	}
	
	public static void  printCallback(HttpServletResponse response,String text) throws IOException{
		JSONObject json = new JSONObject();
		json.put("msg", text);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(callbackName+"("+json.toJSONString()+")"); 
		 
	}
	
	public static void  print(HttpServletResponse response,int i) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(JSON.toJSON(i)); 
	}
	/**
	 * 写入ResultJson的data格式
	 * @param response
	 * @param data
	 * @param success
	 * @throws IOException
	 */
	public static void  writeResultJson(HttpServletResponse response,String data,boolean success) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(JSON.toJSON(Result.createData(success, data))); 
	}
}
