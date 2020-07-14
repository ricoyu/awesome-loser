package com.loserico.entity.auth;

import com.loserico.orm.entity.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "RESOURCE", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }, name = "RESOURCE_UK_NAME"),
		@UniqueConstraint(columnNames = { "URL" }, name = "RESOURCE_UK_URL") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class Resource extends BaseEntity {
	private static final long serialVersionUID = 8174376273396592753L;
	private String name; //资源名称
	private ResourceType type = ResourceType.menu; //资源类型
	private String url; //资源路径
	private String permission; //权限字符串
	private Long parentId; //父编号
	private String parentIds; //父编号列表
	private Boolean available = Boolean.FALSE;

	public static enum ResourceType {
		menu("菜单"),
		button("按钮");

		private final String info;

		private ResourceType(String info) {
			this.info = info;
		}

		public String getInfo() {
			return info;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	@Transient
	public boolean isRootNode() {
		return parentId == 0;
	}

	public String makeSelfAsParentIds() {
		return getParentIds() + getId() + "/";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Resource resource = (Resource) o;

		if (getId() != null ? !getId().equals(resource.getId()) : resource.getId() != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
	
	@Override
	public String toString() {
		return "Resource{" + "id=" + getId() + 
				", name='" + name + '\'' + 
				", type=" + type + 
				", permission='" + permission + '\''
				+ ", parentId=" + parentId + 
				", parentIds='" + parentIds + '\'' + 
				", available=" + available + '}';
	}
}