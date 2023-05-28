package com.loserico.bigdata.napreduce.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * <p>
 * Copyright: (C), 2023-05-27 10:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MyWordCount {
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration(true);
		conf.set("mapreduce.framework.name", "local");
		//让框架知道是Windows平台异构运行
		conf.set("mapreduce.app-submission.cross-platform", "true");
		Job job = Job.getInstance(conf);
		
		job.setJar("D:\\Learning\\awesome-loser\\loser-bigdata\\target\\loser-bigdata-4.12.6.jar");
		//job.setJarByClass(MyWordCount.class);
		job.setJobName("MyFirstMRJob");
		
		TextInputFormat.addInputPath(job, new Path("/data/wc/input"));
		
		Path outPath = new Path("/data/wc/output");
		FileSystem fs = outPath.getFileSystem(conf);
		if(fs.exists(outPath)) {
			fs.delete(outPath, true);
		}
		TextOutputFormat.setOutputPath(job, outPath);
		
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setReducerClass(MYReducer.class);
		
		job.waitForCompletion(true);
	}
}
