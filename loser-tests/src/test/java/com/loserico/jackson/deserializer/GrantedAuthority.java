package com.loserico.jackson.deserializer;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {

	String getAuthority();
}