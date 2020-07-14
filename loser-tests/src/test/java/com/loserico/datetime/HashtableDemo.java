package com.loserico.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HashtableDemo {

	public static void main(String args[]) throws AssertionError, ParseException {

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		//comparing date using compareTo method in Java
		System.out.println("Comparing two Date in Java using CompareTo method");
		compareDatesByCompareTo(df, df.parse("01-01-2012"), df.parse("01-01-2012"));
		compareDatesByCompareTo(df, df.parse("02-03-2012"), df.parse("04-05-2012"));
		compareDatesByCompareTo(df, df.parse("02-03-2012"), df.parse("01-02-2012"));

		//comparing dates in java using Date.before, Date.after and Date.equals
		System.out.println("Comparing two Date in Java using Date's before, after and equals method");
		compareDatesByDateMethods(df, df.parse("01-01-2012"), df.parse("01-01-2012"));
		compareDatesByDateMethods(df, df.parse("02-03-2012"), df.parse("04-05-2012"));
		compareDatesByDateMethods(df, df.parse("02-03-2012"), df.parse("01-02-2012"));

		//comparing dates in java using Calendar.before(), Calendar.after and Calendar.equals()
		System.out.println("Comparing two Date in Java using Calendar's before, after and equals method");
		compareDatesByCalendarMethods(df, df.parse("01-01-2012"), df.parse("01-01-2012"));
		compareDatesByCalendarMethods(df, df.parse("02-03-2012"), df.parse("04-05-2012"));
		compareDatesByCalendarMethods(df, df.parse("02-03-2012"), df.parse("01-02-2012"));

	}

	public static void compareDatesByCompareTo(DateFormat df, Date oldDate, Date newDate) {
		//how to check if date1 is equal to date2
		if (oldDate.compareTo(newDate) == 0) {
			System.out.println(df.format(oldDate) + " and " + df.format(newDate) + " are equal to each other");
		}

		//checking if date1 is less than date 2
		if (oldDate.compareTo(newDate) < 0) {
			System.out.println(df.format(oldDate) + " is less than " + df.format(newDate));
		}

		//how to check if date1 is greater than date2 in java
		if (oldDate.compareTo(newDate) > 0) {
			System.out.println(df.format(oldDate) + " is greater than " + df.format(newDate));
		}
	}

	public static void compareDatesByDateMethods(DateFormat df, Date oldDate, Date newDate) {
		//how to check if two dates are equals in java
		if (oldDate.equals(newDate)) {
			System.out.println(df.format(oldDate) + " and " + df.format(newDate) + " are equal to each other");
		}

		//checking if date1 comes before date2
		if (oldDate.before(newDate)) {
			System.out.println(df.format(oldDate) + " comes before " + df.format(newDate));
		}

		//checking if date1 comes after date2
		if (oldDate.after(newDate)) {
			System.out.println(df.format(oldDate) + " comes after " + df.format(newDate));
		}
	}

	public static void compareDatesByCalendarMethods(DateFormat df, Date oldDate, Date newDate) {

		//creating calendar instances for date comparision
		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();

		oldCal.setTime(oldDate);
		newCal.setTime(newDate);

		//how to check if two dates are equals in java using Calendar
		if (oldCal.equals(newCal)) {
			System.out.println(df.format(oldDate) + " and " + df.format(newDate) + " are equal to each other");
		}

		//how to check if one date comes before another using Calendar
		if (oldCal.before(newCal)) {
			System.out.println(df.format(oldDate) + " comes before " + df.format(newDate));
		}

		//how to check if one date comes after another using Calendar
		if (oldCal.after(newCal)) {
			System.out.println(df.format(oldDate) + " comes after " + df.format(newDate));
		}
	}
}