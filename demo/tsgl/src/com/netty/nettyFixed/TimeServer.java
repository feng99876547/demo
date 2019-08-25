package com.netty.nettyFixed;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author Five老师
 * @createTime 2017年11月13日 下午8:42:39
 * 
 */
public class TimeServer {

	public static void main(String[] args) throws Exception {
		int port=8090; //服务端默认端口
		new TimeServer().bind(port);
	}
	public void bind(int port) throws Exception{
		//Reactor线程组
		//1用于服务端接受客户端的连接
		EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
		//2用于进行SocketChannel的网络读写
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//Netty用于启动NIO服务器的辅助启动类
			ServerBootstrap sb = new ServerBootstrap();
			//将两个NIO线程组传入辅助启动类中
			sb.group(acceptorGroup, workerGroup)
				//设置创建的Channel为NioServerSocketChannel类型
				.channel(NioServerSocketChannel.class)
				//配置NioServerSocketChannel的TCP参数
				.option(ChannelOption.SO_BACKLOG, 102400)
				//设置绑定IO事件的处理类
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel arg0) throws Exception {
						//处理粘包/拆包问题
//						arg0.pipeline().addLast(new FixedLengthFrameDecoder(16));
//						arg0.pipeline().addLast(new StringDecoder());
						arg0.pipeline().addLast(new Decode());
						arg0.pipeline().addLast(new Encode());
						arg0.pipeline().addLast((new IdleStateHandler(60, 120, 180,TimeUnit.SECONDS)));
						arg0.pipeline().addLast(new TimeServerHandler());
					}
				});
			//绑定端口，同步等待成功（sync()：同步阻塞方法）
			//ChannelFuture主要用于异步操作的通知回调
			ChannelFuture cf = sb.bind(port).sync();
				
			//等待服务端监听端口关闭
			cf.channel().closeFuture().sync();
		} finally {
			//优雅退出，释放线程池资源
			acceptorGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}


