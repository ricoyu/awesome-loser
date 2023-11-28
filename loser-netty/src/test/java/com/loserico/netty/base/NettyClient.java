package com.loserico.netty.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-02 11:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class NettyClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		//客户端需要一个事件循环组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			/*
			 * 创建客户端启动对象
			 * 注意客户端使用的不是ServerBootstrap而是Bootstrap
			 */
			Bootstrap bootstrap = new Bootstrap();
			//设置相关参数
			bootstrap.group(group) //设置线程组
					.channel(NioSocketChannel.class) //使用NioSocketChannel作为客户端的通道实现
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new BaseMessageDecoder());
							//加入处理器
							ch.pipeline().addLast(new NettyClientHandler());
						}
					});
			
			log.info("netty client starting......");
			//启动客户端去连接服务器端
			ChannelFuture cf = bootstrap.connect("127.0.0.1", 9000).sync();
			//对通道关闭进行监听
			cf.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}
