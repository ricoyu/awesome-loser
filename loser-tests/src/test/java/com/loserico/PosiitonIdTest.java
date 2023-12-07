package com.loserico;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-12-05 16:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PosiitonIdTest {
	
	@Test
	public void test() {
		String positionId = "01001001S000101";
		String floor = positionId.substring(0, 2);
		String x = positionId.substring(2, 5);
		String y = positionId.substring(5, 8);
		System.out.println(floor +" " + x +" " +y);
		
	}
}
