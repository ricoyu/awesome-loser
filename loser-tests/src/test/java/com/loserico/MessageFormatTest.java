package com.loserico;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import static java.text.MessageFormat.format;

public class MessageFormatTest {

	@Test
	public void testFormatLong() {
		String message = format("结算单号[{0}]", 123456L);
		String message2 = String.format("结算单号[{0}]", 123456L);
		System.out.println(message);
		System.out.println(message2);
		
		System.out.println(NumberFormat.getNumberInstance().format(123456L));
		System.out.println(String.format("%d", 123456L));
		System.out.println(String.format("%f", BigDecimal.valueOf(123456.123)));
	}
	
	@Test
	public void testFormatLong2() {
		// Set the Locale for the MessageFormat.
        Locale.setDefault(Locale.CHINA);

        // Use the default formatting for number.
        String message = MessageFormat.format("This is a {0} and {1} numbers", 10000, 7553453);
        System.out.println(message);

        // This line have the same format as above.
        message = MessageFormat.format("This is a {0,number} and {1,number} numbers", 10000, 7553453);
        System.out.println(message);

        // Format a number with 2 decimal digits.
        message = MessageFormat.format("This is a formatted {0, number,#.##} and {1, number,#.##} numbers", 25.7575, 75.2525);
        System.out.println(message);

        // Format a number as currency.
        message = MessageFormat.format("This is a formatted currency {0,number,currency} and {1,number,currency} numbers", 25.7575, 25.7575);
        System.out.println(message);

        // Format numbers in percentage.
        message = MessageFormat.format("This is a formatted percentage {0,number,percent} and {1,number,percent} numbers", 0.10, 0.75);
        System.out.println(message);
	}
	
	@Test
	public void testFormatTypes() {
		String pattern = "On {0, date}, {1} sent you {2, choice, 0#no messages|1#a message|2#two messages|2<{2, number, integer} messages}.";
		MessageFormat format = new MessageFormat(pattern, Locale.UK);
		
		Object[] arguments;
		String message = format.format(new Object[]{new Date(), "Alice", 2});
		System.out.println(message);
	}
}
