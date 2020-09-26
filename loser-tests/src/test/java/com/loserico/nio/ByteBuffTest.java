package com.loserico.nio;

import com.loserico.common.lang.utils.DateUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020-09-25 16:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ByteBuffTest {
	
	@Test
	public void test() {
		Supplier data = new Supplier(1, "三少爷", new Date());
		byte[] bytes = serialize(data);
		Supplier deserialize = deserialize(bytes);
		System.out.println(deserialize.getID());
		System.out.println(deserialize.getName());
		System.out.println(deserialize.getStartDate());
	}
	
	public byte[] serialize(Supplier data) {
		int sizeOfName;
		int sizeOfDate;
		
		byte[] serializedName;
		byte[] serializedDate;
		
		try {
			if (data == null) {
				return null;
			}
			
			serializedName = data.getName() == null ? new byte[0] : data.getName().getBytes(UTF_8);
			sizeOfName = serializedName.length;
			
			serializedDate = data.getStartDate() == null ? new byte[0] : DateUtils.format(data.getStartDate()).getBytes(UTF_8);
			sizeOfDate = serializedDate.length;
			
			ByteBuffer buf = ByteBuffer.allocate(4 + 4 + sizeOfName + 4 + sizeOfDate);
			buf.putInt(data.getID());
			buf.putInt(sizeOfName);
			buf.put(serializedName);
			buf.putInt(sizeOfDate);
			buf.put(serializedDate);
			
			return buf.array();
		} catch (Exception e) {
			throw new SerializationException("Error when serializing Supplier to byte[]");
		}
	}
	
	public Supplier deserialize(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		int id = buf.getInt();
		
		int sizeOfName = buf.getInt();
		byte[] nameBytes = new byte[sizeOfName];
		buf.get(nameBytes);
		
		String name = new String(nameBytes, UTF_8);
		
		int sizeOfDate = buf.getInt();
		byte[] dateBytes = new byte[sizeOfDate];
		buf.get(dateBytes);
		
		String dateStr = new String(dateBytes, UTF_8);
		Date date = DateUtils.parse(dateStr);
		
		return new Supplier(id, name, date);
	}
	
	static class Supplier {
		
		private int supplierId;
		
		private String supplierName;
		
		private Date supplierStartDate;
		
		public Supplier(int id, String name, Date dt) {
			this.supplierId = id;
			this.supplierName = name;
			this.supplierStartDate = dt;
		}
		
		public int getID() {
			return supplierId;
		}
		
		public String getName() {
			return supplierName;
		}
		
		public Date getStartDate() {
			return supplierStartDate;
		}
	}
}
