package com.loserico.search.builder;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.cache.ElasticCacheUtils;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.exception.ElasticQueryException;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.elasticsearch.common.lucene.search.function.CombineFunction.MULTIPLY;

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
	 * Elasticsearch 默认会以文档的相关度算分进行排序<br/>
	 * 可以通过指定一个或多个字段进行排序<br/>
	 * 使用相关度算分(Score)排序, 不能满足某些特定的条件: 无法针对相关度, 对排序实现更多的控制<br/>
	 * <p/>
	 * 可以在查询结束后, 对每一个匹配的文档进行一系列的重新算分, 根据新生成的分数进行排序<br/>
	 * 提供了几种默认的计算分值的函数:
	 * <ul>
	 * <li/>Weight  为每一个文档设置一个简单而不被规范化的权重
	 * <li/>Field Value Factor 使用该数值来修改_score, 例如将"热度"和"点赞数"作为算分的参考因素
	 * <li/>Random Score 为每一个用户使用一个不同的, 随机算分结果
	 * <li/>衰减函数 以某个字段的值为标准, 距离某个值越近, 得分越高
	 * <li/>Script Score 自定义脚本完全控制所需逻辑
	 */
	private ScoreFunctionBuilder scoreFunctionBuilder;
	
	private CombineFunction boostMode = MULTIPLY;
	
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
	
	/**
	 * Function Score Query中用到
	 * <ul>
	 * <li/>Multiply(默认值)  算分与函数值的乘积
	 * <li/>Sum  算分与函数的和
	 * <li/>Min / Max   算分与函数取 最小/最大值
	 * <li/>Replace   使用函数值取代算分
	 * <ul/>
	 *
	 * @param boostMode
	 * @return
	 */
	public ElasticQueryBuilder boostMode(CombineFunction boostMode) {
		notNull(boostMode, "boostMode cannot be null!");
		this.boostMode = boostMode;
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
		
		//pojo是否标注了@DocId
		Field id = ElasticCacheUtils.idField(resultType);
		boolean hasDocId = id != null;
		//@DocId标注的字段是String类型
		boolean isStringId = hasDocId && id.getType() == String.class;
		
		return (List<T>) asList(hits).stream()
				.filter(Objects::nonNull)
				.map((hit) -> {
					//不需要转成POJO的情况
					if (resultType == null) {
						return hit.getSourceAsString();
					}
					
					String source = hit.getSourceAsString();
					if (isBlank(source)) {
						return null;
					}
					
					T obj = (T) JacksonUtils.toObject(source, resultType);
					if (hasDocId) {
						if (isStringId) {
							ReflectionUtils.setField(id, obj, hit.getId());
						} else {
							ReflectionUtils.setField(id, obj, Transformers.convert(hit.getId(), id.getType()));
						}
					}
					
					return obj;
				}).collect(toList());
	}
	
	/**
	 * 执行查询, 返回一条记录
	 *
	 * @param <T>
	 * @return T
	 */
	public <T> T queryForOne() {
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
	 *
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
		} else if (scoreFunctionBuilder != null) {
			/*
			 * Function Score Query 可以在查询结束后, 对每一个匹配的文档进行一系列的重新算分, 根据新生成的分数进行排序
			 */
			this.builder = QueryBuilders.functionScoreQuery(this.builder, scoreFunctionBuilder).boostMode(boostMode);
		}
		
		SearchRequestBuilder searchRequestBuilder = ElasticUtils.client.prepareSearch(indices)
				.setQuery(this.builder)
				.setFetchSource(fetchSource);
		sortOrders.forEach(sortOrder -> sortOrder.addTo(searchRequestBuilder));
		
		if (from != null) {
			searchRequestBuilder.setFrom(from);
		}
		if (size != null) {
			searchRequestBuilder.setSize(size);
		}
		
		log.debug("Query DSL:\n{}", new JSONObject(searchRequestBuilder.toString()).toString(2));
		SearchResponse response = searchRequestBuilder.get();
		SearchHits searchHits = response.getHits();
		return searchHits.getHits();
	}
	
	private static void notNull(Object obj, String msg) {
		if (obj == null) {
			throw new IllegalArgumentException(msg);
		}
	}
}
