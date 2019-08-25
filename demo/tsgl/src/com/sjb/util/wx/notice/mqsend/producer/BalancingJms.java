package com.sjb.util.wx.notice.mqsend.producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;

import com.sjb.util.Log;
import com.sjb.util.Treat;




/**
 * 
 * @author fxc
 *
 */
public class BalancingJms {

	private static List<JmsProducer> jmss = null;
	
	private volatile int mark = 0;
	
	private int len;
	
	private int retries = 2-1;//发送消息失败 重试的次数 这边是两次
	
	private Object lock = new Object();
	
	private Map<String,String> ma;
	
	
	private JmsProducer getJms(){
		return jmss.get(getMark());
	}
	
	/**
	 * 轮循负载
	 * @return
	 */
	private int getMark(){
		int r = 0;
		synchronized(lock){
			r = mark++;
		}
		if(r==len){
			mark = 0;
		}
		return r;
	}
	
	public BalancingJms(){
		super();
		Map<String,String> map = new HashMap<String,String>();
		ma.put("127.0.0.1:61616", ActiveMQConnection.DEFAULT_USER+","+ActiveMQConnection.DEFAULT_PASSWORD+","+"failover:(tcp://localhost:61616)");
		this.ma = map;
		try {
			init(this.ma);
		} catch (JMSException e) {
			Log.error("初始化失败", e.getMessage());
		}
	}
	
	/**
	 * 初始化
	 * @throws JMSException 
	 */
	public void init(Map<String,String> ma) throws JMSException{
		if(!Treat.isEmpty(ma)){
			len = ma.size()-1;
			try {
				jmss = addJms(ma,true,false);
			} catch (JMSException e) {
				Log.error("初始化JMS失败!", e);
			}
		}else{
			throw new JMSException("初始化的集合集合不能为空!");
		}
		
	}
	
	
	/**
	 * @param b true短连接 false长链接
	 * @param t true开启事务 false不开启事务
	 * 初始化JMS集合
	 */
	private List<JmsProducer> addJms(Map<String,String> ma,boolean b,boolean t) throws JMSException{
		if(!Treat.isEmpty(ma)){
			List<JmsProducer> list = new ArrayList<JmsProducer>();
			for(String key : ma.keySet()){
				String[] str = ma.get(key).split(",");
 				JmsProducer jms = new JmsProducer(str[0],str[1],str[2],b,t);
 				list.add(jms);
			}
			return list;
		}else{
			throw new JMSException("初始化jms集合失败");
		}
	}
	
	
	/**
	 * 发送消息
	 * @throws Exception 
	 */
	public void send(Object para,SendBusiness bus) throws Exception{
		send(para,bus,"WxMessage");
	}
	
	/**
	 * 发送消息
	 * 这边的集群容错使用失败自动切换
	 * @throws Exception 
	 */
	public void send(Object para,SendBusiness bus,String queueName) throws Exception{
		for(int i=0;i<retries;i++){
			try {
				getJms().send(para,bus,queueName);
				break;
			} catch (Exception e) {
				if(i>=retries){
					Log.error("多次使用MQ发送消息失败是否短信通知运维人员查看是异常还是网络问题",e);
					throw new Exception(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 关闭长链接集合
	 * @param listConf
	 * @throws JMSException
	 */
	private void close(List<SendsConf> listConf){
		int len = listConf.size();
		for(int i=0;i<len;i++){
			try {
				listConf.get(i).getSession().commit();
			} catch (JMSException e1) {
				Log.error(this.getClass(),"提交事务异常","", e1);
			}
			try {
				listConf.get(i).getConnection().close();
			} catch (JMSException e) {
				Log.error(this.getClass(),"close方法关闭链接异常","", e);
			}
		}
	}
	
	/**
	 * 初始化mq集合需要使用的配置
	 * @return
	 * @throws JMSException
	 */
	private List<SendsConf> initConnection(String queueName){
		List<SendsConf> list = new ArrayList<SendsConf>();
		for(int i=0;i<jmss.size();i++){
			Connection connection = null;
			try {
				connection = jmss.get(i).getConnection();
				Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);//自动签收
				Destination destination = session.createQueue(queueName);
				MessageProducer messageProducer = session.createProducer(destination);
				messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
				connection.start();
				SendsConf sc = new SendsConf(connection,session,destination,messageProducer);
				list.add(sc);
			} catch (JMSException e) {
				if(connection!=null)
					try {
						Log.error(this.getClass(),"initConnection创建链接异常", jmss.get(i).toString(), e);
						connection.close();
					} catch (JMSException e1) {
						Log.error(this.getClass(),"initConnection关闭链接异常","", e1);
					}
			}
		}
		return list;
	}
	
	 /**
     * 批量发送开启事物
     * @param para
     * @param bus
     * @return
	 * @throws Exception
     */
	public void sends(Object obj,SendBusiness bus,SendsConf sc) throws Exception{
        bus.send(obj,sc.getMessageProducer(),sc.getSession());//业务处理
    }
	
	/**
	 * 批量发送消息 使用长链接分发到不同的mq
	 * @throws JMSException 
	 * @throws Exception 
	 */
	public synchronized void sends(List<Object> list,SendBusiness bus,String queueName) throws JMSException{
		if(!Treat.isEmpty(list)){
			List<SendsConf> listConf = null;
			try{
				listConf = initConnection(queueName);
				for(int j=0;j<list.size();j++){
					int len = retries<list.size() ? retries :list.size();
					for(int i=0;i<len;i++){
						try {
							sends(list.get(i),bus,listConf.get(getMark()));
							break;
						} catch (Exception e) {
							if(i>=len){
								Log.error("多次使用MQ发送消息失败是否短信通知运维人员查看是异常还是网络问题",e);
								throw new JMSException(e.getMessage());
							}
						}
					}
				}
			}catch (Exception e) {
				throw new JMSException(e.getMessage());
			}finally{
				if(listConf!=null){
					close(listConf);
				}
			}
		}
	}
}
