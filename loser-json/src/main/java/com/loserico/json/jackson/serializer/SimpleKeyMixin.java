package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:22
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SimpleKeyMixin {
	
	@JsonIgnore
	private int hashCode;

	public SimpleKeyMixin(@JsonProperty("params") Object... elements) {
		
	}
}