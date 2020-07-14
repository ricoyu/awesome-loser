package com.loserico.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;

/**
 * http://www.baeldung.com/role-and-privilege-for-spring-security-registration?utm_source=email&utm_medium=email&utm_content=roles_permissions&utm_campaign=c2&tl_inbound=1&tl_target_all=1&tl_period_type=3
 * 
 * Redis分布式ID Generator
 * https://github.com/intenthq/icicle
 * http://engineering.intenthq.com/2015/03/icicle-distributed-id-generation-with-redis-lua/
 * https://docs.jboss.org/hibernate/stable/shards/reference/en/html/shards-shardstrategy.html
 * 
 * @author Rico Yu
 * @since 2017-01-11 20:30
 * @version 1.0
 *
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private boolean enabled;
	private boolean tokenExpired;

	@ManyToMany
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isTokenExpired() {
		return tokenExpired;
	}

	public void setTokenExpired(boolean tokenExpired) {
		this.tokenExpired = tokenExpired;
	}
	
	public static void main(String[] args) {
		System.out.println(User.class.getSuperclass().equals(Object.class));
	}

}