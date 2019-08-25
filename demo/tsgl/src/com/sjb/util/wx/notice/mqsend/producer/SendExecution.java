package com.sjb.util.wx.notice.mqsend.producer;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;


public class SendExecution{

	public enum TemplateType{
		XXTZ("xxtz","消息通知"),
		DYMBXX("dymbxx","订阅模板消息");
		private String name;
		private String test;
		TemplateType(String name,String test){
			this.name=name;
			this.test=test;
		}
		public String getName() {
			return name;
		}
		public String getTest() {
			return test;
		}
	}
	
	/** 模板id */
	public final static String TEMPLATEID = "template_id";//模板idkey
	
	/** openId */
	public final static String TOUSER = "touser";//接收者key
	
	/** 用户属性 */
	public final static String USERTYPE = "user_type";//用户属性
	
	/** 消息内容 */
	public final static String DATA = "data";//模板内容
	
	/** 用户id */
	public final static String USERID = "user_id";//用户id
	
	/** 操作者id */
	public final static String OPERATIONID = "operation_id";//操作者id
	
	/** 操作时间 */
	public final static String  OPERATIONTIME= "operation_time";//操作者时间
	
	/** 公众号 */
	public final static String NOPUBLIC = "no_public"; //公众号属性
	
	/** 模板标题 用于标识之一  更具key查找不同服务号对应的模板id*/
	public final static String TEMPLATETYPE = "template_type";//模板key
	
	/** access_token 请求url */
	private static String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	
	/**
	 * POST请求
	 * @param url
	 * @param para
	 * @return
	 * @throws ClientProtocolException 
	 * @throws ParseException
	 * @throws IOException
	 */
//	public static JSONObject doPostStr(String url,String para) throws ClientProtocolException, IOException{
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpPost httpost = new HttpPost(url);
//		JSONObject jsonObject = null;
//		httpost.setEntity(new StringEntity(para,"UTF-8"));
//		HttpResponse response;
//		response = client.execute(httpost);
//		try {
//			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
//			jsonObject = JSONObject.parseObject(result);
//			System.out.println(jsonObject);
//		}catch (Exception e) {
//			Log.error("解析消息返回异常", e);
//		}
//		return jsonObject;
//	}
	
	
	
//	/**
//	 * @param json 参数
//	 * @param access_token 平台所对应的token
//	 * @throws IOException 
//	 * @throws ClientProtocolException 
//	 */
//	public static JSONObject sendpost(JSONObject json,String access_token) throws ClientProtocolException, IOException{
//		String u = url+access_token;
//		JSONObject jsonObject = doPostStr(u,json.toString());
//		return jsonObject;
//	}
	
	/**
	 * 默认消息模板
	 * @param userType
	 * @param context
	 * @return
	 */
	public static JSONObject initdefData(String userType,String context){
		JSONObject data = new JSONObject();
		data.put("first", initData(userType));
		data.put("keyword1", initData(context));
		return data;
	}
	
	/**
	 * 消息模板 由前端生成 后台不需要重新编译 分摊后台的压力  只维护前端就可以 因为模板动态获取的 半小时刷新一次
	 * @param userType
	 * @param context
	 * @return
	 */
	public static JSONObject initData(){
		JSONObject data = new JSONObject();
		data.put("productType", initData("498"));
		data.put("name", initData("好东西"));
		data.put("remark", initData("你好"));
		return data;
	}
	
	public static JSONObject initData(String value){
		JSONObject data = new JSONObject();
		data.put("value", value);
		data.put("color", "#173177");
		return data;
	}
	
}
