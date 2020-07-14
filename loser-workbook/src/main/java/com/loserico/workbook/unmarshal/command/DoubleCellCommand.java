package com.loserico.workbook.unmarshal.command;

import com.loserico.workbook.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class DoubleCellCommand extends BaseCellCommand {

	private static Pattern moneyDoublePattern = Pattern.compile("\\$(.+)");

	private AtomicReference<Function<Cell, Double>> reference = new AtomicReference<Function<Cell, Double>>(null);

	public DoubleCellCommand(Field field) {
		super(field);
	}

	@Override
	public void invoke(Cell cell, Object pojo) {
		Function<Cell, Double> func = reference.get();
		if (func != null) {
			Double doubleValue = null;
			try {
				doubleValue = func.apply(cell);
				if (doubleValue != null) {
					ReflectionUtils.setField(field, pojo, doubleValue);
				}
				return;
			} catch (Exception e) {
				log.error("这是同一列出现了不同的数据格式吗?, Row[{}], Column[{}]" + e.getMessage(), cell.getRowIndex(), cell.getColumnIndex());
				reference.compareAndSet(func, null);
			}
		}

		if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
			Function<Cell, Double> convertor = c -> c.getNumericCellValue();
			reference.compareAndSet(null, convertor);
			Double doubleValue = convertor.apply(cell);
			if (doubleValue != null) {
				ReflectionUtils.setField(field, pojo, doubleValue);
			}
			return;
		}

		Function<Cell, Double> convertor = (c) -> {
			String value = str(c);

			Matcher matcher = moneyDoublePattern.matcher(value);
			if (matcher.matches()) {
				return Double.valueOf(matcher.group(1).replaceAll(",", ""));
			}
			return Double.valueOf(value.replaceAll(",", ""));
		};
		reference.compareAndSet(null, convertor);

		Double doubleValue = convertor.apply(cell);
		if (doubleValue != null) {
			ReflectionUtils.setField(field, pojo, doubleValue);
		}
		return;
	}

}
