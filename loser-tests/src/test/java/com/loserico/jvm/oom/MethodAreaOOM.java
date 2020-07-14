package com.loserico.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M -XX:+PrintGCDetails
 * <p>
 * Copyright: Copyright (c) 2019-08-02 15:08
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class MethodAreaOOM {
	
	private static final byte[] data = new byte[1024 * 1024 * 11];

	public static void main(String[] args) {
		// 使用lit保持常量池引用, 避免Full GC回收常量池行为
		List<String> list = new ArrayList<>();
		// 10MB的MetaspaceSize在Integer的范围内足够产生OOM了
		int i = 0;
		while (true) {
			list.add(String.valueOf(i++).intern());
		}
	}
}
