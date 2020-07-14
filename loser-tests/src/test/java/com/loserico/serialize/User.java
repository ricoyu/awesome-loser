package com.loserico.serialize;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {

	private static final long serialVersionUID = 8580283821662749246L;
	private String name;
	private int id;
	private LocalDate birthday;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
}
