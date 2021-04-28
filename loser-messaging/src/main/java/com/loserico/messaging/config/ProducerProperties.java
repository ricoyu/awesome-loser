package com.loserico.messaging.config;

import com.loserico.messaging.enums.Compression;
import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.util.unit.DataSize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

/**
 * Kafka Producer 相关配置
 * <p>
 * Copyright: Copyright (c) 2021-01-27 15:39
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class ProducerProperties {
	
	/**
	 * Comma-delimited list of host:port pairs to use for establishing the initial
	 * connections to the Kafka cluster. Overrides the global property, for producers.
	 */
	private List<String> bootstrapServers;
	
	/**
	 * 默认 ""
	 * ID to pass to the server when making requests. Used for server-side logging.
	 * The purpose of this is to be able to track the source of requests beyond just ip/port by allowing
	 * a logical application name to be included in server-side request logging.
	 */
	private String clientId;
	
	/**
	 * The acks parameter controls how many partition replicas must receive the record before the producer can consider the write successful.
	 * This option has a significant impact on how likely messages are to be lost.
	 * 可以有以下三个值:
	 * <ul>
	 *  <li>acks=0</li>发射后不管<br/>
	 *  The producer will not wait for a reply from the broker before assuming the message was sent successfully.
	 *  This means that if something went wrong and the broker did not receive the message, the producer will not
	 *  know about it and the message will be lost. However, because the producer is not waiting for any response
	 *  from the server, it can send messages as fast as the network will support, so this setting can be used to achieve very high throughput.
	 *
	 *  <li>acks=1</li>
	 *  Leader副本收到message后, producer客户端收到一个success的response;
	 *  如果message不能写入Leader(Leader挂了并且新的Leader还没选出来), producer会收到一个error response并且可以重试, 以避免潜在的消息丢失风险.
	 *  但还是由丢失message的风险. <br/>
	 *  The producer will receive a success response from the broker the moment the leader replica received the message.
	 *  If the message can’t be written to the leader (e.g., if the leader crashed and a new leader was not elected yet),
	 *  the producer will receive an error response and can retry sending the message, avoiding potential loss of data.
	 *
	 *  <li>acks=all acks=-1</li>
	 *  等待所有副本都收到消息后, producer才会收到success response, 消息没有丢失的风险. 吞吐量比acks=1的情况更低, 因为要等到所有的副本都收到消息. acks=all 和acks=-1等价<br/>
	 *  If acks=all, the producer will receive a success response from the broker once all in-sync replicas received the message.
	 *  This is the safest mode since you can make sure more than one broker has the message and that the message will survive
	 *  even in the case of crash. However, the latency we discussed in the acks=1 case will be even higher, since we will be
	 *  waiting for more than just one broker to receive the message.
	 * </ul>
	 */
	private String acks;
	
	/**
	 * 默认 16384 16K 0禁用batch操作<br/>
	 * <p>
	 * batch size太小会降低吞吐率, 太大浪费内存<br/>
	 * <p>
	 * 精确说是"待发送消息的缓存大小", 注意跟buffer.memory的区别<br/>
	 * <p>
	 * 当有多个消息需要被发送到同一个分区时, 生产者会把它们放在同一个批次里。该参数指定了一个批次可以使用的内存大小, 按照字节数计算（而不是消息个数）。
	 * 当批次被填满, 批次里的所有消息会被发送出去。不过生产者并不一定都会等到批次被填满才发送, 半满的批次, 甚至只包含一个消息的批次也有可能被发送。
	 * 所以就算把批次大小设置得很大, 也不会造成延迟, 只是会占用更多的内存而已。但如果设置得太小, 因为生产者需要更频繁地发送消息, 会增加一些额外的开销。<br/>
	 * <p>
	 * The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition.
	 * This helps performance on both the client and the server.
	 * This configuration controls the default batch size in bytes.
	 */
	private DataSize batchSize;
	
	/**
	 * Total memory size the producer can use to buffer records waiting to be sent to the server.<p>
	 * 默认 33554432 32M<p>
	 * The total bytes of memory the producer can use to buffer records waiting to be sent to the server.<p>
	 * 该参数用来设置生产者内存缓冲区的大小, 生产者用它缓冲要发送到服务器的消息。<p>
	 * 如果应用程序发送消息的速度超过发送到服务器的速度, 会导致生产者空间不足。<p>
	 * 这个时候 send() 方法调用要么被阻塞, 要么抛出异常, 取决于如何设置max.block.ms 参数(表示在抛出异常之前可以阻塞一段时间)。
	 */
	private Long bufferMemory;
	
	/**
	 * max.block.ms  默认 60000
	 * <p>
	 * The configuration controls how long `KafkaProducer.send()` and `KafkaProducer.partitionsFor()` will block.
	 * These methods can be blocked either because the buffer is full or metadata unavailable.
	 * <p>
	 * Blocking in the user-supplied serializers or partitioner will not be counted against this timeout.
	 */
	private Long maxBlockMs;
	
	/**
	 * 值可以是: gzip, snappy, lz4, zstd, uncompressed, producer<p>
	 * Compression type for all data generated by the producer.<p>
	 * Specify the final compression type for a given topic. <p>
	 * This configuration accepts the standard compression codecs ('gzip', 'snappy', 'lz4', 'zstd'). <p>
	 * It additionally accepts 'uncompressed' which is equivalent to no compression; <p>
	 * and 'producer' which means retain the original compression codec set by the producer.<p>
	 */
	private Compression compressionType;
	
	/**
	 * Serializer class for keys.
	 */
	private Class<?> keySerializer = StringSerializer.class;
	
	/**
	 * Serializer class for values.
	 */
	private Class<?> valueSerializer = StringSerializer.class;
	
	/**
	 * When greater than zero, enables retrying of failed sends.<br/>
	 * 默认 2147483647<br/>
	 * 生产者从服务器收到的错误有可能是临时性的错误(比如分区找不到Leader)。<p>
	 * 在这种情况下, retries 参数的值决定了生产者可以重发消息的次数, 如果达到这个次数, 生产者会放弃重试并返回错误。
	 */
	private Integer retries = 99;
	
	/**
	 * Additional producer-specific properties used to configure the client.
	 */
	private final Map<String, String> properties = new HashMap<>();
	
	public Map<String, Object> buildProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		if (isNotBlank(clientId)) {
			properties.put(CLIENT_ID_CONFIG, clientId);
		}
		
		if (isNotBlank(acks)) {
			properties.put(ACKS_CONFIG, acks);
		}
		
		if (batchSize != null) {
			properties.put(BATCH_SIZE_CONFIG, batchSize);
		}
		
		if (bufferMemory != null) {
			properties.put(BUFFER_MEMORY_CONFIG, bufferMemory);
		}
		
		if (compressionType != null) {
			properties.put(COMPRESSION_TYPE_CONFIG, compressionType);
		}
		
		if (retries != null) {
			properties.put(RETRIES_CONFIG, retries);
		}
		
		if (keySerializer != null) {
			properties.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		}
		
		if (valueSerializer != null) {
			properties.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		}
		
		properties.putAll(this.properties);
		
		return properties;
	}
	
}
