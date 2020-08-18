package com.loserico.jackson.listwithtypeinfomixin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class ZooMixIn {
	
	public ZooMixIn(@JsonProperty("name") String name, @JsonProperty("city") String city) {}
}
