package com.netty.nettyFixed;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {

	private String msg;
	private StringBuffer reulst;
	
	
	
	public TimeClientHandler(String msg, StringBuffer reulst) {
		this.msg = msg;
		this.reulst = reulst;
	}

	@Override
	//向服务器发送指令
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(msg);
	}

	@Override
	//接收服务器的响应
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("Now is : "+body);
		reulst.append(body);
	}

	@Override
	//异常处理
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//释放资源
		ctx.close();
	}
	
}

