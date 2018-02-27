package com.factory.controller.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.factory.dao.AboutDao;
import com.factory.domain.About;

@Controller
public class AboutController {
	
	@Autowired private AboutDao aboutDao;
	
	@RequestMapping(value = "/about", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> about(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> respond = new HashMap<String, Object>();
		respond.put("status", "ok");
		
		About about = aboutDao.findObject(AboutDao.Field.TEST.getQueryTerm("works"));
		respond.put("about", about.getTest());
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
}

