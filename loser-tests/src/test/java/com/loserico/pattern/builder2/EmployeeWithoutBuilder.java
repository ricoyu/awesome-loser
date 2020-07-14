package com.loserico.pattern.builder2;

/**
 * Why Do We Need the Builder Pattern?
 * 
 * Suppose that you have a class with a handful of instance attributes, such as
 * the Employee class shown below. In this class, some of the instance
 * attributes are required while the rest are optional. What kind of
 * constructors should you write for such a class? A first option would be to
 * have a constructor that only takes the required attributes as parameters, one
 * that takes all the required attributes plus the first optional one, another
 * one that takes two optional attributes, and so on ( this is called
 * telescoping constructor pattern).
 * 
 * As developers, we offer a solution for clients to construct objects from the
 * Employee class. But, clients may return with a bunch of questions for us, including:
 * <ul>
 * <li>Which constructor should I invoke? The one with four parameters or the one with five or six? 
 * <li>What is the default value for those optional parameters if I don’t pass a value for each? 
 * <li>If I want to pass values only for mail and phone but not for address, how can I fulfill this requirement? 
 * <li>What will happen if I mistakenly pass the value intended for the address to mail (compile may not complain about it because they have the same type)?
 * </ul>
 * 
 * <p>
 * Copyright: Copyright (c) 2019-01-18 09:42
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class EmployeeWithoutBuilder {
	private final String firstName; // required
	private final String lastName; // required
	private final int age; // required
	private final int personalId; // required
	private final String phone; // optional
	private final String address; // optional
	private final String mail; // optional

	public EmployeeWithoutBuilder(String firstName, String lastName, int age, int personalId) {
		this(firstName, lastName, age, personalId, "", "");
	}

	public EmployeeWithoutBuilder(String firstName, String lastName, int age, int personalId, String phone) {
		this(firstName, lastName, age, personalId, phone, "");
	}

	public EmployeeWithoutBuilder(String firstName, String lastName, int age, int personalId, String phone,
			String address) {
		this(firstName, lastName, age, personalId, phone, address, "");
	}

	public EmployeeWithoutBuilder(String firstName, String lastName, int age, int personalId, String phone,
			String address, String mail) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.personalId = personalId;
		this.phone = phone;
		this.address = address;
		this.mail = mail;
	}
}