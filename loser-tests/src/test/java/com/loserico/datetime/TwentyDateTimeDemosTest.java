package com.loserico.datetime;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

/**
 * http://www.importnew.com/16814.html
 * 
 * @of
 * 关于这个新的时间日期库的最大的优点就在于它定义清楚了时间日期相关的一些概念，
 * 比方说，瞬时时间（Instant）,持续时间（duration），日期（date）,时间（time），时区（time-zone）以及时间段（Period）。
 * 同时它也借鉴了Joda库的一些优点，比如将人和机器对时间日期的理解区分开的。
 * Java 8仍然延用了ISO的日历体系，并且与它的前辈们不同，java.time包中的类是不可变且线程安全的。新的时间及日期API位于java.time包中，下面是一些关键的类：
 * 
 * Instant		 —— 它代表的是时间戳
 * LocalDate	 —— 不包含具体时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
 * LocalTime	 —— 它代表的是不含日期的时间
 * LocalDateTime —— 它包含了日期及时间，不过还是没有偏移信息或者说时区。
 * ZonedDateTime —— 这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的。
 * 
 * @on
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-04-03 19:22
 * @version 1.0
 *
 */
public class TwentyDateTimeDemosTest {

	/**
	 * 如何 在Java 8中获取当天的日期
	 */
	@Test
	public void testCurrentDay() {
		/*
		 * Java 8中有一个叫LocalDate的类，它能用来表示今天的日期。这个类与java.util.Date略有不同，因为它只包含日期，没有时间。因此，
		 * 如果你只需要表示日期而不包含时间，就可以使用它。
		 */
		LocalDate today = LocalDate.now();
		/*
		 * 你可以看到它创建了今天的日期却不包含时间信息。它还将日期格式化完了再输出出来，不像之前的Date类那样，打印出来的数据都是未经格式化的。
		 */
		System.out.println("Today's Local date : " + today);
	}

	/**
	 * 如何在Java 8中获取当前的年月日
	 */
	@Test
	public void testGetYearMonthDay() {
		/*
		 * 可以看到，在Java
		 * 8中获取年月信息非常简单，只需使用对应的getter方法就好了，无需记忆，非常直观。你可以拿它和Java中老的获取当前年月日的写法进行一下比较。
		 */
		LocalDate today = LocalDate.now();
		int year = today.getYear();
		int month = today.getMonthValue();
		int day = today.getDayOfMonth();

		System.out.printf("Year : %d Month : %d day : %d \t %n", year, month, day);
	}

	@Test
	public void testGetYearMonthDayInOldFasion() {
		//getting local time, date, day of week and other details in local timezone
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

		Date currentTime = localCalendar.getTime();
		int currentDay = localCalendar.get(Calendar.DATE);
		int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
		int currentYear = localCalendar.get(Calendar.YEAR);
		int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
		int currentDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
		int CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);

		System.out.println("Current Date and time details in local timezone");
		System.out.println("Current Date: " + currentTime);
		System.out.println("Current Day: " + currentDay);
		System.out.println("Current Month: " + currentMonth);
		System.out.println("Current Year: " + currentYear);
		System.out.println("Current Day of Week: " + currentDayOfWeek);
		System.out.println("Current Day of Month: " + currentDayOfMonth);
		System.out.println("Current Day of Year: " + CurrentDayOfYear);

		//getting time, date, day of week and other details in GMT timezone
		Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		//rest of stuff are same
	}

	/**
	 * 在第一个例子中，我们看到通过静态方法now()来生成当天日期是非常简单的，不过通过另一个十分有用的工厂方法LocalDate.of()，
	 * 则可以创建出任意一个日期，它接受年月日的参数，然后返回一个等价的LocalDate实例。
	 * 关于这个方法还有一个好消息就是它没有再犯之前API中的错，比方说，年只能从1900年开始，月必须从0开始，等等。
	 * 这里的日期你写什么就是什么，比如说，下面这个例子中它代表的就是1月14日，没有什么隐藏逻辑。
	 */
	@Test
	public void testGetSpecifiedDate() {
		LocalDate dateOfBirth = LocalDate.of(1982, 11, 9);
		System.out.println("Your Date of birth is : " + dateOfBirth);
	}

	/**
	 * 在Java 8中如何检查两个日期是否相等
	 * 
	 * 如果说起现实中实际的处理时间及日期的任务，有一个常见的就是要检查两个日期是否相等。
	 * 你可能经常会碰到要判断今天是不是某个特殊的日子，比如生日啊，周年纪念日啊，或者假期之类。
	 * 有的时候，会给你一个日期，让你检查它是不是某个日子比方说假日。下面这个例子将会帮助你在Java
	 * 8中完成这类任务。正如你所想的那样，LocalDate重写了equals方法来进行日期的比较
	 * 
	 * 在本例中我们比较的两个日期是相等的。同时，如果在代码中你拿到了一个格式化好的日期串，你得先将它解析成日期然后才能比较。
	 * 你可以将这个例子与Java之前比较日期的方式进行下比较，你会发现它真是爽多了。
	 */
	@Test
	public void testIfSameDay() {
		LocalDate date1 = LocalDate.of(2017, 4, 3);
		LocalDate today = LocalDate.now();
		if (date1.equals(today)) {
			System.out.printf("Today %s and date1 %s are same date %n", today, date1);
		}
	}

	/**
	 * 在Java 8中如何检查重复事件，比如说生日
	 * 
	 * 在Java中还有一个与时间日期相关的实际任务就是检查重复事件，比如说每月的帐单日，结婚纪念日，每月还款日或者是每年交保险费的日子。
	 * 如果你在一家电商公司工作的话，那么肯定会有这么一个模块，会去给用户发送生日祝福并且在每一个重要的假日给他们捎去问候，
	 * 比如说圣诞节，感恩节，在印度则可能是万灯节（Deepawali）。如何在Java中判断是否是某个节日或者重复事件？使用MonthDay类。
	 * 这个类由月日组合，不包含年信息，也就是说你可以用它来代表每年重复出现的一些日子。当然也有一些别的组合，比如说YearMonth类。
	 * 它和新的时间日期库中的其它类一样也都是不可变且线程安全的，并且它还是一个值类（value class）。
	 * 
	 * 我们通过一个例子来看下如何使用MonthDay来检查某个重复的日期：
	 */
	@Test
	public void testRepeatedDay() {
		LocalDate dateOfBirth = LocalDate.of(2016, 04, 03);
		MonthDay birthday = MonthDay.of(dateOfBirth.getMonthValue(), dateOfBirth.getDayOfMonth());
		MonthDay currentMonthDay = MonthDay.from(LocalDateTime.now());
		/*
		 * 虽然年不同，但今天就是生日的那天，所以在输出那里你会看到一条生日祝福。你可以调整下系统的时间再运行下这个程序看看它是否能提醒你下一个生日是什么时候，
		 * 你还可以试着用你的下一个生日来编写一个JUnit单元测试看看代码能否正确运行。
		 */
		if (currentMonthDay.equals(birthday)) {
			System.out.println("Many Many happy returns of the day !!");
		} else {
			System.out.println("Sorry, today is not your birthday");
		}
	}

	/**
	 * 如何在Java 8中获取当前时间
	 * 
	 * 这与第一个例子中获取当前日期非常相似。这次我们用的是一个叫LocalTime的类，它是没有日期的时间，与LocalDate是近亲。
	 * 这里你也可以用静态工厂方法now()来获取当前时间。默认的格式是hh:mm:ss:nnn，这里的nnn是纳秒。可以和Java
	 * 8以前如何获取当前时间做一下比较。
	 */
	@Test
	public void testCurrentTime() {
		LocalTime time = LocalTime.now();
		System.out.println("local time now : " + time);
	}

	/**
	 * 如何增加时间里面的小时数
	 * 
	 * 很多时候我们需要增加小时，分或者秒来计算出将来的时间。Java
	 * 8不仅提供了不可变且线程安全的类，它还提供了一些更方便的方法譬如plusHours()来替换原来的add()方法。
	 * 顺便说一下，这些方法返回的是一个新的LocalTime实例的引用，因为LocalTime是不可变的，可别忘了存储好这个新的引用。
	 */
	@Test
	public void testPlusHour() {
		LocalTime time = LocalTime.now();
		LocalTime newTime = time.plusHours(2);
		System.out.println("Time after 2 hours : " + newTime);
	}

	@Test
	public void testPlusHourInOldFasion() {
		//Java calendar in default timezone and default locale
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));

		System.out.println("current date: " + getDate(cal));

		//adding days into Date in Java
		cal.add(Calendar.DATE, 2);
		System.out.println("date after 2 days : " + getDate(cal));

		//subtracting days from Date in Java
		cal.add(Calendar.DATE, -2);
		System.out.println("date before 2 days : " + getDate(cal));

		//adding moths into Date
		cal.add(Calendar.MONTH, 5);
		System.out.println("date after 5 months : " + getDate(cal));

		//subtracting months from Date
		cal.add(Calendar.MONTH, -5);
		System.out.println("date before 5 months : " + getDate(cal));

		//adding year into Date
		cal.add(Calendar.YEAR, 5);
		System.out.println("date after 5 years : " + getDate(cal));

		//subtracting year from Date
		cal.add(Calendar.YEAR, -5);
		System.out.println("date before 5 years : " + getDate(cal));

		//date after 200 days from now, takes care of how many days are in month
		//for years calendar takes care of leap year as well
		cal.add(Calendar.DATE, 200);
		System.out.println("date after 200 days from today : " + getDate(cal));

		System.out.println("current time in GMT: " + getTime(cal));

		//adding hours into Date
		cal.add(Calendar.HOUR_OF_DAY, 3);
		System.out.println("Time after 3 hours : " + getTime(cal));

		//subtracting hours from Date time
		cal.add(Calendar.HOUR_OF_DAY, -3);
		System.out.println("Time before 3 hours : " + getTime(cal));

		//adding minutes into Date time
		cal.add(Calendar.MINUTE, 3);
		System.out.println("Time after 3 minutes : " + getTime(cal));

		//subtracting minutes from Date time
		cal.add(Calendar.HOUR_OF_DAY, -3);
		System.out.println("Time before 3 minuets : " + getTime(cal));

	}

	/**
	 * 如何获取1周后的日期
	 * 
	 * 这与前一个获取2小时后的时间的例子类似，这里我们将学会如何获取到1周后的日期。
	 * LocalDate是用来表示无时间的日期的，它有一个plus()方法可以用来增加日，星期，或者月，ChronoUnit则用来表示这个时间单位。
	 * 由于LocalDate也是不可变的，因此任何修改操作都会返回一个新的实例，因此别忘了保存起来。
	 * 
	 * 可以看到7天也就是一周后的日期是什么。你可以用这个方法来增加一个月，一年，一小时，一分钟，甚至是十年，查看下Java
	 * API中的ChronoUnit类来获取更多选项。
	 */
	@Test
	public void testOneWeekLater() {
		LocalDate today = LocalDate.now();
		LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);
		System.out.println("Today is : " + today);
		System.out.println("Date after 1 week : " + nextWeek);
	}

	/**
	 * 这是上个例子的续集。上例中，我们学习了如何使用LocalDate的plus()方法来给日期增加日，周或者月，现在我们来学习下如何用minus()方法来找出一年前的那天。
	 */
	@Test
	public void testOneYearBefore() {
		LocalDate today = LocalDate.now();
		LocalDate previousYear = today.minus(1, ChronoUnit.YEARS);
		System.out.println("Date before 1 year : " + previousYear);
		LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);
		System.out.println("Date after 1 year : " + nextYear);
	}

	/**
	 * 在Java 8中使用时钟
	 * 
	 * Java
	 * 8中自带了一个Clock类，你可以用它来获取某个时区下当前的瞬时时间，日期或者时间。可以用Clock来替代System.currentTimeInMillis()与
	 * TimeZone.getDefault()方法
	 */
	@Test
	public void testClock() {
		// Returns the current time based on your system clock and set to UTC.
		Clock clock = Clock.systemUTC();
		System.out.println("Clock : " + clock);

		// Returns time based on system clock zone Clock defaultClock = 
		Clock defaultClock = Clock.systemDefaultZone();
		System.out.println("Clock : " + defaultClock);

		LocalDate localDate = LocalDate.of(2017, 04, 01);
		if (localDate.isBefore(LocalDate.now(clock))) {
			System.out.println("before");
		} else {
			System.out.println("after");
		}
	}

	/**
	 * 在Java中如何判断某个日期是在另一个日期的前面还是后面
	 * 
	 * 这也是实际项目中常见的一个任务。你怎么判断某个日期是在另一个日期的前面还是后面，或者正好相等呢？在Java
	 * 8中，LocalDate类有一个isBefore()和isAfter()方法可以用来比较两个日期。如果调用方法的那个日期比给定的日期要早的话，isBefore()方法会返回true。
	 * 
	 * 可以看到在Java 8中进行日期比较非常简单。不需要再用像Calendar这样的另一个类来完成类似的任务了。
	 */
	@Test
	public void testBeforeAfter() {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.of(2017, 04, 05);
		if (tomorrow.isAfter(today)) {
			System.out.println("Tomorrow comes after today");
		}

		LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);
		if (yesterday.isBefore(today)) {
			System.out.println("Yesterday is day before today");
		}
	}

	/**
	 * Converting local time into GMT or any other timezone in Java is easy as Java
	 * has support for time zones. JDK has a class called java.util.Timezone which
	 * represents timezone and Java also has classes like SimpleDateFormat which can
	 * use Time zone while parsing or formatting dates. By using java.uti.TimeZone and
	 * java.text.SimpleDateFormat we can write simple Java program to convert local
	 * time to GMT or any other time zone in Java.
	 */
	@Test
	public void testConvertLocalDateTime2GMTOldFasion() {
		//Date will return local time in Java  
		Date localTime = new Date();

		//creating DateFormat for converting time from local timezone to GMT
		DateFormat converter = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");

		//getting GMT timezone, you can get any timezone e.g. UTC
		converter.setTimeZone(TimeZone.getTimeZone("GMT"));

		System.out.println("local time : " + localTime);
		;
		System.out.println("time in GMT : " + converter.format(localTime));
	}

	/**
	 * GMT timezone is sort of standard timezone for recording date and timestamp. if
	 * your application is not using any local timezone that you can use GMT for
	 * storing values on server. here is quick example of getting current date and
	 * timeStamp value in GMT timezone in Java.
	 */
	@Test
	public void testGetCurrentDateTimeInGMTTimeZone() {
		SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		//Current Date Time in GMT
		System.out.println("Current Date and Time in GMT time zone: " + gmtDateFormat.format(new Date()));
	}

	/**
	 * 如何表示固定的日期，比如信用卡过期时间
	 * 
	 * 正如MonthDay表示的是某个重复出现的日子的，YearMonth又是另一个组合，它代表的是像信用卡还款日，定期存款到期日，options到期日这类的日期。
	 * 你可以用这个类来找出那个月有多少天，lengthOfMonth()这个方法返回的是这个YearMonth实例有多少天，这对于检查2月到底是28天还是29天可是非常有用的。
	 */
	@Test
	public void testYearMonth() {
		YearMonth currentYearMonth = YearMonth.now();
		System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());

		YearMonth creditCardExpiry = YearMonth.of(2019, Month.FEBRUARY);
		System.out.printf("Your credit card expires on %s %n", creditCardExpiry);
	}

	/**
	 * 如何在Java 8中检查闰年
	 * 
	 * 这并没什么复杂的，LocalDate类有一个isLeapYear()的方法能够返回当前LocalDate对应的那年是否是闰年。
	 */
	@Test
	public void testLeapYear() {
		LocalDate today = LocalDate.of(2020, 01, 01);
		//		LocalDate today = LocalDate.now();
		if (today.isLeapYear()) {
			System.out.println("This year is Leap year");
		} else {
			System.out.println("2014 is not a Leap year");
		}
	}

	/**
	 * 两个日期之间包含多少天，多少个月
	 * 
	 * 还有一个常见的任务就是计算两个给定的日期之间包含多少天，多少周或者多少年。
	 * 你可以用java.time.Period类来完成这个功能。在下面这个例子中，我们将计算当前日期与将来的一个日期之前一共隔着几个月。
	 */
	@Test
	public void testDateDiff() {
		LocalDate java9Release = LocalDate.of(2018, Month.MARCH, 14);
		LocalDate today = LocalDate.now();
		Period periodToNextJavaRelease = Period.between(today, java9Release);
		System.out.println("Months left between today and Java 8 release : " + periodToNextJavaRelease.getMonths());
	}

	/**
	 * @of
	 * 在Java 8中处理不同的时区
	 * 
	 * Java 8不仅将日期和时间进行了分离，同时还有时区。现在已经有好几组与时区相关的类了，比如ZonId代表的是某个特定的时区，而ZonedDateTime代表的是带时区的时间。
	 * 它等同于Java 8以前的GregorianCalendar类。使用这个类，你可以将本地时间转换成另一个时区中的对应时间
	 * 
	 * 注意of()和now()的区别
	 * @on
	 */
	@Test
	public void testZonedDateTime() {
		ZoneId america = ZoneId.of("America/New_York");
		LocalDateTime localDateAndTime = LocalDateTime.now();
		System.out.println("Current date and time : " + localDateAndTime);

		ZonedDateTime dateAndTimeInNewYork = ZonedDateTime.of(localDateAndTime, america);
		System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork);
		System.out.println("Current date and time in a particular timezone : "
				+ dateAndTimeInNewYork.format(ofPattern("yyyy-MM-dd HH:mm:ss")));

		ZoneId shanghai = ZoneId.of("Asia/Shanghai");
		ZonedDateTime dateAndTimeInShangHai = ZonedDateTime.of(localDateAndTime, shanghai);
		System.out.println("Current date and time in a particular timezone : " + dateAndTimeInShangHai);
		System.out.println("Current date and time in a particular timezone : "
				+ dateAndTimeInShangHai.format(ofPattern("yyyy-MM-dd HH:mm:ss")));

		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		System.out.println("Current date and time in a particular timezone : " + utc);

		ZonedDateTime dateAndTimeInNewYork2 = ZonedDateTime.now(ZoneOffset.of("-04:00"));
		System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork2);
		System.out.println("Current date and time in a particular timezone : " + ZonedDateTime.now(america));
	}

	/**
	 * ZonedDateTime表示带时区的日期和时间，支持的操作与LocalDateTime非常类似
	 */
	@Test
	public void testZonedDateTimeConvert() {
		ZoneId.getAvailableZoneIds().forEach(System.out::println);

		//ZonedDateTime与LocalDateTime、Instant之间可以相互转换
		ZonedDateTime nowOfShanghai = LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai"));
		System.out.println("nowOfShanghai: " + nowOfShanghai);
		ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate();
		ZonedDateTime nowOfShanghai2 = Instant.now().atZone(ZoneId.of("Asia/Shanghai"));
		Instant instantUTC = ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.of("UTC")).toInstant();
		System.out.println("instantUTC: " + instantUTC);
	}

	/**
	 * 带时区偏移量的日期与时间
	 * 
	 * 在Java
	 * 8里面，你可以用ZoneOffset类来代表某个时区，比如印度是GMT或者UTC5：30，你可以使用它的静态方法ZoneOffset.of()方法来获取对应的时区。
	 * 只要获取到了这个偏移量，你就可以拿LocalDateTime和这个偏移量创建出一个OffsetDateTime。
	 */
	@Test
	public void testZonedOffset() {
		LocalDateTime localDateTime = LocalDateTime.of(2017, Month.APRIL, 4, 11, 19);
		ZoneOffset zoneOffset = ZoneOffset.of("+05:30");
		OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, zoneOffset);
		/*
		 * 可以看到现在时间日期与时区是关联上了。还有一点就是，OffSetDateTime主要是给机器来理解的，如果是给人看的，
		 * 可以使用ZoneDateTime类。
		 */
		System.out.println("Date and Time with timezone offset in Java : " + offsetDateTime);
	}

	/**
	 *
	 * @return current Date from Calendar in dd/MM/yyyy format adding 1 into month
	 *         because Calendar month starts from zero
	 */
	private static String getDate(Calendar cal) {
		return "" + cal.get(Calendar.DATE) + "/" +
				(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
	}

	/**
	 *
	 * @return current Date from Calendar in HH:mm:SS format
	 *
	 *         adding 1 into month because Calendar month starts from zero
	 */
	private static String getTime(Calendar cal) {
		return "" + cal.get(Calendar.HOUR_OF_DAY) + ":" +
				(cal.get(Calendar.MINUTE)) + ":" + cal.get(Calendar.SECOND);
	}

}
