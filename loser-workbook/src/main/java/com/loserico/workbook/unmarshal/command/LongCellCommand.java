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
 * Copyright: Copyright (c) 2019-10-15 10:48
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class LongCellCommand extends BaseCellCommand {
	
	private AtomicReference<Function<Cell, Long>> atomicReference = new AtomicReference<Function<Cell,Long>>(null);
	
	public LongCellCommand(Field field) {
		super(field);
	}

	@Override
	public void invoke(Cell cell, Object pojo) {
		Function<Cell, Long> func = atomicReference.get();
		if (func != null) {
			Long longValue = null;
			try {
				longValue = func.apply(cell);
				if (longValue != null) {
					ReflectionUtils.setField(field, pojo, longValue);
				}
				return;
			} catch (Exception e) {
				log.error("这是同一列出现了不同的数据格式吗?, Row[{}], Column[{}]" + e.getMessage(), cell.getRowIndex(), cell.getColumnIndex());
				atomicReference.compareAndSet(func, null);
			}
		}
		
		if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
			Function<Cell, Long> convertor = (c) -> (long) c.getNumericCellValue();
			atomicReference.compareAndSet(null, convertor);
			
			Long longValue = convertor.apply(cell);
			if (longValue != null) {
				ReflectionUtils.setField(field, pojo, longValue);
			}
			return;
		}
		
		Function<Cell, Long> convertor = (c) -> {
			String value = str(c);
			if (null == value || "".equals(value)) {
				return null;
			}
			return Long.parseLong(value);
		};
		
		Long longValue = convertor.apply(cell);
		if (longValue != null) {
			ReflectionUtils.setField(field, pojo, longValue);
		}
		atomicReference.compareAndSet(null, convertor);
	}

}
