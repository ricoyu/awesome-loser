package com.loserico.entity;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Authorization implements Serializable {
	private static final long serialVersionUID = -5357041594956190742L;
	private Long id;
	private Long userId;
	private Long appId;
	private List<Long> roleIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public List<Long> getRoleIds() {
		if (roleIds == null) {
			roleIds = new ArrayList<Long>();
		}
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleIdsStr() {
		return Joiner.on(", ").skipNulls().join(roleIds);
	}

	public void setRoleIdsStr(String roleIdsStr) {
		Splitter.on(",")
				.trimResults()
				.omitEmptyStrings()
				.split(roleIdsStr)
				.forEach((roleIdStr) -> {
					roleIds.add(Long.valueOf(roleIdStr));
				});
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Authorization that = (Authorization) o;

		if (id != null ? !id.equals(that.id) : that.id != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Authorization{" + "id=" + id + ", userId=" + userId + ", appId=" + appId + ", roleIds=" + roleIds + '}';
	}
}