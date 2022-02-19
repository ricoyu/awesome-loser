package com.loserico.messaging.builder;

import com.loserico.common.lang.enums.SizeUnit;
import com.loserico.messaging.enums.Acks;
import com.loserico.messaging.enums.Compression;
import com.loserico.messaging.producer.Producer;
import com.loserico.messaging.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.loserico.common.lang.utils.Assert.notNull;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_BLOCK_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

/**
 * KafkaProducer 构造器
 *
 * <p>
 * Copyright: (C), 2021-04-27 14:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProducerBuilder extends BaseBuilder {
	
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
	 *  需要等待 min.insync.replicas(默认1)指定的副本数写入成功才算成功, 消息没有丢失的风险. 吞吐量比acks=1的情况更低, 因为要等到所有的副本都收到消息. acks=all 和acks=-1等价<br/>
	 *  If acks=all, the producer will receive a success response from the broker once all in-sync replicas received the message.
	 *  This is the safest mode since you can make sure more than one broker has the message and that the message will survive
	 *  even in the case of crash. However, the latency we discussed in the acks=1 case will be even higher, since we will be
	 *  waiting for more than just one broker to receive the message.
	 * </ul>
	 */
	private Acks acks = Acks.LEADER;
	
	/**
	 * batch.size <br/>
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
	private Integer batchSize = 102400; //TODO 滴滴示例上是100000
	
	/**
	 * linger.ms 官方默认0, 这边设置默认10, 以提高性能
	 * <p/>
	 * 0 表示消息必须立即发送出去, 但这样会影响性能 <br/>
	 * 一般设置10毫秒左右, 就是说这个消息发送完后会进入本地的一个batch, 如果10毫秒内, 这个batch满了16KB就发送出去 <br/>
	 * 如果10毫秒内batch没有满, 那么也必须把消息发送出去, 不能让消息发送延迟时间太长
	 */
	private Integer lingerMs = 10;
	
	/**
	 * buffer.memory <br/>
	 * Total memory size the producer can use to buffer records waiting to be sent to the server.<p>
	 * 默认 33554432 32M<p>
	 * 发送消息的本地缓冲区, 如果设置了该缓冲区, 消息会先发送到本地缓冲区, 然后再一可以提高消息发送性能, 默认32M
	 */
	private Integer bufferMemory;
	
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
	 * and 'producer' which means retain the original compression codec set by the producer.
	 */
	private Compression compression = Compression.LZ4;
	
	/**
	 * Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error. 
	 * Note that this retry is no different than if the client resent the record upon receiving the error. <p>
	 * 默认 2147483647(文档上真是这么说的)<p>
	 * 生产者从服务器收到的错误有可能是临时性的错误(比如分区找不到Leader)。<p>
	 * 在这种情况下, retries 参数的值决定了生产者可以重发消息的次数, 如果达到这个次数, 生产者会放弃重试并返回错误。
	 */
	private Integer retries = 3;
	
	/**
	 * Serializer class for keys.
	 */
	private Class<?> keySerializer = StringSerializer.class;
	
	/**
	 * Serializer class for values.
	 */
	private Class<?> valueSerializer = JsonSerializer.class;
	
	@Override
	public ProducerBuilder bootstrapServers(String bootstrapServers) {
		notNull(bootstrapServers, "bootstrapServers cannot be null!");
		super.bootstrapServers(bootstrapServers);
		return this;
	}
	
	@Override
	public ProducerBuilder bootstrapServer(String host, String port) {
		notNull(host, "host cannot be null!");
		notNull(port, "port cannot be null!");
		super.bootstrapServer(host, port);
		return this;
	}
	
	/**
	 * 默认 acks=1 <br/>
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
	 *
	 * @param acks
	 * @return ProducerBuilder
	 */
	public ProducerBuilder acks(Acks acks) {
		notNull(acks, "acks cannot be null!");
		this.acks = acks;
		return this;
	}
	
	/**
	 * batch.size <br/>
	 * 官方默认 16384 16K, 我这里设为100K,  0表示禁用batch操作<br/>
	 * <p>
	 * Kafka本地线程会从缓冲区取数据, 批量发送到Broker <br/>
	 * 本参数设置批量发送消息的大小, 默认值16384, 即16K, 就是说一个batch满了16KB就发送出去
	 * <p>
	 * The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition.
	 * This helps performance on both the client and the server.
	 * This configuration controls the default batch size in bytes.
	 *
	 * @param batchSize
	 * @param sizeUnit
	 * @return ProducerBuilder
	 */
	public ProducerBuilder batchSize(Integer batchSize, SizeUnit sizeUnit) {
		notNull(batchSize, "batchSize cannot be null!");
		notNull(sizeUnit, "sizeUnit cannot be null!");
		this.batchSize = ((Long)sizeUnit.toBytes(batchSize.longValue())).intValue();
		return this;
	}
	
	/**
	 * linger.ms 官方默认0, 这边默认10毫秒, 以提高发送性能
	 * <p/>
	 * 0 表示消息必须立即发送出去, 但这样会影响性能 <br/>
	 * 一般设置10毫秒左右, 就是说这个消息发送完后会进入本地的一个batch, 如果10毫秒内, 这个batch满了16KB就发送出去 <br/>
	 * 如果10毫秒内batch没有满, 那么也必须把消息发送出去, 不能让消息发送延迟时间太长(公交车到点发车;)
	 * 
	 * @param lingerMs
	 * @return ProducerBuilder
	 */
	public ProducerBuilder lingerMs(Integer lingerMs) {
		this.lingerMs = lingerMs;
		return this;
	}
	
	/**
	 * buffer.memory <br/>
	 * Total memory size the producer can use to buffer records waiting to be sent to the server.<p>
	 * 默认 33554432 32M<p>
	 * 发送消息的本地缓冲区, 如果设置了该缓冲区, 消息会先发送到本地缓冲区, 可以提高消息发送性能, 默认32M
	 *
	 * @param bufferMemory
	 * @param sizeUnit
	 * @return ProducerBuilder
	 */
	public ProducerBuilder bufferMemory(Integer bufferMemory, SizeUnit sizeUnit) {
		notNull(bufferMemory, "bufferMemory cannot be null!");
		notNull(sizeUnit, "sizeUnit cannot be null!");
		this.bufferMemory = ((Long)sizeUnit.toBytes(bufferMemory.longValue())).intValue();
		return this;
	}
	
	/**
	 * max.block.ms  默认 60000
	 * <p>
	 * The configuration controls how long `KafkaProducer.send()` and `KafkaProducer.partitionsFor()` will block.
	 * These methods can be blocked either because the buffer is full or metadata unavailable.
	 * <p>
	 * Blocking in the user-supplied serializers or partitioner will not be counted against this timeout.
	 *
	 * @param maxBlocks
	 * @param timeUnit
	 * @return ProducerBuilder
	 */
	public ProducerBuilder maxBlocks(Long maxBlocks, TimeUnit timeUnit) {
		notNull(maxBlocks, "maxBlocks cannot be null!");
		notNull(timeUnit, "timeUnit cannot be null!");
		this.maxBlockMs = timeUnit.toMillis(maxBlocks);
		return this;
	}
	
	/**
	 * Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error. 
	 * Note that this retry is no different than if the client resent the record upon receiving the error. <p>
	 * 默认 2147483647(文档上真是这么说的)<p>
	 * 生产者从服务器收到的错误有可能是临时性的错误(比如分区找不到Leader)。<p>
	 * 在这种情况下, retries 参数的值决定了生产者可以重发消息的次数, 如果达到这个次数, 生产者会放弃重试并返回错误。
	 *
	 * @param retries 默认3
	 * @return ProducerBuilder
	 */
	public ProducerBuilder retries(Integer retries) {
		this.retries = retries;
		return this;
	}
	
	/**
	 * 在平衡存储与CPU使用率后推荐使用lz4, 默认
	 * 值可以是: gzip, snappy, lz4, zstd, uncompressed, producer<p>
	 * Compression type for all data generated by the producer.<p>
	 * Specify the final compression type for a given topic. <p>
	 * This configuration accepts the standard compression codecs ('gzip', 'snappy', 'lz4', 'zstd'). <p>
	 * It additionally accepts 'uncompressed' which is equivalent to no compression; <p>
	 * and 'producer' which means retain the original compression codec set by the producer.
	 *
	 * @param compression
	 * @return ProducerBuilder
	 */
	public ProducerBuilder compression(Compression compression) {
		this.compression = compression;
		return this;
	}
	
	/**
	 * 默认 StringSerializer
	 *
	 * @param keySerializer
	 * @return ConsumerBuilder
	 */
	public ProducerBuilder keySerializer(Class<? extends Serializer> keySerializer) {
		notNull(keySerializer, "keySerializer cannot null!");
		this.keySerializer = keySerializer;
		return this;
	}
	
	/**
	 * StringSerializer
	 *
	 * @param valueSerializer
	 * @return ProducerBuilder
	 */
	public ProducerBuilder valueSerializer(Class<? extends Serializer> valueSerializer) {
		this.valueSerializer = valueSerializer;
		return this;
	}
	
	/**
	 * 构造KafkaProducer对象
	 *
	 * @return KafkaProducer
	 */
	public <T> Producer<String, T> build() {
		Map<String, Object> properties = buildProperties();
		return new Producer<String, T>(properties);
	}
	
	private Map<String, Object> buildProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers());
		
		if (acks != null) {
			properties.put(ACKS_CONFIG, acks.toString());
		}
		
		if (lingerMs != null) {
			properties.put(LINGER_MS_CONFIG, lingerMs);
		}
		if (batchSize != null) {
			properties.put(BATCH_SIZE_CONFIG, batchSize);
		}
		
		if (bufferMemory != null) {
			properties.put(BUFFER_MEMORY_CONFIG, bufferMemory);
		}
		
		if (maxBlockMs != null) {
			properties.put(MAX_BLOCK_MS_CONFIG, maxBlockMs);
		}
		
		if (compression != null) {
			properties.put(COMPRESSION_TYPE_CONFIG, compression.toString());
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
