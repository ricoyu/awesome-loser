package com.loserico.bigdata.napreduce.topn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * <p>
 * Copyright: (C), 2023-05-31 下午2:56
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class MyTopN {
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("mapreduce.framework.name", "local");
		conf.set("fs.defaultFS", "local");
		conf.set("mapreduce.app-submission.cross-platform", "true");
		String[] remainingArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		Job job = Job.getInstance(conf);
		job.setJarByClass(MyTopN.class);
		job.setJobName("TopN");
		
		TextInputFormat.addInputPath(job, new Path(remainingArgs[0]));
		Path outPath = new Path(remainingArgs[1]);
		if (outPath.getFileSystem(conf).exists(outPath)) {
			outPath.getFileSystem(conf).delete(outPath, true);
		}
		TextOutputFormat.setOutputPath(job, outPath);
		
		job.setMapperClass(TMapper.class);
		job.setMapOutputKeyClass(TKey.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setPartitionerClass(TPartitioner.class);
		job.setSortComparatorClass(TSortComparator.class);
		job.setGroupingComparatorClass(TGroupingComparator.class);
		
		job.setReducerClass(TReducer.class);
		
		job.waitForCompletion(true);
		
	}
}
