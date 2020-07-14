package com.loserico.workbook.unmarshal.iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

/**
 * 
 * <p>
 * Copyright: Copyright (c) 2019-06-08 10:55
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class RowIterator<T> implements Iterator<Row> {
	
	private Sheet sheet;
	
	/**从哪一行开始*/
	private int startIndex;
	
	/**最后一行的index*/
	private int endIndex;
	
	/**当前读取到第几行, 初始和startIndex一样*/
	private int currentIndex;
	
	private int totalCount;
	
	public RowIterator(Sheet sheet, int startIndex) {
		this.sheet = sheet;
		this.startIndex = startIndex;
		this.currentIndex = startIndex;
		this.endIndex = sheet.getLastRowNum();
	} 

	@Override
	public boolean hasNext() {
		return this.currentIndex <= this.endIndex;
	}

	@Override
	public Row next() {
		return sheet.getRow(currentIndex++);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void reset() {
		this.currentIndex = this.startIndex;
	}
}
