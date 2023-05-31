package com.loserico.bigdata.napreduce.topn;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Copyright: (C), 2023-05-31 下午3:39
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class    TMapper extends Mapper<LongWritable, Text, TKey, IntWritable> {
	
	TKey myKey = new TKey();
	IntWritable myVal = new IntWritable();
	
	public void map(LongWritable key, Text value,
	                Context context) throws IOException, InterruptedException {
		//value是 2019-6-1 22:22:22 1 31 这种格式
		String[] splits = StringUtils.split(value.toString(), '\t');
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(splits[0]);
			myKey.setYear(date.getYear());
			myKey.setMonth(date.getMonth());
			myKey.setDay(date.getDay());
			myKey.setTemp(Integer.parseInt(splits[2]));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		context.write(myKey, myVal);
	}
}
