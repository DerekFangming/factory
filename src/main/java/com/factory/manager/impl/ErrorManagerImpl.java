package com.factory.manager.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.factory.exceptions.NotFoundException;
import com.factory.manager.ErrorManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ErrorManagerImpl implements ErrorManager {
	
	//@Autowired private ErrorLogDao errorLogDao;
	
	@Override
	public void logError(Exception e) {
		Writer writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		
		saveToErrorLog(null, null, stackTrace);
	}
	
	@Override
	public void logError(Exception e, HttpServletRequest request) {
		Writer writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		
		StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();
	    saveToErrorLog(requestURL.toString(), queryString, stackTrace);
	}
	
	@Override
	public void logError(Exception e, String url, Map<String, Object> request) {
		Writer writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		
		saveToErrorLog(url, getPostRequestsString(request), stackTrace); 
	}

	@Override
	public Map<String, Object> createRespondFromException(Exception e, HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();
		return createErrorRespondFromException(e, requestURL.toString(), queryString);
	}

	@Override
	public Map<String, Object> createRespondFromException(Exception e, String url, Map<String, Object> request) {
		return createErrorRespondFromException(e, url, getPostRequestsString(request));
	}
	
	private Map<String, Object> createErrorRespondFromException (Exception e, String url, String params) {
		Map<String, Object> respond = new HashMap<String, Object>();
		boolean writeToLog = true;
		/*if (e instanceof NotFoundException) {
			writeToLog = false;
			respond.put("error", e.getMessage());
		} else if(e instanceof SessionExpiredException) {
			writeToLog = false;
			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
		} else if (e instanceof IllegalStateException) {
			respond.put("error", e.getMessage());
		} else if (e instanceof NullPointerException || e instanceof NumberFormatException || e instanceof ClassCastException) {
			respond.put("error", ErrorMessage.INCORRECT_PARAM.getMsg());
		} else if (e instanceof FileNotFoundException) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_PATH.getMsg());
		} else if (e instanceof IOException) {
			respond.put("error", ErrorMessage.INCORRECT_INTER_IMG_IO.getMsg());
		} else if (e instanceof DataIntegrityViolationException) {
			respond.put("error", ErrorMessage.INTERNAL_LOGIC_ERROR.getMsg());
		} else{
			respond.put("error", ErrorMessage.UNKNOWN_ERROR.getMsg());
		}*/
		
		if(writeToLog) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String stackTrace = writer.toString();
			saveToErrorLog(url, params, stackTrace);
		}
		
		return respond;
	}
	
	private void saveToErrorLog(String url, String params, String trace) {
//		try {
//			ErrorLog errorLog = new ErrorLog();
//			errorLog.setUrl(StringUtils.abbreviate(url, 100));
//			errorLog.setParam(params);
//			errorLog.setTrace(trace);
//			errorLog.setCreatedAt(Instant.now());
//			errorLogDao.persist(errorLog);
//		} catch (Exception e1) {
//			try {
//				ErrorLog errorLog = new ErrorLog();
//				errorLog.setUrl(StringUtils.abbreviate(url, 100));
//				errorLog.setParam(params);
//				errorLog.setTrace("Cannot read or having other issues");
//				errorLog.setCreatedAt(Instant.now());
//				errorLogDao.persist(errorLog);
//			} catch(Exception e2){}
//		}
	}
	
	private String getPostRequestsString(Map<String, Object> request) {
		ObjectMapper mapperObj = new ObjectMapper();
		try {
			return mapperObj.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			String json = "{";
			boolean flag = false;
			for (String key : request.keySet()) {
				if (flag) {
					json += ", ";
				}
				flag = true;
				json += key + ": ";
				json += String.valueOf(request.get(key));
			}
			json += "}";
			return json;
		}
	}

}
