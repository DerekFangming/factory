package com.factory.controller.rest;

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

import com.factory.manager.ErrorManager;
import com.factory.utils.Utils;

@Controller
public class UserController {
	
	@Autowired private ErrorManager errorManager;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			//
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
