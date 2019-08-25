package com.sjb.util.wx.notice.mqsend.producer;

import java.util.HashMap;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.sjb.util.Log;
import com.sjb.util.Treat;



/**
 * @author fxc
 */
public class SendMessage {
	
	private static JmsProducer jms = new JmsProducer();
	
	/** 业务需要满足低重复发送率 那需要获取操作者id 限制发送频率 2秒内只能操作一次 这样可以更具 操作者id+openid+当前时间生产唯一标识 
	 * 好像微信回调不能传自定义参数 也不返回发送内容 也不能自己定义msgid 所以好像无法避免重复发送的问题 */
	private SendBusiness lowRepeat = new SendBusiness() {
		@Override
		public void send(Object para, MessageProducer messageProducer, Session session) throws Exception {
			JSONObject myjson = (JSONObject) para;
			String title = myjson.getString(SendExecution.TEMPLATETYPE);
			if(Treat.isEmpty(title)){
				myjson.put(SendExecution.TEMPLATETYPE, "消费通知");
			}
			if(!Treat.isEmpty(myjson.getString(SendExecution.DATA)) 
					&& !Treat.isEmpty(myjson.getString(SendExecution.TEMPLATETYPE))
					&& !Treat.isEmpty(myjson.getString(SendExecution.OPERATIONID))
					&& !Treat.isEmpty(myjson.getString("list")) ){
				JSONArray list = myjson.getJSONArray("list");
				long time = System.currentTimeMillis();
				if(!Treat.isEmpty(list)){
					int len = list.size();
					for(int i = 0;i<len;i++){
						JSONObject obj = JSONObject.parseObject(list.get(i).toString());
						if(!Treat.isEmpty(obj.getString(SendExecution.USERID))//用户id
								&& !Treat.isEmpty(obj.getString(SendExecution.TOUSER))//接收者OpenId
								&& !Treat.isEmpty(obj.getString(SendExecution.NOPUBLIC))){//关注哪个服务号
							obj.put(SendExecution.TEMPLATETYPE,myjson.getString(SendExecution.TEMPLATETYPE));//使用哪个模板
							obj.put(SendExecution.DATA,JSONObject.parseObject(myjson.getString(SendExecution.DATA)));//模板参数
							obj.put(SendExecution.OPERATIONID,JSONObject.parseObject(myjson.getString(SendExecution.OPERATIONID)));//操作者id
							obj.put(SendExecution.OPERATIONTIME,time);//操作者时间
							TextMessage message = session.createTextMessage();
							message.setText(obj.toString());
							messageProducer.send(message);
						}
					}
				}else{
					throw new Exception("key为list的数组 长度不能为0");
				}
			}else{
				throw new Exception("参数错误 data不能为空");
			}
		}
	};
	
	/**
	 * 将前端提交的消息发送对象添加到队列中
	 * @param json
	 *  	data 模板内容
	 *  	list 用户数组 包含 id opebid 服务号标识
	 */
	public HashMap<String,Object> send(net.sf.json.JSONObject json){
		JSONObject myparam = JSONObject.parseObject(json.toString());
		System.out.println(myparam.toJSONString());
		try {
			jms.send(myparam, new SendBusiness(){
				@Override
				public void send(Object param, MessageProducer messageProducer,Session session) throws Exception {
					JSONObject myjson = (JSONObject) param;
					String title = myjson.getString(SendExecution.TEMPLATETYPE);
					if(Treat.isEmpty(title)){
						myjson.put(SendExecution.TEMPLATETYPE, "消费通知");
					}
					if(!Treat.isEmpty(myjson.getString(SendExecution.DATA)) 
							&& !Treat.isEmpty(myjson.getString(SendExecution.TEMPLATETYPE)) 
							&& !Treat.isEmpty(myjson.getString("list")) ){
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
									TextMessage message = session.createTextMessage();
									message.setText(obj.toString());
									messageProducer.send(message);
								}
							}
						}else{
							throw new Exception("key为list的数组 长度不能为0");
						}
					}else{
						Log.error(this.getClass().getName()+"send发送异常",myparam.toString());
						throw new Exception("参数错误 data不能为空");
					}
				}
			});
			Log.info("发送成功", myparam.toString());
			HashMap<String,Object> ma = new HashMap<String,Object>();
			ma.put("msg", "发送成功");
			ma.put("success", "true");
			return ma;
		} catch (Exception e) {
			Log.error(this.getClass().getName()+"send发送异常",myparam.toString(),e);
		}
		HashMap<String,Object> ma = new HashMap<String,Object>();
		ma.put("msg", "发送失败!");
		ma.put("success", "false");
		return ma;
	}	
	
}
