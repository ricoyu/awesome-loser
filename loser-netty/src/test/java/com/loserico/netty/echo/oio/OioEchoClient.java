package com.loserico.netty.echo.oio;

import com.loserico.netty.echo.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
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
public class OioEchoClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		//Configure the client
		EventLoopGroup group = new OioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(OioSocketChannel.class)
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
