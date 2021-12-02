package com.loserico.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;

/**
 * <p>
 * Copyright: (C), 2021-06-20 8:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EchoClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		//Configure the client
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new LoggingHandler(LogLevel.INFO));
							pipeline.addLast(new EchoClientHandler());
						}
					});
			
			//Start the client
			ChannelFuture f = bootstrap.connect("127.0.0.1", 8090).sync();
			//Wait until the client is closed
			f.channel().closeFuture().sync();
		} finally {
			//Shut down the event loop to terminate all threads.
			group.shutdownGracefully();
		}
	}
}
