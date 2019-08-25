package com.sjb.util.wx.notice.mqsend.producer.add;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class ShortConnectoion implements AddConnectoion<ConnectionFactory>{

	@Override
	public void addConnection(ConnectionFactory connectionFactory,ArrayBlockingQueue<ConnectionFactory> queue) throws JMSException {
//		Connection connection = connectionFactory.createConnection();//这边就已经创建链接了
		queue.add(connectionFactory);
	}

	@Override
	public void endHandle(Connection connection,ConnectionFactory connectionFactory,ArrayBlockingQueue<ConnectionFactory> queue)
			throws JMSException {
		connection.close();
		addConnection(connectionFactory,queue);
	}

	@Override
	public Connection start(int timeout,ArrayBlockingQueue<ConnectionFactory> queue) throws JMSException, InterruptedException {
		ConnectionFactory connectionFactory = queue.poll(timeout, TimeUnit.SECONDS);
		if(connectionFactory == null){
			throw new JMSException("获取JMS connectionFactory 超时");
		}
		Connection connection = connectionFactory.createConnection();
		System.out.println(connection);
		connection.start();
		return connection;
	}

}
