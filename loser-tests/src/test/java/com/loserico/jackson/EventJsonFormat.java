package com.loserico.jackson;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class EventJsonFormat {
	public String name;

	@JsonFormat(
			shape = Shape.STRING,
			pattern = "dd/MM/yyyy")
	public LocalDate eventDate;

	public EventJsonFormat(String name, LocalDate eventDate) {
		this.name = name;
		this.eventDate = eventDate;
	}

	public EventJsonFormat() {
	}
}