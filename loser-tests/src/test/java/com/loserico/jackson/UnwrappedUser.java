package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class UnwrappedUser {
	public int id;

	@JsonUnwrapped
	public Name username;

	public UnwrappedUser() {
	}

	public UnwrappedUser(int id, Name name) {
		this.id = id;
		this.username = name;
	}

	public static class Name {
		public String firstName;
		public String lastName;

		public Name(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public Name() {
		}

	}
}