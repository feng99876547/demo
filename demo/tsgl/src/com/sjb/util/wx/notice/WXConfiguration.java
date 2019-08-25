package com.sjb.util.wx.notice;

import java.util.HashMap;

/**
 * 微信公众号配置信息
 * @author fxc
 *
 */
public class WXConfiguration {

	private String appid;//appid
	
	private String appsecret;//appsecret
	
	private HashMap<String,String> templates;//模板集合
	
	private String access_token;//access_token

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public HashMap<String,String> getTemplates() {
		return templates;
	}

	public void setTemplates(HashMap<String,String> templates) {
		this.templates = templates;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public WXConfiguration() {
		super();
	}
	
}
