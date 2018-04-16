package com.factory.manager;

import java.time.Instant;

public interface UserManager {
	
	public int createUser(String username, String password, Boolean confirmed, String salt, Integer updatedBy,
			Integer roleId, Integer managerId, Integer companyId, Boolean activated, String name, String phone,
			String workId, Integer avatarId, Instant birthday, Instant joinedDate);

}
