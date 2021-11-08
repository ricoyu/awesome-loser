package com.loserico.messaging.consumer;

import com.loserico.common.lang.constants.Units;
import com.loserico.messaging.KafkaUtils;
import com.loserico.messaging.enums.OffsetReset;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-11-07 9:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class KafkaUtilsConsumerTest {
	
	@Test
	public void testConsumerPerformance() {
		Consumer consumer = KafkaUtils.newConsumer("10.10.26.240:9092")
				.maxPartitionFetchBytes(1 * Units.MB)
				.fetchMaxBytes(1 * Units.MB)
				.maxPollRecords(1000)
				.autoOffsetReset(OffsetReset.LATEST)
				.groupId("loser-test-perf")
				.build();
		
		
	}
}
