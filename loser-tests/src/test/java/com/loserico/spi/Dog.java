package com.loserico.spi;

/**
 * <p>
 * Copyright: (C), 2022-08-26 8:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Dog implements Behavial {
	@Override
	public void action() {
		System.out.println("汪汪汪~");
	}
}
