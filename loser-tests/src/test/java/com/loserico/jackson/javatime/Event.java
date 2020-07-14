package com.loserico.jackson.javatime;

import java.time.LocalDate;

public class Event {
	public String name;

	public LocalDate eventDate;

	public Event(String name, LocalDate eventDate) {
		this.name = name;
		this.eventDate = eventDate;
	}

}