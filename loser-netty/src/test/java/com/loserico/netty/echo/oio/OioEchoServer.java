package com.loserico.netty.echo.oio;

import com.loserico.netty.echo.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;

/**
 * Netty服务端, 收到客户端消息后回写到客户端
 * <p>
 * Copyright: (C), 2021-06-20 8:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OioEchoServer {
	
	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new OioEventLoopGroup();
		EventLoopGroup workerGroup = new OioEventLoopGroup();
		EchoServerHandler serverHandler = new EchoServerHandler();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(OioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new LoggingHandler(LogLevel.INFO));
							pipeline.addLast(serverHandler);
						}
					});
			
			ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
