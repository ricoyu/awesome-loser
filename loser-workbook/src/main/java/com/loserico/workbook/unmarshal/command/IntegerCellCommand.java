package com.loserico.workbook.unmarshal.command;

import com.loserico.workbook.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

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
public class IntegerCellCommand extends BaseCellCommand {
	
	private AtomicReference<Function<Cell, Integer>> atomicReference = new AtomicReference<>();

	public IntegerCellCommand(Field field) {
		super(field);
	}

	@Override
	public void invoke(Cell cell, Object pojo) {
		Function<Cell, Integer> func = atomicReference.get();
		if (func != null) {
			Integer integerValue = null;
			try {
				integerValue = func.apply(cell);
				if (integerValue != null) {
					ReflectionUtils.setField(field, pojo, integerValue);
				}
				return;
			} catch (Exception e) {
				log.error("这是同一列出现了不同的数据格式吗?, Row[{}], Column[{}]" + e.getMessage(), cell.getRowIndex(), cell.getColumnIndex());
				atomicReference.compareAndSet(func, null);
			}
		}
		
		if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
			Function<Cell, Integer> convertor = (c) -> (int)c.getNumericCellValue();
			atomicReference.compareAndSet(null, convertor);
			
			Integer integerValue = convertor.apply(cell);
			if (integerValue != null) {
				ReflectionUtils.setField(field, pojo, integerValue);
			}
			return;
		}
		
		Function<Cell, Integer> convertor = (c) -> {
			String value = str(c);
			if (value == null || "".equals(value)) {
				return null;
			}
			return Integer.valueOf(value);
		};
		atomicReference.compareAndSet(null, convertor);
		
		Integer integerValue = convertor.apply(cell);
		if (integerValue != null) {
			ReflectionUtils.setField(field, pojo, integerValue);
		}
	}

}
