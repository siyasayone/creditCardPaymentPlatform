package com.example.creditCardPayment.util.email.smtp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmtpEmailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String hostName;
	private String port;
	private String password;
	private String fromAddress;
	private String contentType;
//	private String webMail;
	private String subject;
	private String content;
	private List<String> attachment;

	private List<String> toAddress = new ArrayList<>();;
	private List<String> bcc = new ArrayList<>();
	private List<String> cc = new ArrayList<>();

}
