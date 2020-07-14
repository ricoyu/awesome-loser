package com.loserico.workbook.unmarshal.command;

import com.loserico.workbook.utils.DateUtils;
import com.loserico.workbook.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 负责将Cell里的值设置到LocalDate类型的字段上
 * <p>
 * Copyright: Copyright (c) 2019-05-23 14:37
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class LocalDateCellCommand extends BaseCellCommand {

	/**
	 * yyyyMMdd
	 */
	private static final Pattern PT_DATE_CONCISE = Pattern.compile("\\d{8}");
	/**
	 * 20180711这种格式
	 */
	private static final DateTimeFormatter FMT_DATE_CONCISE = ofPattern("yyyyMMdd");

	private AtomicReference<Function<Cell, LocalDate>> atomicReference = new AtomicReference<Function<Cell, LocalDate>>(
			null);

	public LocalDateCellCommand(Field field) {
		super(field);
	}

	@Override
	public void invoke(final Cell cell, Object pojo) {
		// ------------- Step 1 -------------
		Function<Cell, LocalDate> func = atomicReference.get();
		if (func != null) {
			LocalDate localDate = null;
			try {
				localDate = func.apply(cell);
				if (localDate != null) {
					ReflectionUtils.setField(this.field, pojo, localDate);
				}
				return;
			} catch (Exception e) {
				log.error("这是同一列出现了不同的数据格式吗?, Row[{}], Column[{}]" + e.getMessage(), cell.getRowIndex(), cell.getColumnIndex());
				atomicReference.compareAndSet(func, null);
			}
		}

		CellType cellTypeEnum = cell.getCellTypeEnum();

		// ------------- Step 2 Cell内容是字符串类型的情况 -------------
		if (cellTypeEnum == CellType.STRING) {
			Function<Cell, LocalDate> functionConvertor = (c) -> {
				String cellValue = str(c);
				return DateUtils.toLocalDate(cellValue);
			};
			atomicReference.compareAndSet(null, functionConvertor);

			LocalDate localDate = functionConvertor.apply(cell);
			if (localDate != null) {
				ReflectionUtils.setField(field, pojo, localDate);
			}
			return;
		}

		// ------------- Step 3 假设Cell内容是Date类型的 -------------
		LocalDate localDate = DateUtils.toLocalDate(cell.getDateCellValue());
		if (localDate != null) {
			Function<Cell, LocalDate> functionConvertor = (c) -> {
				return DateUtils.toLocalDate(c.getDateCellValue());
			};
			ReflectionUtils.setField(field, pojo, localDate);
			atomicReference.compareAndSet(null, functionConvertor);
			return;
		}

		// ------------- Step 4 -------------
		/*
		 * 遇到Excel某列值看到的是2018-9-10这种形式的, 但获取到的CellType却是Numeric, 拿到的是类似65333这样的值
		 * 所以现在改成先拿DateCellValue(上面一步), 拿不到再走Numberic
		 */
		if (cellTypeEnum == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
			String dateStr = String.valueOf((long) cell.getNumericCellValue());
			if (PT_DATE_CONCISE.matcher(dateStr).matches()) {
				/*
				 * 因为functionConvertor一旦创建, 就会缓存起来, 下次就不会走到这边了
				 * 所以取值就得重新调用: String.valueOf((long) cell.getNumericCellValue())
				 */
				Function<Cell, LocalDate> functionConvertor = (c) -> {
					return LocalDate.parse(String.valueOf((long) c.getNumericCellValue()), FMT_DATE_CONCISE);
				};
				atomicReference.compareAndSet(null, functionConvertor);
				localDate = LocalDate.parse(dateStr, FMT_DATE_CONCISE);
				;
				if (localDate != null) {
					ReflectionUtils.setField(field, pojo, localDate);
				}
			}

			return;
		}
	}

}
