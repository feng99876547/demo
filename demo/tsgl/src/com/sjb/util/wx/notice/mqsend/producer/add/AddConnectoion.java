package com.sjb.util.wx.notice.mqsend.producer.add;

import java.util.concurrent.ArrayBlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * 添加链接队列的模式
 * 使用长链接 还是用的时候开用完关
 * @author fxc
 *
 */
public interface AddConnectoion<T> {
	
	 /**
	  * 初始化队列链接的方式
	  * @param connectionFactory
	  * @param connectionQueue
	  * @throws JMSException
	  */
	 void addConnection(ConnectionFactory connectionFactory,ArrayBlockingQueue<T> queue) throws JMSException;
	 
	 /**
	  * 方法结束时的处理
	  * @param connectionFactory
	  * @param connectionQueue
	  * @throws JMSException
	  */
	 void endHandle(Connection connection,ConnectionFactory connectionFactory,ArrayBlockingQueue<T> queue) throws JMSException;
	 
	 /**
	  * Connection 
	  * @param timeout 超时时间
	  * @throws JMSException
	  */
	 Connection start(int timeout,ArrayBlockingQueue<T> queue) throws JMSException, InterruptedException;
	 
	 
}
