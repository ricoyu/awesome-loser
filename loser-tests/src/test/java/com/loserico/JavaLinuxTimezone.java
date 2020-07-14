package com.loserico;

import java.util.Date;
import java.util.TimeZone;

public class JavaLinuxTimezone {

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(date);
		
		TimeZone defaultTimeZone = TimeZone.getDefault();
		System.out.println(defaultTimeZone.toString());
	}
}
