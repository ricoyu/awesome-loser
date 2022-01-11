package com.loserico.networking.builder;

import com.loserico.networking.constants.Proto;
import lombok.extern.slf4j.Slf4j;
import org.graylog2.syslog4j.SyslogConfigIF;
import org.graylog2.syslog4j.SyslogIF;
import org.graylog2.syslog4j.impl.net.tcp.TCPNetSyslogConfig;
import org.graylog2.syslog4j.impl.net.udp.UDPNetSyslogConfig;

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
@Slf4j
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
	
	/**
	 * 每次都重新创建一个SyslogIF实例
	 *
	 * @return
	 */
	public SyslogIF build() {
		SyslogConfigIF config = null;
		if (proto == Proto.UDP) {
			config = new UDPNetSyslogConfig(host, port);
		} else {
			config = new TCPNetSyslogConfig(host, port);
		}
		if (maxMessageLength != null) {
			config.setMaxMessageLength(50000);
		}
		if (charset != null) {
			config.setCharSet(charset.name());
		}
		
		Class syslogClass = config.getSyslogClass();
		
		SyslogIF syslog = null;
		try {
			syslog = (SyslogIF) syslogClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("", e);
			throw new RuntimeException("创建SyslogIF实例失败! Host: " + host + ", Poer: " + port, e);
		}
		syslog.initialize(proto.name().toLowerCase(), config);
		return syslog;
	}
}
