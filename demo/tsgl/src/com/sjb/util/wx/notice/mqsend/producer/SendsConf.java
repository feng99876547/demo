package com.sjb.util.wx.notice.mqsend.producer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 * 消息批量推送mq配置
 * @author fxc
 */
public class SendsConf {
	
	private Connection connection;
	
	private Session session;
	
	private Destination destination;
	
	private MessageProducer messageProducer;
	
	public SendsConf(Connection connection, Session session, Destination destination, MessageProducer messageProducer) {
		super();
		this.connection = connection;
		this.session = session;
		this.destination = destination;
		this.messageProducer = messageProducer;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public MessageProducer getMessageProducer() {
		return messageProducer;
	}

	public void setMessageProducer(MessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}
	
	
	
}
