package com.loserico.jackson.listwithtypeinfomixin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class ElephantMixIn {
	
	public ElephantMixIn(@JsonProperty("name") String name){}
}
