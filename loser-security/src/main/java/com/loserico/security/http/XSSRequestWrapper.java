package com.loserico.security.http;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static com.loserico.security.utils.XSSUtils.stripXSS;

/**
 * <p>
 * Copyright: (C), 2021-02-22 14:21
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
	
	private HttpServletRequest request;
	private byte[] rawData;
	private ResettableServletInputStream servletStream;
	
	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
		this.servletStream = new ResettableServletInputStream();
	}
	
	public void resetInputStream(byte[] newRawData) {
		this.rawData = newRawData;
		this.servletStream.stream = new ByteArrayInputStream(newRawData);
	}
	
	private class ResettableServletInputStream extends ServletInputStream {
		
		private InputStream stream;
		
		@Override
		public int read() throws IOException {
			return stream.read();
		}
		
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
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (rawData == null) {
			rawData = IOUtils.toByteArray(this.request.getReader(), Charsets.UTF_8);
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
		return servletStream;
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		if (rawData == null) {
			rawData = IOUtils.toByteArray(this.request.getReader(), Charsets.UTF_8);
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
		return new BufferedReader(new InputStreamReader(servletStream));
	}
	
	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		
		if (values == null) {
			return null;
		}
		int count = values.length;
		
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = stripXSS(values[i]);
		}
		return encodedValues;
	}
	
	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		return stripXSS(value);
	}
	
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return stripXSS(value);
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		List result = new ArrayList<>();
		Enumeration headers = super.getHeaders(name);
		while (headers.hasMoreElements()) {
			String header = (String) headers.nextElement();
			String[] tokens = header.split(",");
			for (String token : tokens) {
				result.add(stripXSS(token));
			}
		}
		return Collections.enumeration(result);
	}
}
