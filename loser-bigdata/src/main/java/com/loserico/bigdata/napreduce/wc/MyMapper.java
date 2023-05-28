package com.loserico.bigdata.napreduce.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * <p>
 * Copyright: (C), 2023-05-27 15:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MyMapper extends Mapper<Object, Text, Text, IntWritable> {
	
	public static final IntWritable one = new IntWritable(1);
	
	private Text word = new Text();
	
	/**
	 * 
	 * @param key  是每一行字符串的第一个字节在源文件里面的偏移量
	 * @param value
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer str = new StringTokenizer(value.toString());
		while (str.hasMoreTokens()) {
			word.set(str.nextToken());
			context.write(word, one);
		}
	}
}
