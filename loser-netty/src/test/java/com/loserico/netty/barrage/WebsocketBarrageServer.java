package com.loserico.netty.barrage;

import com.loserico.netty.barrage.channel.WebsocketBarrageServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Websocket 聊天服务器-服务端
 *
 * <p>
 * Copyright: (C), 2020-11-07 14:39
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class WebsocketBarrageServer {
	
	private int port;
	
	public WebsocketBarrageServer(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 0;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		
		new WebsocketBarrageServer(port).run();
	}
	
	public void run() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new WebsocketBarrageServerInitializer())
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			System.out.println("SnakeGameServer 启动了" + port);
			
			//绑定端口，开始接收进来的连接
			ChannelFuture future = bootstrap.bind(port).sync();
			/*
			 * 等待服务器socket关闭
			 * 在这个例子中, 这不会发生, 但你可以优雅地关闭你的服务器。
			 */
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			System.out.println("SnakeGameServer 关闭了");
		}
	}
}
