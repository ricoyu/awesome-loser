package com.loserico.bigdata.napreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * <p>
 * Copyright: (C), 2023-05-31 下午4:10
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class TPartitioner extends Partitioner<TKey, IntWritable> {
	
	@Override
	public int getPartition(TKey key, IntWritable value, int numPartitions) {
		return key.getYear() % numPartitions;
	}
}
