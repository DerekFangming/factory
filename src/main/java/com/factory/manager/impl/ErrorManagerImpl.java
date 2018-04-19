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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.factory.dao.ErrorLogDao;
import com.factory.domain.ErrorLog;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InvalidParamException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.ErrorManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ErrorManagerImpl implements ErrorManager {
	
	@Autowired private ErrorLogDao errorLogDao;
	
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
		if (e instanceof NotFoundException) {
			writeToLog = false;
			ErrorType errorType = ((NotFoundException) e).getErrorType();
			respond.put("errCode", errorType.getCode());
			respond.put("errMsg", errorType.getMessage());
		} else if (e instanceof InvalidStateException) {
			ErrorType errorType = ((NotFoundException) e).getErrorType();
			respond.put("errCode", errorType.getCode());
			respond.put("errMsg", errorType.getMessage());
		}
//		else if(e instanceof SessionExpiredException) {
//			writeToLog = false;
//			respond.put("error", ErrorMessage.SESSION_EXPIRED.getMsg());
//		}
//		else if (e instanceof IllegalStateException) {
//			respond.put("error", e.getMessage());
//		} 
		else if (e instanceof InvalidParamException || e instanceof NumberFormatException || e instanceof ClassCastException) {
			respond.put("errCode", ErrorType.INVALID_PARAMS.getCode());
			respond.put("errMsg", ErrorType.INVALID_PARAMS.getMessage());
		} else if (e instanceof FileNotFoundException || e instanceof IOException) {
			respond.put("errCode", ErrorType.INTERNAL_ERROR.getCode());
			respond.put("errMsg", ErrorType.INTERNAL_ERROR.getMessage());
		} else if (e instanceof DataIntegrityViolationException) {
			respond.put("errCode", ErrorType.INTERNAL_ERROR.getCode());
			respond.put("errMsg", ErrorType.INTERNAL_ERROR.getMessage());
		} else{
			respond.put("errCode", ErrorType.UNKNOWN_ERROR.getCode());
			respond.put("errMsg", ErrorType.UNKNOWN_ERROR.getMessage());
		}
		
		if(writeToLog) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String stackTrace = writer.toString();
			saveToErrorLog(url, params, stackTrace);
		}
		
		return respond;
	}
	
	private void saveToErrorLog(String url, String params, String trace) {
		ErrorLog errorLog = new ErrorLog();
		errorLog.setUrl(url);
		errorLog.setParam(params);
		errorLog.setTrace(trace);
		errorLog.setCreatedAt(Instant.now());
		errorLogDao.persist(errorLog);
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
