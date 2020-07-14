package com.loserico.java8.lamd;

import com.loserico.common.lang.enums.Gender;

import java.time.LocalDate;

public class Person {

	private String name;

	private Gender gender;

	private LocalDate birthday;

	public Person(String name, Gender gender, LocalDate birthday) {
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
	}

	public Person() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

}
