package com.loserico.messaging.enums;

/**
 * Kafka Producer 发送消息压缩模式
 * <p>
 * Copyright: Copyright (c) 2021-04-26 11:45
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum Compression {
	
	GZIP,
	
	SNAPPY,
	
	LZ4,
	
	ZSTD,
	
	UNCOMPRESSED,
	
	PRODUCER;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
