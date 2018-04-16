package com.factory.domain;

import java.time.Instant;

public class UserActivation {
	private int id;
	private Integer requesterId;
	private Integer responderId;
	private Instant createdAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Integer requesterId) {
		this.requesterId = requesterId;
	}

	public Integer getResponderId() {
		return responderId;
	}

	public void setResponderId(Integer responderId) {
		this.responderId = responderId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}