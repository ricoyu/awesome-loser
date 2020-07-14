package com.loserico.workbook.unmarshal.assassinator;

import com.loserico.workbook.unmarshal.command.CellCommand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 负责从Sheet的Row中读取正确的Cell, 并将Cell的值写入POJO的字段里面
 * <p>
 * Copyright: Copyright (c) 2019-05-23 15:08
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on 
 */
@Slf4j
@Data
public class POJOAssassinator {

	private CellCommand cellCommand;

	/**
	 * 0 based
	 */
	private int cellIndex = -1;

	/**
	 * 该列的名字
	 */
	private String columnName = null; 
	
	/**
	 * 如果columnName指定的名字在Excel的标题中找不到对应的列
	 * 那么取fallbackName来匹配Excel的标题
	 * @on
	 */
	private String fallbackName = null;
	
	public void assassinate(Row row, Object pojo) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return;
		}
		cellCommand.invoke(cell, pojo);
	}

}
