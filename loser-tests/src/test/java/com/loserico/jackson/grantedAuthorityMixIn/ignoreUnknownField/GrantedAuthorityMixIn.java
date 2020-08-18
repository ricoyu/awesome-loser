package com.loserico.jackson.grantedAuthorityMixIn.ignoreUnknownField;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.loserico.jackson.deserializer.SimpleGrantedAuthority;

/**
 * <p>
 * Copyright: (C), 2020-08-14 16:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({@JsonSubTypes.Type(value = SimpleGrantedAuthority.class, name = "simpleGrantedAuthority")})
public abstract class GrantedAuthorityMixIn {
}
