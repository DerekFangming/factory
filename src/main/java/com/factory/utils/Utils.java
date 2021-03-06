package com.factory.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.factory.exceptions.InvalidParamException;

public class Utils {
	
	public static Date parseDate(Instant instant) {
		try {
			return Date.from(instant);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Instant parseInstantStr(String instantStr) {
		try {
			return Instant.parse(instantStr);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Instant parseTimestamp(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toInstant();
	}
	
	public static String instantToString(Instant instant) {
		return instant == null ? null : instant.toString();
	}
	
	public static <T> T notNull(T o) throws InvalidParamException{
		if (o == null) {
			throw new InvalidParamException();
		}
		return o;
	}
	
	public static boolean isEmail(String email) {
		if (email == null)
			return false;
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		
		return m.matches();
	}

}
