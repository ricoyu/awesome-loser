package com.loserico.bigdata.napreduce.topn;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * <p>
 * Copyright: (C), 2023-05-31 下午4:17
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class TSortComparator extends WritableComparator {
	
	public TSortComparator() {
		super(TKey.class, true);
	}
	
	public int compare(WritableComparable a, WritableComparable b) {
		TKey k1 = (TKey) a;
		TKey k2 = (TKey) b;
		int result = Integer.compare(k1.getYear(), k2.getYear());
		if (result == 0) {
			result = Integer.compare(k1.getMonth(), k2.getMonth());
		}
		if (result == 0) {
			return -Integer.compare(k1.getTemp(), k2.getTemp());
		}
		
		return result;
	}
}
