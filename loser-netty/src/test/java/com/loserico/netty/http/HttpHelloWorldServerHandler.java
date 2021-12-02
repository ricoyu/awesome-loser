package com.loserico.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;

/**
 * <p>
 * Copyright: (C), 2021-08-04 8:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	
	private static final byte[] CONTENT = "helloworld".getBytes();
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (!(msg instanceof HttpRequest)) {
			return;
		}
		
		HttpRequest request = (HttpRequest) msg;
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
				HttpResponseStatus.OK,
				Unpooled.wrappedBuffer(CONTENT));
		response.headers()
				.set(CONTENT_TYPE, TEXT_PLAIN)
				.setInt(CONTENT_LENGTH, response.content().readableBytes());
		
		ChannelFuture channelFuture = ctx.write(response);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
