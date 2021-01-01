package com.loserico.search;

import com.idss.common.datafactory.search.ESSearch5_2_2;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-12-30 17:31
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DslSqlTest {
	
	@Test
	public void testHello() {
		ESSearch5_2_2 search = new ESSearch5_2_2();
		List<String> indexNames = search.getIndexNames(true);
	}
}
