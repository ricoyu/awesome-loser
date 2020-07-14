package com.loserico.workbook.unmarshal.builder;

import com.loserico.workbook.annotation.Col;
import com.loserico.workbook.exception.InvalidConfigurationException;
import com.loserico.workbook.exception.NoCellCommandException;
import com.loserico.workbook.unmarshal.assassinator.POJOAssassinator;
import com.loserico.workbook.unmarshal.command.EnumCellCommand;
import com.loserico.workbook.utils.ReflectionUtils;
import com.loserico.workbook.unmarshal.command.BigDecimalCellCommand;
import com.loserico.workbook.unmarshal.command.BooleanCellCommand;
import com.loserico.workbook.unmarshal.command.DoubleCellCommand;
import com.loserico.workbook.unmarshal.command.IntegerCellCommand;
import com.loserico.workbook.unmarshal.command.LocalDateCellCommand;
import com.loserico.workbook.unmarshal.command.LocalDateTimeCellCommand;
import com.loserico.workbook.unmarshal.command.LongCellCommand;
import com.loserico.workbook.unmarshal.command.StringCellCommand;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * 负责构造 POJOAssassinator 对象, 但是该对象还没有完成初始化
 * 在这边只完成了POJO相关信息的初始化, 对应Excel中的相关信息还未初始化, 比如POJOAssassinator对应Sheet中的哪一列还未确定
 * 
 * <p>
 * Copyright: Copyright (c) 2019-05-23 16:01
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public class POJOAssassinatorBuilder {

	public static List<POJOAssassinator> build(Class<?> pojoType) {
		Objects.requireNonNull(pojoType);
		Map<Field, Col> fieldAnnotationMap = ReflectionUtils.annotatedField(pojoType, Col.class);

		List<POJOAssassinator> assassinators = new ArrayList<>(fieldAnnotationMap.size());
		for (Entry<Field, Col> entry : fieldAnnotationMap.entrySet()) {
			Field field = entry.getKey();
			Col annotation = entry.getValue();
			if (annotation == null) {
				log.warn("字段[{}]没有标注@Col(name = 'XX'), 忽略该字段", field.getName());
				continue;
			}
			POJOAssassinator assassinator = new POJOAssassinator();
			String name = annotation.name();
			assassinator.setColumnName(name == null ? null : name.trim());
			assassinator.setCellIndex(annotation.index());
			String fallback = annotation.fallback();
			assassinator.setFallbackName(fallback == null ? null : fallback.trim());
			if (isBlank(assassinator.getColumnName()) &&
					isBlank(assassinator.getFallbackName()) &&
					assassinator.getCellIndex() < 0) {
				throw new InvalidConfigurationException("Either name, fallbackName, index should be specified!");
			}
			assassinators.add(assassinator);

			Class<?> fieldType = field.getType();
			if (fieldType.isAssignableFrom(String.class)) {
				assassinator.setCellCommand(new StringCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(Long.class)) {
				assassinator.setCellCommand(new LongCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(LocalDate.class)) {
				assassinator.setCellCommand(new LocalDateCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(LocalDateTime.class)) {
				assassinator.setCellCommand(new LocalDateTimeCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(BigDecimal.class)) {
				assassinator.setCellCommand(new BigDecimalCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(Boolean.class)) {
				assassinator.setCellCommand(new BooleanCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(Double.class)) {
				assassinator.setCellCommand(new DoubleCellCommand(field));
				continue;
			}
			if (fieldType.isAssignableFrom(Integer.class)) {
				assassinator.setCellCommand(new IntegerCellCommand(field));
				continue;
			}
			if (fieldType.isEnum()) {
				assassinator.setCellCommand(new EnumCellCommand(field, annotation.enumProperty()));
			}
			if (assassinator.getCellCommand() == null) {
				throw new NoCellCommandException("No CellCommand found for type " + fieldType.getName());
			}
		}

		return assassinators;
	}
	
	private static boolean isBlank(String s) {
		return null == s || "".equals(s.trim());
	}
}
