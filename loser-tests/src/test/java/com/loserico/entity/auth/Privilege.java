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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="PRIVILEGE", uniqueConstraints= {@UniqueConstraint(columnNames= {"NAME"}, name="PRIVILEGE_UK_NAME")})
public class Privilege extends BaseEntity {

	private static final long serialVersionUID = -8346605686933919441L;

	private String name;
	private Set<Resource> resources = new HashSet<Resource>(); //拥有的资源
	private Set<Role> roles = new HashSet<>();

	@Column(name = "NAME", length = 100, nullable = false)
	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @ManyToMany(mappedBy = "privileges")
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany
	@JoinTable(name = "PRIVILEGE_RESOURCE",
			joinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID", referencedColumnName="id"))
	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Privilege)) {
			return false;
		}
		Privilege privilege = (Privilege) o;
		return Objects.equals(name, privilege.getName());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Privilege [name=").append(name).append("]").append("[id=").append(getId()).append("]");
		return builder.toString();
	}
}
