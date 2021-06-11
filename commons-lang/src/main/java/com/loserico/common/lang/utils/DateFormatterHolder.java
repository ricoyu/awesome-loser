package com.loserico.common.lang.utils;

import java.lang.ref.SoftReference;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.loserico.common.lang.constants.DateConstants.CHINA;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_1;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_2;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_3;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_4;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_5;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_6;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_7;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_8;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATETIME_FORMAT_EN_9;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_1;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_2;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_3;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_4;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_5;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_6;
import static com.loserico.common.lang.constants.DateConstants.FMT_DATE_FORMAT_EN_7;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATE;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_1;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_2;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_3;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_4;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_5;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_1;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_2;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_3;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_4;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_5;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_6;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATETIME_SHORT_7;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATE_1;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATE_2;
import static com.loserico.common.lang.constants.DateConstants.FMT_ISO_DATE_3;
import static com.loserico.common.lang.constants.DateConstants.PT_ALL;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_1;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_2;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_3;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_4;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_5;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_6;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_7;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_8;
import static com.loserico.common.lang.constants.DateConstants.PT_DATETIME_FORMAT_EN_9;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_1;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_2;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_3;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_4;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_5;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_6;
import static com.loserico.common.lang.constants.DateConstants.PT_DATE_EN_7;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATE;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_1;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_2;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_3;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_4;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_5;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_1;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_2;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_3;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_4;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_5;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_6;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATETIME_SHORT_7;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATE_1;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATE_2;
import static com.loserico.common.lang.constants.DateConstants.PT_ISO_DATE_3;
import static com.loserico.common.lang.constants.DateConstants.TIME_ZONE_LOCALE_HASH_MAP;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * A factory for {@link DateTimeFormatter}s. The instances are stored in a threadlocal way
 * because DateTimeFormatter is not threadsafe as noted in {@link DateTimeFormatter its javadoc}.
 */
final class DateFormatterHolder {
	
	private static final ThreadLocal<SoftReference<Map<String, DateTimeFormatter>>> THREADLOCAL_FORMATS =
			new ThreadLocal<SoftReference<Map<String, DateTimeFormatter>>>();
	
	/**
	 * 获取DateTimeFormatter对象，timezone默认为Asia/Shanghai，locale为SIMPLIFIED_CHINESE
	 *
	 * @param pattern
	 * @return
	 */
	public static DateTimeFormatter formatFor(final String pattern) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, DateTimeFormatter>> ref = THREADLOCAL_FORMATS.get();
		Map<String, DateTimeFormatter> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, DateTimeFormatter>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, DateTimeFormatter>>(formats));
		}
		
		DateTimeFormatter format = formats.get(pattern);
		if (format == null) {
			format = DateTimeFormatter.ofPattern(pattern);
			format.withZone(CHINA.toZoneId());
			formats.put(pattern, format);
		}
		
		return format;
	}
	
	/**
	 * 根据format和timezone获取DateTimeFormatter对象，根据时区决定locale是什么
	 *
	 * @param pattern
	 * @param timezone
	 * @return
	 */
	public static DateTimeFormatter formatFor(final String pattern, TimeZone timezone) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, DateTimeFormatter>> ref = THREADLOCAL_FORMATS.get();
		Map<String, DateTimeFormatter> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, DateTimeFormatter>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, DateTimeFormatter>>(formats));
		}
		
		DateTimeFormatter format = formats.get(pattern + timezone.getID());
		if (format == null) {
			Locale locale = TIME_ZONE_LOCALE_HASH_MAP.get(timezone.getID());
			if (locale == null) {
				format = DateTimeFormatter.ofPattern(pattern);
			} else {
				format = DateTimeFormatter.ofPattern(pattern, locale);
			}
			format.withZone(timezone.toZoneId());
			formats.put(pattern + timezone.getID(), format);
		}
		
		return format;
	}
	
	/**
	 * 根据format,locale获取DateTimeFormatter对象，显示指定locale
	 *
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static DateTimeFormatter formatFor(final String pattern, Locale locale) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, DateTimeFormatter>> ref = THREADLOCAL_FORMATS.get();
		Map<String, DateTimeFormatter> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, DateTimeFormatter>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, DateTimeFormatter>>(formats));
		}
		
		DateTimeFormatter format = formats.get(pattern + locale.getCountry());
		if (format == null) {
			format = DateTimeFormatter.ofPattern(pattern, locale);
			formats.put(pattern + locale.getCountry(), format);
		}
		
		return format;
	}
	
	/**
	 * 根据format,timezone和locale获取DateTimeFormatter对象，显示指定timezone和locale
	 *
	 * @param pattern
	 * @param timezone
	 * @param locale
	 * @return
	 */
	public static DateTimeFormatter formatFor(final String pattern, TimeZone timezone, Locale locale) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, DateTimeFormatter>> ref = THREADLOCAL_FORMATS.get();
		Map<String, DateTimeFormatter> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, DateTimeFormatter>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, DateTimeFormatter>>(formats));
		}
		
		DateTimeFormatter format = formats.get(pattern + timezone.getID() + locale.getCountry());
		if (format == null) {
			format = DateTimeFormatter.ofPattern(pattern, locale);
			format.withZone(timezone.toZoneId());
			formats.put(pattern + timezone.getID() + locale.getCountry(), format);
		}
		
		return format;
	}
	
	public static DateTimeFormatter getDateTimeFormatter(String source) {
		if (matches(PT_ISO_DATETIME, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_1);
		}
		if (matches(PT_ISO_DATE, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE);
		}
		
		if (matches(PT_ISO_DATETIME_1, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_1);
		}
		
		if (matches(PT_ISO_DATETIME_2, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_2);
		}
		
		if (matches(PT_ISO_DATETIME_3, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_3);
		}
		
		if (matches(PT_ISO_DATETIME_4, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_4);
		}
		
		if (matches(PT_ISO_DATETIME_5, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_5);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT_1, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_1);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT_2, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_2);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT_3, source)) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_3);
		}
		
		if (PT_ISO_DATETIME_SHORT_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_4);
		}
		
		if (PT_ISO_DATETIME_SHORT_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_5);
		}
		
		if (PT_ISO_DATETIME_SHORT_6.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_6);
		}
		
		if (PT_ISO_DATETIME_SHORT_7.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_7);
		}
		
		if (PT_DATETIME_FORMAT_EN.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN);
		}
		
		if (PT_DATETIME_FORMAT_EN_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_1);
		}
		
		if (PT_DATETIME_FORMAT_EN_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_2);
		}
		
		if (PT_DATETIME_FORMAT_EN_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_3);
		}
		
		if (PT_DATETIME_FORMAT_EN_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_4);
		}
		
		if (PT_DATETIME_FORMAT_EN_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_5);
		}
		
		if (PT_DATETIME_FORMAT_EN_6.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_6);
		}
		
		if (PT_DATETIME_FORMAT_EN_7.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_7);
		}
		
		if (PT_DATETIME_FORMAT_EN_8.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_8);
		}
		
		if (PT_DATETIME_FORMAT_EN_9.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_9);
		}
		
		if (PT_ISO_DATE_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE_1);
		}
		
		if (PT_ISO_DATE_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE_2);
		}
		
		if (PT_ISO_DATE_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE_3);
		}
		
		if (PT_DATE_EN.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN);
		}
		
		if (PT_DATE_EN_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_1);
		}
		
		if (PT_DATE_EN_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_2);
		}
		
		if (PT_DATE_EN_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_3);
		}
		
		if (PT_DATE_EN_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_4);
		}
		
		if (PT_DATE_EN_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_5);
		}
		
		if (PT_DATE_EN_6.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_6);
		}
		
		if (PT_DATE_EN_7.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_7);
		}
		
		return finalShot(source);
	}
	
	public static DateTimeFormatter getDateTimeFormatter(String source, TimeZone timeZone) {
		
		if (PT_ISO_DATETIME.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_1, timeZone);
		}
		
		if (PT_ISO_DATE.matcher(source).matches()) { // yyyy-MM-dd
			return DateFormatterHolder.formatFor(FMT_ISO_DATE, timeZone);
		}
		
		if (PT_ISO_DATETIME_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_1, timeZone);
		}
		
		if (PT_ISO_DATETIME_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_2, timeZone);
		}
		
		if (PT_ISO_DATETIME_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_3, timeZone);
		}
		
		if (PT_ISO_DATETIME_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_4, timeZone);
		}
		
		if (PT_ISO_DATETIME_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_5, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_1, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_2, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_3, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_4, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_5, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_6.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_6, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_7.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATETIME_SHORT_7, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_1, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_2, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_3, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_4, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_5, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_6.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_6, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_7.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_7, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_8.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_8, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_9.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATETIME_FORMAT_EN_9, timeZone);
		}
		
		if (PT_ISO_DATE_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE_1, timeZone);
		}
		
		if (PT_ISO_DATE_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE_2, timeZone);
		}
		
		if (PT_ISO_DATE_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_ISO_DATE_3, timeZone);
		}
		
		if (PT_DATE_EN.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN, timeZone);
		}
		
		if (PT_DATE_EN_1.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_1, timeZone);
		}
		
		if (PT_DATE_EN_2.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_2, timeZone);
		}
		
		if (PT_DATE_EN_3.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_3, timeZone);
		}
		
		if (PT_DATE_EN_4.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_4, timeZone);
		}
		
		if (PT_DATE_EN_5.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_5, timeZone);
		}
		
		if (PT_DATE_EN_6.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_6, timeZone);
		}
		
		if (PT_DATE_EN_7.matcher(source).matches()) {
			return DateFormatterHolder.formatFor(FMT_DATE_FORMAT_EN_7, timeZone);
		}
		
		return finalShot(source, timeZone);
	}
	
	public static void clearThreadLocal() {
		THREADLOCAL_FORMATS.remove();
	}
	
	private static boolean matches(Pattern pattern, String source) {
		if (isBlank(source)) {
			return false;
		}
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();
	}
	
	private static boolean isBlank(String s) {
		return s == null || "".equals(s.trim());
	}
	
	private static DateTimeFormatter finalShot(String source) {
		return finalShot(source, null);
	}
	
	private static DateTimeFormatter finalShot(String source, TimeZone timeZone) {
		Matcher matcher = PT_ALL.matcher(source);
		if (!matcher.matches()) {
			return null;
		}
		
		String year = matcher.group(1);
		String month = matcher.group(2);
		String day = matcher.group(3);
		String t = matcher.group(4);
		String hour = matcher.group(5);
		String minute = matcher.group(6);
		String second = matcher.group(7);
		String milli = matcher.group(8);
		String zone = matcher.group(9);
		
		StringBuilder format = new StringBuilder();
		//yyyy
		if (isNotBlank(year)) {
			for (int i = 0; i < year.length(); i++) {
				format.append("y");
			}
		}
		format.append("-");
		
		//MM
		if (isNotBlank(month)) {
			for (int i = 0; i < month.length(); i++) {
				format.append("M");
			}
		}
		format.append("-");
		
		//dd
		if (isNotBlank(day)) {
			for (int i = 0; i < day.length(); i++) {
				format.append("d");
			}
		}
		
		//"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
		if (isNotBlank(t)) {
			format.append("'T'");
		} else {
			format.append(" ");
		}
		
		//HH
		if (isNotBlank(hour)) {
			for (int i = 0; i < hour.length(); i++) {
				format.append("H");
			}
		}
		
		//:mm
		if (isNotBlank(minute)) {
			format.append(":");
			for (int i = 0; i < minute.length(); i++) {
				format.append("m");
			}
		}
		
		//:ss
		if (isNotBlank(second)) {
			format.append(":");
			for (int i = 0; i < second.length(); i++) {
				format.append("s");
			}
		}
		//.SSSSSS
		if (isNotBlank(milli)) {
			format.append(".");
			for (int i = 0; i < milli.length(); i++) {
				format.append("S");
			}
		}
		//Z
		if (isNotBlank(zone)) {
			format.append("Z");
		}
		
		if (timeZone != null) {
			return DateFormatterHolder.formatFor(format.toString(), timeZone);
		}
		
		return DateFormatterHolder.formatFor(format.toString());
	}
	
}
