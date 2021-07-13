package com.loserico.search.enums;

/**
 * 聚合的分类
 * <p>
 * Copyright: (C), 2021-07-12 18:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum AggCategory {
	
	/**
	 * 一些满足特定条件的集合(分组)
	 */
	BUCKET,
	
	/**
	 * 一些数学运算, 可以对文档字段进行统计分析
	 */
	METRIC,
	
	/**
	 * 对其他聚合结果进行二次聚合
	 */
	PIPELINE,
	
	/**
	 * 支持对多个字段的操作并提供一个结果矩阵
	 */
	MATRIX;
}
