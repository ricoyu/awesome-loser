package com.loserico.jackson.grantedAuthorityMixIn;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {

	String getAuthority();
}