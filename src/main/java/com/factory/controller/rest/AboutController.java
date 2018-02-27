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
		
		testColumns();
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}
	
	private void testColumns() {
		About about = aboutDao.findObject(AboutDao.Field.TEST.getQueryTerm("works"));
		System.out.println(about.getNumber());
		System.out.println(about.getBool());
		
		about = aboutDao.findObject(AboutDao.Field.TEST.getQueryTerm("val"));
		System.out.println(about.getNumber());
		System.out.println(about.getBool());
		/*
		about.setTest("test");
		aboutDao.persist(about);
		
		about.setBool(null);
		about.setNumber(null);
		aboutDao.persist(about);*/
		
		//Below are detailed comparison.
		//In conclusion, do direct == on Integer, Boolean etc since we are using boxing rule instead of creating object of them.
		
		boolean truth = false;
		
		Integer a = 1;
		Integer b = 1;

		truth = a.equals(b);
		truth = a == b;
		truth = (int) a == (int)b;
		
		a = new Integer(1);
		b = new Integer(1);
		truth = a.equals(b);
		truth = a == b;
		
		a = null;
		b = null;
		truth = a == b;
		//truth = a.equals(b); Exception
		
		Boolean c = null;
		Boolean d = null;
		
		truth = c == d;
		c = false;
		truth = c == d;
		c = null;
		d = false;
		truth = c == d;
		
		c = false;
		truth = c == d;
	}
	
}

