package com.loserico.search.builder.bulk;

import com.loserico.search.ElasticUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

/**
 * <p>
 * Copyright: (C), 2021-11-15 15:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ESBulkProcessor {
	
	public BulkProcessor bulkProcessor() {
		return BulkProcessor.builder(ElasticUtils.CLIENT,
				new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long executionId, BulkRequest request) {
						log.info("序号: {}, 开始执行 {} 条数据批量操作", executionId, request.numberOfActions());
					}
					
					@Override
					public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
						// 在每次执行BulkRequest后调用, 通过此方法可以获取BulkResponse是否包含错误
						if (response.hasFailures()) {
							log.error("Bulk {} executed with failures", executionId);
						} else {
							log.info("序号: {}, 执行 {} 条数据批量操作成功, 共耗费{}毫秒", executionId, request.numberOfActions(), response.getTook().getMillis());
						}
					}
					
					@Override
					public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
						log.error("序号: {} 批量操作失败, 总记录数: {}, 报错信息为: {}", executionId, request.numberOfActions(), failure.getMessage());
					}
				})
				.setBulkActions(1000)// 每添加1000个request，执行一次bulk操作
				.setBulkSize(new ByteSizeValue(15, ByteSizeUnit.MB)) // 每达到5M的请求size时，执行一次bulk操作
				.setFlushInterval(TimeValue.timeValueSeconds(5))// 每5s执行一次bulk操作
				// 设置并发请求数。默认是1，表示允许执行1个并发请求，积累bulk requests和发送bulk是异步的，其数值表示发送bulk的并发线程数（可以为2、3、...）；若设置为0表示二者同步。
				.setConcurrentRequests(16)
				// 最大重试次数为3次，启动延迟为100ms。
				.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
				.build();
	}
}
