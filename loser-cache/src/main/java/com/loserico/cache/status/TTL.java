package com.loserico.cache.status;

/**
 * 封装TTL结果
 * <p>
 * Copyright: Copyright (c) 2018-10-01 22:17
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public enum TTL {

	KEY_NOT_EXIST(-3),
	FIELD_NOT_EXIST(-2),
	NO_EXPIRE(-1),
	TTL(1);

	private int code;
	private int ttl;

	private TTL(int code) {
		this.setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int ttl() {
		return ttl;
	}

	@Override
	public String toString() {
		if (this != TTL) {
			return this.name();
		}
		return String.valueOf(ttl);
	}

}