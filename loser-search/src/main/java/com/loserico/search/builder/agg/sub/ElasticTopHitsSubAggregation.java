package com.loserico.search.builder.agg.sub;

import com.loserico.search.enums.SortOrder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.loserico.common.lang.utils.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2021-08-24 14:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticTopHitsSubAggregation extends SubAggregation {
	
	/**
	 * 聚合的名字
	 */
	private String name;
	
	/**
	 * The offset from the first result you want to fetch
	 */
	private Integer from;
	
	/**
	 *  The maximum number of top matching hits to return per bucket. By default the top three matching hits are returned.
	 */
	private Integer size;
	
	/**
	 * 是否要获取_source
	 */
	protected boolean fetchSource = true;
	
	/**
	 * 设置 _source 属性, 即指定要返回哪些字段, 而不是返回整个_source
	 */
	protected String[] includeSource;
	
	/**
	 * 指定查询要排除哪些字段
	 */
	protected String[] excludeSource;
	
	/**
	 * 查询返回的结果类型
	 */
	//protected Class resultType;
	
	/**
	 * How the top matching hits should be sorted. By default the hits are sorted by the score of the main query.
	 */
	protected List<SortOrder> sortOrders = new ArrayList<>();
	
	private SubAggregation parentAggregation;
	
	public ElasticTopHitsSubAggregation(SubAggregation parentAggregation, String name) {
		this.parentAggregation = parentAggregation;
		this.name = name;
	}
	
	public ElasticTopHitsSubAggregation(String name) {
		this.name = name;
	}
	
	/**
	 * 从第几条开始返回
	 * @param size
	 * @return
	 */
	public ElasticTopHitsSubAggregation from(int size) {
		this.size = size;
		return this;
	}
	
	/**
	 * ES默认只返回Top 3条数据, 这里可以指定返回多少条数据
	 *
	 * @param size
	 * @return ElasticTopHitsSubAggregation
	 */
	public ElasticTopHitsSubAggregation size(int size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 添加排序规则<p>
	 * sort格式: 字段1:asc,字段2:desc,字段3<p>
	 * 其中字段3按升序排(ASC)<p>
	 * <p>
	 * 注意: text类型字段不能排序, 要用field
	 *
	 * @param sort
	 * @return QueryBuilder
	 */
	public ElasticTopHitsSubAggregation sort(String sort) {
		if (isBlank(sort)) {
			return this;
		}
		
		/*
		 * 先把 name,age:asc,year:desc 这种形式的排序字符串用,拆开
		 */
		String[] sorts = split(sort, ",");
		for (int i = 0; i < sorts.length; i++) {
			String s = sorts[i];
			if (isBlank(s)) {
				log.warn("you entered a block sort!");
				continue;
			}
			
			//再用冒号拆出 field 和 asc|desc
			String[] fieldAndDirection = s.trim().split(":");
			//field:asc 拆开来长度只能是1或者2
			if (fieldAndDirection.length == 0) {
				log.warn("wrong sort gramma {}, should be of type field1:asc,field2:desc", sort);
				continue;
			}
			
			//数组第一个元素是字段名, 不能省略, 所以为空的话表示语法不正确
			String field = fieldAndDirection[0].trim();
			if (isBlank(field)) {
				log.warn("field is blank, skip");
				continue;
			}
			
			String direction = null;
			if (fieldAndDirection.length > 1) {
				direction = fieldAndDirection[1].trim();
			}
			//排序规则为空的话, 看下字段名是不是-开头的, 如果是的话, 降序, 否则默认升序
			if (isBlank(direction)) {
				if (field.startsWith("-")) {
					field = field.substring(1);
					direction = "desc";
				} else {
					direction = "asc";
				}
			}
			SortOrder sortOrder = SortOrder.fieldSort(field).direction(direction);
			this.sortOrders.add(sortOrder);
		}
		
		return this;
	}
	
	/**
	 * 是否要获取source, 默认true
	 * @param fetchSource
	 * @return ElasticTopHitsSubAggregation
	 */
	public ElasticTopHitsSubAggregation fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticTopHitsSubAggregation
	 */
	public ElasticTopHitsSubAggregation excludeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSource = sources;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticTopHitsSubAggregation
	 */
	public ElasticTopHitsSubAggregation excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticTopHitsSubAggregation
	 */
	public ElasticTopHitsSubAggregation includeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.includeSource = sources;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticTopHitsSubAggregation
	 */
	public ElasticTopHitsSubAggregation includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 设置Top Hits 返回结果的类型
	 * @param resultType
	 * @return ElasticTopHitsSubAggregation
	 */
	/*public ElasticTopHitsSubAggregation resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}*/
	
	@Override
	public AggregationBuilder build() {
		TopHitsAggregationBuilder topHitsAggregationBuilder = AggregationBuilders.topHits(name);
		//在获取Source的前提下再看包含/排除的字段
		if (fetchSource) {
			topHitsAggregationBuilder.fetchSource(includeSource, excludeSource);
		}
		if (from != null) {
			topHitsAggregationBuilder.from(from);
		}
		if (size != null) {
			topHitsAggregationBuilder.size(size);
		}
		
		sortOrders.forEach(sortOrder -> sortOrder.addTo(topHitsAggregationBuilder));
		return topHitsAggregationBuilder;
	}
	
	@Override
	public SubAggregation and() {
		return parentAggregation;
	}
}
