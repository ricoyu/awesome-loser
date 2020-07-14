package com.loserico.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -7833247830642842225L;

	private Long id;

	private String creator;

	private LocalDateTime createTime;

	private String modifier;

	private LocalDateTime modifiyTime;

	private boolean deleted;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, unique = true, nullable = false)
	//分布式ID生成
	/*
	 * @Id
	 * 
	 * @GenericGenerator(name = "distributed-identifier", strategy =
	 * "com.peacefish.orm.commons.identifier.ZKDistributedIdentifier", parameters =
	 * { @Parameter(name = "fetch_size", value = "1") })
	 * 
	 * @GeneratedValue(generator = "distributed-identifier", strategy =
	 * GenerationType.AUTO)
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CREATOR", length = 100, nullable = false)
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/*
	 * 默认映射的数据库字段类型为TIMESTAMP
	 */
	@Column(name = "CREATE_TIME", columnDefinition = "DATETIME", nullable = false, length = 19)
	//	@Column(name = "CREATE_TIME", columnDefinition="TIMESTAMP", nullable = false, length = 19)
	//	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFIER", length = 100, nullable = false)
	public String getModifier() {
		return modifier;
	}

	public void setModifier(String lastModifier) {
		this.modifier = lastModifier;
	}

	@Column(name = "MODIFY_TIME", columnDefinition = "DATETIME", nullable = false, length = 19)
	public LocalDateTime getModifyTime() {
		return modifiyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifiyTime = modifyTime;
	}

	/**
	 * 在Entity被持久化之前做一些操作
	 */
	@PrePersist
	protected void onPrePersist() {
		LocalDateTime now = LocalDateTime.now();
		setCreateTime(now);
		setModifyTime(now);
	}

	@PreUpdate
	protected void onPreUpdate() {
		setModifyTime(LocalDateTime.now());
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
