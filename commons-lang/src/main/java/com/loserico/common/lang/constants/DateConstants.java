package com.loserico.common.lang.constants;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.ENGLISH;
import static java.util.regex.Pattern.compile;

/**
 * 日期相关正则表达式常量, DateTimeFormatter常量等
 * <p>
 * Copyright: (C), 2019/11/12 20:18
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class DateConstants {
	/**
	 * -------------- 正则表达式, 日期格式对 -------------------------
	 */
	
	/**
	 * yyyy-MM-dd
	 */
	public static final Pattern PT_ISO_DATE = compile("\\d{4}-\\d{2}-\\d{2}");
	public static final String FMT_ISO_DATE = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd
	 */
	public static final DateTimeFormatter DTF_ISO_DATE = ofPattern(FMT_ISO_DATE);
	
	/**
	 * yyyy-MM-d
	 */
	public static final Pattern PT_ISO_DATE_1 = compile("\\d{4}-\\d{2}-\\d{1}");
	public static final String FMT_ISO_DATE_1 = "yyyy-MM-d";
	public static final DateTimeFormatter DTF_ISO_DATE_1 = ofPattern(FMT_ISO_DATE_1);
	
	/**
	 * yyyy-M-dd
	 */
	public static final Pattern PT_ISO_DATE_2 = compile("\\d{4}-\\d{1}-\\d{2}");
	public static final String FMT_ISO_DATE_2 = "yyyy-M-dd";
	public static final DateTimeFormatter DTF_ISO_DATE_2 = ofPattern(FMT_ISO_DATE_2);
	
	/**
	 * yyyy-M-d
	 */
	public static final Pattern PT_ISO_DATE_3 = compile("\\d{4}-\\d{1}-\\d{1}");
	public static final String FMT_ISO_DATE_3 = "yyyy-M-d";
	public static final DateTimeFormatter DTF_ISO_DATE_3 = ofPattern(FMT_ISO_DATE_3);
	
	/**
	 * MM-dd-yyyy
	 */
	public static final Pattern PT_DATE_EN = compile("\\d{2}-\\d{2}-\\\\d{4}");
	public static final String FMT_DATE_FORMAT_EN = "MM/dd/yyyy";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN = ofPattern(FMT_DATE_FORMAT_EN);
	
	/**
	 * MM-d-yyyy
	 */
	public static final Pattern PT_DATE_EN_1 = compile("\\d{2}-\\d{1}-\\\\d{4}");
	public static final String FMT_DATE_FORMAT_EN_1 = "MM/d/yyyy";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_1 = ofPattern(FMT_DATE_FORMAT_EN_1);
	
	/**
	 * M-dd-yyyy
	 */
	public static final Pattern PT_DATE_EN_2 = compile("\\d{1}-\\d{2}-\\\\d{4}");
	public static final String FMT_DATE_FORMAT_EN_2 = "M/dd/yyyy";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_2 = ofPattern(FMT_DATE_FORMAT_EN_2);
	
	/**
	 * M-d-yyyy
	 */
	public static final Pattern PT_DATE_EN_3 = compile("\\d{1}-\\d{1}-\\\\d{4}");
	public static final String FMT_DATE_FORMAT_EN_3 = "M/d/yyyy";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_3 = ofPattern(FMT_DATE_FORMAT_EN_3);
	
	/**
	 * yyyy/MM/dd
	 */
	public static final Pattern PT_DATE_EN_4 = compile("\\d{4}/\\d{2}/\\d{2}");
	public static final String FMT_DATE_FORMAT_EN_4 = "yyyy/MM/dd";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_4 = ofPattern(FMT_DATE_FORMAT_EN_4);
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_LOCALE_4 = ofPattern(FMT_DATE_FORMAT_EN_4, ENGLISH);
	
	/**
	 * yyyy/MM/d
	 */
	public static final Pattern PT_DATE_EN_5 = compile("\\d{4}/\\d{2}/\\d{1}");
	public static final String FMT_DATE_FORMAT_EN_5 = "yyyy/MM/d";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_5 = ofPattern(FMT_DATE_FORMAT_EN_5);
	
	/**
	 * yyyy/M/dd
	 */
	public static final Pattern PT_DATE_EN_6 = compile("\\d{4}/\\d{1}/\\d{2}");
	public static final String FMT_DATE_FORMAT_EN_6 = "yyyy/M/dd";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_6 = ofPattern(FMT_DATE_FORMAT_EN_6);
	
	/**
	 * yyyy/M/d
	 */
	public static final Pattern PT_DATE_EN_7 = compile("\\d{4}/\\d{1}/\\d{1}");
	public static final String FMT_DATE_FORMAT_EN_7 = "yyyy/M/d";
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_7 = ofPattern(FMT_DATE_FORMAT_EN_7);
	
	/**
	 * yyyyMMdd
	 */
	public static final Pattern PT_DATE_CONCISE = compile("\\d{8}");
	public static final String FMT_DATE_CONCISE = "yyyyMMdd";
	public static final DateTimeFormatter DTF_DATE_CONCISE = ofPattern(FMT_DATE_CONCISE);
	
	/**
	 * d-MMM-yy
	 */
	public static final Pattern PT_DATE_FORMAT_EN_8 = compile("\\d{1}-\\w{3}-\\d{2}");
	public static final String FMT_DATE_FORMAT_EN_8 = "d-MMM-yy"; // 15-Sep-18 1-Sep-18 这种格式
	public static final DateTimeFormatter DTF_DATE_FORMAT_EN_8 = ofPattern(FMT_DATE_FORMAT_EN_8);
	
	
	// ----------------------- 下面是日期时间类型 ---------------------------------------------------
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final Pattern PT_ISO_DATETIME = compile("\\d{4}-\\d{2}-\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME = ofPattern(FMT_ISO_DATETIME);
	
	/**
	 * yyyy-MM-d HH:mm:ss
	 */
	public static final Pattern PT_ISO_DATETIME_1 = compile("\\d{4}-\\d{2}-\\d{1}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_1 = "yyyy-MM-d HH:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME_1 = ofPattern(FMT_ISO_DATETIME_1);
	
	/**
	 * yyyy-M-dd HH:mm:ss
	 */
	public static final Pattern PT_ISO_DATETIME_2 = compile("\\d{4}-\\d{1}-\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_2 = "yyyy-M-dd HH:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME_2 = ofPattern(FMT_ISO_DATETIME_2);
	
	/**
	 * yyyy-M-d HH:mm:ss
	 */
	public static final Pattern PT_ISO_DATETIME_3 = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_3 = "yyyy-M-d HH:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME_3 = ofPattern(FMT_ISO_DATETIME_3);
	
	/**
	 * yyyy-M-d H:mm:ss
	 */
	public static final Pattern PT_ISO_DATETIME_4 = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{1}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_4 = "yyyy-M-d H:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME_4 = ofPattern(FMT_ISO_DATETIME_4);
	
	/**
	 * yyyy-MM-d H:mm:ss
	 */
	public static final Pattern PT_ISO_DATETIME_5 = compile("\\d{4}-\\d{2}-\\d{1}(\\s+)\\d{1}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_5 = "yyyy-MM-d H:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME_5 = ofPattern(FMT_ISO_DATETIME_5);
	
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT = compile("\\d{4}-\\d{2}-\\d{2}(\\s+)\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT = "yyyy-MM-dd HH:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT = ofPattern(FMT_ISO_DATETIME_SHORT);
	
	/**
	 * yyyy-MM-d HH:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_1 = compile("\\d{4}-\\d{2}-\\d{1}(\\s+)\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_1 = "yyyy-MM-d HH:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_1 = ofPattern(FMT_ISO_DATETIME_SHORT_1);
	
	/**
	 * yyyy-M-dd HH:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_2 = compile("\\d{4}-\\d{1}-\\d{2}(\\s+)\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_2 = "yyyy-M-dd HH:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_2 = ofPattern(FMT_ISO_DATETIME_SHORT_2);
	
	/**
	 * yyyy-M-d HH:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_3 = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_3 = "yyyy-M-d HH:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_3 = ofPattern(FMT_ISO_DATETIME_SHORT_3);
	
	/**
	 * yyyy-MM-dd H:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_4 = compile("\\d{4}-\\d{2}-\\d{2}(\\s+)\\d{1}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_4 = "yyyy-MM-dd H:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_4 = ofPattern(FMT_ISO_DATETIME_SHORT_4);
	
	/**
	 * yyyy-MM-d H:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_5 = compile("\\d{4}-\\d{2}-\\d{1}(\\s+)\\d{1}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_5 = "yyyy-MM-d H:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_5 = ofPattern(FMT_ISO_DATETIME_SHORT_5);
	
	/**
	 * yyyy-M-dd H:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_6 = compile("\\d{4}-\\d{1}-\\d{2}(\\s+)\\d{1}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_6 = "yyyy-M-dd H:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_6 = ofPattern(FMT_ISO_DATETIME_SHORT_6);
	
	/**
	 * yyyy-M-d H:mm
	 */
	public static final Pattern PT_ISO_DATETIME_SHORT_7 = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{1}:\\d{2}");
	public static final String FMT_ISO_DATETIME_SHORT_7 = "yyyy-M-d H:mm";
	public static final DateTimeFormatter DTF_ISO_DATETIME_SHORT_7 = ofPattern(FMT_ISO_DATETIME_SHORT_7);
	
	/**
	 * MM/dd/yyyy HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN = compile("\\d{2}/\\d{2}/\\d{4}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN = "MM/dd/yyyy HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN = ofPattern(FMT_DATETIME_FORMAT_EN);
	
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_1 =
			compile("\\d{4}/\\d{2}/\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_1 = "yyyy/MM/dd HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_1 = ofPattern(FMT_DATETIME_FORMAT_EN_1);
	
	/**
	 * yyyy/MM/d HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_2 =
			compile("\\d{4}/\\d{2}/\\d{1}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_2 = "yyyy/MM/d HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_2 = ofPattern(FMT_DATETIME_FORMAT_EN_2);
	
	/**
	 * yyyy/M/dd HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_3 =
			compile("\\d{4}/\\d{1}/\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_3 = "yyyy/M/dd HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_3 = ofPattern(FMT_DATETIME_FORMAT_EN_3);
	
	/**
	 * yyyy/M/d HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_4 = compile("\\d{4}/\\d{1}/\\d{1}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_4 = "yyyy/M/d HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_4 = ofPattern(FMT_DATETIME_FORMAT_EN_4);
	
	/**
	 * MM/dd/yyyy HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_5 = compile("\\d{2}/\\d{2}/\\d{4}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_5 = "MM/dd/yyyy HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_5 = ofPattern(FMT_DATETIME_FORMAT_EN_5);
	
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_6 = compile("\\d{4}/\\d{2}/\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_6 = "yyyy/MM/dd HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_6 = ofPattern(FMT_DATETIME_FORMAT_EN_6);
	
	/**
	 * yyyy/MM/d HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_7 = compile("\\d{4}/\\d{2}/\\d{1}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_7 = "yyyy/MM/d HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_7 = ofPattern(FMT_DATETIME_FORMAT_EN_7);
	
	/**
	 * yyyy/M/dd HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_8 = compile("\\d{4}/\\d{1}/\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_8 = "yyyy/M/dd HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_8 = ofPattern(FMT_DATETIME_FORMAT_EN_8);
	
	/**
	 * yyyy/M/d HH:mm:ss
	 */
	public static final Pattern PT_DATETIME_FORMAT_EN_9 = compile("\\d{4}/\\d{1}/\\d{1}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_DATETIME_FORMAT_EN_9 = "yyyy/M/d HH:mm:ss";
	public static final DateTimeFormatter DTF_DATETIME_FORMAT_EN_9 = ofPattern(FMT_DATETIME_FORMAT_EN_9);
	
	/**
	 * Http 请求头中的日期格式<p>
	 * format for RFC 1123 date string -- "Sun, 06 Nov 1994 08:49:37 GMT"
	 */
	//TODO GMT pattern  https://howtodoinjava.com/java/date-time/parse-string-to-date-time-utc-gmt/
	// https://stackoverflow.com/questions/685377/how-to-check-an-utc-formatted-date-with-a-regular-expression
	public final static String FMT_RFC1123_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
	public static final DateTimeFormatter DFT_DATETIME_FORMAT_RFC = ofPattern(FMT_RFC1123_FORMAT, ENGLISH);
	public static final SimpleDateFormat SDT_GMT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
	static {
		SDT_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	/**
	 * UTC 时间格式<p> 
	 * 'Z' in date string represents the UTC timezone<p>
	 * 2021-05-22T02:01:43.003Z
	 * 2019-03-08T16:20:17:717 UTC+05:30  yyyy-MM-dd’T’HH:mm:ss:SSS z
	 */
	public static final Pattern PT_UTC_DATETIME = Pattern.compile("^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z)?$");
	public static final String FMT_UTC_DATETIME = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String FMT_UTC_DATETIME2 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final DateTimeFormatter DFT_UTC_DATETIME = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss:SSS z");
	
	// ----------------------- 下面是时间类型 ---------------------------------------------------
	
	/**
	 * HH:mm:ss
	 */
	public static final Pattern PT_TIME_FORMAT = compile("\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_TIME_FORMAT = "HH:mm:ss";
	public static final DateTimeFormatter DTF_TIME_FORMAT = ofPattern(FMT_TIME_FORMAT);
	
	/**
	 * HH:mm
	 */
	public static final Pattern PT_TIME_FORMAT1 = compile("\\d{2}:\\d{2}");
	public static final String FMT_TIME_FORMAT1 = "HH:mm";
	public static final DateTimeFormatter DTF_TIME_FORMAT1 = ofPattern(FMT_TIME_FORMAT1);
	
	/**
	 * H:mm:ss
	 */
	public static final Pattern PT_TIME_FORMAT2 = compile("\\d{1}:\\d{2}:\\d{2}");
	public static final String FMT_TIME_FORMAT2 = "H:mm:ss";
	public static final DateTimeFormatter DTF_TIME_FORMAT2 = ofPattern(FMT_TIME_FORMAT2);
	
	/**
	 * H:mm
	 */
	public static final Pattern PT_TIME_FORMAT3 = compile("\\d{1}:\\d{2}");
	public static final String FMT_TIME_FORMAT3 = "H:mm";
	public static final DateTimeFormatter DTF_TIME_FORMAT3 = ofPattern(FMT_TIME_FORMAT3);
	
	/**
	 * 2020-12-23T12:51:18.456019+0200
	 */
	public static final Pattern PT_ALL = Pattern.compile("(\\d{2,4})-(\\d{1,2})-(\\d{1,2})(T?)\\s*(\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\.?(\\d*)(\\+\\d+)?");
	
	public static final TimeZone CHINA = TimeZone.getTimeZone("Asia/Shanghai");
	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
	public static final TimeZone PST = TimeZone.getTimeZone("America/Los_Angeles");
	public static final TimeZone LONDON = TimeZone.getTimeZone("Europe/London");
	public static final TimeZone INDIA = TimeZone.getTimeZone("Asia/Calcutta");
	public static final TimeZone JAPAN = TimeZone.getTimeZone("Asia/Tokyo");
	
	public static final Map<TimeZone, Locale> TIME_ZONE_LOCALE_HASH_MAP = new HashMap<>();
	
	static {
		TIME_ZONE_LOCALE_HASH_MAP.put(CHINA, Locale.CHINA);
		TIME_ZONE_LOCALE_HASH_MAP.put(GMT, Locale.ENGLISH);
		TIME_ZONE_LOCALE_HASH_MAP.put(UTC, Locale.ENGLISH);
		TIME_ZONE_LOCALE_HASH_MAP.put(PST, Locale.ENGLISH);
		TIME_ZONE_LOCALE_HASH_MAP.put(LONDON, Locale.ENGLISH);
		TIME_ZONE_LOCALE_HASH_MAP.put(INDIA, Locale.ENGLISH);
		TIME_ZONE_LOCALE_HASH_MAP.put(JAPAN, Locale.JAPAN);
	}
	
	public static final ZoneId ZONE_ID_SHANG_HAI = ZoneId.of("Asia/Shanghai");
	public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
	public static final ZoneId ZONE_ID_GMT = ZoneId.of("GMT");
}
