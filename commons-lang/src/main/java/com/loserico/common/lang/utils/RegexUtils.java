package com.loserico.common.lang.utils;

import com.loserico.common.lang.bean.UrlParts;
import com.loserico.common.lang.transformer.Transformers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 正则表达式工具类
 * <p>
 * Copyright: Copyright (c) 2019/10/14 17:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class RegexUtils {
	
	/**
	 * 匹配{NotBlank.username}这种形式的字符串
	 */
	public static final String I18N_MSG_REGEX = "\\{([^\\s]+)\\}";
	
	/**
	 * 用于匹配URL的正则表达式
	 *
	 * 给定一个URL:<p>
	 * https://www.google.com:80/dir/1/2/search.html?arg=0-a&arg1=1-b&arg3-c#hash
	 * <p>
	 * <ul>抽取URL中不同部分对应的group如下
	 * <li/>0      完整url
	 * <li/>1      https:                     (([^:/?#]+):)?
	 * <li/>2      https                      ([^:/?#]+)
	 * <li/>3      //www.google.com:80        (//([^/?#:]*)(:(\d{2,}))?)?
	 * <li/>4      www.google.com             ([^/?#:]*):?
	 * <li/>5      :80                        (:(\d{2,}))?
	 * <li/>6      80                         (\d{2,})
	 * <li/>7      /dir/1/2/search.html       ([^?#]*)
	 * <li/>8      ?arg=0-a&arg1=1-b&arg3-c   (\?([^#]*))?
	 * <li/>9      arg=0-a&arg1=1-b&arg3-c    ([^#]*)
	 * <li/>10      #hash                     (#(.*))?
	 * <li/>11      hash                      (.*)
	 * </ul>
	 * <p>
	 * 我们一般比较关系的是Group 2, 4, 6, 7, 9, 分别代表 scheme, host, port, path, args
	 */
	public static final String URL_REGEX = "^(([^:/?#]+):)?(//([^/?#:]*)(:(\\d{2,}))?)?([^?#]*)(\\?([^#]*))?(#(.*))?";
	
	public static final Pattern I18N_MSG_PATTERN = Pattern.compile(I18N_MSG_REGEX);
	
	public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
	
	public static boolean matches(Pattern pattern, String value) {
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	public static UrlParts teardown(String url) {
		if (isBlank(url)) {
			return null;
		}
		
		Matcher matcher = URL_PATTERN.matcher(url);
		if (!matcher.matches()) {
			return null;
		}
		
		UrlParts urlParts = new UrlParts();
		String scheme = matcher.group(2);
		urlParts.setScheme(scheme);
		urlParts.setHost(matcher.group(4));
		String port = matcher.group(6);
		if ("https".equalsIgnoreCase(scheme) && isBlank(port)) {
			port = "443";
		}
		if ("http".equalsIgnoreCase(scheme) && isBlank(port)) {
			port = "80";
		}
		urlParts.setPort(Transformers.convert(port, Integer.class));
		urlParts.setPath(matcher.group(7));
		urlParts.setParams(matcher.group(9));
		
		return urlParts;
	}
	
	/**
	 * 从{NotBlank.username}这种形式的字符串中提取NotBlank.username部分, 作为国际化支持
	 * 如果不满足{NotBlank.username}这种格式, 则原样返回
	 * @param msgTemplate
	 * @return
	 */
	public static MsgTemplate msgTemplate(String msgTemplate) {
		if (isBlank(msgTemplate)) {
			return null;
		}
		Matcher matcher = I18N_MSG_PATTERN.matcher(msgTemplate);
		if (matcher.matches()) {
			return new MsgTemplate(true, matcher.group(1).trim());
		}
		
		return new MsgTemplate(false, msgTemplate);
	}
	
	public static class MsgTemplate{
		private boolean isMsgTemplate;
		private String msgTemplate;
		
		public MsgTemplate(boolean isMsgTemplate, String msgTemplate) {
			this.isMsgTemplate = isMsgTemplate;
			this.msgTemplate = msgTemplate;
		}
		
		public boolean isMsgTemplate() {
			return isMsgTemplate;
		}
		
		public void setMsgTemplate(boolean msgTemplate) {
			isMsgTemplate = msgTemplate;
		}
		
		public String getMsgTemplate() {
			return msgTemplate;
		}
		
		public void setMsgTemplate(String msgTemplate) {
			this.msgTemplate = msgTemplate;
		}
	}

	/**
	 * 去除字符串首尾的双引号, 返回新的字符串
	 * @param s
	 * @return 去除了首尾双引号后的新字符串
	 */
	public static String trimQuotes(String s) {
		/*
		 * ^\" 匹配字符串开头的引号
		 * | 表示“或者”
		 * \"$ 匹配字符串结尾的引号
		 */
		return s != null ? s.replaceAll("^\"|\"$", "") : null;
	}
}
