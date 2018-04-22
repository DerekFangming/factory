package com.factory.utils;

import java.time.Duration;
import java.time.Instant;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.factory.exceptions.InternalServerException;

public class Token {
	
	public static String createVerificationCode(String username) throws InternalServerException {
		return createToken(username, Instant.now().plus(Duration.ofDays(1)), "email");
	}
	
	public static String createAccessToken(String username, Instant expDate) throws InternalServerException {
		return createToken(username, expDate, null);
	}
	
	public static String createToken(String username, Instant expDate, String action) throws InternalServerException {
		try {
			Algorithm algorithm = Algorithm.HMAC256(Params.JWT_SECRET);
			Builder builder = JWT.create()
		    		.withClaim("username", username)
		    		.withClaim("expire", expDate.toString());
			if (action != null)
				builder = builder.withClaim("action", "email");
			
		    String token = builder.sign(algorithm);
		    return token;
		} catch (Exception e) {
			throw new InternalServerException(e);
		}
	}

}
