package com.netty.nettyFixed;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
	
	public static void main(String[] args) throws Exception {
		int port=8090; //服务端默认端口
		String reulst = new TimeClient().connect(port, "127.0.0.1","ssss");
	}
	public String connect(int port, String host,String msg) throws Exception{
		//配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		StringBuffer reulst = new StringBuffer();
		try {
			Bootstrap bs = new Bootstrap();
			bs.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					//创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
					protected void initChannel(SocketChannel arg0) throws Exception {
						//处理粘包/拆包问题
//						arg0.pipeline().addLast(new FixedLengthFrameDecoder(16));
//						arg0.pipeline().addLast(new StringDecoder());
						arg0.pipeline().addLast(new Decode());
						arg0.pipeline().addLast(new Encode());
						arg0.pipeline().addLast(new TimeClientHandler(msg,reulst));
					}
				});
			//发起异步连接操作
			ChannelFuture cf = bs.connect(host, port).sync();
			//等待客户端链路关闭
			cf.channel().closeFuture().sync();
			System.out.println(reulst);
			return reulst.toString();
		} finally {
			//优雅退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}
}

