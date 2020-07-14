package com.loserico.collections;

import java.util.Comparator;
import java.util.Map;

public class KeyComparator implements Comparator<Object> {

	private Map<?, ?> map;

	public KeyComparator(Map<?, ?> map) {
		this.map = map;
	}

	public int compare(Object a, Object b) {
		if (null == a) {
			return 1;
		}
		if (null == b) {
			return -1;
		}
		return a.toString().compareTo(b.toString());
	}
}