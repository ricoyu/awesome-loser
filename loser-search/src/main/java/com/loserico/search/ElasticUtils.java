package com.loserico.search;

import com.loserico.common.lang.context.ThreadContext;
import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.enums.HttpMethod;
import com.loserico.networking.utils.HttpUtils;
import com.loserico.search.builder.ElasticContextSuggestBuilder;
import com.loserico.search.builder.ElasticMultiGetBuilder;
import com.loserico.search.builder.ElasticQueryBuilder;
import com.loserico.search.builder.ElasticSuggestBuilder;
import com.loserico.search.builder.ElasticUpdateBuilder;
import com.loserico.search.builder.admin.ClusterSettingBuilder;
import com.loserico.search.builder.admin.ElasticIndexBuilder;
import com.loserico.search.builder.admin.ElasticIndexTemplateBuilder;
import com.loserico.search.builder.admin.ElasticPutMappingBuilder;
import com.loserico.search.builder.admin.ElasticReindexBuilder;
import com.loserico.search.builder.admin.ElasticSettingsBuilder;
import com.loserico.search.builder.admin.ElasticUpdateSettingBuilder;
import com.loserico.search.builder.agg.ElasticAvgAggregationBuilder;
import com.loserico.search.builder.agg.ElasticCardinalityAggregationBuilder;
import com.loserico.search.builder.agg.ElasticCompositeAggregationBuilder;
import com.loserico.search.builder.agg.ElasticDateHistogramAggregationBuilder;
import com.loserico.search.builder.agg.ElasticHistogramAggregationBuilder;
import com.loserico.search.builder.agg.ElasticMaxAggregationBuilder;
import com.loserico.search.builder.agg.ElasticMinAggregationBuilder;
import com.loserico.search.builder.agg.ElasticMultiTermsAggregationBuilder;
import com.loserico.search.builder.agg.ElasticSumAggregationBuilder;
import com.loserico.search.builder.agg.ElasticTermsAggregationBuilder;
import com.loserico.search.builder.query.ElasticBoolQueryBuilder;
import com.loserico.search.builder.query.ElasticExistsQueryBuilder;
import com.loserico.search.builder.query.ElasticIdsQueryBuilder;
import com.loserico.search.builder.query.ElasticMatchAllQueryBuilder;
import com.loserico.search.builder.query.ElasticMatchPhraseQueryBuilder;
import com.loserico.search.builder.query.ElasticMatchQueryBuilder;
import com.loserico.search.builder.query.ElasticQueryStringBuilder;
import com.loserico.search.builder.query.ElasticTemplateQueryBuilder;
import com.loserico.search.builder.query.ElasticTermQueryBuilder;
import com.loserico.search.builder.query.ElasticTermsQueryBuilder;
import com.loserico.search.builder.query.ElasticUriQueryBuilder;
import com.loserico.search.cache.ElasticCacheUtils;
import com.loserico.search.constants.ElasticConstants;
import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.exception.IndexTemplateCreateException;
import com.loserico.search.factory.TransportClientFactory;
import com.loserico.search.support.BulkResult;
import com.loserico.search.support.IndexSupport;
import com.loserico.search.support.MappingSupport;
import com.loserico.search.support.RestSupport;
import com.loserico.search.support.SettingsSupport;
import com.loserico.search.support.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction.AnalyzeToken;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction.Response;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.forcemerge.ForceMergeResponse;
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
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.metadata.IndexTemplateMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.bytes.BytesArray;
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
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.loserico.common.lang.utils.Assert.notNull;
import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.json.jackson.JacksonUtils.toObject;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
	 * 只是初始化一下ES客户端连接
	 */
	public static void ping() {
		Admin.existsIndex("ricoyu");
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
	public static String index(String index, String doc, int id) {
		return index(index, doc, String.valueOf(id));
	}
	
	/**
	 * 创建一个新的文档, 返回新创建文档的ID
	 * 对应REST API POST 方式
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public static String index(String index, Object doc, int id) {
		return index(index, doc, String.valueOf(id));
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
	 * 批量创建文档<p>
	 * 返回创建结果, 包括成功数量, 失败数量, 失败消息, 成功创建的文档id列表<p>
	 * 单个bulk请求体的数据量不要太大, 官方建议大于5~15mb
	 * @param index
	 * @return ElasticBulkIndexBuilder
	 */
	public static ElasticBulkIndexBuilder bulkIndex(String index) {
		return new ElasticBulkIndexBuilder(index);
	}
	
	/**
	 * 批量创建文档<p>
	 * 返回创建结果, 包括成功数量, 失败数量, 失败消息, 成功创建的文档id列表<p>
	 * 单个bulk请求体的数据量不要太大, 官方建议大于5~15mb
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
	 * 批量创建文档<p>
	 * 返回创建结果, 包括成功数量, 失败数量, 失败消息, 成功创建的文档id列表<p>
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
	 * 批量更新
	 * @return ElasticBulkUpdateBuilder
	 */
	public static ElasticBulkUpdateBuilder bulkUpdate() {
		return new ElasticBulkUpdateBuilder();
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
	 * @return ElasticUpdateBuilder
	 */
	public static ElasticUpdateBuilder update(String index) {
		return new ElasticUpdateBuilder(index);
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
		UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate(index, ONLY_TYPE, id).setDoc(doc, XContentType.JSON);
		updateRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
		UpdateResponse response = updateRequestBuilder.get();
		return UpdateResult.from(response);
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
	public static UpdateResult update(String index, String id, Map<String, Object> doc) {
		return update(index, id, toJson(doc));
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
	 * <li/>Weight             为每一个文档设置一个简单而不被规范化的权重
	 * <li/>Field Value Factor 使用该数值来修改_score, 例如将"热度"和"点赞数"作为算分的参考因素
	 * <li/>Random Score       为每一个用户使用一个不同的, 随机算分结果
	 * <li/>衰减函数            以某个字段的值为标准, 距离某个值越近, 得分越高
	 * <li/>Script Score       自定义脚本完全控制所需逻辑
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
	
	/**
	 * 用指定分词器分析文本
	 *
	 * @param analyzer
	 * @param texts
	 * @return List<String> 分析后的文本
	 */
	public static List<String> analyze(Analyzer analyzer, String... texts) {
		if (texts == null || texts.length == 0) {
			return Collections.emptyList();
		}
		
		if (analyzer == null) {
			throw new IllegalArgumentException("analyzer cannot be null");
		}
		
		AnalyzeRequestBuilder analyzeRequestBuilder = new AnalyzeRequestBuilder(client, AnalyzeAction.INSTANCE)
				.setText(texts)
				.setAnalyzer(analyzer.toString());
		Response response = analyzeRequestBuilder.get();
		
		return response.getTokens()
				.stream()
				.map(AnalyzeToken::getTerm)
				.distinct()
				.collect(toList());
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
	 * Elasticsearch 索引管理相关API
	 */
	public static class Admin {
		
		/**
		 * 基于Entity上的注解信息创建索引    
		 *
		 * @param entityClass 标注了@Index注解的POJO
		 * @return boolean 索引是否创建成功
		 */
		public static boolean createIndex(Class entityClass) {
			return createIndex(entityClass, null);
		}
		
		/**
		 * 基于Entity上的注解信息创建索引
		 *
		 * @param entityClass 标注了@Index注解的POJO
		 * @param index       显式指定的索引名
		 * @return boolean 索引是否创建成功
		 */
		public static boolean createIndex(Class entityClass, String index) {
			notNull(entityClass, "entityClass cannot be null!");
			//抽取索引的Mapping信息
			ElasticPutMappingBuilder putMappingBuilder = MappingSupport.extractIndexMapping(entityClass);
			//抽取索引的Setting信息
			ElasticSettingsBuilder settingsBuilder = SettingsSupport.extractIndexSettings(entityClass);
			//抽取索引名
			if (isBlank(index)) {
				index = IndexSupport.indexName(entityClass);
			}
			
			IndicesAdminClient indicesAdminClient = client.admin().indices();
			CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(index);
			ElasticIndexBuilder indexBuilder = new ElasticIndexBuilder(createIndexRequestBuilder);
			boolean created = indexBuilder.settings(settingsBuilder)
					.mapping(putMappingBuilder)
					.create();
			log.info("Index {} {}", index, (created ? "created" : "not created"));
			return created;
		}
		
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
		 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/indices-templates.html
		 *
		 * @param templateName
		 */
		public static ElasticIndexTemplateBuilder putIndexTemplateByFile(String templateName) {
			return ElasticIndexTemplateBuilder.newInstance(client, templateName);
		}
		
		/**
		 * 从classpath读取指定的Index Template文件, 然后创建Index Template
		 *
		 * @param templateName
		 * @param templateFileName
		 * @return boolean
		 */
		public static boolean putIndexTemplateByFile(String templateName, String templateFileName) {
			return putIndexTemplate(templateName, IOUtils.readClassPathFileAsString(templateFileName));
		}
		
		/**
		 * 从classpath读取指定的Index Template文件, 然后创建Index Template
		 *
		 * @param templateName
		 * @param templateContent
		 * @return boolean
		 */
		public static boolean putIndexTemplate(String templateName, String templateContent) {
			String result = HttpUtils.post(RestSupport.HOST + "/_template/" + templateName)
					.body(templateContent)
					.method(HttpMethod.PUT)
					.request();
			
			boolean hasError = JsonPathUtils.ifExists(result, "$.error");
			if (hasError) {
				String errors = JsonPathUtils.readNode(result, "$.error.caused_by.reason");
				log.error("PUT index template failed, [{}]", errors);
				throw new IndexTemplateCreateException(errors);
			}
			
			Boolean acknowledged = JsonPathUtils.readNode(result, "$.acknowledged");
			log.info("acknowledged: {}", acknowledged);
			return acknowledged;
		}
		
		/**
		 * 获取指定的Index Template
		 *
		 * @param templateName
		 * @return IndexTemplateMetaData
		 */
		public static Map<String, IndexTemplateMetaData> getIndexTemplate(String templateName) {
			GetIndexTemplatesResponse response = client.admin().indices().prepareGetTemplates(templateName).get();
			List<IndexTemplateMetaData> indexTemplates = response.getIndexTemplates();
			return indexTemplates.stream()
					.collect(toMap(IndexTemplateMetaData::getName, identity()));
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
		
		/**
		 * 创建 Search Template
		 *
		 * @param templateName
		 * @return boolean
		 */
		public static boolean createSearchTemplate(String templateName, String templateFileName) {
			String templateContent = IOUtils.readClassPathFileAsString(templateFileName);
			
			Map<String, Object> rootNode = new HashMap<>();
			Map<String, Object> scriptNode = new HashMap<>();
			rootNode.put("script", scriptNode);
			scriptNode.put("lang", "mustache");
			scriptNode.put("source", templateContent);
			
			AcknowledgedResponse response = client.admin().cluster().preparePutStoredScript()
					.setId(templateName)
					.setContent(new BytesArray(toJson(rootNode)), XContentType.JSON)
					.get();
			return response.isAcknowledged();
		}
		
		/**
		 * 删除 Search Template
		 *
		 * @param templateName
		 * @return boolean
		 */
		public static boolean deleteSearchTemplate(String templateName) {
			AcknowledgedResponse response = client.admin().cluster().prepareDeleteStoredScript(templateName).get();
			return response.isAcknowledged();
		}
		
		/**
		 * 将索引设为只读, 不再写入的索引设为只读后, 可以提升索引的读性能
		 *
		 * @param indices
		 * @return boolean
		 */
		public boolean setReadOnly(String... indices) {
			AcknowledgedResponse response = ElasticUtils.client.admin().indices()
					.prepareUpdateSettings(indices)
					.setSettings(org.elasticsearch.common.settings.Settings.builder().put("blocks.read_only", true))
					.get();
			return response.isAcknowledged();
		}
		
		/**
		 * 执行段合并, 可以先设为只读, 然后进行段合并
		 *
		 * @param indices
		 * @return ActionFuture<ForceMergeResponse>
		 */
		public ActionFuture<ForceMergeResponse> forceMerge(String indices) {
			return ElasticUtils.client.admin().indices()
					.prepareForceMerge(indices)
					.setMaxNumSegments(1)
					.execute();
		}
	}
	
	/**
	 * Elasticsearch Mapping 相关API
	 */
	public static class Mappings {
		
		/**
		 * 获取所有的Mapping信息
		 * 返回的Map是Map套Map, 输出成JSON大概是这样子
		 * <pre> {@code
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
		 * }</pre>
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
			return new ElasticPutMappingBuilder(index, dynamic);
		}
	}
	
	/**
	 * Elasticsearch Settings 相关 API
	 */
	public static class Settings {
		
		/**
		 * 更新索引的Settings
		 *
		 * @param indices
		 * @return ElasticUpdateSettingBuilder
		 */
		public static ElasticUpdateSettingBuilder update(String... indices) {
			return new ElasticUpdateSettingBuilder(indices);
		}
	}
	
	/**
	 * Elasticsearch 集群相关 API
	 */
	public static class Cluster {
		
		/**
		 * 获取集群的健康状态, Green Yellow Red
		 *
		 * @param index
		 * @return String
		 */
		public static String health(String index) {
			ClusterHealthResponse response = client.admin().cluster().prepareHealth().get();
			ClusterHealthStatus status = response.getStatus();
			return status.toString();
		}
		
		public static ClusterSettingBuilder settings() {
			return new ClusterSettingBuilder();
		}
		
		
		/**
		 * 创建多字段聚合, 用法:
		 * <pre> {@code
		 * Map<String, Object> params = new HashMap<>();
		 * params.put("fields", new String[]{"src_ip", "src_port"});
		 * 
		 * Script painless = new Script(ScriptType.STORED, null, "multi_field_agg", params);
		 * SearchResponse response = ElasticUtils.client.prepareSearch("events")
		 * 		.addAggregation(AggregationBuilders.terms("script_agg").script(painless))
		 * 		.get();
		 * Aggregations aggregations = response.getAggregations();
		 * Aggregation scriptAgg = aggregations.get("script_agg");
		 * System.out.println(JacksonUtils.toPrettyJson(scriptAgg.toString()));
		 * }</pre>
		 * 
		 * @return
		 */
		public static boolean createMultiFieldAgg() {
			String script = "{  \"script\": { " +
					"\"lang\": \"painless\", " +
					"\"source\": " +
					"\"String fieldName = ''; " +
					"for(int i=0; i<params.fields.length; i++) {" +
						"String field = params.fields[i];" +
						"if(!''.equals(fieldName) && (doc.containsKey(field+'.keyword') || doc.containsKey(field))) {" +
							"fieldName +='|';" +
						"}" +
						"if(doc.containsKey(field+'.keyword')) {" +
							"if(doc[field+'.keyword'].size() != 0) {" +
								"fieldName += doc[field+'.keyword'].value;" +
							"} else {" +
								"fieldName += 'null';" +
							"}" +
						"} else if(doc.containsKey(field)){" +
							"if(doc[field].size() != 0) {" +
								"fieldName += doc[field].value;" +
							"} else {" +
								"fieldName += 'null';"+
							"}" +
						"}" +
					"}" +
					"return fieldName;\" }}";
			
			AcknowledgedResponse response = client.admin().cluster().preparePutStoredScript()
					.setId("multi_fields")
					.setContent(new BytesArray(script), XContentType.JSON)
					.get();
			
			return response.isAcknowledged();
		}
	}
	
	/**
	 * <p>
	 * Copyright: Copyright (c) 2021-04-28 18:21
	 * <p>
	 * Company: Sexy Uncle Inc.
	 * <p>
	 *
	 * @author Rico Yu  ricoyu520@gmail.com
	 * @version 1.0
	 */
	public static final class Query {
		
		/**
		 * 根据ID获取文档
		 *
		 * @param index
		 * @param id
		 * @return String
		 */
		public static String byId(String index, Object id) {
			Objects.requireNonNull(index, "index cannot be null!");
			Objects.requireNonNull(id, "id cannot be null!");
			GetResponse response = client.prepareGet(index, ONLY_TYPE, id.toString()).get();
			return response.getSourceAsString();
		}
		
		/**
		 * 根据ID获取文档
		 *
		 * @param index
		 * @param id
		 * @return String
		 */
		public static <T> T byId(String index, Object id, Class<T> resultType) {
			Objects.requireNonNull(index, "index cannot be null!");
			Objects.requireNonNull(id, "id cannot be null!");
			Objects.requireNonNull(resultType, "clazz cannot be null!");
			GetResponse response = client.prepareGet(index, ONLY_TYPE, id.toString()).get();
			String source = response.getSourceAsString();
			T obj = toObject(source, resultType);
			Field idField = ElasticCacheUtils.idField(resultType);
			ReflectionUtils.setField(idField, obj, Transformers.convert(id, idField.getType()));
			
			return obj;
		}
		
		/**
		 * 基于ID列表获取
		 * @param indices
		 * @return ElasticIdsQueryBuilder
		 */
		public static ElasticIdsQueryBuilder idsQuery(String... indices) {
			return new ElasticIdsQueryBuilder(indices);
		}
		
		/**
		 * 基于ID列表获取
		 * @param indices
		 * @return ElasticIdsQueryBuilder
		 */
		public static ElasticIdsQueryBuilder idsQuery(Collection<String> indices) {
			return new ElasticIdsQueryBuilder(indices.stream().toArray(String[]::new));
		}
		
		/**
		 * 指定查询语句, 使用Query String Syntax<p>
		 * 有多种查询语法
		 * <ul>
		 * <li/>df查询                            GET movies/_search?q=2012&df=title
		 * <li/>指定字段查询                       GET movies/_search?q=title:2012
		 * <li/>泛查询                            GET movies/_search?q=2012              会对文档中所有字段进行查询
		 * <li/>Term Query                       GET movies/_search?q=title:Beautiful Mind     与下面的等价
		 * <li/>                                 GET movies/_search?q=title:Beautiful OR Mind
		 * <li/>Pahrase Query(引号引起来的)        GET movies/_search?q=title:"Beautiful Mind"  表示Beautiful Mind要同时出现并且按照规定的顺序, 与下面的等价
		 * <li/>                                  GET movies/_search?q=title:Beautiful AND Mind
		 * <li/>分组                              GET movies/_search?q=title:(Beautiful Mind)
		 * <li/>包含Beautiful 不包含 Mind          GET movies/_search?q=title:(Beautiful NOT Mind)
		 * <li/>必须包含Mind, %2B是 + 号的转义字符  GET movies/_search?q=title:(Beautiful %2BMind)
		 * <li/>范围查询                          GET movies/_search?q=year:>1980
		 * </ul>
		 * 你可以就写查询条件: 2012<p>
		 * 也可以写完成的查询: q=2012 或者 q=2012&df=title 等<p>
		 *
		 * @param index
		 * @return QueryStringQueryBuilder
		 */
		public static ElasticUriQueryBuilder uriQuery(String index) {
			return new ElasticUriQueryBuilder(index);
		}
		
		/**
		 * 是Match Query的一种
		 *
		 * @param index
		 * @return QueryStringBuilder
		 */
		public static ElasticQueryStringBuilder queryString(String index) {
			return new ElasticQueryStringBuilder(index);
		}
		
		/**
		 * Match Query是会对搜索的内容做分词后再去ES中查询的
		 *
		 * @param indices
		 * @return ElasticMatchQueryBuilder
		 */
		public static ElasticMatchQueryBuilder matchQuery(String... indices) {
			return new ElasticMatchQueryBuilder(indices);
		}
		
		/**
		 * Match All Query
		 *
		 * @param indices
		 * @return ElasticMatchAllQueryBuilder
		 */
		public static ElasticMatchAllQueryBuilder matchAllQuery(String... indices) {
			return new ElasticMatchAllQueryBuilder(indices);
		}
		
		/**
		 * 在ES中, Term查询, 对输入不做分词. 会将输入作为一个整体, 在倒排索引中查找准确的词项, 并且使用相关度计算公式为每个包含该此项的文档进行相关度算分<p>
		 * <ol>
		 * <li/> Term Query 不会对查询条件做分词
		 * <li/> 如果被查询字段在文档里面是被分词的, 但是又想用Term Query对其做精确匹配, 那么可以使用Elasticsearch提供的多字段特性, 查询其keyword字段, 如productID.keyword
		 * <li/> Term Query 会算分
		 * <li/> 可以通过 Constant Score 将查询转换成一个 Filtering, 避免算分, 并利用缓存, 提高性能
		 * <li/> Avoid using the term query for text fields.
		 * </ol>
		 *
		 * @param indices
		 * @return ElasticTermQueryBuilder
		 */
		public static ElasticTermQueryBuilder termQuery(String... indices) {
			return new ElasticTermQueryBuilder(indices);
		}
		
		/**
		 * 在ES中, Term查询, 对输入不做分词. 会将输入作为一个整体, 在倒排索引中查找准确的词项, 并且使用相关度计算公式为每个包含该此项的文档进行相关度算分<p>
		 * <ol>
		 * <li/> Term Query 不会对查询条件做分词
		 * <li/> 如果被查询字段在文档里面是被分词的, 但是又想用Term Query对其做精确匹配, 那么可以使用Elasticsearch提供的多字段特性, 查询其keyword字段, 如productID.keyword
		 * <li/> Term Query 会算分
		 * <li/> 可以通过 Constant Score 将查询转换成一个 Filtering, 避免算分, 并利用缓存, 提高性能
		 * <li/> Avoid using the term query for text fields.
		 * </ol>
		 *
		 * @param indices
		 * @return ElasticTermsQueryBuilder
		 */
		public static ElasticTermsQueryBuilder termsQuery(String... indices) {
			return new ElasticTermsQueryBuilder(indices);
		}
		
		/**
		 * Match Phrase Query查的是一个短语, 比如查title="one love", 那么title是"the one love"可以搜到, "one I love"搜不到
		 *
		 * @param indices
		 * @return
		 */
		public static ElasticMatchPhraseQueryBuilder matchPhraseQuery(String... indices) {
			return new ElasticMatchPhraseQueryBuilder(indices);
		}
		
		/**
		 * exists Query
		 *
		 * @param indices
		 * @return ElasticExistsQueryBuilder
		 */
		public static ElasticExistsQueryBuilder exists(String... indices) {
			return new ElasticExistsQueryBuilder(indices);
		}
		
		/**
		 * Range Query, 支持日期, 数字类型
		 *
		 * @param indices
		 * @return ElasticRangeQueryBuilder
		 */
		public static ElasticRangeQueryBuilder range(String... indices) {
			return new ElasticRangeQueryBuilder(indices);
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
		 * Multi Match Query 跨字段搜索
		 *
		 * <pre> {@code
		 * POST blogs/_search
		 * {
		 *   "query": {
		 *     "multi_match": {
		 *       "query": "Quick pets",
		 *       "type": "best_fields",
		 *       "fields": ["title", "body"],
		 *       "tie_breaker": 0.2,
		 *       "minimum_should_match": "20%"
		 *     }
		 *   }
		 * }
		 * }</pre>
		 *
		 * <ul>
		 *     <li/>multi_match 声明这是一个Multi Match Query
		 *     <li/>query       提供一个查询的语句
		 *     <li/>fields      查询语句要匹配到哪些字段上
		 *     <li/>type        best_fields 默认类型, 可以不指定. 表示会在fields指定的字段中取一个评分最高的作为一个返回结果
		 * </ul>
		 *
		 * @param indices
		 */
		public static void multiMatch(String... indices) {
			
		}
		
		/**
		 * 布尔/组合 查询
		 *
		 * @param indices
		 * @return ElasticBoolQueryBuilder
		 */
		public static ElasticBoolQueryBuilder bool(String... indices) {
			return new ElasticBoolQueryBuilder(indices);
		}
		
		/**
		 * Search Template Query
		 *
		 * @param indices
		 * @return ElasticTemplateQueryBuilder
		 */
		public static ElasticTemplateQueryBuilder templateQuery(String... indices) {
			return new ElasticTemplateQueryBuilder(indices);
		}
	}
	
	/**
	 * 聚合查询相关API<p/>
	 * 聚合分两大类
	 * <ol>
	 *     <li/>Bucket 聚合<br/>
	 *          按照一定的规则, 将文档分配到不同的桶中, 从而达到分类的目的. ES提供了一些常见的Bucket Aggregation
	 *          <ul>
	 *              <li/>Terms
	 *              <li/>Range / Date Range
	 *              <li/>Histogram / Date Histogram
	 *              <li/>支持嵌套, 也就是在桶里再做分桶
	 *          </ul>
	 *     <li/>Metric 聚合<br/>
	 *          主要是对数据做一些统计分析, 分为两大类
	 *          <ul>
	 *              <li/>单值分析: 只输出一个统计结果
	 *              <ul>
	 *                  <li/>min max avg sum
	 *                  <li/>Cardinality(类似distinct count)
	 *              </ul>
	 *              <li/>多值分析:输出多个分析结果
	 *              <ul>
	 *                  <li/>stats, extended stats
	 *                  <li/>percentile, percentile rank
	 *                  <li/>top_hits(排在前面的示例)
	 *              </ul>
	 *          </ul>
	 * </ol>
	 */
	public static class Aggs {
		
		/**
		 * 返回总命中数
		 * @return Long
		 */
		public static Long totalHits() {
			return ThreadContext.get(ElasticConstants.TOTAL_HITS);
		}
		
		// ---------------------- Bucket 聚合 ----------------------
		
		/**
		 * terms聚合, Bucket聚合的一种
		 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/7.x/java-aggs.html
		 *
		 * @param indices
		 * @return ElasticTermsAggregationBuilder
		 */
		public static ElasticTermsAggregationBuilder terms(String... indices) {
			return ElasticTermsAggregationBuilder.instance(indices);
		}
		
		/**
		 * multi terms聚合, Bucket聚合的一种, 使用painless script实现
		 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/7.x/java-aggs.html
		 *
		 * @param indices
		 * @return ElasticTermsAggregationBuilder
		 */
		public static ElasticMultiTermsAggregationBuilder multiTerms(String... indices) {
			return ElasticMultiTermsAggregationBuilder.instance(indices);
		}
		
		/**
		 * Histogram Aggregation
		 * <p>
		 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-bucket-histogram-aggregation.html
		 *
		 * @param indices
		 * @return ElasticHistogramAggregationBuilder
		 */
		public static ElasticHistogramAggregationBuilder histogram(String... indices) {
			return ElasticHistogramAggregationBuilder.instance(indices);
		}
		
		/**
		 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-bucket-datehistogram-aggregation.html
		 *
		 * @param indices
		 * @return ElasticDateHistogramAggregationBuilder
		 */
		public static ElasticDateHistogramAggregationBuilder dateHistogram(String... indices) {
			return ElasticDateHistogramAggregationBuilder.instance(indices);
		}
		
		// ---------------------- Metric 聚合 ----------------------
		
		/**
		 * min聚合, Metric聚合的一种
		 *
		 * @param indices
		 * @return ElasticMinAggregationBuilder
		 */
		public static ElasticMinAggregationBuilder min(String... indices) {
			return ElasticMinAggregationBuilder.instance(indices);
		}
		
		/**
		 * max聚合, Bucket聚合的一种
		 *
		 * @param indices
		 * @return ElasticMaxAggregationBuilder
		 */
		public static ElasticMaxAggregationBuilder max(String... indices) {
			return ElasticMaxAggregationBuilder.instance(indices);
		}
		
		/**
		 * avg聚合
		 *
		 * @param indices
		 * @return ElasticMaxAggregationBuilder
		 */
		public static ElasticAvgAggregationBuilder avg(String... indices) {
			return ElasticAvgAggregationBuilder.instance(indices);
		}
		
		/**
		 * sum聚合
		 *
		 * @param indices
		 * @return ElasticSumAggregationBuilder
		 */
		public static ElasticSumAggregationBuilder sum(String... indices) {
			return ElasticSumAggregationBuilder.instance(indices);
		}
		
		/**
		 * Cardinality聚合, 对字段去重后统计数量 <br/>
		 * 比如你想通过聚合分析知道, 每天网站中的访客来自多少个不同的IP
		 * <p>
		 *
		 * @param indices
		 * @return ElasticCardinalityAggregationBuilder
		 */
		public static ElasticCardinalityAggregationBuilder cardinality(String... indices) {
			return ElasticCardinalityAggregationBuilder.instance(indices);
		}
		
		/**
		 * 组合多个聚合
		 *
		 * @param indices
		 * @return ElasticCompositeAggregationBuilder
		 */
		public static ElasticCompositeAggregationBuilder composite(String... indices) {
			return ElasticCompositeAggregationBuilder.instance(indices);
		}
	}
	
	
	protected static void logDsl(SearchRequestBuilder builder) {
		if (log.isDebugEnabled()) {
			log.debug("Query DSL:\n{}", new JSONObject(builder.toString()).toString(2));
		}
	}
	
	protected static void logDsl(UpdateRequestBuilder builder) {
		if (log.isDebugEnabled()) {
			log.debug("Update DSL:\n{}", new JSONObject(builder.toString()).toString(2));
		}
	}
}
