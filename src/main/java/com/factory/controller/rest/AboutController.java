package com.factory.controller.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AboutController {
	
	@RequestMapping(value = "/about", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> about(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> respond = new HashMap<String, Object>();
		respond.put("status", "ok");
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
}

