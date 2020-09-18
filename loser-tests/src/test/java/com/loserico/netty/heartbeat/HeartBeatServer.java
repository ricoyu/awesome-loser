package com.loserico.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-09-17 8:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HeartBeatServer {
	
	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("decoder", new StringEncoder());
							pipeline.addLast("encoder", new StringEncoder());
							/*
							 * IdleStateHandler的readerIdleTime参数指定超过3秒还没收到客户端的连接
							 * 会触发IdleStateEvent事件并且交给下一个handler处理, 下一个handler必须
							 * 实现userEventTriggered方法处理对应事件
							 */
							pipeline.addLast(new IdleStateHandler(3, 0, 0, TimeUnit.SECONDS));
							pipeline.addLast(new HeartBeatServerHandler());
						}
					});
			log.info("server starting ...");
			
			ChannelFuture channelFuture = bootstrap.bind("localhost", 9000).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
