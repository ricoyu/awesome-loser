package com.loserico.search.enums;

/**
 * Elasticsearch的分词器 
 * <p>
 * Copyright: Copyright (c) 2020-12-29 9:28
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum Analyzer {
	
	/**
	 * 默认分词器, 按词切分, 小写处理
	 */
	STANDARD("standard"),
	
	/**
	 * 按照非字母切分, 小写处理
	 */
	SIMPLE("simple"),
	
	/**
	 * 小写处理, 停用词过滤(the, a, is)
	 */
	STOP("stop"),
	
	/**
	 * 按照空格切分, 不转小写
	 */
	WHITESPACE("whitespace"),
	
	/**
	 * 不分词, 直接将输入当作输出
	 */
	KEYWORD("keyword"),
	
	/**
	 * 正则表达式, 默认 \W+ (非字符分隔)
	 */
	PATTERN("pattern"),
	
	/**
	 * language 分词器
	 */
	LANGUAGE_ENG("english"),
	
	/**
	 * ICU Analyzer
	 */
	ICU("icu_analyzer"), 
	
	IK_SMART("ik_smart"), 
	
	IK_MAX_WORD("ik_max_word");
	
	/**
	 * 分词器名称
	 */
	private String analyzer;
	
	private Analyzer(String analyzer) {
		this.analyzer = analyzer;
	}
	
	
	@Override
	public String toString() {
		return analyzer;
	}
}
