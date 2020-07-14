package com.loserico.pattern.creational.prototype;

import java.io.Serializable;

public class User implements Serializable {
	private String name;
	private String passwd;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return "name: " + name + ", passwd: " + passwd;
	}

}