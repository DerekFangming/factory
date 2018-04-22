package com.factory.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;

public class Email {
	
	
	public static void sendAccountVerification(String name, String email, String verificationCode) throws MessagingException {
		String message = "Hello " + StringUtils.capitalize(name) + ",\n\n";
		message += "Welcome to factory. Please click on the following link to confirm your email address. \n";
		message += Params.EMAIL_CONF_ADDRESS + verificationCode + "\n\n";
		message += "Thank you,\n" + "Factory team";
		
		sendEmail(Params.NO_REPLAY_EMAIL, email, "Email Confirmation", message);
		
	}
	
	public static void sendEmail(String from, String to, String subject, String content) throws MessagingException{
		//TODO: Move this to set up code
		String host = "localhost";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		if (to.indexOf(',') > 0){
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		} else {
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}
		message.setSubject(subject);
		message.setText(content);
		Transport.send(message);
	}

}
