package com.example.creditCardPayment.util.email.smtp;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 
 * @author Siya
 *
 */
public class SMTPAuthenticator extends Authenticator {
	private String userName;
	private String password;

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public SMTPAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 
	 */
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.userName, this.password);
	}
}
