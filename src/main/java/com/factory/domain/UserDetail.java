package com.factory.domain;

import java.time.Instant;

public class UserDetail implements Principal {
	
	private int id;
	private String password;
	private String accessToken;
	private Boolean remember;
	private Boolean confirmed;
	private String salt;
	private Boolean activated;
	private String name;
	private String phone;
	private String workId;
	private Integer avatarId;
	private Instant birthday;
	private Instant joinedDate;
	
	/*Fields from company table*/
	private String companyName;
	private String companyIndustry;
	
	/*Fields from role table*/
	private String roleName;
	private boolean canCreateTask;
	private boolean canCreateProduct;
	
	/*Extra fields not from DB*/
	private boolean tokenUpdated;

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public Boolean getRemember() {
		return remember;
	}

	public void setRemember(Boolean remember) {
		this.remember = remember;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public Integer getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Integer avatarId) {
		this.avatarId = avatarId;
	}

	public Instant getBirthday() {
		return birthday;
	}

	public void setBirthday(Instant birthday) {
		this.birthday = birthday;
	}

	public Instant getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Instant joinedDate) {
		this.joinedDate = joinedDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyIndustry() {
		return companyIndustry;
	}

	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isCanCreateTask() {
		return canCreateTask;
	}

	public void setCanCreateTask(boolean canCreateTask) {
		this.canCreateTask = canCreateTask;
	}

	public boolean isCanCreateProduct() {
		return canCreateProduct;
	}

	public void setCanCreateProduct(boolean canCreateProduct) {
		this.canCreateProduct = canCreateProduct;
	}

	public void setTokenUpdated(boolean tokenUpdated) {
		this.tokenUpdated = tokenUpdated;
	}

	public boolean isTokenUpdated() {
		return tokenUpdated;
	}

	@Override
	public void setTokenUpdated() {
		this.tokenUpdated = true;
	}

}
