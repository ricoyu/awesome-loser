package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

public class UserWithIgnoreType {
	public int id;
	public Name name;

	public UserWithIgnoreType(int id, Name name) {
		super();
		this.id = id;
		this.name = name;
	}

	@JsonIgnoreType
	public static class Name {
		public String firstName;
		public String lastName;

		public Name(String firstName, String lastName) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}
}