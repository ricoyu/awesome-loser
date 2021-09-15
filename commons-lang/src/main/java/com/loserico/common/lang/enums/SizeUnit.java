package com.loserico.common.lang.enums;

import com.loserico.common.lang.exception.UnsupportedSizeUnitException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		public long toBytes(long value) { return value * 1024;}
		public long toKiloBytes(long value) { return value; }
	}, 
	
	MB {
		public long toBytes(long value) { return value * 1024 * 1024; }
		public long toKiloBytes(long value) { return value * 1024; }
	}, 
	
	GB {
		public long toBytes(long value) { return value * 1024 * 1024 * 1024; }
		public long toKiloBytes(long value) { return value * 1024 * 1024; }
	};
	
	public long toBytes(long value) {
		throw new AbstractMethodError();
	}
	
	public long toKiloBytes(long value) {
		throw new AbstractMethodError();
	}
	
	public static SizeUnit parse(String unit) {
		if ("k".equalsIgnoreCase(unit)) {
			return KB;
		}
		
		if ("kb".equalsIgnoreCase(unit)) {
			return KB;
		}
		
		if ("m".equalsIgnoreCase(unit)) {
			return MB;
		}
		
		if ("mb".equalsIgnoreCase(unit)) {
			return MB;
		}
		
		if ("g".equalsIgnoreCase(unit)) {
			return GB;
		}
		
		if ("gb".equalsIgnoreCase(unit)) {
			return GB;
		}
		
		String units = Stream.of(SizeUnit.values()).map(sizeUnit -> sizeUnit.name().toLowerCase()).collect(Collectors.joining(","));
		throw new UnsupportedSizeUnitException("支持的单位有: " + units);
	}
}
