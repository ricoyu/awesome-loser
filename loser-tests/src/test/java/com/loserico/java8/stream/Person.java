package com.loserico.java8.stream;

public class Person {
	public int no;
	private String name;
	private int age;
	private String firstName;

	public Person(String firstName, String name) {
		this.firstName = firstName;
		this.name = name;
	}
	
	public Person(int no, String name) {
		this.no = no;
		this.name = name;
	}
	
	public Person(int no, String name, int age) {
		this.no = no;
		this.name = name;
		this.age = age;
	}

	public String getName() {
		System.out.println(name);
		return name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public int getNo() {
		return no;
	}
	
	public void setNo(int no) {
		this.no = no;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}