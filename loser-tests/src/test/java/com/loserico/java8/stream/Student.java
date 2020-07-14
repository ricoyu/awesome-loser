package com.loserico.java8.stream;

import com.loserico.json.jackson.JacksonUtils;

import java.util.HashSet;
import java.util.Set;

public class Student {

	private String name;
	private Set<String> books;

	public void addBook(String book) {
		if (this.books == null) {
			this.books = new HashSet<>();
		}
		this.books.add(book);
	}
	
	public Set<String> getBooks() {
		return books;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return JacksonUtils.toJson(this);
	}

}