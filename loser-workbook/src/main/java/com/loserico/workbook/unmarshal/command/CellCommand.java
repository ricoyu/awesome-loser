package com.loserico.workbook.unmarshal.command;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 命令模式接口
 * 
 * 负责将Excel中Cell的内容写到POJO的field中
 * <p>
 * Copyright: Copyright (c) 2019-05-23 09:29
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface CellCommand {

	/**
	 * 将cell里面的值写到pojo相应的字段里面去, 同时做好类型转换工作
	 * 
	 * @param cell
	 * @param pojo
	 */
	void invoke(Cell cell, Object pojo);
}
