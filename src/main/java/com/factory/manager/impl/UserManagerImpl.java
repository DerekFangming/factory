package com.factory.manager.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.factory.dao.CompanyDao;
import com.factory.dao.UserActivationDao;
import com.factory.dao.UserDao;
import com.factory.dao.UserDetailDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPair;
import com.factory.dao.impl.QueryBuilder;
import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.QueryType;
import com.factory.dao.impl.RelationalOpType;
import com.factory.dao.impl.ResultsOrderType;
import com.factory.dao.impl.TableJoinExpression;
import com.factory.dao.impl.TableJoinType;
import com.factory.domain.User;
import com.factory.domain.UserActivation;
import com.factory.domain.UserDetail;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InvalidParamException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.UserManager;
import com.factory.utils.Params;
import com.factory.utils.Token;
import com.factory.utils.Utils;

@Component
public class UserManagerImpl implements UserManager {
	
	@Autowired private UserDao userDao;
	@Autowired private UserDetailDao userDetailDao;
	@Autowired private UserActivationDao userActivationDao;

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
	
	@Override
	public User validateAccessToken(Map<String, Object> request) throws InvalidStateException, InvalidParamException {
		return validateAccessToken(request, false);
	}
	
	@Override
	public User validateAccessToken(Map<String, Object> request, boolean loadDetails) throws InvalidStateException, InvalidParamException {
		String accessToken = (String) Utils.notNull(request.get("accessToken"));
		try{
			Map<String, Object> result = Token.decodeJWT(accessToken);
			Instant exp = Instant.parse((String)result.get("expire"));
			String username = (String) Utils.notNull(request.get("username"));
			
			User user = null;
			if (loadDetails) {
				
			} else {
				user = getUserByUsername(username);
			}
			
			if(exp.compareTo(Instant.now()) < 0){//Token from client is out-dated and needs update
				user.setTokenUpdated();
				result = Token.decodeJWT(user.getAccessToken());
				exp = Instant.parse((String)result.get("expire"));
				
				if(exp.compareTo(Instant.now()) < 0){//The token in DB is also expired
					
					if(user.getRemember()) {//Renew token since user chooses to remember
						Instant expDate = Instant.now().plus(Duration.ofDays(Params.TOKEN_REMEMBER_EXP_DAY));
						String newAccessToken = Token.createAccessToken(username, expDate);
						NVPair pair = new NVPair(UserDao.Field.ACCESS_TOKEN.name, newAccessToken);
						
						userDao.update(user.getId(), pair);
						user.setAccessToken(newAccessToken);
						
						return user;
					} else {//Return session expired error
						throw new InvalidStateException(ErrorType.SESSION_EXPIRED, false);
					}
				} else {
					if (!accessToken.equals(user.getAccessToken()))
						throw new InvalidStateException(ErrorType.INVALID_ACCESS_TOKEN);
					return user;
				}
			} else {//Token from client is ok as long as it matches the DB record
				if (!accessToken.equals(user.getAccessToken()))
					throw new InvalidStateException(ErrorType.INVALID_ACCESS_TOKEN);
				return user;
			}
		} catch (InvalidStateException e) {
			throw e;
		} catch (Exception e){
			throw new InvalidStateException(ErrorType.INVALID_ACCESS_TOKEN);
		}
	}
	
	@Override
	public void updateUserNotNull(int userId, String username, String password, String accessToken, Boolean remember,
			String verificationCode, Boolean confirmed, Integer updatedBy, Integer roleId, Integer managerId, Integer companyId,
			Boolean activated, String name, String phone, String workId, Integer avatarId, Instant birthday, Instant joinedDate) {
		List<NVPair> newValue = new ArrayList<NVPair>();
		
		if (username != null)
			newValue.add(new NVPair(UserDao.Field.USERNAME.name, username));
		if (password != null)
			newValue.add(new NVPair(UserDao.Field.PASSWORD.name, password));
		if (accessToken != null)
			newValue.add(new NVPair(UserDao.Field.ACCESS_TOKEN.name, accessToken));
		if (remember != null)
			newValue.add(new NVPair(UserDao.Field.REMEMBER.name, remember));
		if (verificationCode != null)
			newValue.add(new NVPair(UserDao.Field.VERIFICATION_CODE.name, verificationCode));
		if (confirmed != null)
			newValue.add(new NVPair(UserDao.Field.CONFIRMED.name, confirmed));
		if (updatedBy != null)
			newValue.add(new NVPair(UserDao.Field.UPDATED_BY.name, updatedBy));
		if (roleId != null)
			newValue.add(new NVPair(UserDao.Field.ROLE_ID.name, roleId));
		if (managerId != null)
			newValue.add(new NVPair(UserDao.Field.MANAGER_ID.name, managerId));
		if (companyId != null)
			newValue.add(new NVPair(UserDao.Field.COMPANY_ID.name, companyId));
		if (activated != null)
			newValue.add(new NVPair(UserDao.Field.ACTIVATED.name, activated));
		if (name != null)
			newValue.add(new NVPair(UserDao.Field.NAME.name, name));
		if (phone != null)
			newValue.add(new NVPair(UserDao.Field.PHONE.name, phone));
		if (workId != null)
			newValue.add(new NVPair(UserDao.Field.WORK_ID.name, workId));
		if (avatarId != null)
			newValue.add(new NVPair(UserDao.Field.AVATAR_ID.name, avatarId));
		if (birthday != null)
			newValue.add(new NVPair(UserDao.Field.BIRTHDAY.name, birthday));
		if (joinedDate != null)
			newValue.add(new NVPair(UserDao.Field.JOINED_DATE.name, joinedDate));
		
		userDao.update(userId, newValue);
	}
	
	@Override
	public UserDetail getUserDetailById(int userId) throws NotFoundException {
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.USER_DETAILS, QueryType.FIND)
				.addTableJoinExpression(new TableJoinExpression(CoreTableType.USERS, UserDao.Field.COMPANY_ID.name,
						TableJoinType.LEFT_JOIN, CoreTableType.COMPANIES, CompanyDao.Field.ID.name))
				.addTableJoinExpression(new TableJoinExpression(CoreTableType.USERS, UserDao.Field.ROLE_ID.name,
						TableJoinType.LEFT_JOIN, CoreTableType.ROLES, CompanyDao.Field.ID.name))
				.addQueryExpression(new QueryTerm(UserDetailDao.Field.ID.expression, RelationalOpType.EQ, userId))
				.setReturnField(UserDetailDao.FieldTypes);
		
		
		return userDetailDao.findObject(qb.createQuery());
	}

	@Override
	public int createUserActivation(int requesterId, int responderId) {
		UserActivation userActivation = new UserActivation();
		userActivation.setRequesterId(requesterId);
		userActivation.setResponderId(responderId);
		userActivation.setCreatedAt(Instant.now());
		return userActivationDao.persist(userActivation);
	}
	
	@Override
	public List<UserDetail> getAllUserDetails(int companyId, int offset, int limit) throws NotFoundException {
		//TODO: Fix this query
		QueryBuilder qb = QueryType.getQueryBuilder(CoreTableType.USER_DETAILS, QueryType.FIND)
				.addTableJoinExpression(new TableJoinExpression(CoreTableType.USERS, UserDao.Field.ROLE_ID.name,
						TableJoinType.LEFT_JOIN, CoreTableType.ROLES, CompanyDao.Field.ID.name))
				.addQueryExpression(new QueryTerm("users.company_id", RelationalOpType.EQ, companyId))
				.setReturnField("users.id, password, access_token, remember, confirmed, salt, activated, users.name, phone, work_id, avatar_id, birthday, joined_date, '' as company_name, '' as industry, roles.name as role_name, can_create_task, can_create_product")
				.setOrdering(UserDetailDao.Field.NAME.name, ResultsOrderType.ASCENDING)
				.setOffset(offset)
				.setLimit(limit);
		return userDetailDao.findAllObjects(qb.createQuery());
	}

}
