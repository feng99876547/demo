package com.sjb.util.wx.notice.mqsend.producer;


import java.util.ArrayList;
import java.util.List;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class main {
	
	public static void main(String[] args) {
		
//		List<Object> list = new ArrayList<Object>();
//		list.add("one");
//		list.add("two");
//		list.add("three");
//		list.add("four");
//		BalancingJms b = new BalancingJms();
//		SendBusiness bus = new SendBusiness(){
//			@Override
//			public void send(Object para, MessageProducer messageProducer, Session session) throws Exception {
//				TextMessage message = session.createTextMessage();
//				message.setText(para.toString());
//				messageProducer.send(message);
//			}
//		};
//		try {
//			b.sends(list, bus,"WxMessage");
//		} catch (Exception e) {
//		}
		
		SendMessage sm = new SendMessage();
		JSONObject json = new JSONObject();
		json.put(SendExecution.DATA, SendExecution.initData());

		JSONArray list = new JSONArray();
		JSONObject users = new JSONObject();
		users.put(SendExecution.USERID, "123");
		users.put(SendExecution.TOUSER, "ozHMKwuj0zUNFIArc-w0qrp_GHYs");
		users.put(SendExecution.NOPUBLIC, "one");
		
		list.add(users);
		json.put("list", list.toString());
		sm.send(json);
		SendMessage sm2 = new SendMessage();
		json.put(SendExecution.DATA, SendExecution.initData());
		sm2.send(json);
	}
		
}
