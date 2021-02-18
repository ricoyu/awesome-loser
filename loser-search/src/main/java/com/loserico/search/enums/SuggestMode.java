package com.loserico.search.enums;

/**
 * <p>
 * Copyright: Copyright (c) 2021-02-13 20:58
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum SuggestMode {
	
	/**
	 * 默认值
	 * 如索引中已经存在, 就不提供建议
	 * Only provide suggestions for suggest text terms that are not in the index.
	 */
	MISSING("missing"),
	
	/**
	 * 推荐出现频率更加高的词
	 * Only suggest suggestions that occur in more docs than the original suggest text term.
	 */
	POPULAR("popular"),
	
	/**
	 * 无论是否存在, 都提供建议
	 * Suggest any matching suggestions based on terms in the suggest text.
	 */
	ALWAYS("always");
	
	private String mode;
	
	private SuggestMode(String mode) {
		this.mode = mode;
	}
}
