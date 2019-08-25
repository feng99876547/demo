package com.sjb.util.wx.notice.mqsend.producer.add;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class LongConnectoion implements AddConnectoion<Connection>{

	@Override
	public void addConnection(ConnectionFactory connectionFactory,ArrayBlockingQueue<Connection> queue)
			throws JMSException {
		Connection connection = connectionFactory.createConnection();
		queue.add(connection);
	}

	@Override
	public void endHandle(Connection connection, ConnectionFactory connectionFactory,ArrayBlockingQueue<Connection> queue) throws JMSException {
		addConnection(connectionFactory,queue);
	}
	
	@Override
	public Connection start(int timeout,ArrayBlockingQueue<Connection> queue) throws JMSException, InterruptedException  {
		Connection connection = queue.poll(timeout, TimeUnit.SECONDS);
		if(connection == null){
			throw new JMSException("获取JMS connectionFactory 超时");
		}
		connection.start();
		return connection;
	}

}
