package com.sjb.util.wx.notice;

import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjb.util.Log;

/**
 * 注意票据的更新同一时间只有一个是有效的 在未过期时间内其他地方更新票据后之前的票据将无效
 * 如果是分布式需要统一票据
 * @author fxc
 *
 */
public class WxTokenRefurbish extends TimerTask{
	
	/** 服务号配置集合 */
	public static HashMap<String,WXConfiguration> serviceNoMap = new HashMap<String,WXConfiguration>();
	
	private final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";
	
	private final String GET_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";

	//不方便管理保存到数据库动态获取
	private enum Configuration{
		
		ONE("one","xxxxx","xxxxxxxxxxxxxxxxx");
		
		private String key;
		
		private String appid;
		
		private String appsecret;
		
		Configuration(String key,String appid,String appsecret){
			this.key = key;
			this.appid = appid;
			this.appsecret = appsecret;
		}
		
		public String getAppid() {
			return appid;
		}
		
		public String getAppsecret() {
			return appsecret;
		}

		public String getKey() {
			return key;
		}

	}
	
	
	/**
	 * 获取OpenID
	 * 一次拉取调用最多拉取10000
	 * next_openid类似分页 指定该值从该值后面获取 不指定从最开始获取
	 * @return
	 * @throws  
	 * @throws IOException
	 */
	public  JSONObject getOpenId(String access_token) throws IOException{
		String url = GET_USER_URL.replace("ACCESS_TOKEN", access_token);
		return doGetStr(url);
	}
	
	public static String getAccessToken(String key){
		return serviceNoMap.get(key) == null ? null : serviceNoMap.get(key).getAccess_token();
	}
	
	/**
	 * get请求 没网要抛异常一般都是日志NoClass
	 * 
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * 
	 */
	public JSONObject doGetStr(String url) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(outStr,"UTF-8"));
		HttpResponse response = client.execute(httpost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.parseObject(result);
		return jsonObject;
	}
	
	/**
	 * 获取accessToken
	 * @return
	 * @throws IOException
	 *
	 */
	public JSONObject getAccessToken(String appId,String appsecret) throws IOException{
		String url = ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appsecret);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	
	/**
	 * 获取template
	 * @return
	 * @throws IOException
	 *
	 */
	public JSONObject getTemplate(String access_token) throws IOException{
		String url = TEMPLATE_URL.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	
	
	@Override
	public void run() {
		for(Configuration conf:Configuration.values()){
			try {
				//更新access_token
				JSONObject access_token = getAccessToken(conf.getAppid(),conf.getAppsecret());
				String token = access_token.getString("access_token");
				if(token == null){//获取失败
					Log.error("更新微信配置错误",access_token.toString());
				}else{//更新成功
					WXConfiguration wc = new WXConfiguration();
					
					wc.setAppid(conf.getAppid());
					wc.setAppsecret(conf.getAppsecret());
					wc.setAccess_token(token);
					
					//更新模板集合
					JSONObject templates = getTemplate(token);
					
					Log.info("获取模板集合成功",templates.toString());
					
					if(templates!=null && templates.getJSONArray("template_list")!=null){
						HashMap<String,String> ma = new HashMap<String,String>();
						
						JSONArray list = templates.getJSONArray("template_list");
						int len = list.size();
						for(int i=0;i<len;i++){
							JSONObject obj = list.getJSONObject(i);
							ma.put(obj.getString("title"), obj.getString("template_id"));
						}
						wc.setTemplates(ma);
					}
					serviceNoMap.put(conf.getKey(), wc);
					
					Log.info("更新微信配置成功",access_token.toString());
					
					break;
				}
			} catch (IOException e1) {
				Log.error("更新微信配置异常",conf.getKey().toString(),e1);
			}
		}
	}

}
