package com.loserico.networking.builder;

import com.loserico.networking.constants.Proto;
import org.graylog2.syslog4j.SyslogIF;

import java.nio.charset.Charset;

/**
 * <p>
 * Copyright: (C), 2021-09-30 15:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SyslogBuilder {
	
	private Proto proto;
	
	private String host;
	
	private Integer port;
	
	private Charset charset;
	
	private Integer maxMessageLength = 1024;
	
	public SyslogBuilder(String host, Integer port) {
		this.host = host;
		this.port = port;
	}
	
	public SyslogBuilder proto(Proto proto) {
		this.proto = proto;
		return this;
	}
	
	public SyslogBuilder charset(String charset) {
		this.charset = Charset.forName(charset);
		return this;
	}
	
	public SyslogBuilder charset(Charset charset) {
		this.charset = charset;
		return this;
	}
	
	/**
	 * 默认1024
	 *
	 * @param maxMessageLength
	 * @return
	 */
	public SyslogBuilder maxMessageLength(Integer maxMessageLength) {
		this.maxMessageLength = maxMessageLength;
		return this;
	}
	
	public SyslogIF build() {
		SyslogIF syslog = org.graylog2.syslog4j.Syslog.getInstance(proto.name());
		syslog.getConfig().setHost(host);
		syslog.getConfig().setPort(port);
		if (maxMessageLength != null) {
			syslog.getConfig().setMaxMessageLength(50000);
		}
		if (charset != null) {
			syslog.getConfig().setCharSet(charset.name());
		}
		return syslog;
	}
}
