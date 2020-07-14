package com.loserico.jvm;

/**
 * 对象的大小超过-XX:PretenureSizeThreshold就直接在老年代分配
 * 
 * VM参数: -XX:+UseSerialGC -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3M
 * <p>
 * Copyright: Copyright (c) 2019-08-05 14:25
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class PretenureSizeThresholdTest {

	private static final int _1MB = 1024 * 1024;

	public static void main(String[] args) {
		byte[] allocation = new byte[4 * _1MB]; // 直接在老年代分配
	}
}
