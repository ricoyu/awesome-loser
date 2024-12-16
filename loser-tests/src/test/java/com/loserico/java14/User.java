package com.loserico.java14;

import java.util.Objects;

class User {
	private Integer pid;
	private String pname;
	private Integer page;

	@Override
	public String toString() {
		return "Person{" +
				"pid=" + pid +
				", pname='" + pname + '\'' +
				", page=" + page +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User person = (User) o;
		return Objects.equals(pid, person.pid) && Objects.equals(pname, person.pname) && Objects.equals(page,
				person.page);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pid, pname, page);
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public User() {
	}

	public User(Integer pid, String pname, Integer page) {
		this.pid = pid;
		this.pname = pname;
		this.page = page;
	}
}
