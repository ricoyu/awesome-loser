package com.loserico.chars;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-12-11 10:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CharacterTest {
	
	@Test
	public void testCharLength() {
		String s = "<STX>123;12;MC001;0;ERRS;100000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000;<ETX>";
		System.out.println(s.length());
	}
}
