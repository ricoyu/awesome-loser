package com.loserico.search;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.annotation.DocId;
import com.loserico.search.factory.TransportClientFactory;
import com.loserico.search.support.BulkResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.json.jackson.JacksonUtils.toObject;

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
	private static final String TYPE = "_doc";
	
	public static final TransportClient client = TransportClientFactory.create();
	
	/**
	 * 创建索引, 默认1个分片, 0个副本
	 *
	 * @param index 索引名
	 * @return boolean 创建成功失败标识
	 */
	public static boolean createIndex(String index) {
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		CreateIndexResponse response = indicesAdminClient.prepareCreate(index).get();
		return response.isAcknowledged();
	}
	
	
	/**
	 * 创建索引
	 *
	 * @param index    索引名
	 * @param shards   分片数
	 * @param replicas 副本数
	 * @return boolean 创建成功失败标识
	 */
	public static boolean createIndex(String index, Integer shards, Integer replicas) {
		CreateIndexResponse response = client.admin().indices().prepareCreate("index")
				.setSettings(Settings.builder()
						.put("index.number_of_shards", shards)
						.put("index.number_of_replicas", replicas)
				)
				.get();
		return response.isAcknowledged();
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
		return Arrays.asList(indices);
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
		/*Objects.requireNonNull(index, "index cannot be null!");
		if (doc == null) {
			return null;
		}
		IndexResponse response = client.prepareIndex(index, TYPE)
				.setSource(doc, XContentType.JSON)
				.get();
		return response.getId();*/
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
		
		String id = null;
		
		/*
		 * POJO类型才支持@ElasticId注解
		 */
		if (ReflectionUtils.isPojo(doc)) {
			/*
			 * 拿到这个class的所有字段, 包括自己定义的和父类中定义的字段, 但是不包括Object对象中的字段
			 */
			Field[] fields = ReflectionUtils.getFields(doc.getClass());
			for (Field field : fields) {
				//找到标记了@DocId注解的字段, 把这个字段的值作为文档的_id
				DocId elasticId = field.getAnnotation(DocId.class);
				if (elasticId != null) {
					Object value = ReflectionUtils.getFieldValue(field, doc);
					id = Transformers.convert(value);
				}
				//只能由一个id字段, 找到了就不用再找了
				break;
			}
		}
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
		IndexResponse response = client.prepareIndex(index, TYPE, id)
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
		IndexResponse response = client.prepareIndex(index, TYPE, id)
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
		BulkRequestBuilder bulkRequest = client.prepareBulk(index, TYPE);
		Arrays.asList(docs).forEach((doc) -> {
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
		String[] jsons = docs.stream()
				.filter(Objects::nonNull)
				.map(doc -> toJson(doc))
				.toArray(length -> new String[length]);
		return bulkIndex(index, jsons);
	}
	
	/**
	 * 根据ID获取并转成指定类型对象
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
		
		GetResponse response = client.prepareGet(index, TYPE, id).get();
		String source = response.getSourceAsString();
		return toObject(source, clazz);
	}
	
}
