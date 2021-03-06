package com.factory.domain;

import java.time.Instant;

public class User implements Principal {
	private int id;
	private String username;
	private String password;
	private String accessToken;
	private Boolean remember;
	private String verificationCode;
	private Boolean confirmed;
	private String salt;
	private Instant createdAt;
	private Integer updatedBy;
	private Integer roleId;
	private Integer managerId;
	private Integer companyId;
	private String registrationCode;
	private Boolean verificationNeeded;
	private Boolean activated;
	private String name;
	private String phone;
	private String workId;
	private Integer avatarId;
	private Instant birthday;
	private Instant joinedDate;
	
	/*Extra fields not from DB*/
	private boolean tokenUpdated;

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}

	public Boolean getVerificationNeeded() {
		return verificationNeeded;
	}

	public void setVerificationNeeded(Boolean verificationNeeded) {
		this.verificationNeeded = verificationNeeded;
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

	/*Extra methodes not from DB*/
	public boolean isTokenUpdated() {
		return tokenUpdated;
	}

	@Override
	public void setTokenUpdated() {
		this.tokenUpdated = true;
	}

}