package com.factory.manager;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.factory.domain.User;
import com.factory.domain.UserDetail;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InvalidParamException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;

public interface UserManager {
	
	/**
	 * Create an user and save it into the database
	 * @param username the username of the user
	 * @param password the hashed password of the user
	 * @param accessToken the access token of the user
	 * @param remember if we auto renew the token or not
	 * @param verificationCode code for email & reset password, etc.
	 * @param confirmed if the account (email or phone #) is confirmed or not
	 * @param salt for password hashing
	 * @param updatedBy the user id that updated the user
	 * @param roleId the role id of the user
	 * @param managerId the manager (user) id of the user
	 * @param companyId the company id of the user
	 * @param activated if the registration is activated or not
	 * @param name the name of the user
	 * @param phone the phone # of the user
	 * @param workId the work id of the user
	 * @param avatarId the avatar id of the user
	 * @param birthday the birthday of the user
	 * @param joinedDate the joined date of the user
	 * @return the database ID of the user
	 */
	public int createUser(String username, String password, String accessToken, Boolean remember, String verificationCode,
			Boolean confirmed, String salt, Integer updatedBy, Integer roleId, Integer managerId, Integer companyId,
			Boolean activated, String name, String phone, String workId, Integer avatarId, Instant birthday, Instant joinedDate);
	
	/**
	 * Get the user object base on the username
	 * @param username the username of the user
	 * @return user object
	 * @throws NotFoundException if the user with the username does not exist
	 */
	public User getUserByUsername(String username) throws NotFoundException;
	
	/**
	 * Get the user object base on registration code
	 * @param regCode the registration code
	 * @return user object
	 * @throws NotFoundException if the user with the registration code does not exist
	 */
	public User getUserByRegCode(String regCode) throws NotFoundException;
	
	/**
	 * Get the user object base on registration code
	 * @param regCode the registration code
	 * @param errorType a custom error message if encountered not found exception
	 * @return user object
	 * @throws NotFoundException if the user with the registration code does not exist
	 */
	public User getUserByRegCode(String regCode, ErrorType errorType) throws NotFoundException;
	
	/**
	 * Validate access token. Return the user object if the validation is successful
	 * The user detail is not loaded
	 * @param request the request JSON from post requests
	 * @return the user object
	 * @throws InvalidStateException if validation failed
	 * @throws InvalidParamException if the request does not have key accessToken
	 */
	public User validateAccessToken(Map<String, Object> request) throws InvalidStateException, InvalidParamException;
	/**
	 * Validate access token. Return the user object if the validation is successful
	 * @param request the request JSON from post requests
	 * @param loadDetails load the user details or not
	 * @return the user or user detail object
	 * @throws InvalidStateException if validation failed
	 * @throws InvalidParamException if the request does not have key accessToken
	 */
	public User validateAccessToken(Map<String, Object> request, boolean loadDetails) throws InvalidStateException, InvalidParamException;
	
	/**
	 * Update the user record if the input parameter is not null
	 * @param userId the DB id of the user
	 * @param username the username of the user
	 * @param password the hashed password of the user
	 * @param accessToken the access token of the user
	 * @param remember if we auto renew the token or not
	 * @param verificationCode code for email & reset password, etc.
	 * @param confirmed if the account (email or phone #) is confirmed or not
	 * @param updatedBy the user id that updated the user
	 * @param roleId the role id of the user
	 * @param managerId the manager (user) id of the user
	 * @param companyId the company id of the user
	 * @param activated if the registration is activated or not
	 * @param name the name of the user
	 * @param phone the phone # of the user
	 * @param workId the work id of the user
	 * @param avatarId the avatar id of the user
	 * @param birthday the birthday of the user
	 * @param joinedDate the joined date of the user
	 */
	public void updateUserNotNull(int userId, String username, String password, String accessToken, Boolean remember,
			String verificationCode, Boolean confirmed, Integer updatedBy, Integer roleId, Integer managerId, Integer companyId,
			Boolean activated, String name, String phone, String workId, Integer avatarId, Instant birthday, Instant joinedDate);
	
	/**
	 * Get user detail object by user id
	 * @param userId the id of the user
	 * @return the user details object
	 * @throws NotFoundException if the user does not exist
	 */
	public UserDetail getUserDetailById(int userId) throws NotFoundException;
	
	/**
	 * Create the user activation request and save into database
	 * @param requesterId the user id of the requester
	 * @param responderId the user id of the manager & responder
	 * @return the DB id of the row
	 */
	public int createUserActivation (int requesterId, int responderId);
	
	/**
	 * Get all users with details base on company ID. Users are returned base on limit and offset and ordered by alphabeticaly order on name
	 * @param companyId the company id of the users
	 * @param offset the starting index
	 * @param limit the amount of users to be selected
	 * @return the list of users with details
	 * @throws NotFoundException if no users are found base on the conditions
	 */
	public List<UserDetail> getAllUserDetails(int companyId, int offset, int limit) throws NotFoundException;
	

}
