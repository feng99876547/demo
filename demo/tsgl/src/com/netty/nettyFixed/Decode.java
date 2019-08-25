package com.netty.nettyFixed;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decode extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> list)
			throws Exception {
		if(bytebuf.readableBytes()<8){//小于八格式不对 肯定没有消息头
			return ;
		}
		bytebuf.markReaderIndex();//标记当前的readerIndex的位置
		byte[] datalength = new byte[8];//读取消息头的长度
		bytebuf.readBytes(datalength);
		
		int contentSize = Integer.parseInt(new String(datalength));//拿到消息头
		if(contentSize<0){//消息体长度为0 
			ctx.close();
			return ;
		}else if(bytebuf.readableBytes() < contentSize){//消息头的值不对
			ctx.close();
			return ;
		}
		
		byte[] data = new byte[contentSize];
		bytebuf.readBytes(data);//读出消息体写入byte
		String da = new String(data,"UTF-8");
		list.add(da);
	}

}
