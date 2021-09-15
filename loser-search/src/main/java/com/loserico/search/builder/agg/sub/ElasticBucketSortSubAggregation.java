package com.loserico.search.builder.agg.sub;

import com.loserico.common.lang.vo.Page;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.SortSupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.BaseAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对Bucket Aggregation进行分页操作
 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-pipeline-bucket-sort-aggregation.html#CO106-1
 * <p>
 * Copyright: (C), 2021-08-25 14:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticBucketSortSubAggregation extends SubAggregation {
	
	
	private SubAggregation parentAggregation;
	
	/**
	 * 子聚合的名字
	 */
	private String name;
	
	/**
	 * 分页相关, 起始位置
	 */
	protected Integer from;
	
	/**
	 * 分页相关, 每页大小
	 */
	protected Integer size;
	
	/**
	 * 排序 ASC DESC
	 */
	protected List<SortOrder> sortOrders = new ArrayList<>();
	
	public ElasticBucketSortSubAggregation(String name) {
		Objects.requireNonNull(name, "name cannot be null!");
		this.name = name;
	}
	
	
	/**
	 * 设置分页属性, 第一条数据从0开始
	 *
	 * @param from
	 * @param size
	 * @return ElasticBucketSortSubAggregation
	 */
	public ElasticBucketSortSubAggregation paging(Integer from, Integer size) {
		this.from = from;
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
	public ElasticBucketSortSubAggregation sort(String sort) {
		List<SortOrder> sortOrders = SortSupport.sort(sort);
		this.sortOrders.addAll(sortOrders);
		return this;
	}
	
	@Override
	public BaseAggregationBuilder build() {
		List<FieldSortBuilder> sorts = sortOrders.stream().map(SortOrder::toFieldSortBuilder).collect(Collectors.toList());
		BucketSortPipelineAggregationBuilder bucketSortPipelineAggregationBuilder = new BucketSortPipelineAggregationBuilder(name, sorts);
		if (from != null) {
			bucketSortPipelineAggregationBuilder.from(from);
		}
		if (size != null) {
			bucketSortPipelineAggregationBuilder.size(size);
		}
		return bucketSortPipelineAggregationBuilder;
	}
	
	public Page toPage() {
		Page page = new Page();
		int size = this.size == null ? 10 : this.size;
		page.setPageSize(size);
		if (from != null) {
			int currentPage = (int) Math.floor(from / (double) size) + 1;
			page.setCurrentPage(currentPage);
		}
		return page;
	}
	
	@Override
	public SubAggregation and() {
		return parentAggregation;
	}
}
