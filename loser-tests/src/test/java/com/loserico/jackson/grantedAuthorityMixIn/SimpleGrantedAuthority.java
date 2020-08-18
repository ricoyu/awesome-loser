package com.loserico.jackson.grantedAuthorityMixIn;

public final class SimpleGrantedAuthority implements GrantedAuthority {

	private final String role;

	public SimpleGrantedAuthority(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof SimpleGrantedAuthority) {
			return role.equals(((SimpleGrantedAuthority) obj).role);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.role.hashCode();
	}

	@Override
	public String toString() {
		return this.role;
	}
}