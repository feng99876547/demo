package com.sjb.util.wx.notice.mqsend.consumer.receive;

import javax.jms.Message;

/**
 * 实现JMS 接收业务
 * @author fxc
 *
 */
public interface ReceiveBusiness {
	
	public void receive(Message message) throws Exception;
	
}
