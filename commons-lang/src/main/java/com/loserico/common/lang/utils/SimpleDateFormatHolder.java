package com.loserico.common.lang.utils;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.loserico.common.lang.constants.DateConstants.*;

/**
 * A factory for {@link SimpleDateFormat}s. The instances are stored in a threadlocal way
 * because SimpleDateFormat is not threadsafe as noted in {@link SimpleDateFormat its javadoc}.
 */
final class SimpleDateFormatHolder {
	
	private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS =
			new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>();
	
	/**
	 * 获取SimpleDateFormat对象，timezone默认为Asia/Shanghai，locale为SIMPLIFIED_CHINESE
	 *
	 * @param pattern
	 * @return
	 */
	public static SimpleDateFormat formatFor(final String pattern) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
		Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, SimpleDateFormat>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
		}
		
		SimpleDateFormat format = formats.get(pattern);
		if (format == null) {
			format = new SimpleDateFormat(pattern, Locale.CHINA);
			format.setTimeZone(CHINA);
			formats.put(pattern, format);
		}
		
		return format;
	}
	
	/**
	 * 根据format和timezone获取SimpleDateFormat对象，根据时区决定locale是什么
	 *
	 * @param pattern
	 * @param timezone
	 * @return
	 */
	public static SimpleDateFormat formatFor(final String pattern, TimeZone timezone) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
		Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, SimpleDateFormat>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
		}
		
		SimpleDateFormat format = formats.get(pattern + timezone.getID());
		if (format == null) {
			Locale locale = TIME_ZONE_LOCALE_HASH_MAP.get(timezone.getID());
			if (locale == null) {
				format = new SimpleDateFormat(pattern);
			} else {
				format = new SimpleDateFormat(pattern, locale);
			}
			format.setTimeZone(timezone);
			formats.put(pattern + timezone.getID(), format);
		}
		
		return format;
	}
	
	/**
	 * 根据format,locale获取SimpleDateFormat对象，显示指定locale
	 *
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static SimpleDateFormat formatFor(final String pattern, Locale locale) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
		Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, SimpleDateFormat>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
		}
		
		SimpleDateFormat format = formats.get(pattern + locale.getCountry());
		if (format == null) {
			format = new SimpleDateFormat(pattern, locale);
			formats.put(pattern + locale.getCountry(), format);
		}
		
		return format;
	}
	
	/**
	 * 根据format,timezone和locale获取SimpleDateFormat对象，显示指定timezone和locale
	 *
	 * @param pattern
	 * @param timezone
	 * @param locale
	 * @return
	 */
	public static SimpleDateFormat formatFor(final String pattern, TimeZone timezone, Locale locale) {
		Objects.requireNonNull(pattern);
		final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
		Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
		if (formats == null) {
			formats = new HashMap<String, SimpleDateFormat>();
			THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
		}
		
		SimpleDateFormat format = formats.get(pattern + timezone.getID() + locale.getCountry());
		if (format == null) {
			format = new SimpleDateFormat(pattern, locale);
			format.setTimeZone(timezone);
			formats.put(pattern + timezone.getID() + locale.getCountry(), format);
		}
		
		return format;
	}
	
	public static SimpleDateFormat getSimpleDateFormat(String source) {
		if (matches(PT_ISO_DATETIME, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_1);
		}
		if (matches(PT_ISO_DATE, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE);
		}
		
		if (matches(PT_ISO_DATETIME_1, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_1);
		}
		
		if (matches(PT_ISO_DATETIME_2, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_2);
		}
		
		if (matches(PT_ISO_DATETIME_3, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_3);
		}
		
		if (matches(PT_ISO_DATETIME_4, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_4);
		}
		
		if (matches(PT_ISO_DATETIME_5, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_5);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT_1, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_1);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT_2, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_2);
		}
		
		if (matches(PT_ISO_DATETIME_SHORT_3, source)) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_3);
		}
		
		if (PT_ISO_DATETIME_SHORT_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_4);
		}
		
		if (PT_ISO_DATETIME_SHORT_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_5);
		}
		
		if (PT_ISO_DATETIME_SHORT_6.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_6);
		}
		
		if (PT_ISO_DATETIME_SHORT_7.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_7);
		}
		
		if (PT_DATETIME_FORMAT_EN.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN);
		}
		
		if (PT_DATETIME_FORMAT_EN_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_1);
		}
		
		if (PT_DATETIME_FORMAT_EN_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_2);
		}
		
		if (PT_DATETIME_FORMAT_EN_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_3);
		}
		
		if (PT_DATETIME_FORMAT_EN_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_4);
		}
		
		if (PT_DATETIME_FORMAT_EN_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_5);
		}
		
		if (PT_DATETIME_FORMAT_EN_6.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_6);
		}
		
		if (PT_DATETIME_FORMAT_EN_7.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_7);
		}
		
		if (PT_DATETIME_FORMAT_EN_8.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_8);
		}
		
		if (PT_DATETIME_FORMAT_EN_9.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_9);
		}
		
		if (PT_ISO_DATE_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE_1);
		}
		
		if (PT_ISO_DATE_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE_2);
		}
		
		if (PT_ISO_DATE_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE_3);
		}
		
		if (PT_DATE_EN.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN);
		}
		
		if (PT_DATE_EN_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_1);
		}
		
		if (PT_DATE_EN_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_2);
		}
		
		if (PT_DATE_EN_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_3);
		}
		
		if (PT_DATE_EN_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_4);
		}
		
		if (PT_DATE_EN_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_5);
		}
		
		if (PT_DATE_EN_6.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_6);
		}
		
		if (PT_DATE_EN_7.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_7);
		}
		return null;
	}
	
	public static SimpleDateFormat getSimpleDateFormat(String source, TimeZone timeZone) {
		
		if (PT_ISO_DATETIME.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_1, timeZone);
		}
		
		if (PT_ISO_DATE.matcher(source).matches()) { // yyyy-MM-dd
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE, timeZone);
		}
		
		if (PT_ISO_DATETIME_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_1, timeZone);
		}
		
		if (PT_ISO_DATETIME_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_2, timeZone);
		}
		
		if (PT_ISO_DATETIME_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_3, timeZone);
		}
		
		if (PT_ISO_DATETIME_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_4, timeZone);
		}
		
		if (PT_ISO_DATETIME_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_5, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_1, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_2, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_3, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_4, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_5, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_6.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_6, timeZone);
		}
		
		if (PT_ISO_DATETIME_SHORT_7.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_SHORT_7, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_1, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_2, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_3, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_4, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_5, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_6.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_6, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_7.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_7, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_8.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_8, timeZone);
		}
		
		if (PT_DATETIME_FORMAT_EN_9.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATETIME_FORMAT_EN_9, timeZone);
		}
		
		if (PT_ISO_DATE_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE_1, timeZone);
		}
		
		if (PT_ISO_DATE_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE_2, timeZone);
		}
		
		if (PT_ISO_DATE_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_ISO_DATE_3, timeZone);
		}
		
		if (PT_DATE_EN.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN, timeZone);
		}
		
		if (PT_DATE_EN_1.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_1, timeZone);
		}
		
		if (PT_DATE_EN_2.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_2, timeZone);
		}
		
		if (PT_DATE_EN_3.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_3, timeZone);
		}
		
		if (PT_DATE_EN_4.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_4, timeZone);
		}
		
		if (PT_DATE_EN_5.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_5, timeZone);
		}
		
		if (PT_DATE_EN_6.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_6, timeZone);
		}
		
		if (PT_DATE_EN_7.matcher(source).matches()) {
			return SimpleDateFormatHolder.formatFor(FMT_DATE_FORMAT_EN_7, timeZone);
		}
		
		return null;
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
	
}