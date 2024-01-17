package com.loserico.common.lang.utils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2024-01-12 15:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class TraceId {
	
	private static final AtomicLong SEQUENCE = new AtomicLong(0);
	
	private static final String PREFIX = "TID-";
	
	/**
	 * 生成traceId, 格式为REQ-时间戳-序列号
	 *
	 * @return String
	 */
	public static String traceId() {
		return traceId(null);
	}
	
	/**
	 * 生成traceId, 格式为REQ-服务名-时间戳-序列号
	 *
	 * @param serviceId
	 * @return String
	 */
	public static String traceId(String serviceId) {
		String dateFormat = DateUtils.format(new Date(), "yyyyMMddHHmmss");
		long currentTimeMillis = System.currentTimeMillis();
		if (isBlank(serviceId)) {
			return PREFIX + currentTimeMillis + "-" + SEQUENCE.incrementAndGet();
		}
		return PREFIX + serviceId + "-" + currentTimeMillis + "-" + SEQUENCE.incrementAndGet();
	}
	
}
