package com.loserico.messaging.admin;

import com.loserico.messaging.builder.TopicBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.loserico.common.lang.utils.Assert.notNull;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

/**
 * Kafka Admin 相关API, 这个类的实例创建后应该用一个变量持有它, 不要每次都重新创建
 */
@Slf4j
public class KafkaAdmin {
	
	private Admin admin = null;
	
	public KafkaAdmin(Admin admin) {
		this.admin = admin;
	}
	
	/**
	 * 创建Topic
	 *
	 * @param topicName
	 * @return TopicBuilder
	 */
	public TopicBuilder createTopic(String topicName) {
		return new TopicBuilder(admin, topicName);
	}
	
	/**
	 * 返回删除成功/失败标识
	 *
	 * @param topicName
	 * @return boolean
	 */
	public boolean deleteTopic(String topicName) {
		notNull(topicName, "topicName cannot be null!");
		DeleteTopicsResult deleteTopicsResult = admin.deleteTopics(asList(topicName));
		try {
			deleteTopicsResult.all().get(3, SECONDS);
			return true;
		} catch (Exception e) {
			log.error("删除Topic: {} 失败", topicName, e);
		}
		return false;
	}
	
	/**
	 * 获取所有用户创建的Topics
	 * @return List<String>
	 */
	public List<String> listTopics() {
		ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
		listTopicsOptions.listInternal(false);
		ListTopicsResult listTopicsResult = admin.listTopics(listTopicsOptions);
		try {
			Set<String> topics = listTopicsResult.names().get(3, SECONDS);
			return topics.stream().collect(toList());
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 检查topics是否都存在, 只要有一个不存在就返回false, 都存在返回true
	 * @param topicNames
	 * @return boolean
	 */
	public boolean existsTopic(String... topicNames) {
		ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
		listTopicsOptions.listInternal(false);
		ListTopicsResult listTopicsResult = admin.listTopics(listTopicsOptions);
		try {
			Set<String> topics = listTopicsResult.names().get(3, SECONDS);
			return topics.containsAll(asList(topicNames));
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param topicName
	 * @return
	 */
	public TopicDescription describeTopic(String topicName) {
		notNull(topicName, "topicName cannot be null!");
		DescribeTopicsResult describeTopics = admin.describeTopics(asList(topicName));
		try {
			Map<String, TopicDescription> descriptionMap = describeTopics.all().get(3, SECONDS);
			return descriptionMap.get(topicName);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, TopicDescription> describeTopics(Collection<String> topicNames) {
		if (topicNames == null || topicNames.isEmpty()) {
			throw new IllegalArgumentException("topicNames cannot be null or empty!");
		}
		DescribeTopicsResult describeTopics = admin.describeTopics(topicNames);
		try {
			return describeTopics.all().get(3, SECONDS);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
}
