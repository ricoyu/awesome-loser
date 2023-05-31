package com.loserico.bigdata.napreduce.topn;

import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p>
 * Copyright: (C), 2023-05-31 下午3:23
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
@Data
public class TKey implements WritableComparable<TKey> {
	
	private int year;
	
	private int month;
	
	private int day;
	
	private int temp;
	
	@Override
	public int compareTo(TKey that) {
		int result = Integer.compare(this.year, that.year);
		if (result == 0) {
			result = Integer.compare(this.month, that.month);
		}
		if (result == 0) {
			result = Integer.compare(this.day, that.day);
		}
		//if (result == 0) {
		//	result = Integer.compare(this.temp, that.temp);
		//}
		return result;
	}
	
	/**
	 * 序列化
	 *
	 * @param out <code>DataOuput</code> to serialize this object into.
	 * @throws IOException
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(year);
		out.writeInt(month);
		out.writeInt(day);
		out.writeInt(temp);
	}
	
	/**
	 * 反序列化
	 *
	 * @param in <code>DataInput</code> to deseriablize this object from.
	 * @throws IOException
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		this.year = in.readInt();
		this.month = in.readInt();
		this.day = in.readInt();
		this.temp = in.readInt();
	}
}
