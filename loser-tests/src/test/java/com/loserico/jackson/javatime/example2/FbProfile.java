package com.loserico.jackson.javatime.example2;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class FbProfile {
    private long id;

    // remove @JsonDeserialize annotation
    private LocalDate birthday;

    @JsonFormat(shape=Shape.NUMBER)
	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}