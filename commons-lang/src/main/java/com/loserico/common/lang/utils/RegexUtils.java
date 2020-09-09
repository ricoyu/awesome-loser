package com.loserico.common.lang.utils;

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
	
	public static final Pattern I18N_MSG_PATTERN = Pattern.compile(I18N_MSG_REGEX);
	
	public static boolean matches(Pattern pattern, String value) {
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
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
}
