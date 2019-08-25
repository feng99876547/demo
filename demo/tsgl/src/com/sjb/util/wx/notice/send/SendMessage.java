package com.sjb.util.wx.notice.send;

import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjb.util.Treat;
import com.sjb.util.wx.notice.send.execution.SendExecution;



/**
 * @author fxc
 */
public class SendMessage {
	
	/**
	 * 将前端提交的消息发送对象添加到队列中
	 * @param json
	 *  	data 模板内容
	 *  	list 用户数组 包含 id opebid 服务号标识
	 */
	public HashMap<String,Object> send(net.sf.json.JSONObject json){
		
		JSONObject myjson = JSONObject.parseObject(json.toString());
		
		System.out.println(myjson.toJSONString());
		String title = myjson.getString(SendExecution.TEMPLATETYPE);
		if(Treat.isEmpty(title)){
			myjson.put(SendExecution.TEMPLATETYPE, "消费通知");
		}
		
		if(!Treat.isEmpty(myjson.getString(SendExecution.DATA)) 
				&& !Treat.isEmpty(myjson.getString(SendExecution.TEMPLATETYPE)) 
				&& !Treat.isEmpty(myjson.getString("list")) ){
			StringBuffer message = null;
			JSONArray list = myjson.getJSONArray("list");
			if(!Treat.isEmpty(list)){
				int len = list.size();
				for(int i = 0;i<len;i++){
					JSONObject obj = JSONObject.parseObject(list.get(i).toString());
					if(!Treat.isEmpty(obj.getString(SendExecution.USERID))//用户id
							&& !Treat.isEmpty(obj.getString(SendExecution.TOUSER))//接收者OpenId
							&& !Treat.isEmpty(obj.getString(SendExecution.NOPUBLIC))){//关注哪个服务号
						
						obj.put(SendExecution.TEMPLATETYPE,myjson.getString(SendExecution.TEMPLATETYPE));//使用哪个模板
						obj.put(SendExecution.DATA,JSONObject.parseObject(myjson.getString(SendExecution.DATA)));//模板参数
						//消息量不大队列应该满不了
//						System.out.println(obj.toString());
						SendExecution.addQueue(obj);
					}
				}
				HashMap<String,Object> ma = new HashMap<String,Object>();
				ma.put("msg", "发送成功");
				ma.put("success", "true");
				return ma;
			}else{
				HashMap<String,Object> ma = new HashMap<String,Object>();
				ma.put("msg", "数组长度为0");
				ma.put("success", "false");
				return ma;
			}
		}else{
			HashMap<String,Object> ma = new HashMap<String,Object>();
			ma.put("msg", "参数错误");
			ma.put("success", "false");
			return ma;
		}
	}	
	
}
