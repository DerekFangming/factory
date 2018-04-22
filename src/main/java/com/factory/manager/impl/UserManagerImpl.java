package com.factory.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.factory.dao.UserDao;
import com.factory.dao.impl.QueryTerm;
import com.factory.domain.User;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.UserManager;

@Component
public class UserManagerImpl implements UserManager {
	
	@Autowired private UserDao userDao;

	@Override
	public int createUser(String username, String password, String accessToken, Boolean remember, String verificationCode,
			Boolean confirmed, String salt, Integer updatedBy, Integer roleId, Integer managerId, Integer companyId,
			Boolean activated, String name, String phone, String workId, Integer avatarId, Instant birthday, Instant joinedDate) {
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setAccessToken(accessToken);
		user.setRemember(remember);
		user.setVerificationCode(verificationCode);
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

	@Override
	public User getUserByUsername(String username) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.USERNAME.getQueryTerm(username.toLowerCase()));
		try{
			return userDao.findObject(terms);
		} catch(NotFoundException e){
			throw new NotFoundException(ErrorType.USER_NOT_FOUND);
		}
	}

	@Override
	public User getUserByRegCode(String regCode) throws NotFoundException {
		return getUserByRegCode(regCode, ErrorType.USER_NOT_FOUND);
	}

	@Override
	public User getUserByRegCode(String regCode, ErrorType errorType) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(UserDao.Field.REGISTRATION_CODE.getQueryTerm(regCode.toUpperCase()));
		try{
			return userDao.findObject(terms);
		}catch(NotFoundException e){
			throw new NotFoundException(errorType);
		}
	}

}
