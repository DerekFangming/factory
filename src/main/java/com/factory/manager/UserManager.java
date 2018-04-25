package com.factory.manager;

import java.time.Instant;
import java.util.Map;

import com.factory.domain.Principal;
import com.factory.domain.User;
import com.factory.domain.UserDetail;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InvalidParamException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;

public interface UserManager {
	
	public int createUser(String username, String password, String accessToken, Boolean remember, String verificationCode,
			Boolean confirmed, String salt, Integer updatedBy, Integer roleId, Integer managerId, Integer companyId,
			Boolean activated, String name, String phone, String workId, Integer avatarId, Instant birthday, Instant joinedDate);
	
	public User getUserByUsername(String username) throws NotFoundException;
	
	public User getUserByRegCode(String regCode) throws NotFoundException;
	public User getUserByRegCode(String regCode, ErrorType errorType) throws NotFoundException;
	
	public Principal validateAccessToken(Map<String, Object> request) throws InvalidStateException, InvalidParamException;
	public Principal validateAccessToken(Map<String, Object> request, boolean loadDetails) throws InvalidStateException, InvalidParamException;
	
	public void updateUserNotNull(int userId, String username, String password, String accessToken, Boolean remember,
			String verificationCode, Boolean confirmed, Integer updatedBy, Integer roleId, Integer managerId, Integer companyId,
			Boolean activated, String name, String phone, String workId, Integer avatarId, Instant birthday, Instant joinedDate);
	
	public UserDetail getUserDetailById(int userId) throws NotFoundException;

}
