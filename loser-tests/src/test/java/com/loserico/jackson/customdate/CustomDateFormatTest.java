package com.loserico.jackson.customdate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.jackson.customserializer.Car;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomDateFormatTest {
	
	/**
	 * 输出日期格式: yyyy-MM-dd HH:mm a z
	 */
	@Test
	@SneakyThrows
	public void test() {
		ObjectMapper objectMapper = new ObjectMapper();
		String dateStr = objectMapper.writeValueAsString(new Date());
		assertThat(StringUtils.isNumeric(dateStr)).isTrue();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
		objectMapper.setDateFormat(simpleDateFormat);
		
		Car car = new Car("Yellow", "Benz");
		Request request = new Request(car, new Date());
		String requestStr = objectMapper.writeValueAsString(request);
		System.out.println(requestStr);
	}
}
