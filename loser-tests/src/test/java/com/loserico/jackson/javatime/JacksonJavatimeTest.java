package com.loserico.jackson.javatime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.loserico.jackson.CustomDateDeserializer;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JacksonJavatimeTest {

	/**
	 * What’s important here is that Jackson will serialize the Date to a timestamp
	 * format by default (number of milliseconds since January 1st, 1970, UTC).
	 * 这个测试的重点是：Jackson默认会序列化Date为格林威治时间1970年1月1号以来的毫秒数
	 * 
	 * @throws JsonProcessingException
	 * @throws ParseException
	 */
	@Test
	public void testSerializeToMiliseconds()
			throws JsonProcessingException, ParseException {

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = df.parse("01-01-1970 01:00");
		Event event = new Event("party", date);

		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(event);
		System.out.println(result);
	}

	/**
	 * Serializing to this terse timestamp format is not optimal. Let’s now serialize
	 * the Date to the ISO-8601 format: 
	 * 这次改为序列化Date成ISO格式的日期字符串
	 * 
	 * @throws ParseException
	 * @throws JsonProcessingException
	 * @on
	 */
	@Test
	public void testSerializingDateToHumanReableDateStr() throws ParseException, JsonProcessingException {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		String toParse = "01-01-1970 02:30";
		Date date = df.parse(toParse);
		Event event = new Event("party", date);

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(new ISO8601DateFormat());
		String result = mapper.writeValueAsString(event);
		System.out.println(result);
	}

	/**
	 * 测试用@JsonFormat注解来格式化json输出
	 * 
	 * @throws ParseException
	 * @throws JsonProcessingException
	 */
	@Test
	public void testJsonFormatAnnotation() throws ParseException, JsonProcessingException {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		String toParse = "20-12-2014 02:30:00";
		Date date = df.parse(toParse);
		Event2 event = new Event2("party", date);

		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(event);
		assertThat(result, Matchers.containsString(toParse));
	}

	/**
	 * 测试用自定义的序列化器
	 * @throws ParseException
	 * @throws JsonProcessingException
	 */
	@Test
	public void testCustomDateSerializer() throws ParseException, JsonProcessingException {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

		String toParse = "20-12-2014 02:30:00";
		Date date = df.parse(toParse);
		Event3 event = new Event3("party", date);

		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(event);
		assertThat(result, Matchers.containsString(toParse));
	}
	
	@Test
	public void testCustomDateDeserializer() throws JsonProcessingException, IOException {
		String json = "{\"name\":\"party\",\"eventDate\":\"20-12-2014 02:30:00\"}";
		 
	    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	    ObjectMapper mapper = new ObjectMapper();
	 
	    Event4 event = mapper.readerFor(Event4.class).readValue(json);
	    assertEquals("20-12-2014 02:30:00", df.format(event.eventDate));
	}
	
	@Test
	public void testSerializeLocalDate() throws JsonProcessingException {
		LocalDateTime date = LocalDateTime.of(2014, 12, 20, 2, 30);
		 
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	 
	    String result = mapper.writeValueAsString(date);
	    assertThat(result, Matchers.containsString("2014-12-20T02:30"));
	}

	private static class Event {
		public String name;

		public Date eventDate;

		public Event(String name, Date eventDate) {
			this.name = name;
			this.eventDate = eventDate;
		}

	};

	private static class Event2 {
		public String name;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
		public Date eventDate;

		public Event2(String name, Date eventDate) {
			this.name = name;
			this.eventDate = eventDate;
		}
	}

	private static class Event3 {
		public String name;

		@JsonSerialize(using = CustomDateSerializer.class)
		public Date eventDate;

		public Event3(String name, Date eventDate) {
			this.name = name;
			this.eventDate = eventDate;
		}
	}
	
	private static class Event4 {
		public String name;
		
		@JsonDeserialize(using = CustomDateDeserializer.class)
		public Date eventDate;
		
		public Event4(String name, Date eventDate) {
			this.name = name;
			this.eventDate = eventDate;
		}
	}
}
