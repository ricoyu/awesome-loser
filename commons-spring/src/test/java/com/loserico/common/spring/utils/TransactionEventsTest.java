package com.loserico.common.spring.utils;

import com.loserico.common.spring.transaction.TransactionEvents;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-04-25 11:54
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class TransactionEventsTest {
	
	@Test
	public void testExecuteAfterTransactionCommit() {
		TransactionEvents.instance().afterCommit(() -> {
			log.info("事务正确提交后执行该任务");
		});
		
		TransactionEvents.instance().afterCompletion(() -> {
			log.info("在事务正确提交或者回滚以后运行task");
		});
		
	}
}
