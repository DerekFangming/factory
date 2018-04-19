package com.factory.manager.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.factory.dao.UserDao;
import com.factory.dao.impl.QueryTerm;
import com.factory.domain.User;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InternalServerException;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.UserManager;
import com.factory.utils.Params;

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
	public int registerUser(String username, String password, Boolean remember, Boolean confirmed, String salt, Integer updatedBy,
			Integer roleId, Integer managerId, Integer companyId, Boolean activated, String name, String phone, String workId,
			Integer avatarId, Instant birthday, Instant joinedDate) throws InternalServerException {
		
		Instant expDate = Instant.now().plus(Duration.ofDays(Params.TOKEN_DEFAULT_EXP_DAY));
		String accessToken = createAccessToken(username, expDate);
		String verificationCode = createVerificationCode(username);
		
		return createUser(username, password, accessToken, remember, verificationCode, confirmed, salt, updatedBy, roleId,
				managerId, companyId, activated, name, phone, workId, avatarId, birthday, joinedDate);
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
	
	/* Helper methods */
	private String createVerificationCode(String username) throws InternalServerException{
		return createToken(username, Instant.now().plus(Duration.ofDays(1)), "email");
	}
	
	private String createAccessToken(String username, Instant expDate) {
		return createToken(username, expDate, null);
	}
	
	private String createToken(String username, Instant expDate, String action) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(Params.JWT_SECRET);
			Builder builder = JWT.create()
		    		.withClaim("username", username)
		    		.withClaim("expire", expDate.toString());
			if (action != null)
				builder = builder.withClaim("action", "email");
			
		    String token = builder.sign(algorithm);
		    return token;
		} catch (Exception e) {
			throw new InternalServerException(e);
		}
	}

}
