package com.loserico.netty.barrage.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 负责处理HTTP请求
 * <p>
 * Copyright: (C), 2020-11-07 9:28
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private static final File INDEX;
	private final String wsUri;
	
	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI() + "WebsocketBarrage.html";
			path = path.contains("file:") ? path.substring(5) : path;
			INDEX = new File(path);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Unable to locate WebsocketChatClient.html", e);
		}
	}
	
	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		/*
		 * 先判断一下uri是否为Websocket协议的uri
		 */
		if (wsUri.equalsIgnoreCase(request.uri())) {
			/*
			 * 直接把消息交给下一个handler来处理
			 * 参考WebsocketDanmuServerInitializer中的配置, HttpRequestHandler的下一个handler是:
			 * WebSocketServerProtocolHandler, 最后才是
			 * TextWebSocketFrameHandler
			 */
			ctx.fireChannelRead(request.retain());
		} else {
			if (HttpUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}
			
			RandomAccessFile file = new RandomAccessFile(INDEX, "r");
			
			DefaultHttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
			
			boolean keepAlive = HttpUtil.isKeepAlive(response);
			
			if (keepAlive) {
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			ctx.write(response);
			
			if (ctx.pipeline().get(SslHandler.class) == null) {
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			} else {
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
			
			file.close();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("Client: " + channel.remoteAddress() + " 异常");
		//当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
	
	private static void send100Continue(ChannelHandlerContext ctx) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}
}
