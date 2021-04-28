package com.loserico.common.lang.enums;

/**
 * <p>
 * Copyright: (C), 2021-04-27 16:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum SizeUnit {
	
	KB {
		public int toBytes(int value) { return value * 1024;}
		public int toKiloBytes(int value) { return value; }
	}, 
	
	MB {
		public int toBytes(int value) { return value * 1024 * 1024; }
		public int toKiloBytes(int value) { return value * 1024; }
	}, 
	
	DB {
		public int toBytes(int value) { return value * 1024 * 1024 * 1024; }
		public int toKiloBytes(int value) { return value * 1024 * 1024; }
	};
	
	public int toBytes(int value) {
		throw new AbstractMethodError();
	}
	
	public int toKiloBytes(int value) {
		throw new AbstractMethodError();
	}
}
