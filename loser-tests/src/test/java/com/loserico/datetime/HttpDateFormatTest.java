package com.loserico.datetime;

import com.loserico.common.lang.utils.DateUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HttpDateFormatTest {

	@Test
	public void testDateHeaderFormat() throws ParseException {
		Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    String format = dateFormat.format(calendar.getTime());
	    System.out.println(format);
	    
	    Date date = dateFormat.parse(format);
	    LocalDateTime localDateTime = DateUtils.toLocalDateTime(date);
	    System.out.println(localDateTime);
	}
	
	@Test
	public void testParse() {
		//2018-08-03T11:42:07
		String dateHeader = "Fri, 03 Sep 2018 23:42:07 GMT";
		String dateHeaderPattern = "[MTWFS][a-z]{2},\\s+\\d{2}\\s+[JFMASOND][a-z]{2}\\s+\\d{4}\\s+\\d{2}:\\d{2}:\\d{2}\\s+GMT";
		boolean matches = dateHeader.matches(dateHeaderPattern);
		System.out.println(matches);
		if(matches) {
			Date date = DateUtils.parse(dateHeader);
			System.out.println(date);
			LocalDateTime localDateTime = DateUtils.toLocalDateTime(dateHeader);
			System.out.println(localDateTime);
		}
	}
}
