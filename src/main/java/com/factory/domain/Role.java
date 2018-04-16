package com.factory.domain;

import java.time.Instant;

public class Role {
	private int id;
	private Integer companyId;
	private String name;
	private Integer level;
	private Instant createdAt;
	private Integer ownerId;
	private Integer updatedBy;
	private Boolean canCreateTask;
	private Boolean canCreateProduct;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getCanCreateTask() {
		return canCreateTask;
	}

	public void setCanCreateTask(Boolean canCreateTask) {
		this.canCreateTask = canCreateTask;
	}

	public Boolean getCanCreateProduct() {
		return canCreateProduct;
	}

	public void setCanCreateProduct(Boolean canCreateProduct) {
		this.canCreateProduct = canCreateProduct;
	}

}