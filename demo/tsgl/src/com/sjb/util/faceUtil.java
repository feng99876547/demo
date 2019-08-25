package com.sjb.util;

import java.util.HashMap;

import com.sjb.util.session.SystemUtil;

public class faceUtil {
	
	private static final String url = "https://dev.newlandfinance.com/YTongB/channel/getFaceIdUrl";
	
	private static final String callbackUel = "http://www.xxx.net";//回调接口
	private static final  String returnUrl = "http://www.xxx.net";//回调url
	
	public static String getUrl() throws Exception{
		HashMap<String, String> json = new HashMap<String, String>();
		json.put("orgCode","XXXX");
		json.put("notifyUrl",callbackUel);
		json.put("returnUrl",returnUrl);
		json.put("webTitle","人脸认证");
		json.put("bizNo",SystemUtil.getUserId().toString());//长度128
		json.put("actionCmd","GET_TOKEN");
		
		return HttpsGetData.postSend(url,json);
	}
	//返回图片和其他信息
//	public static String get() throws Exception{
//		HashMap<String, String> json = new HashMap<String, String>();
//		json.put("orgCode","SJiuB");
//		json.put("bizNo","1539761784,820caa25-31d6-484c-a58e-292d3f63e764");//长度128
//		json.put("actionCmd","GET_RESULT");
//		return HttpsGetData.postSend(url,json);
//	}
//	
//	public static void main(String[] args) throws Exception {
//	}
}
