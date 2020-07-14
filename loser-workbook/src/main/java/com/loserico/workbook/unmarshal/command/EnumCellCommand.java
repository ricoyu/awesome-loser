package com.loserico.workbook.unmarshal.command;

import com.loserico.common.lang.utils.EnumUtils;
import com.loserico.workbook.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 处理enum类型
 * <p>
 * Copyright: Copyright (c) 2019-12-14 10:06
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class EnumCellCommand extends BaseCellCommand {
	
	private AtomicReference<Function<Cell, Enum>> atomicReference = new AtomicReference<>();
	
	/**
	 * 如果Excel中某一列的值匹配的是POJO中某个Enum类型字段中的某个属性值, 那么需要指定属性的名字
	 * 如果匹配的是Enum字段的ordinal或者name则不需要指定
	 */
	private String property;
	
	public EnumCellCommand(Field field) {
		super(field);
	}
	
	public EnumCellCommand(Field field, String property) {
		super(field);
		this.property = property;
	}
	
	@Override
	public void invoke(Cell cell, Object pojo) {
		Function<Cell, Enum> func = atomicReference.get();
		if (func != null) {
			Enum enumValue = null;
			try {
				enumValue = func.apply(cell);
				if (enumValue != null) {
					ReflectionUtils.setField(field, pojo, enumValue);
				}
				return;
			} catch (Exception e) {
				log.error("这是同一列出现了不同的数据格式吗?, Row[{}], Column[{}]" + e.getMessage(), cell.getRowIndex(), cell.getColumnIndex());
				atomicReference.compareAndSet(func, null);
			}
		}
		
		if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
			Function<Cell, Enum> convertor = (c) -> {
				if (isNotBlank(property)) {
					return EnumUtils.lookupEnum(field.getType(), c, property);
				}
				return EnumUtils.lookupEnum(field.getType(), c);
			};
			atomicReference.compareAndSet(null, convertor);
			
			Enum enumValue = convertor.apply(cell);
			if (enumValue != null) {
				ReflectionUtils.setField(field, pojo, enumValue);
			}
			return;
		}
		
		Function<Cell, Enum> convertor = (c) -> {
			String value = str(c);
			if (value == null || "".equals(value)) {
				return null;
			}
			if (isNotBlank(property)) {
				return EnumUtils.lookupEnum(field.getType(), value, property);
			}
			return EnumUtils.lookupEnum(field.getType(), value);
		};
		atomicReference.compareAndSet(null, convertor);
		
		Enum enumValue = convertor.apply(cell);
		if (enumValue != null) {
			ReflectionUtils.setField(field, pojo, enumValue);
		}
	}
	
	private static boolean isNotBlank(String s) {
		return s != null && !"".equals(s.trim());
	}
	
}
