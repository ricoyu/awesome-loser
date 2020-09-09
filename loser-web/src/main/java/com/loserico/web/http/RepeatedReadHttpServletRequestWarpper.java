package com.loserico.web.http;

import com.loserico.web.utils.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020-09-08 14:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RepeatedReadHttpServletRequestWarpper extends HttpServletRequestWrapper {
	
	private final byte[] body;
	
	/**
	 * Constructs a request object wrapping the given request.
	 *
	 * @param request the {@link HttpServletRequest} to be wrapped.
	 * @throws IllegalArgumentException if the request is null
	 */
	public RepeatedReadHttpServletRequestWarpper(HttpServletRequest request) {
		super(request);
		body = WebUtils.bodyString(request).getBytes(UTF_8);
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		
		final ByteArrayInputStream bias = new ByteArrayInputStream(body);
		
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}
			
			@Override
			public boolean isReady() {
				return false;
			}
			
			@Override
			public void setReadListener(ReadListener readListener) {
				
			}
			
			@Override
			public int read() throws IOException {
				return bias.read();
			}
		};
	}
}
