package com.loserico.java13;

import org.junit.Test;

public class TestStringBlock {

	@Test
	public void test() {
		var words = """
				select *
				from orders 
				where name = 'rico' and age = 40
				order by name asc
				""";

		System.out.println(words);
	}

	@Test
	public void test2() {
		var word = """
				hello""";
		var msg = "hello";
		System.out.println(word == msg); //true
		var word2 = """
				hello
				""";
		System.out.println(word2.equals(msg)); //false
	}

	@Test
	public void test3() {
		var html = """
				<!doctype html>
				<html lang="en">
					<head>
					    <meta charset="UTF-8">
					    <meta name="viewport"
					          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
					    <meta http-equiv="X-UA-Compatible" content="ie=edge">
					    <title>Document</title>
					</head>
					<body>
					    
					</body>\s\s
				</html>
				""";
		System.out.println(html);
	}
}
