package com.loserico.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Copyright: (C), 2021-02-04 14:40
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RegexTest {
	
	/*
	 * https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	 * https://stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression
	 */
	@Test
	public void testEmail() {
		String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
		String email = "12345@xxx.ccc.yy";
		boolean matches = email.matches(regex);
		//assertTrue(matches);
		
		regex = "^(?=.{1,64}@)[\\\\p{L}0-9_-]+(\\\\.[\\\\p{L}0-9_-]+)*@[^-][\\\\p{L}0-9-]+(\\\\.[\\\\p{L}0-9-]+)*(\\\\.[\\\\p{L}]{2,})$";
		/*matches = email.matches(regex);
		assertTrue(matches);*/
		
		System.out.println("h@example.example-example.com".matches(regex));
		System.out.println("12345@xxx.ccc.yy".matches(regex));
		
		String regex_v1 =
				"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		
		System.out.println("h@example.example-example.com".matches(regex_v1));
		System.out.println("12345@xxx.ccc.yy".matches(regex_v1));
		System.out.println("ricoyu520@gmail.com".matches(regex_v1));
		System.out.println("xxxx@gmail.com".matches(regex_v1));
	}
	
	@Test
	public void testUrl() {
		//String regex = "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";
		//String regex = "^((http[s]?|ftp):\\/\\/)?\\/?([^\\/\\.]+\\.)*?([^\\/\\.]+\\.[^:\\/\\s\\.]{2,3}(\\.[^:\\/\\s\\.]{2,3})?(:\\d+)?)($|\\/)([^#?\\s]+)?(.*?)?(#[\\w\\-]+)?$";
		//String regex = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";
		//String regex = "^(([^:/?#]+):)?(//([^/?#:\\d]*)(:(\\d{2,}))?)?([^?#]*)(\\?([^#]*))?(#(.*))?";
		String regex = "^(([^:/?#]+):)?(//([^/?#:]*)(:(\\d{2,}))?)?([^?#]*)(\\?([^#]*))?(#(.*))?";
		String url = "http://192.168.100.101:9200/rico/_mapping";
		//String url = "https://www.google.com";
		//String url = "https://www.google.com:80/dir/1/2/search.html?arg=0-a&arg1=1-b&arg3-c#hash";
		//String url = "https://www.google.com/dir/1/2/search.html?arg=0-a&arg1=1-b&arg3-c#hash";
		
		StringBuilder patternBuilder;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		if (matcher.matches()) {
			System.out.println(matcher.group(0)); //完整url
			System.out.println(matcher.group(1)); //https:               (([^:/?#]+):)?
			System.out.println(matcher.group(2)); //https                ([^:/?#]+)
			System.out.println(matcher.group(3)); ////www.google.com:80  (//([^/?#:]*)(:(\d{2,}))?)?
			System.out.println(matcher.group(4)); //www.google.com       ([^/?#:]*):?
			System.out.println(matcher.group(5)); //:80                  (:(\d{2,}))?
			System.out.println(matcher.group(6)); //80                   (\d{2,})
			System.out.println(matcher.group(7)); ///dir/1/2/search.html ([^?#]*)
			System.out.println(matcher.group(8)); //?arg=0-a&arg1=1-b&arg3-c   (\?([^#]*))?
			System.out.println(matcher.group(9)); //arg=0-a&arg1=1-b&arg3-c    ([^#]*)
			System.out.println(matcher.group(10)); //#hash               (#(.*))?
			System.out.println(matcher.group(11)); //hash                (.*)
		}
	}
	
	@Test
	public void testMaxUploadSizeExceeded() {
		String message =
				"org.springframework.web.multipart.MaxUploadSizeExceededException: Maximum upload size exceeded; nested exception is java.lang.IllegalStateException: org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException: the request was rejected because its size (405491652) exceeds the configured maximum (104857600)";
		Pattern ACTUAL_SIZE_PATTERN = Pattern.compile(".*size\\s{1}\\((\\d+)\\).*maximum\\s{1}\\((\\d+)\\)$");
		Matcher matcher = ACTUAL_SIZE_PATTERN.matcher(message);
		if (matcher.matches()) {
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
		} else {
			System.out.println("Not match");
		}
	}
	
	@Test
	public void testPhraseQueryPattern() {
		String phrase = "q=title:Beautiful Mind";
		Pattern pattern = Pattern.compile("(q=)?([1-9a-z])+:?(.)*");
		Matcher matcher = pattern.matcher(phrase);
		if (matcher.matches()) {
			String group3 = matcher.group(3);
			System.out.println(group3);
		}
	}
}
