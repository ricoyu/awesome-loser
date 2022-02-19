package com.loserico.messaging.builder;

import com.loserico.messaging.callback.TopicCreationCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.loserico.common.lang.utils.Assert.notNull;
import static java.util.Arrays.asList;

/**
 * 创建Topic时, Partition和Replication是核心关注点
 * <p>
 * Copyright: (C), 2022-02-19 16:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class TopicBuilder {
	
	private Map<String, String> configs = new HashMap<>();
	
	private Admin admin;
	
	/**
	 * Topic 名字
	 */
	private String topicName;
	
	/**
	 * 分区数, 默认1
	 */
	private int partitions = 1;
	
	/**
	 * 副本数, 默认1
	 */
	private short replications = 1;
	
	/**
	 * 一次拉取消息的最大值(bytes), 默认 1000012 0.9M 对应的服务器属性 message.max.bytes
	 */
	private Integer maxMessageBytes;
	
	/**
	 * Topic创建完成后的回调函数
	 */
	private TopicCreationCallback callback;
	
	public TopicBuilder(Admin admin, String topicName) {
		this.admin = admin;
		this.topicName = topicName;
	}
	
	/**
	 * 分区数, 默认1
	 */
	public TopicBuilder partitions(int partitions) {
		this.partitions = partitions;
		return this;
	}
	
	/**
	 * 副本数, 默认1
	 */
	public TopicBuilder replications(int replications) {
		this.replications = (short) replications;
		return this;
	}
	
	/**
	 * 默认为1, 比如有3个副本的Topic test, 发送端配置acks=all, Broker端配置min.insync.replicas=2,
	 * 那么只要2个副本写成功就行了, 不用等所有副本写成功. 这种策略会保证只要有一个备份存活就不会丢失数据, 这是最强的数据保证.
	 * 一般除非是金融级别, 或跟钱打交道的场景才会使用这种配置. <p/>
	 * min.insync.replicas specifies the minimum number of replicas that must acknowledge a write for the write to be considered successful.
	 */
	public TopicBuilder minInsyncReplicas(int minInsyncReplicas) {
		if (minInsyncReplicas < 1) {
			throw new IllegalArgumentException("minInsyncReplicas 至少为1");
		}
		configs.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, minInsyncReplicas + "");
		return this;
	}
	
	/**
	 * 一次拉取消息的最大值(bytes), 默认 1000012 0.9M 对应的服务器属性 message.max.bytes
	 */
	public TopicBuilder maxMessageBytes(int maxMessageBytes) {
		this.maxMessageBytes = maxMessageBytes;
		return this;
	}
	
	/**
	 * TopicConfig 类提供了Topic相关的所有控制参数
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	public TopicBuilder config(String property, Object value) {
		Objects.requireNonNull(property, "property cannot be null!");
		notNull(value, "value cannot be null!");
		configs.put(property, value + "");
		return this;
	}
	
	/**
	 * Topic创建完成后的回调函数, 如果创建成功, 回调函数第一个参数e为null
	 *
	 * @param callback
	 * @return TopicBuilder
	 */
	public TopicBuilder callback(TopicCreationCallback callback) {
		this.callback = callback;
		return this;
	}
	
	public void create() {
		NewTopic topic = new NewTopic(topicName, partitions, replications);
		topic.configs(configs);
		CreateTopicsResult result = admin.createTopics(asList(topic));
		try {
			result.all().get(3, TimeUnit.SECONDS);
			if (callback != null) {
				callback.onComplete(null, topicName);
			}
		} catch (Exception e) {
			if (callback != null) {
				callback.onComplete(e, topicName);
			} else {
				log.error("", e);
				throw new RuntimeException(e);
			}
		}
	}
}
