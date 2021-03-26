package com.loserico.searchlegacy.enums;

/**
 * Elasticsearch的分词器
 * <p>
 * Copyright: Copyright (c) 2020-12-29 9:28
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
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
	 * <p>
	 * These language analyzers typically perform four roles:
	 * <ul>
	 *     <li/>Tokenize text into individual words: The quick brown foxes → [The, quick, brown, foxes]
	 *     <li/>Lowercase tokens: The → the
	 *     <li/>Remove common stopwords: [The, quick, brown, foxes] → [quick, brown, foxes]
	 *     <li/>Stem tokens to their root form: foxes → fox
	 * </ul>
	 * English analyzer额外会做如下动作:
	 * <ul>
	 *     <li/>The English analyzer removes the possessive ‘s: John’s → john
	 * <ul/>
	 */
	ENGLISH("english"),
	
	/**
	 * ICU Analyzer
	 */
	ICU("icu_analyzer"),
	
	IK_SMART("ik_smart"),
	
	IK_MAX_WORD("ik_max_word"),
	
	/**
	 * HanLP 默认分词器
	 */
	HANLP("hanlp"),
	
	/**
	 * 标准分词
	 */
	HANLP_STANDARD("hanlp_standard"),
	
	/**
	 * NLP分词
	 */
	HANLP_NLP("hanlp_nlp"),
	
	/**
	 * 索引分词
	 */
	HANLP_INDEX("hanlp_index"),
	
	/**
	 * N-最短路分词
	 */
	HANLP_N_SHORT("hanlp_n_short"),
	
	/**
	 * 最短路分词
	 */
	HANLP_DIJKSTRA("hanlp_dijkstra"),
	
	/**
	 * 急速词典分词
	 */
	HANLP_SPEED("hanlp_speed");
	
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
