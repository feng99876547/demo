package com.sjb.util.wx.notice.mqsend.producer;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class main22 {
	
	public static class test implements Runnable{

		@Override
		public void run() {
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
		}
		
	}
	public static void main(String[] args) {
		
		for(int i = 0;i<3;i++){
			Thread t = new Thread(new test());
			t.start();
		}
	}
		
}
