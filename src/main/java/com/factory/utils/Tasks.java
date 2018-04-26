package com.factory.utils;

import java.util.Properties;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Tasks {
	
	@Value("${jwtSecret}") private String jwtSecret;
	
	@PostConstruct
    public void initializer() {
		
		//Set up time zone
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		//Set up email host
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", Params.EMAIL_HOST);
		
		//Set up JWT
		Params.JWT_SECRET = jwtSecret;
	}
	
	//@Scheduled(cron = "*/5 * * * * *") //Every 5 seconds, for testing
	@Scheduled(cron = "0 0 6 * * ?") //6 am in EST 1 am in UTC time
    public void scheduler() {
		
	}

}
