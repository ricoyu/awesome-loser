package com.loserico.netty.barrage.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 处理TextWebSocketFrame
 *
 * <p>
 * Copyright: (C), 2020-11-07 9:58
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	/*
	 * 这是一个客户端连接的池子, 所有的客户端连接都保存到这里
	 */
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	/**
	 * 经过前面的WebSocketServerProtocolHandler, Netty已经帮我们把传输的二进制流转换成了TextWebSocketFrame对象
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			if (incoming != channel) {
				/*
				 * 这个方法不是阻塞的, 所以循环调用也是很快的
				 */
				channel.writeAndFlush(new TextWebSocketFrame(msg.text()));
			} else {
				channel.writeAndFlush(new TextWebSocketFrame("我发送的" + msg.text()));
			}
		}
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		// Broadcast a message to multiple Channels
		channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + "加入"));
		channels.add(incoming);
		System.out.println("Client: " + incoming.remoteAddress() + " 加入");
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		// Broadcast a message to multiple Channels
		channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
		System.err.println("Client:" + incoming.remoteAddress() + "离开");
		// A closed Channel is automatically removed from ChannelGroup,
		// so there is no need to do "channels.remove(ctx.channel());"
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("Client: " + channel.remoteAddress() + " 在线");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.err.println("Client: " + channel.remoteAddress() + " 掉线");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel incoming = ctx.channel();
		System.err.println("Client:" + incoming.remoteAddress() + "异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
