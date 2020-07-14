package com.loserico.unsafe;

import com.loserico.common.lang.magic.UnsafeInstance;
import sun.misc.Unsafe;

/**
 * <p>
 * Copyright: (C), 2019/11/22 13:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FenceTest {
	
	public static void main(String[] args) {
		Unsafe unsafe = UnsafeInstance.get();
		unsafe.loadFence(); //读屏障
		unsafe.storeFence();//写屏障
		unsafe.fullFence(); //读写屏障
	}
}
