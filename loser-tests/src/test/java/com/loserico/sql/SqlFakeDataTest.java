package com.loserico.sql;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Copyright: (C), 2019 2019/9/26 13:15
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SqlFakeDataTest {

	@Test
	public void testGenerateTxt() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< 10000000; i++) {
			sb.append(ThreadLocalRandom.current().nextInt(3000000)).append(",")
					.append(ThreadLocalRandom.current().nextInt(3000000)).append(",")
					.append(ThreadLocalRandom.current().nextInt(3000000)).append(IOUtils.LINE_SEPARATOR_UNIX);
		}
		IOUtils.write("d:/multi_col.txt", sb.toString());
	}
}
