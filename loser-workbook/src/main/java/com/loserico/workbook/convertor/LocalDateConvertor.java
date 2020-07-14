package com.loserico.workbook.convertor;

import com.loserico.workbook.utils.DateUtils;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 将Cell中读到的值转成LocalDate类型 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:31
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDateConvertor implements Convertor<Object, LocalDate> {
	
	private volatile AtomicReference<Function<Object, LocalDate>> atomicReference = new AtomicReference<Function<Object,LocalDate>>(null);

	@Override
	public LocalDate convert(Object source) {
		if (source == null) {
			return null;
		}
		
		if (atomicReference.get() != null) {
			return atomicReference.get().apply(source);
		}
		
		Function<Object, LocalDate> convertFunction = null;
		if (source instanceof LocalDate) {
			convertFunction = obj -> (LocalDate)obj;
			atomicReference.compareAndSet(null, convertFunction);
			convertFunction.apply(source);
		}
		
		if (source instanceof String) {
			convertFunction = (obj) -> {
				String dateStr = ((String)obj).trim();
				return DateUtils.toLocalDate(dateStr);
			};
			atomicReference.compareAndSet(null, convertFunction);
			return convertFunction.apply(source);
		}
		
		return null;
	}

}
