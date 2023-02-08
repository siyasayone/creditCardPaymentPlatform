package com.example.creditCardPayment.controllers.statement;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creditCardPayment.dto.statement.PayBillsDTO;
import com.example.creditCardPayment.services.statement.CustomerStatementService;


@RestController
@RequestMapping("/creditcardPayment/statement")
public class CustomerStatementsController {
	
	@Autowired
	CustomerStatementService customerStatementService;


	@PostMapping("/generateStatement")
	//@Scheduled(cron = "0 0 0 * * *")
	public ResponseEntity<?> generateStatements() throws MessagingException  {
		return customerStatementService.generateStatements();
	}
	
	@PostMapping("sendPaymentDueRemainders")
	//@Scheduled(cron = "0 0 0 * * *")
	public ResponseEntity<?> sendPaymentDueRemainders() {
		return customerStatementService.sendPaymentDueRemainders();
	}
	
	@PostMapping("/payStatementBills")
	public ResponseEntity<?> payStatementBills(@Valid @RequestBody PayBillsDTO payBillsDTO) {
		return customerStatementService.payStatementBills(payBillsDTO);
	}

}
