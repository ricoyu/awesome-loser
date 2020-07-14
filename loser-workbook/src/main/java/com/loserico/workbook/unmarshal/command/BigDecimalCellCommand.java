package com.loserico.workbook.unmarshal.command;

import com.loserico.workbook.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

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
@Slf4j
public class BigDecimalCellCommand extends BaseCellCommand {
	
	private AtomicReference<Function<Cell, BigDecimal>> reference = new AtomicReference<Function<Cell,BigDecimal>>(null);
	
	public BigDecimalCellCommand(Field field) {
		super(field);
	}

	@Override
	public void invoke(Cell cell, Object pojo) {
		Function<Cell, BigDecimal> func = reference.get();
		if (func != null) {
			BigDecimal bigDecimal = null;
			try {
				bigDecimal = func.apply(cell);
				if (bigDecimal != null) {
					ReflectionUtils.setField(field, pojo, bigDecimal);
				}
				return;
			} catch (Exception e) {
				log.error("这是同一列出现了不同的数据格式吗?, Row[{}], Column[{}]" + e.getMessage(), cell.getRowIndex(), cell.getColumnIndex());
				reference.compareAndSet(func, null);
			}
		}
		
		if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
			Function<Cell, BigDecimal> convertor = (c) -> {
				double value = c.getNumericCellValue();
				return new BigDecimal(value);
			};
			reference.compareAndSet(null, convertor);
			BigDecimal bigDecimal = convertor.apply(cell);
			if (bigDecimal != null) {
				ReflectionUtils.setField(field, pojo, bigDecimal);
			}
			return;
		}
		
		Function<Cell, BigDecimal> convertor = (c) -> {
			String value = numericStr(c);
			if (null == value || "".equals(value)) {
				return null;
			}
			return new BigDecimal(value);
		};
		BigDecimal bigDecimal = convertor.apply(cell);
		reference.compareAndSet(null, convertor);
		if (bigDecimal != null) {
			ReflectionUtils.setField(field, pojo, bigDecimal);
		}
	}

}
