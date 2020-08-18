package com.loserico.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class BirdMixIn {
	
	public BirdMixIn(@JsonProperty("name") String name) {
		
	}
	
	@JsonProperty("sound")
	public abstract String getSound();
	
	@JsonProperty("habitat")
	public abstract String getHabitat();
}
