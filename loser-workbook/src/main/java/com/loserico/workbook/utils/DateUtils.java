package com.loserico.workbook.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: Copyright (c) 2019/10/15 10:28
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class DateUtils {

	/**
	 * yyyy-MM-dd
	 */
	private static final Pattern PT_ISO_DATE = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
	public static final String FMT_ISO_DATE = "yyyy-MM-dd";
	public static final DateTimeFormatter DTF_ISO_DATE = ofPattern(FMT_ISO_DATE);

	// yyyy-MM-dd HH:mm:ss
	private static final Pattern PT_ISO_DATETIME = Pattern.compile("\\d{4}-\\d{2}-\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}");
	public static final String FMT_ISO_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final DateTimeFormatter DTF_ISO_DATETIME = ofPattern(FMT_ISO_DATETIME);

	public static TimeZone CHINA = TimeZone.getTimeZone("Asia/Shanghai");

	public static Map<TimeZone, Locale> timeZoneLocaleMap = new HashMap<>();
	static {
		timeZoneLocaleMap.put(CHINA, Locale.CHINA);
	}
	
	/**
	 * 日期字符串转LocalDate，根据日期模式自动匹配格式
	 *
	 * @param source
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(String source) {
		Objects.nonNull(source);
		if (PT_ISO_DATE.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_ISO_DATE);
		}
		log.warn("{} does not match any LocalDate format! ", source);
		return null;
	}

	/**
	 * 日期字符串转LocalDateTime，根据日期模式自动匹配格式
	 *
	 * @param source
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(String source) {
		if (isBlank(source)) {
			return null;
		}
		if (PT_ISO_DATETIME.matcher(source).matches()) {
			return LocalDateTime.parse(source, ofPattern(FMT_ISO_DATETIME));
		}
		log.warn("{} does not match any LocalDateTime format! ", source);
		return null;
	}

	/**
	 * 将Date用系统默认时区转成LocalDate
	 *
	 * @param date
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDateTime.toLocalDate();
	}


	/**
	 * 将Date用系统默认时区转成LocalDateTime
	 *
	 * @param date
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}


	/**
	 * 根据指定的format格式化Date对象, 时区为"Asia/Shanghai", Locale为CHINA
	 *
	 * @param date
	 * @return String
	 */
	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format);
		return simpleDateFormat.format(date);
	}


	/**
	 * 根据指定的format格式化LocalDate对象
	 *
	 * @param localDate
	 * @return String
	 */
	public static String format(LocalDate localDate, String format) {
		if (localDate == null) {
			return null;
		}
		return localDate.format(ofPattern(format));
	}

	/**
	 * 根据指定的format格式化LocalDateTime对象
	 *
	 * @param localDateTime
	 * @return String
	 */
	public static String format(LocalDateTime localDateTime, String format) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.format(ofPattern(format));
	}

	final static class SimpleDateFormatHolder {

		private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>();

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
				Locale locale = timeZoneLocaleMap.get(timezone.getID());
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

		public static void clearThreadLocal() {
			THREADLOCAL_FORMATS.remove();
		}

	}
}
