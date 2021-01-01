package com.loserico.common.lang.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.util.OptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2020-12-15 11:08
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class LogbackUtils {
	
	/**
	 * @param clazz
	 * @param rsyslogServers ip:port形式指定rsyslog 服务器的IP, 端口
	 * @return
	 */
	public static Logger addRsyslog(Class clazz, List<String> rsyslogServers) {
		ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(clazz.getName() + UUID.randomUUID().toString());
		LoggerContext loggerContext = logger.getLoggerContext();
		
		// 设置不向上级打印信息
		logger.setAdditive(false);
		
		Map<String, Appender<ILoggingEvent>> appenders = allAppenders();
		for (Appender appender : appenders.values()) {
			logger.addAppender(appender);
		}
		
		if (rsyslogServers == null || rsyslogServers.isEmpty()) {
			return logger;
		}
		
		rsyslogServers.stream()
				.filter(rsyslogServer -> isNotBlank(rsyslogServer))
				.map(rsyslogServer -> rsyslogServer.split(":"))
				.forEach((ipPortArr) -> {
					try {
						String ip = ipPortArr[0];
						if (isBlank(ip)) {
							log.error("Rsyslog 服务器格式不正确, 应该是 ip:port 形式, 当前提供的格式: {}", ipPortArr);
							return;
						}
						int port = Integer.parseInt(ipPortArr[1]);
						SyslogAppender syslogAppender = createSyslogAppender(ip, port, Level.INFO, loggerContext);
						logger.addAppender(syslogAppender);
					} catch (Exception e) {
						log.error("Rsyslog 服务器格式不正确, 应该是 ip:port 形式, 当前提供的格式: " + ipPortArr, e);
						return;
					}
				});
		
		return logger;
	}
	
	private static SyslogAppender createSyslogAppender(String ip, int port, Level level, LoggerContext loggerContext) {
		SyslogAppender syslogAppender = new SyslogAppender();
		syslogAppender.setContext(loggerContext);
		syslogAppender.addFilter(createLevelFilter(Level.INFO));
		syslogAppender.setSyslogHost(ip);
		syslogAppender.setPort(port);
		syslogAppender.setFacility("SYSLOG");
		syslogAppender.start();
		return syslogAppender;
	}
	
	private static PatternLayoutEncoder createEncoder(LoggerContext context) {
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		// 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
		encoder.setContext(context);
		// 设置格式
		String pattern = OptionHelper.substVars("${pattern}", context);
		encoder.setPattern(pattern);
		encoder.setCharset(Charset.forName("utf-8"));
		encoder.start();
		return encoder;
	}
	
	/**
	 * 设置打印日志的级别
	 *
	 * @param level
	 * @return Filter
	 */
	private static Filter createLevelFilter(Level level) {
		LevelFilter levelFilter = new LevelFilter();
		levelFilter.setLevel(level);
		levelFilter.setOnMatch(FilterReply.ACCEPT);
		levelFilter.setOnMismatch(FilterReply.DENY);
		levelFilter.start();
		return levelFilter;
	}
	
	/**
	 * 先把logback配置文件中的appender都捞出来
	 *
	 * @return
	 */
	private static Map<String, Appender<ILoggingEvent>> allAppenders() {
		Map<String, Appender<ILoggingEvent>> appenders = new HashMap<>();
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders(); it.hasNext(); ) {
				Appender<ILoggingEvent> appender = it.next();
				appenders.put(appender.getName(), appender);
			}
		}
		
		return appenders;
	}
}
