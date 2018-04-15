package com.factory.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
	
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
