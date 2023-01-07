package com.loserico.common.lang.mq;

/**
 * RocketMQ 延时消息, 延时级别有 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h 
 * <p/>序号从1开始
 * <p>
 * Copyright: (C), 2022-12-03 12:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class DelayLevel {
	
	/**
	 * 延迟一秒
	 */
	public static final int S_1 = 1;
	
	/**
	 * 延时5秒
	 */
	public static final int S_5 = 2;
	
	/**
	 * 延时10秒
	 */
	public static final int S_10 = 3;
	
	/**
	 * 延时30秒
	 */
	public static final int S_30 = 4;
	
	/**
	 * 延时一分钟发射
	 */
	public static final int M_1 = 5;
	
	/**
	 * 延时2分钟发射
	 */
	public static final int M_2 = 6;
	
	/**
	 * 延时3分钟
	 */
	public static final int M_3 = 7;
	
	/**
	 * 延时4分钟
	 */
	public static final int M_4 = 8;
	
	/**
	 * 延时5分钟
	 */
	public static final int M_5 = 9;
	
	/**
	 * 延时6分钟
	 */
	public static final int M_6 = 10;
	
	/**
	 * 延时8分钟
	 */
	public static final int M_8 = 11;
	
	/**
	 * 延时9分钟
	 */
	public static final int M_9 = 12;
	
	/**
	 * 延时10分钟
	 */
	public static final int M_10 = 13;
	
	/**
	 * 延时20分钟
	 */
	public static final int M_20 = 14;
	
	/**
	 * 延时30分钟
	 */
	public static final int M_30 = 15;
	
	/**
	 * 延时1小时
	 */
	public static final int H_1 = 16;
	
	/**
	 * 延时2小时
	 */
	public static final int H_2 = 17;
	
}
