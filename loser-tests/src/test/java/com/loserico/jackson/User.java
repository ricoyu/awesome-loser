package com.loserico.jackson;

public class User {

	public int id;
	public String name;

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public User() {
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}