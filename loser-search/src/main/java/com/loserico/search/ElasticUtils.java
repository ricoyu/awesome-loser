package com.loserico.search;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.builder.ElasticAggregationBuilder;
import com.loserico.search.builder.ElasticContextSuggestBuilder;
import com.loserico.search.builder.ElasticIndexBuilder;
import com.loserico.search.builder.ElasticIndexTemplateBuilder;
import com.loserico.search.builder.ElasticMultiGetBuilder;
import com.loserico.search.builder.ElasticPutMappingBuilder;
import com.loserico.search.builder.ElasticQueryBuilder;
import com.loserico.search.builder.ElasticReindexBuilder;
import com.loserico.search.builder.ElasticSuggestBuilder;
import com.loserico.search.cache.ElasticCacheUtils;
import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.exception.AnalyzeException;
import com.loserico.search.factory.TransportClientFactory;
import com.loserico.search.support.BulkResult;
import com.loserico.search.support.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsAction;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexTemplateMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.indices.IndexTemplateMissingException;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.json.jackson.JacksonUtils.toObject;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * Elasticsearch 的工具类, 开箱即用的ES操作
 * <p>
 * Copyright: (C), 2021-01-01 8:37
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ElasticUtils {
	
	/**
	 * 唯一的一个type是_doc
	 */
	public static final String ONLY_TYPE = "_doc";
	
	public static final TransportClient client = TransportClientFactory.create();
	
	/**
	 * 创建索引, 默认1个分片, 0个副本
	 *
	 * @param index 索引名
	 * @return boolean 创建成功失败标识
	 */
	public static ElasticIndexBuilder createIndex(String index) {
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(index);
		return new ElasticIndexBuilder(createIndexRequestBuilder);
	}
	
	/**
	 * 判断索引存在与否
	 *
	 * @param indices
	 * @return boolean
	 */
	public static boolean existsIndex(String... indices) {
		IndicesExistsResponse response = client.admin().indices().prepareExists(indices).get();
		return response.isExists();
	}
	
	/**
	 * 删除索引
	 *
	 * @param indices
	 * @return boolean 删除成功与否
	 */
	public static boolean deleteIndex(String... indices) {
		if (!existsIndex(indices)) {
			log.info("索引{}不存在", indices);
			return false;
		}
		AcknowledgedResponse response = client.admin()
				.indices()
				.prepareDelete(indices)
				.get();
		return response.isAcknowledged();
	}
	
	/**
	 * 列出所有索引
	 *
	 * @return List<String>
	 */
	public static List<String> listIndices() {
		IndicesStatsResponse response = client.admin()
				.indices()
				.stats(new IndicesStatsRequest().all())
				.actionGet();
		
		Set<String> indices = response.getIndices().keySet();
		return new ArrayList<>(indices);
	}
	
	/**
	 * 为Index创建别名
	 *
	 * @param index
	 * @param alias
	 * @return 创建成功与否
	 */
	public static boolean createIndexAlias(String index, String alias) {
		IndicesAliasesRequestBuilder builder = client.admin().indices().prepareAliases().addAlias(index, alias);
		AcknowledgedResponse response = builder.get();
		return response.isAcknowledged();
	}
	
	/**
	 * 为Index创建别名
	 *
	 * @param indices
	 * @param alias
	 * @param queryBuilder
	 * @return
	 */
	public static boolean createIndexAlias(String[] indices, String alias, QueryBuilder queryBuilder) {
		IndicesAliasesRequestBuilder builder = client.admin()
				.indices()
				.prepareAliases()
				.addAlias(indices, alias, queryBuilder);
		AcknowledgedResponse response = builder.get();
		return response.isAcknowledged();
	}
	
	/**
	 * 删除Index的别名
	 *
	 * @param index
	 * @param alias
	 * @return 删除成功与否
	 */
	public static boolean deleteIndexAlias(String index, String alias) {
		AcknowledgedResponse response = client.admin()
				.indices()
				.prepareAliases()
				.removeAlias(index, alias)
				.get();
		return response.isAcknowledged();
	}
	
	/**
	 * 创建一个新的文档, 返回新创建文档的ID
	 * 对应REST API POST 方式
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public static String index(String index, String doc) {
		return index(index, doc, null);
	}
	
	/**
	 * 创建一个新的文档, 返回新创建文档的ID
	 * 对应REST API POST 方式
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public static String index(String index, Object doc) {
		if (doc == null) {
			return null;
		}
		
		String id = ElasticCacheUtils.getIdValue(doc);
		return index(index, doc, id);
	}
	
	/**
	 * 创建一个新的文档, 返回新创建文档的ID
	 * 对应REST API POST 方式
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public static String index(String index, String doc, String id) {
		Objects.requireNonNull(index, "index cannot be null!");
		if (doc == null) {
			return null;
		}
		IndexResponse response = client.prepareIndex(index, ONLY_TYPE, id)
				.setSource(doc, XContentType.JSON)
				.get();
		return response.getId();
	}
	
	/**
	 * 创建一个新的文档, 返回新创建文档的ID
	 * 对应REST API POST 方式
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public static String index(String index, Object doc, String id) {
		Objects.requireNonNull(index, "index cannot be null!");
		if (doc == null) {
			return null;
		}
		IndexResponse response = client.prepareIndex(index, ONLY_TYPE, id)
				.setSource(toJson(doc), XContentType.JSON)
				.get();
		return response.getId();
	}
	
	/**
	 * 批量创建文档
	 * 返回创建结果, 包括成功数量, 失败数量, 失败消息, 成功创建的文档id列表
	 *
	 * @param index
	 * @param docs
	 * @return BulkResult
	 */
	public static BulkResult bulkIndex(String index, String... docs) {
		//配置全局index和type
		BulkRequestBuilder bulkRequest = client.prepareBulk(index, ONLY_TYPE);
		asList(docs).forEach((doc) -> {
			bulkRequest.add(client.prepareIndex().setSource(doc, XContentType.JSON));
		});
		
		BulkResponse responses = bulkRequest.get();
		
		BulkResult bulkResult = new BulkResult();
		for (BulkItemResponse response : responses) {
			if (response.isFailed()) {
				//记录失败数
				bulkResult.fail();
				//记录失败message
				bulkResult.addFailMessage(response.getFailureMessage());
			} else {
				//记录成功数
				bulkResult.success();
				//记录生成的id
				bulkResult.addId(response.getId());
			}
		}
		
		return bulkResult;
	}
	
	/**
	 * 批量创建文档
	 * 返回创建结果, 包括成功数量, 失败数量, 失败消息, 成功创建的文档id列表
	 * 注意docs里面的pojo就算加了@DocId也不起作用, 批量只支持自动创建ID
	 *
	 * @param index
	 * @param docs
	 * @return BulkResult
	 */
	public static BulkResult bulkIndex(String index, List<?> docs) {
		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk(index, ONLY_TYPE);
		docs.stream()
				.filter(Objects::nonNull)
				.map((doc) -> {
					String id = ElasticCacheUtils.getIdValue(doc);
					return client.prepareIndex()
							.setSource(toJson(doc), XContentType.JSON)
							.setId(id);
				}).forEach(builder -> bulkRequestBuilder.add(builder));
		BulkResponse itemResponses = bulkRequestBuilder.get();
		BulkResult bulkResult = new BulkResult();
		
		for (Iterator<BulkItemResponse> iterator = itemResponses.iterator(); iterator.hasNext(); ) {
			BulkItemResponse itemResponse = iterator.next();
			if (itemResponse.isFailed()) {
				//记录失败数
				bulkResult.fail();
				//记录失败message
				bulkResult.addFailMessage(itemResponse.getFailureMessage());
			} else {
				bulkResult.success();
				bulkResult.addId(itemResponse.getId());
			}
		}
		
		return bulkResult;
	}
	
	/**
	 * 根据ID获取文档
	 *
	 * @param index 索引名
	 * @param id    文档id
	 * @return T
	 */
	public static String get(String index, String id) {
		Objects.requireNonNull(index, "索引名不能为null");
		Objects.requireNonNull(id, "id 不能为null");
		
		GetResponse response = client.prepareGet(index, ONLY_TYPE, id).get();
		return response.getSourceAsString();
	}
	
	/**
	 * 根据ID获取并转成指定类型对象
	 *
	 * @param index 索引名
	 * @param id    文档id
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	public static <T> T get(String index, String id, Class<T> clazz) {
		Objects.requireNonNull(index, "索引名不能为null");
		Objects.requireNonNull(id, "id 不能为null");
		Objects.requireNonNull(clazz, "clazz不能为null");
		
		GetResponse response = client.prepareGet(index, ONLY_TYPE, id).get();
		String source = response.getSourceAsString();
		return toObject(source, clazz);
	}
	
	/**
	 * 从一个或者多个索引中根据id获取多文档
	 *
	 * @return
	 */
	public static ElasticMultiGetBuilder mget() {
		return new ElasticMultiGetBuilder(client);
	}
	
	/**
	 * 删除一篇文档
	 *
	 * @param index
	 * @param id
	 * @return Result
	 */
	public static boolean delete(String index, String id) {
		DeleteResponse response = client.prepareDelete(index, ONLY_TYPE, id).get();
		return response.getResult() == DocWriteResponse.Result.DELETED;
	}
	
	/**
	 * 根据单个条件删除, 返回删除的记录数
	 * 条件匹配是按照精确匹配来处理的, 为了防止误删
	 *
	 * @param index
	 * @param field
	 * @param value
	 * @return long
	 */
	public static long deleteBy(String index, String field, String value) {
		BulkByScrollResponse response = new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE)
				.filter(QueryBuilders.termQuery(field, value))
				.source(index)
				.get();
		return response.getDeleted();
	}
	
	
	/**
	 * 更新文档的一部分
	 * <ol>
	 * <li/>如果ID对应的文档在ES中还不存在, 那么报错
	 * <li/>如果docPiece对应的字段在文档中还不存在, 那么在原文档中插入这个字段
	 * <li/>如果docPiece对应的字段在文档中存在, 并且值不一样, 那么执行更新
	 * <li/>docPiece对应的字段在文档中存在, 但是值是一样的, 那么不执行更新
	 * </ol>
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-update.html
	 *
	 * @param index
	 * @param id
	 * @param doc   整篇文档或者文档的一部分
	 * @return Result 更新结果(更新了? 没更新?)
	 */
	public static UpdateResult update(String index, String id, String doc) {
		UpdateResponse response = client.prepareUpdate(index, ONLY_TYPE, id)
				.setDoc(doc, XContentType.JSON)
				.get();
		return UpdateResult.from(response);
	}
	
	/**
	 * 更新文档的一部分, 或者ID对应的文档不存在时创建文档
	 * <ol>
	 * <li/>如果ID对应的文档在ES中还不存在, 那么创建文档
	 * <li/>如果docPiece对应的字段在文档中还不存在, 那么在原文档中插入这个字段
	 * <li/>如果docPiece对应的字段在文档中存在, 并且值不一样, 那么执行更新
	 * <li/>docPiece对应的字段在文档中存在, 但是值是一样的, 那么不执行更新
	 * </ol>
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/7.x/java-docs-update.html
	 *
	 * @param index
	 * @param id
	 * @param doc   整篇文档或者文档的一部分
	 * @return Result 创建? 更新? 没更新?
	 */
	public static UpdateResult upsert(String index, String id, String doc) {
		UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate(index, ONLY_TYPE, id);
		UpdateResponse updateResponse = updateRequestBuilder.setDoc(doc, XContentType.JSON)
				.setDocAsUpsert(true)
				.get();
		return UpdateResult.from(updateResponse);
	}
	
	/**
	 * 检查指定索引中是否存在指定id的文档
	 *
	 * @param index
	 * @param id
	 * @return boolean
	 */
	public static boolean exists(String index, String id) {
		GetResponse response = client.prepareGet(index, ONLY_TYPE, id)
				.setFetchSource(false)
				.get();
		return response.isExists();
	}
	
	/**
	 * 获取所有的Mapping信息
	 * 返回的Map是Map套Map, 输出成JSON大概是这样子
	 * <pre>
	 * {
	 *   "properties": {
	 *     "title": {
	 *       "type": "text",
	 *       "fields": {
	 *         "keyword": {
	 *           "type": "keyword",
	 *           "ignore_above": 256
	 *         }
	 *       }
	 *     },
	 *     "year": {
	 *       "type": "long"
	 *     }
	 *   }
	 * }
	 * </pre>
	 *
	 * @param index
	 * @return
	 */
	public static Map<String, Object> getMapping(String index) {
		GetMappingsResponse response = client.admin()
				.indices()
				.prepareGetMappings(index)
				.get();
		
		ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = response.mappings();
		if (mappings.isEmpty()) {
			return null;
		}
		//获取真正的mapping部分, 其他的都不需要
		MappingMetaData mappingMetaData = mappings.get(index).get(ONLY_TYPE);
		if (mappingMetaData != null) {
			return mappingMetaData.sourceAsMap();
		}
		
		return new HashMap<>(12);
	}
	
	/**
	 * 获取索引中某个字段的mapping定义, 返回的Map格式类似这样:
	 * <pre>
	 * {
	 *   "income": {
	 *     "type": "long",
	 *     "index": false
	 *   },
	 *   "carrer": {
	 *     "type": "text",
	 *     "analyzer": "ik_max_word",
	 *     "search_analyzer": "ik_smart"
	 *   },
	 *   "fans": {
	 *     "type": "text"
	 *   }
	 * }
	 * </pre>
	 *
	 * @param index
	 * @param fields
	 * @return Map<String, Object>
	 */
	public static Map<String, Map<String, Object>> getMapping(String index, String... fields) {
		GetFieldMappingsRequestBuilder builder = new GetFieldMappingsRequestBuilder(client, GetFieldMappingsAction.INSTANCE, index);
		GetFieldMappingsResponse response = builder.setFields(fields).get();
		
		Map<String, GetFieldMappingsResponse.FieldMappingMetaData> map = response.mappings().get(index).get(ONLY_TYPE);
		List<Map> mapList = map.values().stream().map(GetFieldMappingsResponse.FieldMappingMetaData::sourceAsMap).collect(toList());
		Map fieldMappingMap = new HashMap<>(mapList.size());
		for (Map<String, ?> fieldMap : mapList) {
			for (String key : fieldMap.keySet()) {
				fieldMappingMap.put(key, fieldMap.get(key));
			}
		}
		
		return fieldMappingMap;
	}
	
	/**
	 * 设置索引的Mapping, index必须先创建, 可以为index增加字段定义, 但是不能删除已有的字段定义<p/>
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/mapping.html<br/>
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/dynamic-mapping.html<br/>
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/mapping-params.html
	 *
	 * @param index
	 * @param dynamic
	 * @return boolean Mapping创建成功失败标识
	 */
	public static ElasticPutMappingBuilder putMapping(String index, Dynamic dynamic) {
		return new ElasticPutMappingBuilder(index,  dynamic);
	}
	
	/**
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/indices-templates.html
	 *
	 * @param templateName
	 */
	public static ElasticIndexTemplateBuilder putIndexTemplate(String templateName) {
		return ElasticIndexTemplateBuilder.newInstance(client, templateName);
	}
	
	/**
	 * 获取指定的Index Template
	 *
	 * @param templateName
	 * @return IndexTemplateMetaData
	 */
	public static IndexTemplateMetaData getIndexTemplate(String templateName) {
		GetIndexTemplatesResponse response = client.admin().indices().prepareGetTemplates(templateName).get();
		List<IndexTemplateMetaData> indexTemplates = response.getIndexTemplates();
		Optional<IndexTemplateMetaData> first = indexTemplates.stream()
				.filter(indexTemplate -> templateName.equals(indexTemplate.getName()))
				.findFirst();
		if (first.isPresent()) {
			IndexTemplateMetaData indexTemplateMetaData = first.get();
			return indexTemplateMetaData;
		}
		return null;
	}
	
	/**
	 * 删除Index Template
	 *
	 * @param templateName
	 * @return boolean
	 */
	public static boolean deleteIndexTemplate(String templateName) {
		try {
			AcknowledgedResponse response = client.admin().indices().prepareDeleteTemplate(templateName).get();
			return response.isAcknowledged();
		} catch (IndexTemplateMissingException e) {
			log.info("Index Template [{}] 不存在", templateName);
			return false;
		}
	}
	
	/**
	 * 通用查询接口, 既可以基于值查询, 如: Term Query, Match Query, Query string, Simple query string
	 * 可以基于字段存在性查询, 比如 Exists Query
	 * 给ElasticQueryBuilder传入不同的 QueryBuilder即可
	 *
	 * @param indices
	 * @return ElasticQueryBuilder
	 */
	public static ElasticQueryBuilder query(String... indices) {
		return ElasticQueryBuilder.instance(indices);
	}
	
	/**
	 * https://www.elastic.co/guide/cn/elasticsearch/guide/current/ignoring-tfidf.html
	 * <p>
	 * 即便是对Keyword 进行 Term 查询, 同样会被算分
	 * 可以将查询转为 Filtering, 取消相关性算分的环节, 以提升性能
	 * filter可以有效利用缓存
	 *
	 * @param indices
	 * @return
	 */
	public static ElasticQueryBuilder constantScoreQuery(String... indices) {
		ElasticQueryBuilder builder = ElasticQueryBuilder.instance(indices);
		builder.constantScore(true);
		return builder;
	}
	
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
	 * </ul>
	 * <p/>
	 * ScoreFunctionBuilder可以通过ScoreFunctionBuilders构造出来<p/>
	 * <p>
	 * random_score 一致性随机函数
	 *
	 * <ul>
	 * <li/>使用场景  网站的广告需要提高展现率
	 * <li/>具体需求  让每一个用户能看到不同的随机排名, 但是也希望同一个用户访问时, 结果的相对顺序保持一致(Constantly Random)
	 * </ul>
	 * <br/>
	 * 实际使用random_score时, 只要同一个人使用同一个seed就可以保证这个人的多次查询顺序是一致的
	 * <p>
	 * https://www.elastic.co/guide/cn/elasticsearch/guide/current/function-score-query.html
	 * https://www.elastic.co/guide/cn/elasticsearch/guide/current/random-scoring.html
	 *
	 * @param scoreFunctionBuilder
	 * @param indices
	 * @return ElasticQueryBuilder
	 */
	public static ElasticQueryBuilder functionScoreQuery(ScoreFunctionBuilder scoreFunctionBuilder, String... indices) {
		if (scoreFunctionBuilder == null) {
			throw new IllegalArgumentException("scoreFunctionBuilder can not be null!");
		}
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices can not be empty");
		}
		ElasticQueryBuilder builder = ElasticQueryBuilder.instance(indices);
		ReflectionUtils.setField("scoreFunctionBuilder", builder, scoreFunctionBuilder);
		return builder;
	}
	
	/**
	 * https://www.programcreek.com/java-api-examples/?api=org.elasticsearch.search.suggest.term.TermSuggestionBuilder
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-search.html#_requesting_suggestions
	 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html
	 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#context-suggester
	 *
	 * @param indices
	 */
	public static ElasticSuggestBuilder suggest(String... indices) {
		return new ElasticSuggestBuilder(indices);
	}
	
	/**
	 * Term Suggest, suggest mode 是POPULAR
	 *
	 * @param text
	 * @param field
	 * @param indices
	 * @return Set<String>
	 */
	public static Set<String> termSuggest(String text, String field, String... indices) {
		ElasticSuggestBuilder elasticSuggestBuilder = new ElasticSuggestBuilder(indices);
		
		TermSuggestionBuilder suggestionBuilder = SuggestBuilders.termSuggestion(field)
				.suggestMode(TermSuggestionBuilder.SuggestMode.POPULAR)
				.text(text);
		
		String suggestName = UUID.randomUUID().toString();
		return ElasticUtils.suggest(indices)
				.name(suggestName)
				.suggestionBuilder(suggestionBuilder)
				.suggest();
	}
	
	/**
	 * Phrase Suggestion
	 *
	 * @param text
	 * @param field
	 * @param indices
	 * @return Set<String>
	 */
	public static Set<String> phraseSuggest(String text, String field, String... indices) {
		ElasticSuggestBuilder elasticSuggestBuilder = new ElasticSuggestBuilder(indices);
		
		PhraseSuggestionBuilder suggestionBuilder = SuggestBuilders.phraseSuggestion(field)
				.text(text)
				.maxErrors(2f)
				.confidence(0)
				.highlight("<em>", "</em>");
		
		String suggestName = UUID.randomUUID().toString();
		return ElasticUtils.suggest(indices)
				.name(suggestName)
				.suggestionBuilder(suggestionBuilder)
				.suggest();
	}
	
	/**
	 * Completion Suggestion 只支持前缀匹配<p/>
	 * 对索引的Mapping有要求, 自动完成的字段, 其类型必须是completion
	 *
	 * @param prefix
	 * @param field
	 * @param indices
	 * @return Set<String>
	 */
	public static Set<String> completionSuggest(String prefix, String field, String... indices) {
		ElasticSuggestBuilder elasticSuggestBuilder = new ElasticSuggestBuilder(indices);
		
		CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders.completionSuggestion(field)
				.prefix(prefix);
		
		String suggestName = UUID.randomUUID().toString();
		return ElasticUtils.suggest(indices)
				.name(suggestName)
				.suggestionBuilder(suggestionBuilder)
				.suggest();
	}
	
	/**
	 * 基于上下文的自动完成
	 *
	 * @param indices
	 */
	public static ElasticContextSuggestBuilder contextSuggest(String... indices) {
		return new ElasticContextSuggestBuilder(indices);
	}
	
	public static List<String> analyze(Analyzer analyzer, String... texts) {
		if (texts == null || texts.length == 0) {
			return Collections.emptyList();
		}
		
		if (analyzer == null) {
			throw new IllegalArgumentException("analyzer cannot be null");
		}
		
		AnalyzeRequest analyzeRequest = new AnalyzeRequest().text(texts).analyzer(analyzer.toString());
		AnalyzeResponse analyzeTokens = null;
		try {
			analyzeTokens = client.admin().indices().analyze(analyzeRequest).get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("", e);
			throw new AnalyzeException(e);
		}
		return analyzeTokens.getTokens()
				.stream()
				.map(AnalyzeResponse.AnalyzeToken::getTerm)
				.distinct()
				.collect(toList());
	}
	
	/**
	 * 聚合
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/7.x/java-aggs.html
	 *
	 * @param indices
	 * @return ElasticAggregationBuilder
	 */
	public static ElasticAggregationBuilder agg(String... indices) {
		
		return null;
	}
	
	/**
	 * 在更新索引的mapping后, 在原索引上重建索引<p>
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-update-by-query.html
	 *
	 * @param indices
	 * @return BulkByScrollResponse
	 */
	public static BulkByScrollResponse updateByQuery(String... indices) {
		UpdateByQueryRequestBuilder updateByQuery = new UpdateByQueryRequestBuilder(client, UpdateByQueryAction.INSTANCE);
		updateByQuery.source(indices).abortOnVersionConflict(false);
		BulkByScrollResponse bulkByScrollResponse = updateByQuery.get();
		log.info(bulkByScrollResponse.toString());
		return bulkByScrollResponse;
	}
	
	/**
	 * 重建索引<p>
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-reindex.html
	 * https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-reindex.html
	 * 
	 * @param srcIndex
	 * @param destIndex
	 * @return ElasticReindexBuilder
	 */
	public static ElasticReindexBuilder reindex(String srcIndex, String destIndex) {
		return new ElasticReindexBuilder(srcIndex, destIndex);
	}
}
