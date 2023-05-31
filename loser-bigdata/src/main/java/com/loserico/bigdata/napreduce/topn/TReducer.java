package com.loserico.bigdata.napreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * <p>
 * Copyright: (C), 2023-05-31 下午4:45
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class TReducer extends Reducer<TKey, IntWritable, Text, IntWritable> {
	
	Text rkey = new Text();
	IntWritable rval = new IntWritable();
	
	@Override
	protected void reduce(TKey key, Iterable<IntWritable> values,
	                      Context context) throws IOException, InterruptedException {
		Iterator<IntWritable> iterator = values.iterator();
		
		int flag = 0;
		int day = 0;
		while (iterator.hasNext()) {
			IntWritable value = iterator.next();
			if (flag == 0) {
				rkey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay());
				rval.set(key.getTemp());
				context.write(rkey, rval);
				flag++;
				day = key.getDay();
			}
			
			if (flag != 0  && day != key.getDay()) {
				rkey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay());
				rval.set(key.getTemp());
				context.write(rkey, rval);
				break;
			}
		}
	}
}
