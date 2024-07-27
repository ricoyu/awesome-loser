package com.loserico.java14;

public class TestNullpointEXception {

	public static void main(String[] args) {
		Person p = new Person();
		/*
		 * Exception in thread "main" java.lang.NullPointerException: Cannot invoke "com.loserico.java14.TestNullpointEXception$Cat.eat()"
		 * because "p.cat" is null
		 */
		p.cat.eat();

	}

	static class Person {
		public Cat cat;
	}

	static class Cat {
		public void eat() {

		}
	}
}
