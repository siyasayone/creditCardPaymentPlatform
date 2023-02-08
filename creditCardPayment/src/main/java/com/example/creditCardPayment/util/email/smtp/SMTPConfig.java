package com.example.creditCardPayment.util.email.smtp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SMTPConfig {

	static @Bean public SMTPBroker getSMTPBroker() {
		return new SMTPBroker();
	}
}
