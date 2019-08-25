package com.sjb.util.wx.notice.send;

import java.io.IOException;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sjb.util.wx.notice.send.execution.SendExecution;
import com.sjb.util.wx.notice.send.execution.SendThread;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class main {
	
	
	public static void main(String[] args) {
		
		
		
		Timer timer = new Timer(); 
		//微信票据刷新
//		timer.schedule(new WxTokenRefurbish(),100,1700000);//1000等于1秒 每30分钟更新一次
//		//如果推送量不大 先用一个线程用于负责所有的消息发送
		Thread sendWXMessage = new Thread(new SendThread());
		sendWXMessage.start();
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//		}
//		try {
//			JSONObject obj = getOpenId(WxTokenRefurbish.serviceNoMap.get("one").getAccess_token());
//			SendExecution.sendMessageLog.info("openIds{}",obj.toString());
//		} catch (IOException e) {
//		}
//		SendMessage sm = new SendMessage();
		JSONObject json = new JSONObject();
		json.put(SendExecution.DATA, SendExecution.initData());

		JSONArray list = new JSONArray();
		JSONObject users = new JSONObject();
		users.put(SendExecution.USERID, "123");
		users.put(SendExecution.TOUSER, "ozHMKwuj0zUNFIArc-w0qrp_GHYs");
		users.put(SendExecution.NOPUBLIC, "one");
		
		list.add(users);
		json.put("list", list.toString());
//		sm.send(json);
	}
		
}
