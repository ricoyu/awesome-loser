package com.loserico.search;

import com.loserico.search.builder.ElasticQueryBuilder;
import com.loserico.search.builder.IndexBuilder;
import com.loserico.search.builder.IndexTemplateBuilder;
import com.loserico.search.builder.MappingBuilder;
import com.loserico.search.builder.MultiGetBuilder;
import com.loserico.search.cache.ElasticCacheUtils;
import com.loserico.search.factory.TransportClientFactory;
import com.loserico.search.support.BulkResult;
import com.loserico.search.support.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsAction;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexTemplateMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.indices.IndexTemplateMissingException;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.json.jackson.JacksonUtils.toObject;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

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
	public static IndexBuilder createIndex(String index) {
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(index);
		return new IndexBuilder(createIndexRequestBuilder);
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
		GetAliasesResponse response = client.admin()
				.indices()
				.getAliases(new GetAliasesRequest())
				.actionGet();
		String[] indices = response.getAliases().keys().toArray(String.class);
		return asList(indices);
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
	public static MultiGetBuilder mget() {
		return new MultiGetBuilder(client);
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
	 * @param mappingBuilder
	 * @return boolean Mapping创建成功失败标识
	 */
	public static boolean putMapping(String index, MappingBuilder mappingBuilder) {
		Objects.requireNonNull(index, "index cannot be null");
		Objects.requireNonNull(mappingBuilder, "mappingBuilder cannot be null");
		Map<String, Object> source = mappingBuilder.build();
		PutMappingRequestBuilder putMappingRequestBuilder = client.admin().indices().preparePutMapping(index);
		AcknowledgedResponse acknowledgedResponse = putMappingRequestBuilder.setType(ElasticUtils.ONLY_TYPE)
				.setSource(source)
				.get();
		return acknowledgedResponse.isAcknowledged();
	}
	
	/**
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/indices-templates.html
	 *
	 * @param templateName
	 */
	public static IndexTemplateBuilder putIndexTemplate(String templateName) {
		return IndexTemplateBuilder.newInstance(client, templateName);
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
	 * 
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
	 * https://www.programcreek.com/java-api-examples/?api=org.elasticsearch.search.suggest.term.TermSuggestionBuilder
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-search.html#_requesting_suggestions
	 *
	 * @param indices
	 */
	public static void suggester(String... indices) {
		TermSuggestionBuilder suggestionBuilder = SuggestBuilders.termSuggestion("title_completion").suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS).text("luce");
		SuggestBuilder suggestBuilder = new SuggestBuilder();
		suggestBuilder.addSuggestion("article_suggester", suggestionBuilder);
		
		SearchResponse searchResponse = client.prepareSearch(indices).suggest(suggestBuilder).get();
		
		Suggest suggest = searchResponse.getSuggest();
		TermSuggestion termSuggestion = suggest.getSuggestion("article_suggester");
		for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
				String suggestText = entry.getText().string();
				System.out.println(suggestText);
		}
	}
}
