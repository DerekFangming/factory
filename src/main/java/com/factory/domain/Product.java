package com.factory.domain;

import java.time.Instant;

public class Product {
	private int id;
	private Integer companyId;
	private String model;
	private String name;
	private String description;
	private Boolean combinedProduct;
	private Double netCost;
	private Double marketPrice;
	private Integer priceVisibleRoleId;
	private Double laborCost;
	private Integer visibleRoleId;
	private Instant createdAt;
	private Integer ownerId;
	private Integer updatedBy;

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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getCombinedProduct() {
		return combinedProduct;
	}

	public void setCombinedProduct(Boolean combinedProduct) {
		this.combinedProduct = combinedProduct;
	}

	public Double getNetCost() {
		return netCost;
	}

	public void setNetCost(Double netCost) {
		this.netCost = netCost;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Integer getPriceVisibleRoleId() {
		return priceVisibleRoleId;
	}

	public void setPriceVisibleRoleId(Integer priceVisibleRoleId) {
		this.priceVisibleRoleId = priceVisibleRoleId;
	}

	public Double getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(Double laborCost) {
		this.laborCost = laborCost;
	}

	public Integer getVisibleRoleId() {
		return visibleRoleId;
	}

	public void setVisibleRoleId(Integer visibleRoleId) {
		this.visibleRoleId = visibleRoleId;
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

}