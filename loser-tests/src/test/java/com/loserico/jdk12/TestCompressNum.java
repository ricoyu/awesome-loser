package com.loserico.jdk12;

import org.junit.Test;

import java.text.NumberFormat;
import java.util.Locale;

public class TestCompressNum {

	@Test
	public void test() {
		var cnf = NumberFormat.getCompactNumberInstance(Locale.CHINA, NumberFormat.Style.SHORT);
		System.out.println(cnf.format(1_0000)); //1万
		System.out.println(cnf.format(1_9200)); //2万
		System.out.println(cnf.format(1_000_000)); //100万
		System.out.println(cnf.format(1L << 30)); //11亿
		System.out.println(cnf.format(1L << 40)); //1万亿
		System.out.println(cnf.format(1L << 50)); //1126万亿
	}
}
