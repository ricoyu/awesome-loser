package com.loserico.workbook.unmarshal.command;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:45
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class BaseCellCommand implements CellCommand {

	/**
	 * 有些cell里面的值是双引号括起来的, 这里要去掉双引号
	 */
	protected static Pattern PATTERN_QUOTE = Pattern.compile("\"(.+)\"");

	/**
	 * 要写入的POJO的字段
	 */
	protected Field field;

	public BaseCellCommand(Field field) {
		this.field = field;
	}

	protected String str(Cell cell) {
		String cellValue = cell.getStringCellValue();
		if (cellValue == null || "".equals(cellValue.trim())) {
			return null;
		}

		cellValue = cellValue.trim();

		if ("\"\"".equals(cellValue)) {
			return null;
		}

		Matcher matcher = PATTERN_QUOTE.matcher(cellValue);
		if (matcher.matches()) {
			return matcher.group(1).trim();
		}

		return cellValue;
	}

	protected String numericStr(Cell cell) {
		String value = cell.getStringCellValue();
		if (value == null || "".equals(value.trim())) {
			return null;
		}
		value = value.trim();

		if ("\"\"".equals(value)) {
			return null;
		}

		Matcher matcher = PATTERN_QUOTE.matcher(value);
		if (matcher.matches()) {
			value = matcher.group(1).trim();
		}

		value = value.replaceAll(",", "")
				.replaceAll("_", "");

		return value;
	}
}
