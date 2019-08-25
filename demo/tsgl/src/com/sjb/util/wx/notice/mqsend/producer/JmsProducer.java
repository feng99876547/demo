package com.sjb.util.wx.notice.mqsend.producer;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.sjb.util.Log;
import com.sjb.util.wx.notice.mqsend.producer.add.AddConnectoion;
import com.sjb.util.wx.notice.mqsend.producer.add.LongConnectoion;
import com.sjb.util.wx.notice.mqsend.producer.add.ShortConnectoion;

import java.util.concurrent.ArrayBlockingQueue;

import javax.jms.*;

/**
 * JMS 发送模板
 * 不要建议多次次初始化 每一次初始化都是一个新的对象 所谓的次数只是对同一个对象有效 建议使用单例模式
 * @author fxc
 */
@SuppressWarnings("unchecked")
public class JmsProducer {

	private String username = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    
    private String timeout = "5000";//默认连接超时时间5秒
    
    /*
     */
//    private String brokeurl = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址
    
    private String brokeurl = "failover:(tcp://localhost:61616)";
    
    		
    private  ConnectionFactory connectionFactory;//连接工厂
    
    private  ArrayBlockingQueue<?> connectionQueue;
    
//    private  ArrayBlockingQueue<Connection> connectionQueue;
//    
//    private  ArrayBlockingQueue<ConnectionFactory> connectionQueueShort;//短链接用的时候创建链接 用完就
    
    @SuppressWarnings("rawtypes")
	private AddConnectoion addConnection= null;
    
    //调用该节点的失败次数 如果调用正常清0 失败累加 如果大于10 说明连续失败10次 该节点应该出现问题了
    //先移除该节点 节点重新启动时信息添加进配置 本地监听配置的改变 更新本地配置
    private int errorNum = 0;
    	
    
    private int len = 10;//最大链接数
    
    private boolean long_or_short = true;//使用长链接还是短链接
    
    private boolean transaction = true;//事物状态 true默认开
    
    public JmsProducer(){
    	init();
    }
    
    public JmsProducer(String username,String password,String brokeurl){
    	this.username = username;
    	this.password = password;
    	this.brokeurl = brokeurl;
    	init();
    }
    
    public JmsProducer(String username,String password,String brokeurl,boolean long_or_short,boolean transaction){
    	this.username = username;
    	this.password = password;
    	this.brokeurl = brokeurl;
    	this.long_or_short = long_or_short;
    	this.transaction = transaction;
    	init();
    }
    
    private void init(){
		if(long_or_short){
    		addConnection = new ShortConnectoion();
    		connectionQueue = new ArrayBlockingQueue <ConnectionFactory>(len);
    	}else{
    		addConnection = new LongConnectoion();
        	connectionQueue = new ArrayBlockingQueue <Connection>(len);
    	}
		String symbol = null;
		if(this.brokeurl.indexOf("?")>-1){
			System.out.println(1);
			symbol = "&";
		}else{
			System.out.println(2);
			symbol = "?";
		}
		connectionFactory = new ActiveMQConnectionFactory(username,password,brokeurl+symbol+"timeout="+this.timeout);
		for(int i = 0;i<len;i++){
    		try {
    			addConnection.addConnection(connectionFactory,connectionQueue);
			} catch (JMSException e) {
				Log.error(this.getClass(),"init方法中执行addConnection.addConnection操作异常","", e);
			}
		}
    }
    

    /**
     * 获取工厂链接
     * @return
     * @throws JMSException
     */
    public Connection getConnection() throws JMSException{
    	Connection connection = connectionFactory.createConnection();
    	return connection;
    }
    
    /**
     * 批量发送 开启事物 默认发送的到WxMessage队列中
     * @param para
     * @param bus
     * @return
     * @throws Exception
     */
    public void send(Object para,SendBusiness bus) throws Exception{
    	send(para,bus,"WxMessage");
    }
    
    /**
     * 发送 开启事物
     * @param para
     * @param bus
     * @return
     * @throws Exception
     */
	public void send(Object para,SendBusiness bus,String queueName) throws Exception{
    	Connection connection = null;
    	try {
    		connection = addConnection.start(2,connectionQueue);
			Session session = connection.createSession(transaction,Session.AUTO_ACKNOWLEDGE);//自动签收
			Destination destination = session.createQueue(queueName);
			MessageProducer messageProducer = session.createProducer(destination);
	        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
//	        messageProducer.setTimeToLive(5000);//5秒后过期这个只对点对点模式有效 5秒内没有被消费消息过期删除
	        bus.send(para,messageProducer,session);
	        if(transaction){
	        	session.commit();
	        }
		} catch (JMSException e) {
			if(connection!=null){
				connection.close();
	    		connection = null;
	    		addConnection.addConnection(connectionFactory,connectionQueue);
			}
			throw new JMSException(e.getMessage());
		}finally{
			if(connection!=null){
				addConnection.endHandle(connection, connectionFactory,connectionQueue);
			}
		}
    }

}
