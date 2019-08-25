package com.sjb.util.wx.notice.mqsend.consumer.receive;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.sjb.util.Log;


/**
 * @author fxc
 *
 */
public class JmsConsumer {

    private String username = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    
    private String brokeurl = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址
//    private String brokeurl = "failover://tcp://192.168.1.111:61616";
    private ConnectionFactory connectionFactory;
    
    public JmsConsumer(){
    	connectionFactory = new ActiveMQConnectionFactory(username,password,brokeurl);
    }
    
    public JmsConsumer(String username,String password,String brokeurl){
    	this.username = username;
    	this.password = password;
    	this.brokeurl = brokeurl;
    	connectionFactory = new ActiveMQConnectionFactory(username,password,brokeurl);
    }
    
    /**
     * 实现监听 采用自动确认
     * @param recrive
     * @param queueName
     * @throws JMSException
     */
    public void start(ReceiveBusiness recrive,String queueName) throws JMSException{
    	Connection connection = connectionFactory.createConnection();
		connection.start();// 启动连接
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queueName);
		MessageConsumer messageConsumer = session.createConsumer(destination);		
		messageConsumer.setMessageListener(new MessageListener(){
		    public void onMessage(Message message) {
		    	try {//捕获异常不往外抛人工处理否则被其它消费端继续消费重复异常
					recrive.receive(message);
				} catch (Exception e) {
					Log.error("解析异常", e);
				}
		    }
		});
		System.out.println("=====================================开始消费======================================");
    }
    
    /**
     * 实现监听 采用自动确认
     * @param recrive
     * @throws JMSException
     */
    public void start(ReceiveBusiness recrive) throws JMSException{
    	start(recrive,"WxMessage");
    }
    
}