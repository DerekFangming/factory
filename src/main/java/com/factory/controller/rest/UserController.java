package com.factory.controller.rest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.factory.domain.User;
import com.factory.domain.UserDetail;
import com.factory.domain.type.RoleOffsetType;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InternalServerException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.CompanyManager;
import com.factory.manager.ErrorManager;
import com.factory.manager.ImageManager;
import com.factory.manager.RoleManager;
import com.factory.manager.UserManager;
import com.factory.utils.Email;
import com.factory.utils.Params;
import com.factory.utils.Token;
import com.factory.utils.Utils;

@Controller
public class UserController {
	
	@Autowired private ErrorManager errorManager;
	@Autowired private UserManager userManager;
	@Autowired private RoleManager roleManager;
	@Autowired private CompanyManager companyManager;
	@Autowired private ImageManager imageManager;
	 
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			String username = ((String) Utils.notNull(request.get("username"))).toLowerCase();
			String password = (String) Utils.notNull(request.get("password"));
			boolean remember = (boolean) Utils.notNull(request.get("remember"));
			String name = (String) Utils.notNull(request.get("name"));
			String phone = (String) request.get("phone");
			String workId = (String) request.get("workId");
			Instant birthday = Utils.parseInstantStr((String) request.get("birthday"));
			Instant joinedDate = Utils.parseInstantStr((String) request.get("joinedDate"));
			String avatar = (String) request.get("avatar");
			
			if(!Utils.isEmail(username))
				throw new InvalidStateException(ErrorType.INVALID_USERNAME_FORMAT);
			
			try {
				userManager.getUserByUsername(username);
				throw new InvalidStateException(ErrorType.USERNAME_UNAVAILABLE);
			} catch (NotFoundException ignored) {}
			
			String salt = BCrypt.gensalt();
			password = BCrypt.hashpw(password, salt);
			Instant expDate = Instant.now().plus(Duration.ofDays(remember ? Params.TOKEN_REMEMBER_EXP_DAY : Params.TOKEN_DEFAULT_EXP_DAY));
			String accessToken = Token.createAccessToken(username, expDate);
			String verificationCode = Token.createVerificationCode(username);
			
			String regCode = (String)request.get("regCode");
			if (regCode != null) { // Registering with a registration code
				
				User manager = userManager.getUserByRegCode(regCode, ErrorType.INVALID_REG_CODE);
				
				Integer roleId = null;
				try {
					roleId = roleManager.getRoleByIdAndLevelOffset(manager.getRoleId(), RoleOffsetType.ONE_LEVEL_DOWN).getId();
				} catch (NotFoundException e) {
					throw new InternalServerException(e);
				}
				
				Integer avatarId = null;
				if (avatar != null) {
					try {
						avatarId = imageManager.createImage(avatar, manager.getCompanyId(), null, null, null, 0);
					} catch(Exception e) {}
				}
				
				int userId = userManager.createUser(username, password, accessToken, remember, verificationCode, false, salt, null, roleId, manager.getId(),
						manager.getCompanyId(), !manager.getVerificationNeeded(), name, phone, workId, avatarId, birthday, joinedDate);
				
				if (avatar != null) {
					imageManager.updateImageNotNull(avatarId, null, null, null, null, userId);
				}
				
				if (manager.getVerificationNeeded()) {
					userManager.createUserActivation(userId, manager.getId());
				}
				
				respond.put("activated", !manager.getVerificationNeeded());
			} else { // Registering a new company
				String companyName = (String) Utils.notNull(request.get("companyName"));
				String description = (String) request.get("description");
				String industry = (String) request.get("industry");
				
				try {
					companyManager.getCompanyByName(companyName);
					throw new InvalidStateException(ErrorType.COMPANY_NAME_UNAVAILABLE);
				} catch (NotFoundException ignored) {}
				
				int companyId = companyManager.createCompany(companyName, description, industry, 0);
				
				int roleId = roleManager.createRole(companyId, companyName, 0, 1, true, true);
				
				Integer avatarId = null;
				if (avatar != null) {
					try {
						avatarId = imageManager.createImage(avatar, companyId, null, null, null, 0);
					} catch(Exception e) {}
				}
				
				int userId = userManager.createUser(username, password, accessToken, remember, verificationCode, false, salt, null, roleId, null,
						companyId, true, name, phone, workId, avatarId, birthday, joinedDate);
				
				if (avatar != null) {
					imageManager.updateImageNotNull(avatarId, null, null, null, null, userId);
				}
				
				respond.put("activated", true);
			}

			try {
				Email.sendAccountVerification(name, username, verificationCode);
			} catch (Exception e) {
				errorManager.logError(e, "/register", request);
			}
			
			if ((boolean) respond.get("activated")) {
				respond.put("accessToken", accessToken);
			} else {
				throw new InvalidStateException(ErrorType.USER_NOT_ACTIVITED);
			}
			
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/register", request);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		
		try {
			User user = null;
			Object usernameObj = request.get("username");
			
			if (usernameObj != null) { //Logging in with username and password
				String username = ((String) usernameObj).toLowerCase();
				String password = (String) Utils.notNull(request.get("password"));
				boolean remember = (boolean) Utils.notNull(request.get("remember"));
				
				try {
					user = userManager.getUserByUsername(username);
					password = BCrypt.hashpw(password, user.getSalt());
					
					if (!user.getPassword().equals(password))
						throw new InvalidStateException();
				} catch (Exception e) {
					throw new InvalidStateException(ErrorType.USERNAME_OR_PASSWORD_INCORRECT);
				}
				
				//If token is expired or invalid, recreate the token
				boolean recreateAccessToken = false;
				try{
					Map<String, Object> result = Token.decodeJWT(user.getAccessToken());
					Instant exp = Instant.parse((String)result.get("expire"));
					
					if(exp.compareTo(Instant.now()) < 0){
						recreateAccessToken = true;
					}
				}catch (Exception e) {
					recreateAccessToken = true;
				}
				
				//If the flag is still false but there's a change in remember field, recreate token
				if (recreateAccessToken == false && remember != user.getRemember())
					recreateAccessToken = true;
				
				if(recreateAccessToken){
					Instant expDate = Instant.now().plus(Duration.ofDays(remember ? Params.TOKEN_REMEMBER_EXP_DAY : Params.TOKEN_DEFAULT_EXP_DAY));
					String accessToken = Token.createAccessToken(username, expDate);
					
					userManager.updateUserNotNull(user.getId(), null, null, accessToken, remember, null, null, null, null, null, null, null, null, null, null, null, null, null);
					user.setAccessToken(accessToken);
				}
				
			} else { //Logging in with access token
				user = userManager.validateAccessToken(request);
			}
			
			if (user.getActivated()) {
				respond.put("accessToken", user.getAccessToken());
				respond.put("name", user.getName());
				respond.put("phone", user.getPhone());
				respond.put("workId", user.getWorkId());
				respond.put("avatarId", user.getAvatarId());
				respond.put("birthday", Utils.instantToString(user.getBirthday()));
				respond.put("joinedDate", Utils.instantToString(user.getJoinedDate()));
			} else {
				throw new InvalidStateException(ErrorType.USER_NOT_ACTIVITED);
			}
			
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/login", request);
		}
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/validate_reg_code", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> validateRegCode(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			String regCode = (String) Utils.notNull(request.get("regCode"));
			userManager.getUserByRegCode(regCode, ErrorType.INVALID_REG_CODE);
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/validate_reg_code", request);
		}
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/check_username", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			String username = (String) Utils.notNull(request.get("username"));
			
			try {
				userManager.getUserByUsername(username);
				throw new InvalidStateException(ErrorType.USERNAME_UNAVAILABLE);
			} catch (NotFoundException ignored) {}
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/check_username", request);
		}
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_all_users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			int offset = (int) Utils.notNull(request.get("offset"));
			int limit = (int) Utils.notNull(request.get("limit"));
			
			User user = userManager.validateAccessToken(request);
			
			List<UserDetail> userDetailList = userManager.getAllUserDetails(user.getCompanyId(), offset, limit);
			List<Map<String, Object>> processedList = new ArrayList<>();
			for (UserDetail u : userDetailList) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", u.getId());
				map.put("confirmed", u.getConfirmed());
				map.put("activated", u.getActivated());
				map.put("name", u.getName());
				map.put("phone", u.getPhone());
				map.put("workId", u.getWorkId());
				map.put("avatarId", u.getAvatarId());
				map.put("birthday", Utils.instantToString(u.getBirthday()));
				map.put("joinedDate", Utils.instantToString(u.getJoinedDate()));
				map.put("roleName", u.getRoleName());
				
				processedList.add(map);
			}
			
			respond.put("userList", processedList);
			
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/get_all_users", request);
		}
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
