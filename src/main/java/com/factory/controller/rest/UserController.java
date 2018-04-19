package com.factory.controller.rest;

import java.time.Instant;
import java.util.HashMap;
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
import com.factory.domain.type.RoleOffsetType;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InternalServerException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.ErrorManager;
import com.factory.manager.RoleManager;
import com.factory.manager.UserManager;
import com.factory.utils.Utils;

@Controller
public class UserController {
	
	@Autowired private ErrorManager errorManager;
	@Autowired private UserManager userManager;
	@Autowired private RoleManager roleManager;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			String regCode = (String)request.get("regcode");
			
			if (regCode != null) { // Registering with a registration code
				String username = (String) Utils.notNull(request.get("username"));
				String password = (String) Utils.notNull(request.get("password"));
				boolean remember = (boolean) Utils.notNull(request.get("remember"));
				String name = (String) Utils.notNull(request.get("name"));
				String phone = (String) request.get("phone");
				String workId = (String) request.get("workIdd");
				Instant birthday = Utils.parseInstantStr((String) request.get("birthday"));
				Instant joinedDate = Utils.parseInstantStr((String) request.get("joinedDate"));
				String avatar = (String) request.get("avatar");
				
				User manager = userManager.getUserByRegCode(regCode, ErrorType.INVALID_REG_CODE);
				
				username = username.toLowerCase();
				try {
					userManager.getUserByUsername(username);
					throw new InvalidStateException(ErrorType.USERNAME_UNAVAILABLE);
				} catch (NotFoundException ignored) {}
				
				
				String salt = BCrypt.gensalt();
				password = BCrypt.hashpw(password, salt);
				
				Integer roleId = null;
				try {
					roleId = roleManager.getRoleByIdAndLevelOffset(manager.getRoleId(), RoleOffsetType.ONE_LEVEL_DOWN).getId();
				} catch (NotFoundException e) {
					throw new InternalServerException(e);
				}
				Integer avatarId = null;
				if (avatar != null) {
					//TODO
				}
				int userId = userManager.registerUser(username, password, remember, false, salt, null, roleId, manager.getId(),
						manager.getCompanyId(), !manager.getVerificationNeeded(), name, phone, workId, avatarId, birthday, joinedDate);
				
				respond.put("userId", userId);
			}
			
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/register", request);
		}
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();

		String username = (String) Utils.notNull(request.get("username"));
		String password = (String) Utils.notNull(request.get("password"));
		
		
		
		
		
		
		System.out.println(BCrypt.gensalt());
		
		String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());
		System.out.println(pw_hash);
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/validate_reg_code", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> about(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		
		String code = (String)request.get("regCode");
		if (code.equals("1")) {
			respond.put("error", "");
		} else {
			respond.put("error", "The registration code does not exist");
		}
		
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	

}
