package com.sjb.util.wx.notice.mqsend.producer;

import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 * 实现mq发送业务
 * @author fxc
 *
 */
public interface SendBusiness {
	
	public void send(Object para,MessageProducer messageProducer,Session session) throws Exception;
	
}
