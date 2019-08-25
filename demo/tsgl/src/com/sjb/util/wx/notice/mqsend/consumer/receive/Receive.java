package com.sjb.util.wx.notice.mqsend.consumer.receive;

import java.io.IOException;
import java.util.Date;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.http.ParseException;

import com.alibaba.fastjson.JSONObject;
import com.sjb.util.Log;
import com.sjb.util.wx.notice.WXConfiguration;
import com.sjb.util.wx.notice.WxTokenRefurbish;

public class Receive implements ReceiveBusiness{

	
	public JSONObject send(JSONObject json,String key) throws Exception{
		return SendExecution.sendpost(json,WxTokenRefurbish.getAccessToken(key));
	}
	
	/**
	 * 失败处理
	 */
	public void error(){
	}
	
	/**
	 * 消息推送
	 */
	@Override
	public void receive(Message message) throws Exception {
		TextMessage textMessage = (TextMessage) message;
		JSONObject json = JSONObject.parseObject(textMessage.getText());
		String key = json.getString(SendExecution.NOPUBLIC);
		System.out.println(textMessage.getText());
		WXConfiguration wc = WxTokenRefurbish.serviceNoMap.get(key);
		if(wc!=null){
			//添加对应的模板id
			String template_key = json.getString(SendExecution.TEMPLATETYPE);
			json.put(SendExecution.TEMPLATEID, wc.getTemplates().get(template_key));
			try {
				JSONObject reObj = send(json,key);
				if("0".equals(reObj.getString("errcode"))){
					Log.info("发送消息成功", reObj.toString());
				}else{
					Log.info("发送消息失败",reObj.toString());
				}
			} catch (ParseException e) {
				Log.error("消息发送失败ParseException",json.toString(),e);
			} catch (IOException e) {
				Log.error("消息发送失败IOException",json.toString(),e);
			}
		}else{
			Log.error("没有找到服务号!", json.toString());
		}
	}

	public static void main(String[] args) {
		
	}
}
