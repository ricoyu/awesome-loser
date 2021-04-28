package com.loserico.messaging.enums;

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
 *
 * <p>
 * Copyright: (C), 2021-04-27 16:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum Acks {
	
	/**
	 * acks=0 发射后不管<p>
	 * The producer will not wait for a reply from the broker before assuming the message was sent successfully.
	 * This means that if something went wrong and the broker did not receive the message, the producer will not
	 * know about it and the message will be lost. However, because the producer is not waiting for any response
	 * from the server, it can send messages as fast as the network will support, so this setting can be used to achieve very high throughput.
	 */
	NONE("0"),
	
	/**
	 * acks=1
	 * <p>
	 * Leader副本收到message后, producer客户端收到一个success的response;
	 * 如果message不能写入Leader(Leader挂了并且新的Leader还没选出来), producer会收到一个error response并且可以重试, 以避免潜在的消息丢失风险.
	 * 但还是由丢失message的风险. <br/>
	 * The producer will receive a success response from the broker the moment the leader replica received the message.
	 * If the message can’t be written to the leader (e.g., if the leader crashed and a new leader was not elected yet),
	 * the producer will receive an error response and can retry sending the message, avoiding potential loss of data.
	 */
	LEADER("1"),
	
	/**
	 * acks=all acks=-1
	 * <p>
	 * 等待所有副本都收到消息后, producer才会收到success response, 消息没有丢失的风险. 吞吐量比acks=1的情况更低, 因为要等到所有的副本都收到消息. acks=all 和acks=-1等价<br/>
	 * If acks=all, the producer will receive a success response from the broker once all in-sync replicas received the message.
	 * This is the safest mode since you can make sure more than one broker has the message and that the message will survive
	 * even in the case of crash. However, the latency we discussed in the acks=1 case will be even higher, since we will be
	 * waiting for more than just one broker to receive the message.
	 */
	ALL("all");
	
	private String value;
	
	private Acks(String value) {
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return value;
	}
}
