package com.loserico.jackson.listwithtypeinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Lion extends Animal {
	
	@JsonCreator
	public Lion(@JsonProperty("name") String name) {
		setName(name);
	}
	
	@Override
	public String toString() {
		return "Lion: " + getName();
	}
}
