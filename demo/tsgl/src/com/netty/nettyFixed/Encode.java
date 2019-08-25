package com.netty.nettyFixed;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encode extends MessageToByteEncoder{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		out.clear();
		
		String data = msg.toString();
		
		byte[] byteBody = data.getBytes("UTF-8");
		
		String dataLength = String.format("%08d", byteBody.length);
		
		byte[] byteDataLength = dataLength.getBytes("UTF-8");
		
		out.writeBytes(byteDataLength);//消息头固定八位 （消息长度）
		
		out.writeBytes(byteBody);//消息内容
	}

}
