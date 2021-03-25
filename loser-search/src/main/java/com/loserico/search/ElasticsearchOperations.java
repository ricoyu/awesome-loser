package com.loserico.search;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.builder.MultiGetBuilder;
import com.loserico.search.builder.RestPutMappingBuilder;
import com.loserico.search.exception.BulkOperationException;
import com.loserico.search.exception.DocumentDeleteException;
import com.loserico.search.exception.DocumentExistsException;
import com.loserico.search.exception.DocumentGetException;
import com.loserico.search.exception.DocumentSaveException;
import com.loserico.search.exception.DocumentUpdateException;
import com.loserico.search.exception.IndexCreateException;
import com.loserico.search.exception.IndexDeleteException;
import com.loserico.search.exception.IndexExistsException;
import com.loserico.search.exception.IndexSearchException;
import com.loserico.search.exception.ListIndicesException;
import com.loserico.search.exception.MappingException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

/**
 * <p>
 * Copyright: (C), 2020-12-06 12:47
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticsearchOperations {
	
	@Autowired
	private RestHighLevelClient client;
	
	
	/**
	 * 创建索引, 默认1个分片, 0个副本
	 *
	 * @param index 索引名
	 * @return boolean 创建成功失败标识
	 */
	public boolean createIndex(String index) {
		return createIndex(index, 1, 0);
	}
	
	/**
	 * 创建索引
	 *
	 * @param index    索引名
	 * @param shards   分片数
	 * @param replicas 副本数
	 * @return boolean 创建成功失败标识
	 */
	public boolean createIndex(String index, Integer shards, Integer replicas) {
		CreateIndexRequest request = new CreateIndexRequest(index);
		request.settings(Settings.builder()
				.put("index.number_of_shards", shards)
				.put("index.number_of_replicas", replicas));
		try {
			CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
			return response.isAcknowledged();
		} catch (ElasticsearchStatusException e) {
			log.warn("创建索引失败, {}", e.getMessage());
			return false;
		} catch (IOException e) {
			log.error("创建索引:" + index + "失败", e);
			throw new IndexCreateException(e);
		}
	}
	
	/**
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.1/java-rest-high-indices-exists.html
	 *
	 * @param index
	 * @return
	 * @description: 判断索引是否存在
	 */
	public Boolean existsIndex(String index) {
		GetIndexRequest request = new GetIndexRequest(index);
		try {
			return client.indices().exists(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new IndexExistsException(e);
		}
	}
	
	/**
	 * 删除索引
	 *
	 * @param index
	 * @return boolean 删除成功与否
	 */
	public boolean deleteIndex(String index) {
		DeleteIndexRequest request = new DeleteIndexRequest(index);
		AcknowledgedResponse response = null;
		try {
			response = client.indices().delete(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new IndexDeleteException(e);
		}
		return response.isAcknowledged();
	}
	
	/**
	 * 列出索引列表
	 *
	 * @return List<String>
	 */
	public List<String> listIndices() {
		String[] indices = new String[0];
		try {
			Map<String, Set<AliasMetaData>> aliases = client.indices()
					.getAlias(new GetAliasesRequest(), RequestOptions.DEFAULT)
					.getAliases();
			return aliases.keySet().stream().collect(toList());
		} catch (IOException e) {
			throw new ListIndicesException(e);
		}
	}
	
	
	/**
	 * 创建一个新的文档, 返回新创建文档的ID
	 * 对应REST API POST 方式
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public String index(String index, String doc) {
		IndexRequest request = new IndexRequest(index);
		//request.opType(OpType.CREATE);
		request.source(doc, XContentType.JSON);
		IndexResponse response = null;
		try {
			response = client.index(request, DEFAULT);
		} catch (IOException e) {
			throw new IndexCreateException(e);
		}
		return response.getId();
	}
	
	/**
	 * Index方式创建文档
	 * 如果索引不存在, 就创建新的文档;
	 * 如果文档存在, 就删除文档, 新的文档将被索引, id不变, 版本信息+1
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public String index(String index, String id, String doc) {
		IndexRequest request = new IndexRequest(index);
		request.source(doc, XContentType.JSON);
		request.opType(OpType.INDEX);
		if (isNotBlank(id)) {
			request.id(id);
		}
		
		try {
			IndexResponse indexResponse = client.index(request, DEFAULT);
			String docId = indexResponse.getId();
			if (indexResponse.getResult() == Result.CREATED) {
				log.info("已创建 {}", docId);
			} else if (indexResponse.getResult() == Result.UPDATED) {
				log.info("已更新 {}", docId);
			}
			return docId;
		} catch (ElasticsearchException e) {
			log.error("", e);
			throw new DocumentSaveException(e);
		} catch (IOException e) {
			log.error("保存文档失败", e);
			throw new DocumentSaveException(e);
		}
	}
	
	/**
	 * 指定ID创建文档, 如果已经存在同样ID的文档则抛出异常
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public String indexIfNotExists(String index, String id, String doc) {
		IndexRequest request = new IndexRequest(index);
		request.opType(OpType.CREATE);
		request.id(id).source(doc, XContentType.JSON);
		IndexResponse response = null;
		try {
			response = client.index(request, DEFAULT);
		} catch (Exception e) {
			throw new DocumentExistsException(e);
		}
		return response.getId();
	}
	
	/**
	 * 批量创建文档
	 *
	 * @param index
	 * @param docs
	 * @return int 批量创建文档成功的数量
	 */
	public int index(String index, List<String> docs) {
		BulkRequest request = new BulkRequest(index);
		
		docs.stream().map((doc) -> {
			IndexRequest indexRequest = new IndexRequest();
			indexRequest.source(doc, XContentType.JSON);
			return indexRequest;
		}).forEach(insertRequest -> request.add(insertRequest));
		
		try {
			BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
			int successCount = 0;
			for (Iterator<BulkItemResponse> iterator = response.iterator(); iterator.hasNext(); ) {
				BulkItemResponse itemResponse = iterator.next();
				if (!itemResponse.isFailed()) {
					successCount++;
				}
			}
			return successCount;
		} catch (IOException e) {
			throw new BulkOperationException(e);
		}
	}
	
	/**
	 * @param index 索引名
	 * @param id    文档id
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T get(String index, String id, Class<T> clazz) {
		Objects.requireNonNull(index, "索引名不能为null");
		Objects.requireNonNull(id, "id 不能为null");
		Objects.requireNonNull(clazz, "clazz不能为null");
		
		GetRequest request = new GetRequest(index, id);
		request.fetchSourceContext(FetchSourceContext.FETCH_SOURCE);
		try {
			GetResponse response = client.get(request, DEFAULT);
			return JacksonUtils.toObject(response.getSourceAsString(), clazz);
		} catch (IOException e) {
			log.error("", e);
			throw new DocumentGetException(e);
		}
		
	}
	
	/**
	 * MGET 操作
	 *
	 * @return
	 */
	public MultiGetBuilder mget() {
		return new MultiGetBuilder(client);
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
	public Result update(String index, String id, String doc) {
		UpdateRequest request = new UpdateRequest(index, id);
		request.doc(doc, XContentType.JSON);
		try {
			UpdateResponse response = client.update(request, DEFAULT);
			return ReflectionUtils.getFieldValue("result", response);
		} catch (Exception e) {
			throw new DocumentUpdateException(e);
		}
	}
	
	/**
	 * 更新文档的一部分, 或者ID对应的文档不存在时创建文档
	 * <ol>
	 * <li/>如果ID对应的文档在ES中还不存在, 那么创建文档
	 * <li/>如果docPiece对应的字段在文档中还不存在, 那么在原文档中插入这个字段
	 * <li/>如果docPiece对应的字段在文档中存在, 并且值不一样, 那么执行更新
	 * <li/>docPiece对应的字段在文档中存在, 但是值是一样的, 那么不执行更新
	 * </ol>
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-update.html
	 *
	 * @param index
	 * @param id
	 * @param doc   整篇文档或者文档的一部分
	 * @return Result 创建? 更新? 没更新?
	 */
	public Result upsert(String index, String id, String doc) {
		UpdateRequest request = new UpdateRequest(index, id);
		request.doc(doc, XContentType.JSON);
		request.docAsUpsert(true);
		try {
			UpdateResponse response = client.update(request, DEFAULT);
			return ReflectionUtils.getFieldValue("result", response);
		} catch (Exception e) {
			throw new DocumentUpdateException(e);
		}
	}
	
	/**
	 * 检查指定索引中是否存在指定id的文档
	 *
	 * @param index
	 * @param id
	 * @return boolean
	 */
	public boolean exists(String index, String id) {
		GetRequest request = new GetRequest(index, id);
		request.fetchSourceContext(new FetchSourceContext(false));
		request.storedFields("_none_");
		try {
			return client.exists(request, DEFAULT);
		} catch (IOException e) {
			throw new DocumentExistsException(e);
		}
	}
	
	/**
	 * 删除一篇文档
	 *
	 * @param index
	 * @param id
	 * @return Result
	 */
	public Result delete(String index, String id) {
		DeleteRequest request = new DeleteRequest(index, id);
		try {
			DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
			return response.getResult();
		} catch (IOException e) {
			throw new DocumentDeleteException(e);
		}
	}
	
	/**
	 * 获取所有的Mapping信息
	 *
	 * @param indices
	 * @return Map<String, MappingMetaData>
	 */
	public Map<String, MappingMetaData> getMapping(String... indices) {
		GetMappingsRequest request = new GetMappingsRequest();
		request.indices(indices);
		try {
			GetMappingsResponse response = client.indices().getMapping(request, DEFAULT);
			return response.mappings();
		} catch (IOException e) {
			log.error("", e);
			throw new MappingException(e);
		}
	}
	
	public RestPutMappingBuilder putMapping(String index) {
		return new RestPutMappingBuilder(client, index);
	}
	
	public List<String> searchAll(String index) {
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.fetchSource(true);
		
		try {
			SearchResponse response = client.search(searchRequest, DEFAULT);
			SearchHits hits = response.getHits();
			return Stream.of(hits.getHits())
					.map(SearchHit::getSourceAsString)
					.collect(toList());
		} catch (IOException e) {
			log.error("", e);
			throw new IndexSearchException(e);
		}
	}
	
	public Object search(String... indices) {
		//return new SearchBuilder(indices);
		return null;
	}
	
	/**
	 * 查询指定索引, 默认返回前十条记录
	 * @param index
	 * @return List<String>
	 */
	public List<String> search(String index) {
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.fetchSource(true);
		
		try {
			SearchResponse response = client.search(searchRequest, DEFAULT);
			SearchHits hits = response.getHits();
			return Stream.of(hits.getHits())
					.map(SearchHit::getSourceAsString)
					.collect(toList());
		} catch (IOException e) {
			log.error("", e);
			throw new IndexSearchException(e);
		}
	}
	
	private String toJson(Object doc) {
		if (doc == null) {
			return null;
		}
		
		if (doc instanceof String) {
			return (String) doc;
		}
		
		return JacksonUtils.toJson(doc);
	}
	
}
