package com.sjb.util.wx.notice.send.execution;


import java.io.IOException;

import org.apache.http.ParseException;

import com.alibaba.fastjson.JSONObject;
import com.sjb.util.Log;
import com.sjb.util.wx.notice.WXConfiguration;
import com.sjb.util.wx.notice.WxTokenRefurbish;


/**
 * 从队列中获取消息发送
 * @author fxc
 *
 */
public class SendThread extends Thread {

//	public  LinkedBlockingQueue<JSONObject> sendQueue;
	
	@Override
	public void run(){
		while(true){
			//从队列中取出消息
			JSONObject json = SendExecution.queueTake();
			String key = json.getString(SendExecution.NOPUBLIC);
			WXConfiguration wc = WxTokenRefurbish.serviceNoMap.get(key);
			if(wc!=null){
				//添加对应的模板id
				String template_key = json.getString(SendExecution.TEMPLATETYPE);
				json.put(SendExecution.TEMPLATEID, wc.getTemplates().get(template_key));
				try {
					JSONObject reObj = SendExecution.sendpost(json,WxTokenRefurbish.getAccessToken("one"));
					if("0".equals(reObj.getString("errcode"))){
						Log.info("发送消息成功", reObj.toString());
					}else{
						//应该是accseetoken过期或是参数问题测试通过参数问题应该不存在 可以记录到一张错误表中由人工重新发送
						//该异常暂时不处理 发现了在看情况处理
						Log.info("发送消息失败",reObj.toString());
					}
				} catch (ParseException e) {
					Log.error("消息发送失败ParseException",json.toString(),e);
				} catch (IOException e) {
//					SendExecution.addQueue(json);//消息发送失败重新发送
					//添加到发送失败表中由人工解决 如果因为网络问题一直无法消费消 生产方在不段生产消息这样可能会引起系统崩溃 无法继续服务
					Log.error("消息发送失败IOException",json.toString(),e);
				}
			}else{
				Log.error("没有找到服务号!", json.toString());
			}
		}
	}
	
}
