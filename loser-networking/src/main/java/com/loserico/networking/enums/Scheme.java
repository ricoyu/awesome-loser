package com.loserico.networking.enums;

/**
 * URL的scheme部分 
 * <p>
 * Copyright: Copyright (c) 2021-03-22 16:34
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum Scheme {
	
	HTTP, 
	
	HTTPS;
	
	@Override
	public String toString() {
		return this.name();
	}
	
	/**
	 * 将字符串转成Scheme枚举对象
	 * @param schemeStr
	 * @return Scheme
	 */
	public Scheme of(String schemeStr) {
		for(Scheme scheme : Scheme.values()) {
			if (scheme.name().equalsIgnoreCase(schemeStr)) {
				return scheme;
			}
		}
		
		return null;
	}
}
