package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

public class EventDeserializer {
	public String name;

	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonProperty("eventDate")
	public LocalDateTime birthday;
}