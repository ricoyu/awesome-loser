package com.loserico.entity.auth;

import com.loserico.common.lang.enums.Gender;
import com.loserico.entity.Address;
import com.loserico.orm.entity.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * 代表一个用户 一个用户可以有多个地址、多种角色
 * 
 * @author Loser
 * @since Jan 28, 2016
 * @version
 *
 */
@Entity
@Table(name = "USER", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "USERNAME" }, name = "USER_UK_USERNAME"),
		@UniqueConstraint(columnNames = { "EMAIL" }, name = "UK_EMAIL"),
		@UniqueConstraint(columnNames = { "CELLPHONE" }, name = "UK_CELLPHONE") })
@NamedQueries({ @NamedQuery(name = "User.findWithAddressesByUsername",
		query = "SELECT u from User u LEFT JOIN FETCH u.addresses where u.username=:username",
		hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class User extends BaseEntity {

	private static final long serialVersionUID = -2467389753957466344L;

	private String username;
	private String password;
	private boolean locked;
	private Gender gender;
	private LocalDate birthday;
	private String email;
	private String cellphone;
	private String name;
	private int version;
	private String salt;
	private Set<Address> addresses = new HashSet<>();
	private Set<Role> roles = new HashSet<Role>();

	@Column(name = "USERNAME", length = 100, nullable = false)
	@NotNull
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", length = 10000, nullable = false)
	@NotNull
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "GENDER", nullable = true, columnDefinition = "enum('MALE', 'FEMALE') default 'MALE'")
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/*
	 * Hibernate5.2 开始自动支持Java8的LocalDateTime等新的日期API
	 */
	@Column(name = "BIRTHDAY", nullable = true)
	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	@Column(name = "EMAIL", nullable = true)
	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CELLPHONE", nullable = true)
	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	@Column(name = "NAME", nullable = true, length = 1024)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	@ManyToMany
	@JoinTable(name = "USER_ROLE",
			joinColumns = @JoinColumn(name = "USER_ID"),
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Basic
	@Column(name = "salt", nullable = true, insertable = true, updatable = true, length = 255)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column(name = "LOCKED", columnDefinition = "TINYINT(1)", nullable = true)
	@NotNull
	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Transient
	public String getCredentialsSalt() {
		return username + salt;
	}

	@Override
	public String toString() {
		StringBuilder user = new StringBuilder();
		user.append("用户名：").append(username)
				.append("，姓名：").append(name)
				.append("，性别：").append(gender.desc())
				.append("，状态：").append(locked)
				.append("，出生日期： ").append(birthday)
				.append("，邮箱：").append(email)
				.append("，电话：").append(cellphone);
		return user.toString();
	}

	@Override
	public int hashCode() {
		int result = getId() == null ? 0 : getId().intValue();
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (gender != null ? gender.hashCode() : 0);
		result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (salt != null ? salt.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		User that = (User) o;

		if (getId() != that.getId()) {
			return false;
		}
		if (username != null ? !username.equals(that.username) : that.username != null) {
			return false;
		}
		if (password != null ? !password.equals(that.password) : that.password != null) {
			return false;
		}
		if (locked != that.locked) {
			return false;
		}
		if (gender != that.gender) {
			return false;
		}
		if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) {
			return false;
		}
		if (email != null ? !email.equals(that.email) : that.email != null) {
			return false;
		}
		if (cellphone != null ? !cellphone.equals(that.cellphone) : that.cellphone != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		if (salt != null ? !salt.equals(that.salt) : that.salt != null) {
			return false;
		}

		return true;
	}

}
