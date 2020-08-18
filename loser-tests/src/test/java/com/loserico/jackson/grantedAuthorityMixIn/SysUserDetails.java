package com.loserico.jackson.grantedAuthorityMixIn;

import com.loserico.zookeeper.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

@Data
public class SysUserDetails implements UserDetails, CredentialsContainer {

	private String userId;
	private String username;
	private String password;
	private String nickname;
	private Set<GrantedAuthority> authorities;
	private Set<String> permissions;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	public SysUserDetails() {

	}

	/**
	 * Calls the more complex constructor with all boolean arguments set to
	 * {@code true}.
	 */
	public SysUserDetails(String userId, String username, String password,
	                      Collection<? extends GrantedAuthority> authorities) {
		this(userId, username, password, null, true, true, true, true, authorities);
	}

	public SysUserDetails(String userId, String username, String password, String nickname,
	                      Collection<? extends GrantedAuthority> authorities) {
		this(userId, username, password, nickname, true, true, true, true, authorities);
	}

	public SysUserDetails(String userId, String username, String password, String nickname, boolean enabled,
	                      boolean accountNonExpired, boolean credentialsNonExpired,
	                      boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

		if (StringUtils.isBlank(username) || (password == null)) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}

		this.setUserId(userId);
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.enabled = enabled;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	// ~ Methods
	// ========================================================================================================

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}
	
	@Override
	public void eraseCredentials() {
		password = null;
	}
	
	private static class AuthorityComparator implements Comparator<GrantedAuthority>,
			Serializable {

		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			// Neither should ever be null as each entry is checked before adding it to
			// the set.
			// If the authority is null, it is a custom authority and should precede
			// others.
			if (g2.getAuthority() == null) {
				return -1;
			}

			if (g1.getAuthority() == null) {
				return 1;
			}

			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}

	/**
	 * Returns {@code true} if the supplied object is a {@code User} instance with
	 * the same {@code username} value.
	 * <p>
	 * In other words, the objects are equal if they have the same username,
	 * representing the same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof SysUserDetails) {
			return username.equals(((SysUserDetails) rhs).username);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("Username: ").append(this.username).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

		if (!authorities.isEmpty()) {
			sb.append("Granted Authorities: ");

			boolean first = true;
			for (GrantedAuthority auth : authorities) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append(auth);
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}

	/**
	 * Creates a UserBuilder with a specified user name
	 *
	 * @param username the username to use
	 * @return the UserBuilder
	 */
	public static UserBuilder withUsername(String username) {
		return builder().username(username);
	}

	/**
	 * Creates a UserBuilder
	 *
	 * @return the UserBuilder
	 */
	public static UserBuilder builder() {
		return new UserBuilder();
	}

	/**
	 * Builds the user to be added. At minimum the username, password, and
	 * authorities should provided. The remaining attributes have reasonable
	 * defaults.
	 */
	public static class UserBuilder {
		private String username;
		private String password;
		private String nickname;
		private List<GrantedAuthority> authorities;
		private boolean accountExpired;
		private boolean accountLocked;
		private boolean credentialsExpired;
		private boolean disabled;
		private Function<String, String> passwordEncoder = password -> password;

		/**
		 * Creates a new instance
		 */
		private UserBuilder() {
		}

		/**
		 * Populates the username. This attribute is required.
		 *
		 * @param username the username. Cannot be null.
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder username(String username) {
			this.username = username;
			return this;
		}

		/**
		 * Populates the password. This attribute is required.
		 *
		 * @param password the password. Cannot be null.
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}

		/**
		 * Encodes the current password (if non-null) and any future passwords supplied
		 * to {@link #password(String)}.
		 *
		 * @param encoder the encoder to use
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder passwordEncoder(Function<String, String> encoder) {
			this.passwordEncoder = encoder;
			return this;
		}

		public UserBuilder roles(String... roles) {
			List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return authorities(authorities);
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this user. Cannot be null, or contain
		 * null values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder authorities(GrantedAuthority... authorities) {
			return authorities(Arrays.asList(authorities));
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this user. Cannot be null, or contain
		 * null values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
			this.authorities = new ArrayList<>(authorities);
			return this;
		}


		/**
		 * Defines if the account is expired or not. Default is false.
		 *
		 * @param accountExpired true if the account is expired, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder accountExpired(boolean accountExpired) {
			this.accountExpired = accountExpired;
			return this;
		}

		/**
		 * Defines if the account is locked or not. Default is false.
		 *
		 * @param accountLocked true if the account is locked, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder accountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
			return this;
		}

		/**
		 * Defines if the credentials are expired or not. Default is false.
		 *
		 * @param credentialsExpired true if the credentials are expired, false
		 * otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder credentialsExpired(boolean credentialsExpired) {
			this.credentialsExpired = credentialsExpired;
			return this;
		}

		/**
		 * Defines if the account is disabled or not. Default is false.
		 *
		 * @param disabled true if the account is disabled, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		public UserBuilder fullName(String fullName) {
			this.nickname = fullName;
			return this;
		}

		public UserDetails build() {
			String encodedPassword = this.passwordEncoder.apply(password);
			return new SysUserDetails(null, username, encodedPassword, nickname, !disabled, !accountExpired,
					!credentialsExpired, !accountLocked, authorities);
		}
	}
}