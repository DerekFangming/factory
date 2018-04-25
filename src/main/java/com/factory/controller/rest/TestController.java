package com.factory.controller.rest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.factory.utils.Utils;

@SuppressWarnings("unused")
@Controller
public class TestController {
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		
		Instant birthday = Utils.parseInstantStr((String) request.get("birthday"));
		
		System.out.println(birthday);
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
