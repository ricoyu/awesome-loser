package com.loserico.common.lang.utils;

import com.loserico.common.lang.constants.DateConstants;
import com.loserico.common.lang.exception.DateParseException;
import com.loserico.common.lang.exception.NoDateFormatFoundException;
import com.loserico.common.lang.exception.UnsupportedLocalDateFormatException;
import com.loserico.common.lang.exception.UnsupportedLocalDateTimeFormatException;
import com.loserico.common.lang.exception.UnsupportedLocalTimeFormatException;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.loserico.common.lang.constants.DateConstants.*;
import static com.loserico.common.lang.utils.SimpleDateFormatHolder.getSimpleDateFormat;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 最全, 功能最完整的DateUtils
 * <p>
 * Copyright: (C), 2019/11/7 9:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class DateUtils {
	
	private DateUtils() {
	}
	
	/**
	 * 采用"yyyy-MM-dd HH:mm:ss"格式化Date对象, 时区为"Asia/Shanghai", Locale为CHINA
	 *
	 * @param date
	 * @return String
	 */
	public static String format(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME);
		return simpleDateFormat.format(date);
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
	 * 根据指定的format格式化Date对象, 指定Locale
	 *
	 * @param date
	 * @param format
	 * @param locale
	 * @return
	 */
	public static String format(Date date, String format, Locale locale) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format, locale);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 采用"yyyy-MM-dd HH:mm:ss", 根据指定的时区格式化Date对象
	 *
	 * @param date
	 * @param timeZone
	 * @return String
	 */
	public static String format(Date date, TimeZone timeZone) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME, timeZone);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 根据指定格式及时区格式化Date对象
	 *
	 * @param date
	 * @param format
	 * @param timeZone
	 * @return String
	 */
	public static String format(Date date, String format, TimeZone timeZone) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format, timeZone);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 采用"yyyy-MM-dd HH:mm:ss", 指定的时区和locale格式化Date对象
	 *
	 * @param date
	 * @param timeZone
	 * @param locale
	 * @return String
	 */
	public static String format(Date date, TimeZone timeZone, Locale locale) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME, timeZone, locale);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 根据指定的format, 指定的时区和locale格式化Date对象
	 *
	 * @param date
	 * @param format
	 * @param timeZone
	 * @param locale
	 * @return String
	 */
	public static String format(Date date, String format, TimeZone timeZone, Locale locale) {
		if (date == null) {
			return null;
		}
		Objects.requireNonNull(format);
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format, timeZone, locale);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 用yyyy-MM-dd格式化LocalDate对象
	 *
	 * @param localDate
	 * @return String
	 */
	public static String format(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		
		return localDate.format(DTF_ISO_DATE);
	}
	
	/**
	 * 根据指定的format格式化LocalDate对象
	 *
	 * @param localDate
	 * @param format
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
	 * @param format
	 * @return String
	 */
	public static String format(LocalDateTime localDateTime, String format) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.format(ofPattern(format));
	}
	
	/**
	 * 用ISO 日期格式化 yyyy-MM-dd HH:mm:ss
	 *
	 * @param localDateTime
	 * @return String
	 */
	public static String format(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.format(ofPattern(FMT_ISO_DATETIME));
	}
	
	/**
	 * 用HH:mm:ss格式化LocalTime
	 *
	 * @param localTime
	 * @return
	 */
	public static String format(LocalTime localTime) {
		if (localTime == null) {
			return null;
		}
		
		return localTime.format(DTF_TIME_FORMAT);
	}
	
	/**
	 * 用指定格式格式化LocalTime对象
	 *
	 * @param localTime
	 * @param format
	 * @return
	 */
	public static String format(LocalTime localTime, String format) {
		if (localTime == null) {
			return null;
		}
		
		Objects.requireNonNull(format, "format cannot be null!");
		return localTime.format(ofPattern(format));
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * 根据正则表达式匹配日期格式并解析日期字符串, 时区为"Asia/Shanghai", Locale为CHINA
	 *
	 * @param source
	 * @return Date
	 */
	public static Date parse(String source) {
		if (isBlank(source)) {
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(source);
		
		if (simpleDateFormat == null) {
			log.warn("No suitable Dateformat found!");
			return null;
		}
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format("Parse date string:[{0}]", source);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	/**
	 * 根据指定的format解析日期字符串, 时区为"Asia/Shanghai", Locale为CHINA
	 *
	 * @param source
	 * @param format
	 * @return Date
	 */
	public static Date parse(String source, String format) {
		if (isBlank(source)) {
			return null;
		}
		Objects.requireNonNull(format);
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format("Parse date string:[{0}]", source);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	/**
	 * 采用"yyyy-MM-dd HH:mm:ss", 根据指定的时区解析日期字符串
	 *
	 * @param source
	 * @param timezone
	 * @return Date
	 */
	public static Date parse(String source, TimeZone timezone) {
		if (isBlank(source)) {
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(source, timezone);
		
		if (simpleDateFormat == null) {
			String msg = "No suitable Dateformat found!";
			log.info(msg);
			throw new NoDateFormatFoundException(msg);
		}
		
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format("Parse date string:[{0}]", source);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	/**
	 * 根据指定的format, 指定的时区解析日期字符串
	 *
	 * @param source
	 * @param format
	 * @return Date
	 */
	public static Date parse(String source, String format, TimeZone timezone) {
		if (isBlank(source)) {
			return null;
		}
		Objects.requireNonNull(format);
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format, timezone);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format("Parse date string:[{0}] with timezone:[{1}] and format:[{2}] failed!",
					source, timezone,
					format);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	/**
	 * 根据指定的format, 指定的时区解析日期字符串
	 *
	 * @param source
	 * @param format
	 * @return Date
	 */
	public static Date parse(String source, String format, Locale locale) {
		if (isBlank(source)) {
			return null;
		}
		Objects.requireNonNull(format);
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format, locale);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format("Parse date string:[{0}] with locale:[{1}] and format:[{2}] failed!",
					source, locale,
					format);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	/**
	 * 采用"yyyy-MM-dd HH:mm:ss", 指定的时区和locale解析日期字符串
	 *
	 * @param source
	 * @param timezone
	 * @param locale
	 * @return Date
	 */
	public static Date parse(String source, TimeZone timezone, Locale locale) {
		if (isBlank(source)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(FMT_ISO_DATETIME_1, timezone, locale);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format(
					"Parse date string:[{0}] with timezone:[{1}], locale:[{2}] and format:[{3}] failed!",
					source, timezone, locale, FMT_ISO_DATETIME);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	/**
	 * 根据指定的format, 指定的时区和locale解析日期字符串
	 *
	 * @param source
	 * @param format
	 * @return Date
	 */
	public static Date parse(String source, String format, TimeZone timezone, Locale locale) {
		if (isBlank(source)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = SimpleDateFormatHolder.formatFor(format, timezone, locale);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			String message = MessageFormat.format(
					"Parse date string:[{0}] with timezone:[{1}], locale:[{2}] and format:[{3}] failed!",
					source, timezone, locale, format);
			log.error(message);
			throw new DateParseException(message, e);
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * 采用"yyyy-MM-dd HH:mm:ss"格式将日期字符串从srcTimezone转换成destTimezone日期字符串
	 *
	 * @param source
	 * @param srcTimezone
	 * @param destTimezone
	 * @return String
	 */
	public static String convert2TargetTimezone(String source, TimeZone srcTimezone, TimeZone destTimezone) {
		if (isBlank(source)) {
			return null;
		}
		Date srcDate = parse(source, srcTimezone);
		return format(srcDate, destTimezone);
	}
	
	
	/**
	 * 根据指定格式将日期字符串从srcTimezone转换成destTimezone日期字符串
	 *
	 * @param source
	 * @param srcTimezone
	 * @param destTimezone
	 * @return String
	 */
	public static String convert2TargetTimezone(String source, String format, TimeZone srcTimezone,
	                                            TimeZone destTimezone) {
		if (isBlank(source)) {
			return null;
		}
		Objects.requireNonNull(format);
		Date srcDate = parse(source, format, srcTimezone);
		return format(srcDate, format, destTimezone);
	}
	
	/**
	 * 根据指定格式将日期字符串从srcTimezone转换成destTimezone, destFormat日期字符串
	 *
	 * @param source
	 * @param srcFormat
	 * @param destFormat
	 * @param srcTimezone
	 * @param destTimezone
	 * @return String
	 */
	public static String convert2TargetTimezone(String source, String srcFormat, String destFormat,
	                                            TimeZone srcTimezone,
	                                            TimeZone destTimezone) {
		if (isBlank(source)) {
			return null;
		}
		Objects.requireNonNull(srcFormat);
		Objects.requireNonNull(destFormat);
		Date srcDate = parse(source, srcFormat, srcTimezone);
		return format(srcDate, destFormat, destTimezone);
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	
	public static LocalDate toLocalDate(String source) {
		if (isBlank(source)) {
			return null;
		}
		if (PT_ISO_DATE.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_ISO_DATE);
		}
		
		if (PT_ISO_DATE_1.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_ISO_DATE_1);
		}
		
		if (PT_ISO_DATE_2.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_ISO_DATE_2);
		}
		
		if (PT_ISO_DATE_3.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_ISO_DATE_3);
		}
		
		if (PT_DATE_EN.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN);
		}
		
		if (PT_DATE_EN_1.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_1);
		}
		
		if (PT_DATE_EN_1.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_2);
		}
		
		if (PT_DATE_EN_3.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_3);
		}
		
		if (PT_DATE_EN_5.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_5);
		}
		
		if (PT_DATE_EN_6.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_6);
		}
		
		if (PT_DATE_EN_7.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_7);
		}
		
		if (PT_DATE_CONCISE.matcher(source).matches()) {
			return LocalDate.parse(source, DTF_DATE_CONCISE);
		}
		
		try {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_LOCALE_4);
		} catch (DateTimeParseException e) {
		}
		try {
			return LocalDate.parse(source, DTF_DATE_FORMAT_EN_4);
		} catch (DateTimeParseException e) {
		}
		log.warn("{} does not match any LocalDate format! ", source);
		throw new UnsupportedLocalDateFormatException(source + " does not match any LocalDate format!");
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
	 * 将Date用指定时区转成LocalDate
	 *
	 * @param date
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
		if (date == null) {
			return null;
		}
		Objects.nonNull(zoneId);
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
		return localDateTime.toLocalDate();
	}
	
	/**
	 * 将LocalDateTime用系统默认时区转成LocalDate
	 *
	 * @param localDateTime
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.toLocalDate();
	}
	
	/**
	 * 将LocalDateTime用+8(东8区 Asia/Shanghai)时区转成LocalDate
	 *
	 * @param localDateTime
	 * @return LocalDate
	 */
	public static LocalDate toLocalDateCTT(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.atZone(ZONE_ID_SHANG_HAI).toLocalDate();
	}
	
	/**
	 * 将LocalDateTime用指定时区转成LocalDate
	 *
	 * @param localDateTime
	 * @param zoneId
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(LocalDateTime localDateTime, ZoneId zoneId) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.atZone(zoneId).toLocalDate();
	}
	
	/**
	 * 将Date用+8(东8区 Asia/Shanghai)时区转成LocalDate
	 *
	 * @param date
	 * @return LocalDate
	 */
	public static LocalDate toLocalDateCTT(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE_ID_SHANG_HAI);
		return localDateTime.toLocalDate();
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
		
		if (matches(PT_ISO_DATETIME, source)) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME);
		}
		
		if (matches(PT_ISO_DATETIME_1, source)) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_1);
		}
		
		if (matches(PT_ISO_DATETIME_2, source)) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_2);
		}
		
		if (matches(PT_ISO_DATETIME_3, source)) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_3);
		}
		
		if (matches(PT_ISO_DATETIME_4, source)) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_4);
		}
		
		if (matches(PT_ISO_DATETIME_5, source)) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_5);
		}
		
		if (PT_ISO_DATETIME_SHORT.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT);
		}
		
		if (PT_ISO_DATETIME_SHORT_1.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_1);
		}
		
		if (PT_ISO_DATETIME_SHORT_2.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_2);
		}
		
		if (PT_ISO_DATETIME_SHORT_3.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_3);
		}
		
		if (PT_ISO_DATETIME_SHORT_4.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_4);
		}
		
		if (PT_ISO_DATETIME_SHORT_5.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_5);
		}
		
		if (PT_ISO_DATETIME_SHORT_6.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_6);
		}
		
		if (PT_ISO_DATETIME_SHORT_7.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_ISO_DATETIME_SHORT_7);
		}
		
		if (PT_DATETIME_FORMAT_EN.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN);
		}
		
		if (PT_DATETIME_FORMAT_EN_1.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_1);
		}
		
		if (PT_DATETIME_FORMAT_EN_2.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_2);
		}
		
		if (PT_DATETIME_FORMAT_EN_3.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_3);
		}
		
		if (PT_DATETIME_FORMAT_EN_4.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_4);
		}
		
		if (PT_DATETIME_FORMAT_EN_5.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_5);
		}
		
		if (PT_DATETIME_FORMAT_EN_6.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_6);
		}
		
		if (PT_DATETIME_FORMAT_EN_7.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_7);
		}
		
		if (PT_DATETIME_FORMAT_EN_8.matcher(source).matches()) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_8);
		}
		
		if (matches(PT_DATETIME_FORMAT_EN_9, source)) {
			return LocalDateTime.parse(source, DTF_DATETIME_FORMAT_EN_9);
		}
		log.warn("{} does not match any LocalDateTime format! ", source);
		throw new UnsupportedLocalDateTimeFormatException(source + " does not match any LocalDateTime format!");
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
	 * 将Date用指定时区转成LocalDateTime
	 *
	 * @param date
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
		if (date == null) {
			return null;
		}
		Objects.nonNull(zoneId);
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, zoneId);
	}
	
	/**
	 * 将Date用+8(东8区 Asia/Shanghai)时区转成LocalDateTime
	 *
	 * @param date
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTimeCTT(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZONE_ID_SHANG_HAI);
	}
	
	/**
	 * 毫秒数转成LocalDateTime, 默认时区Asia/Shanghai
	 *
	 * @param millis
	 * @return
	 */
	public static LocalDateTime toLocalDateTime(long millis) {
		Instant instant = Instant.ofEpochMilli(millis);
		return instant.atZone(ZONE_ID_SHANG_HAI).toLocalDateTime();
	}
	
	/**
	 * 秒数转成LocalDateTime, 默认时区Asia/Shanghai
	 *
	 * @param seconds
	 * @return
	 */
	public static LocalDateTime secondsToLocalDateTime(long seconds) {
		Instant instant = Instant.ofEpochSecond(seconds);
		return instant.atZone(ZONE_ID_SHANG_HAI).toLocalDateTime();
	}
	
	/**
	 * 用系统默认时区将LocalDate转成LocalDateTime
	 *
	 * @param localDate
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(LocalDate localDate) {
		long milis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milis), ZoneId.systemDefault());
	}
	
	/**
	 * 用指定的时区将LocalDate转成LocalDateTime
	 *
	 * @param localDate
	 * @param zoneId
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(LocalDate localDate, ZoneId zoneId) {
		long milis = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli();
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milis), zoneId);
	}
	
	/**
	 * 用+8(东8区 Asia/Shanghai)将LocalDate转成LocalDateTime
	 *
	 * @param localDate
	 * @param zoneId
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDateTimeCTT(LocalDate localDate, ZoneId zoneId) {
		long milis = localDate.atStartOfDay(ZONE_ID_SHANG_HAI).toInstant().toEpochMilli();
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milis), ZONE_ID_SHANG_HAI);
	}
	
	/**
	 * 用系统默认时区将LocalDateTime转成Date对象
	 *
	 * @param localDateTime
	 * @return Date
	 */
	public static Date toDate(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return Date.from(instant);
	}
	
	/**
	 * 用指定时区将LocalDateTime转成Date对象
	 *
	 * @param localDateTime
	 * @return Date
	 */
	public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
		Instant instant = localDateTime.atZone(zoneId).toInstant();
		return Date.from(instant);
	}
	
	/**
	 * java.sql.Date 转 java.util.Date
	 * @param date
	 * @return
	 */
	public static Date toDate(java.sql.Date date) {
		if (date == null) {
			return null;
		}
		
		return new Date(date.getTime());
	}
	
	public static LocalTime toLocalTime(String source) {
		if (isBlank(source)) {
			return null;
		}
		
		if (matches(DateConstants.PT_TIME_FORMAT, source)) {
			return LocalTime.parse(source, DateConstants.DTF_TIME_FORMAT);
		}
		if (matches(DateConstants.PT_TIME_FORMAT1, source)) {
			return LocalTime.parse(source, DateConstants.DTF_TIME_FORMAT1);
		}
		if (matches(DateConstants.PT_TIME_FORMAT2, source)) {
			return LocalTime.parse(source, DateConstants.DTF_TIME_FORMAT2);
		}
		if (matches(DateConstants.PT_TIME_FORMAT3, source)) {
			return LocalTime.parse(source, DateConstants.DTF_TIME_FORMAT3);
		}
		
		throw new UnsupportedLocalTimeFormatException(source + " does not match any known LocalTime format");
	}
	
	/**
	 * 获取当前时间点到下一个整点之间相差的毫秒数
	 *
	 * @return long
	 */
	public static long milisToNextHour() {
		Calendar calendar = Calendar.getInstance();
		//加一个小时
		calendar.add(Calendar.HOUR, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis() - System.currentTimeMillis();
	}
	
	/**
	 * 在指定日期上+天数
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date datePlus(Date date, int days) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		
		return calendar.getTime();
	}
	
	/**
	 * 在指定日期上-天数
	 *
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date dateMinus(Date date, int days) {
		return datePlus(date, 0 - days);
	}
	
	/**
	 * 在指定日期上+小时
	 *
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date hourPlus(Date date, int hours) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		
		return calendar.getTime();
	}
	
	/**
	 * 在指定日期上-小时
	 *
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date hourMinus(Date date, int hours) {
		return hourPlus(date, 0 - hours);
	}
	
	/**
	 * 在指定日期上+分钟
	 *
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date minutePlus(Date date, int minutes) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		
		return calendar.getTime();
	}
	
	/**
	 * 在指定日期上+分钟
	 *
	 * @param date
	 * @param minutes
	 * @return LocalDateTime
	 */
	public static LocalDateTime minutePlus(LocalDateTime date, int minutes) {
		if (date == null) {
			return null;
		}
		return date.plusMinutes(minutes);
	}
	
	/**
	 * 在指定日期上-分钟
	 *
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date minuteMinus(Date date, int minutes) {
		return minutePlus(date, 0 - minutes);
	}
	
	/**
	 * 在指定日期上-分钟
	 *
	 * @param date
	 * @param minutes
	 * @return LocalDateTime
	 */
	public static LocalDateTime minuteMinus(LocalDateTime date, int minutes) {
		if (date == null) {
			return null;
		}
		return date.minusMinutes(minutes);
	}
	
	/**
	 * 在指定日期上+秒数
	 *
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date secondPlus(Date date, int seconds) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		
		return calendar.getTime();
	}
	
	/**
	 * 在指定日期上-秒数
	 *
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date secondMonus(Date date, int seconds) {
		return secondPlus(date, 0 - seconds);
	}
	
	/**
	 * 在指定日期上+毫秒数
	 *
	 * @param date
	 * @param millis
	 * @return
	 */
	public static Date milliSecondPlus(Date date, int millis) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, millis);
		
		return calendar.getTime();
	}
	
	/**
	 * 在指定日期上-毫秒数
	 *
	 * @param date
	 * @param millis
	 * @return
	 */
	public static Date milliSecondMinus(Date date, int millis) {
		return milliSecondPlus(date, 0 - millis);
	}
	
	/**
	 * 用默认时区将日期字符串转成1970-1-1 00:00:00以来的毫秒数
	 *
	 * @param source
	 * @return long
	 */
	public static Long toEpochMilis(String source) {
		LocalDateTime localDateTime = toLocalDateTime(source);
		return toEpochMilis(localDateTime);
	}
	
	/**
	 * 用指定时区将LocalDateTime转成1970-1-1 00:00:00以来的毫秒数
	 *
	 * @param source
	 * @param zoneId
	 * @return long
	 */
	public static Long toEpochMilis(String source, ZoneId zoneId) {
		LocalDateTime localDateTime = toLocalDateTime(source);
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
	}
	
	/**
	 * 用默认时区将LocalDateTime转成1970-1-1 00:00:00以来的毫秒数
	 *
	 * @param localDateTime
	 * @return
	 */
	public static Long toEpochMilis(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	private static boolean isBlank(String s) {
		return s == null || "".equals(s.trim());
	}
	
	private static boolean matches(Pattern pattern, String source) {
		if (isBlank(source)) {
			return false;
		}
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();
	}
}
