package com.loserico.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <p>
 * Copyright: (C), 2022-08-26 8:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SpiTest {
	
	/**
	 * classpath下META-INF/services/com.loserico.spi.Behavial文件里面每行写一个实现类的全名即可
	 * @param args
	 */
	public static void main(String[] args) {
		ServiceLoader<Behavial> behavials = ServiceLoader.load(Behavial.class);
		Iterator<Behavial> iterator = behavials.iterator();
		while (iterator.hasNext()) {
			Behavial behavial = iterator.next();
			behavial.action();
		}
	}
}
