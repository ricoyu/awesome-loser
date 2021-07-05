package com.loserico;

/**
 * <p>
 * Copyright: (C), 2021-07-04 21:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BreakContinueLabelTest {
	
	public static void main(String[] args) {
		retry:
		for(;;) {
			System.out.println("outer");
			for(;;) {
				System.out.println("inner");
				break retry;
			}
		}
		System.out.println("end");
	}
}
