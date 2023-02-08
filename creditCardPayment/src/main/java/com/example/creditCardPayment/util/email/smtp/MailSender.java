package com.example.creditCardPayment.util.email.smtp;

import java.util.List;

import javax.mail.MessagingException;

public class MailSender {

	public static void sendMail(String port, String hostName, String fromAddress, String password, String contentType,
			String subject, String content, List<String> attachment, List<String> toAddress, List<String> bcc,
			List<String> cc) {

		SMTPBroker broker = new SMTPBroker();

		try {
			broker.sendMessage(port, fromAddress, hostName, password, contentType, subject, content, attachment,
					toAddress, bcc, cc);
		} catch (MessagingException messagingException) {
			messagingException.printStackTrace();
		}
	}

}
