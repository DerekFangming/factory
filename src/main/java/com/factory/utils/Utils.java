package com.factory.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

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

}
