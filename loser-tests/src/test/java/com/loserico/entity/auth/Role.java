package com.loserico.entity.auth;

import com.loserico.orm.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.String.join;

@Entity
@Table(name = "ROLE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ROLE" }, name = "ROLE_UK_ROLE") })
/*@NamedQueries({
		@NamedQuery(name = "Role.withResource",
				query = "SELECT r FROM Role r LEFT JOIN FETCH r.resources where r.id = id"),
		@NamedQuery(name = "Role.MultiRoleWithResource",
				query = "SELECT r FROM Role r LEFT JOIN FETCH r.resources where r.id in (:roleIds)")
})*/
public class Role extends BaseEntity {
	private static final long serialVersionUID = 4947795355289836802L;
	private String role; //角色标识 程序中判断使用,如"admin"
	private String description; //角色描述,UI界面显示使用
	private Set<User> users = new HashSet<User>(); //该角色被赋予哪些用户
	private Set<Privilege> privileges = new HashSet<>(); //该角色有哪些权限
	private Boolean available = FALSE; //是否可用,如果不可用将不会添加给用户

	@Column(name = "ROLE", length = 100, nullable = false)
	@NotNull
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "DESCRIPTION", length = 1000, nullable = false)
	@NotNull
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "AVAILABLE", nullable = false)
	@NotNull
	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	@ManyToMany
	@JoinTable(name = "ROLE_PRIVILEGE",
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	@ManyToMany(mappedBy = "roles")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Role role = (Role) o;

		if (getId() != null ? !getId().equals(role.getId()) : role.getId() != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}

	@Override
	public String toString() {
		StringBuilder privilegeStr = new StringBuilder();
		String[] privilegeNames = (String[]) privileges.stream().map((privilege) -> {
			return privilege.getName();
		}).toArray();

		return "Role{" + "id=" + getId() + ", role='" + role + '\'' + ", description='" + description + '\''
				+ ", privileges="
				+ join(", ", privilegeNames) + ", available=" + available + '}';
	}

}
