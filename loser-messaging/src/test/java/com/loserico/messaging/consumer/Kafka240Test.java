package com.loserico.messaging.consumer;

import com.loserico.common.lang.constants.Units;
import com.loserico.messaging.KafkaUtils;
import com.loserico.messaging.enums.OffsetReset;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-12-21 13:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class Kafka240Test {
	
	@SneakyThrows
	@Test
	public void testConsumeFrom240() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("10.10.26.240:9092")
				.groupId("metadata-loser-group")
				.maxPollRecords(1000)
				.fetchMaxBytes(1 * Units.MB)
				.autoCommit(false)
				.autoOffsetReset(OffsetReset.EARLIEST)
				.build();
		
		consumer.subscribe("ids-metadata", (messages) -> {
			log.info("Message size {}", messages.size());
		});
		
		Thread.currentThread().join();
	}
}
