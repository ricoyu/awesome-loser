package com.loserico.search.builder;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.exception.ElasticQueryException;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

/**
 * <p>
 * Copyright: (C), 2021-01-11 9:23
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ElasticQueryBuilder {
	
	/**
	 * 要查询的索引
	 */
	private String[] indices;
	
	/**
	 * TermQueryBuilder, MatchQueryBuilder 等等builder的父类
	 */
	private QueryBuilder builder;
	
	/**
	 * 是否要获取_source
	 */
	private boolean fetchSource = true;
	
	/**
	 * 分页相关, 起始位置
	 */
	private Integer from;
	
	/**
	 * 分页相关, 每页大小
	 */
	private Integer size;
	
	/**
	 * 排序 ASC DESC
	 */
	private List<SortOrder> sortOrders = new ArrayList<>();
	
	/**
	 * 提升或者降低查询的权重
	 */
	private float boost = 1f;
	
	/**
	 * 查询返回的结果类型
	 */
	private Class resultType;
	
	/**
	 * 是否要将Query转为constant_scre query, 以避免算分, 提高查询性能
	 * 适用于有时候我们根本不关心 TF/IDF ，只想知道一个词是否在某个字段中出现过。
	 */
	private boolean constantScore = false;
	
	/**
	 * 仅仅是因为不喜欢 new
	 */
	private ElasticQueryBuilder() {
	}
	
	/**
	 * 创建QueryBuilder实例, 同时设置要查询的Indices
	 *
	 * @param indices
	 * @return QueryBuilder
	 */
	public static ElasticQueryBuilder instance(String... indices) {
		ElasticQueryBuilder builder = new ElasticQueryBuilder();
		builder.indices = indices;
		return builder;
	}
	
	/**
	 * 参数 builder 可以用 org.elasticsearch.index.query.QueryBuilders类提供的各种便利方法来创建
	 *
	 * @param builder
	 * @return QueryBuilder
	 */
	/*public QueryBuilder builder(AbstractQueryBuilder builder) {
		this.builder = builder;
		return this;
	}*/
	
	/**
	 * 设置分页属性
	 *
	 * @param from
	 * @param size
	 * @return QueryBuilder
	 */
	public ElasticQueryBuilder paging(int from, int size) {
		this.from = from;
		this.size = size;
		return this;
	}
	
	/**
	 * 添加排序规则, 可以添加多个<br/>
	 * 要注意的是text类型字段不能作为排序字段, 因为text类型字段是被分词的, 如果非要按这个字段排序,
	 * 假设text类型字段为description, 那么可以通过Elasticsearch的多字段特性, 用 description.keyword来排序
	 *
	 * @param sortOrder
	 * @return QueryBuilder
	 */
	public ElasticQueryBuilder addSort(SortOrder sortOrder) {
		if (sortOrder == null) {
			return this;
		}
		
		this.sortOrders.add(sortOrder);
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return QueryBuilder
	 */
	public ElasticQueryBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 提升或者降低查询的权重
	 *
	 * @param boost
	 * @return QueryBuilder
	 */
	public ElasticQueryBuilder boost(float boost) {
		this.boost = boost;
		return this;
	}
	
	public ElasticQueryBuilder type(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	/**
	 * 设置QueryBuilder, 可以通过QueryBuilders提供的静态方法构造出来
	 *
	 * @param builder
	 * @return ElasticQueryBuilder
	 */
	public ElasticQueryBuilder queryBuilder(QueryBuilder builder) {
		this.builder = builder;
		return this;
	}
	
	/**
	 * 是否要将Query转为constant_scre query, 以避免算分, 提高查询性能
	 *
	 * @param constantScore
	 * @return ElasticQueryBuilder
	 */
	public ElasticQueryBuilder constantScore(boolean constantScore) {
		this.constantScore = constantScore;
		return this;
	}
	
	/**
	 * 执行查询
	 *
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList() {
		SearchHit[] hits = getSearchHits();
		
		if (hits.length == 0) {
			return Collections.emptyList();
		}
		
		//先拿到所有的source
		List<String> sources = asList(hits).stream()
				.filter(Objects::nonNull)
				.map(SearchHit::getSourceAsString)
				.filter(Objects::nonNull)
				.collect(toList());
		
		//不需要封装成POJO的情况
		if (resultType == null) {
			return (List<T>) sources;
		}
		
		return (List<T>) sources.stream()
				.map(source -> JacksonUtils.toObject(source, resultType))
				.collect(toList());
	}
	
	/**
	 * 执行查询, 返回一条记录
	 *
	 * @param <T>
	 * @return T
	 */
	public <T> T selectOne() {
		SearchHit[] hits = getSearchHits();
		
		if (hits.length == 0) {
			return null;
		}
		
		SearchHit hit = hits[0];
		String source = hit.getSourceAsString();
		
		//如果没有source, 就不用管是否要封装成POJO了
		if (source == null) {
			return null;
		}
		
		//如果要封装成POJO对象
		if (ReflectionUtils.isPojo(resultType)) {
			return (T) JacksonUtils.toObject(source, resultType);
		}
		
		return (T) source;
	}
	
	/**
	 * 返回查询到的记录数, 查询不会真正返回文档
	 * @return
	 */
	public long getCount() {
		if (this.builder == null) {
			throw new ElasticQueryException("请先设置QueryBuilder");
		}
		
		SearchRequestBuilder builder = ElasticUtils.client.prepareSearch(indices)
				.setQuery(this.builder)
				.setSize(0) //count 不需要真正返回数据
				.setFetchSource(false);
		
		SearchResponse response = builder.get();
		TotalHits totalHits = response.getHits().getTotalHits();
		if (totalHits == null) {
			return 0L;
		}
		
		return totalHits.value;
	}
	
	/**
	 * 作为查询的公共部分抽取出来
	 *
	 * @return
	 */
	private SearchHit[] getSearchHits() {
		if (this.builder == null) {
			throw new ElasticQueryException("请先设置QueryBuilder");
		}
		
		/*
		 * 如果要转成constant_score query, 在queryBuilder之上再套一层
		 */
		if (this.constantScore) {
			this.builder = QueryBuilders.constantScoreQuery(this.builder);
		}
		
		SearchRequestBuilder builder = ElasticUtils.client.prepareSearch(indices)
				.setQuery(this.builder)
				.setFetchSource(fetchSource);
		sortOrders.forEach(sortOrder -> sortOrder.addTo(builder));
		
		if (from != null) {
			builder.setFrom(from);
		}
		if (size != null) {
			builder.setSize(size);
		}
		
		log.debug(builder.toString());
		SearchResponse response = builder.get();
		SearchHits searchHits = response.getHits();
		return searchHits.getHits();
	}
}
