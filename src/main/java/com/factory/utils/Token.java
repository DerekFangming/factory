package com.factory.utils;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InternalServerException;
import com.factory.exceptions.InvalidStateException;

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
	
	public static Map<String, Object> decodeJWT(String JWTStr) throws InvalidStateException {
		Algorithm algorithm;
		try {
			algorithm = Algorithm.HMAC256(Params.JWT_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build();
		    DecodedJWT jwt = verifier.verify(JWTStr);
		    Map<String, Claim> claims = jwt.getClaims();
		    Map<String, Object> result = new HashMap<String, Object>();
	        for (String key : claims.keySet()) {
	            result.put(key, claims.get(key).asString());
	        }
	        
	        return result;
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			throw new InvalidStateException(ErrorType.INVALID_ACCESS_TOKEN);
		}
	    
	}

}
