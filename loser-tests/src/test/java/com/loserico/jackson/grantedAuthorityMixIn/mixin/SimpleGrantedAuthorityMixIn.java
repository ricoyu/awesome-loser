package com.loserico.jackson.grantedAuthorityMixIn.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright: (C), 2020-08-14 16:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class SimpleGrantedAuthorityMixIn {
	
	public SimpleGrantedAuthorityMixIn(@JsonProperty("authority") String role) {}
}
