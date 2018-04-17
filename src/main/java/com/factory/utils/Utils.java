package com.factory.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import com.factory.exceptions.InvalidParamException;

public class Utils {
	
	public static boolean prodMode  = false;
	
	public static Date parseDate(Instant instant) {
		try {
			return Date.from(instant);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Instant parseTimestamp(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toInstant();
	}
	
	public static <T> T notNull(T o) {
		if (o == null) {
			throw new InvalidParamException();
		}
		return o;
	}

}
