package com.factory.manager.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.factory.dao.UserDao;
import com.factory.domain.User;
import com.factory.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager {
	
	@Autowired private UserDao userDao;

	@Override
	public int createUser(String username, String password, Boolean confirmed, String salt, Integer updatedBy,
			Integer roleId, Integer managerId, Integer companyId, Boolean activated, String name, String phone,
			String workId, Integer avatarId, Instant birthday, Instant joinedDate) {
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setConfirmed(confirmed);
		user.setSalt(salt);
		user.setCreatedAt(Instant.now());
		user.setUpdatedBy(updatedBy);
		user.setRoleId(roleId);
		user.setManagerId(managerId);
		user.setCompanyId(companyId);
		user.setActivated(activated);
		user.setName(name);
		user.setPhone(phone);
		user.setWorkId(workId);
		user.setAvatarId(avatarId);
		user.setBirthday(birthday);
		user.setJoinedDate(joinedDate);
		
		return userDao.persist(user);
	}
	

}
