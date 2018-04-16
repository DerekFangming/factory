package com.factory.manager.impl;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.factory.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager {

	@Override
	public int createUser(String username, String password, Boolean confirmed, String salt, Integer updatedBy,
			Integer roleId, Integer managerId, Integer companyId, Boolean activated, String name, String phone,
			String workId, Integer avatarId, Instant birthday, Instant joinedDate) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
