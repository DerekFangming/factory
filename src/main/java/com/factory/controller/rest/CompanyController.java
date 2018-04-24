package com.factory.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.CompanyManager;
import com.factory.manager.ErrorManager;
import com.factory.utils.Utils;

@Controller
public class CompanyController {
	
	@Autowired private CompanyManager companyManager;
	@Autowired private ErrorManager errorManager;
	
	@RequestMapping(value = "/check_company_name", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> about(@RequestBody Map<String, Object> request) {
		Map<String, Object> respond = new HashMap<String, Object>();
		try {
			String companyName = (String) Utils.notNull(request.get("companyName"));
			
			try {
				companyManager.getCompanyByName(companyName);
				throw new InvalidStateException(ErrorType.COMPANY_NAME_UNAVAILABLE);
			} catch (NotFoundException ignored) {}
		} catch (Exception e) {
			respond = errorManager.createRespondFromException(e, "/check_company_name", request);
		}
		
		return new ResponseEntity<Map<String, Object>>(respond, HttpStatus.OK);
	}

}
