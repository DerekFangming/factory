package com.factory.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ErrorManager {
	
	/**
	 * Log an exception into error log table
	 * @param e the exception
	 * @param url the url for current request
	 * @param request the request object
	 */
	public void logError(Exception e);
	public void logError(Exception e, HttpServletRequest request);
	public void logError(Exception e, String url, Map<String, Object> request);
	
	/**
	 * Create error respond base on the exception. If exception is not acceptable, record to error log table
	 * @param e the exception
	 * @param request the servlet request object
	 * @return a map with key error with error message
	 */
	public Map<String, Object> createRespondFromException(Exception e, HttpServletRequest request);
	
	/**
	 * Create error respond base on the exception. If exception is not acceptable, record to error log table
	 * @param e the exception
	 * @param url the url for current request
	 * @param request the request object
	 * @return a map with key error with error message
	 */
	public Map<String, Object> createRespondFromException(Exception e, String url, Map<String, Object> request);

}
