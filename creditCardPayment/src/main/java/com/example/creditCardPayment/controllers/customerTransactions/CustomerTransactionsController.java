package com.example.creditCardPayment.controllers.customerTransactions;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creditCardPayment.dto.customerTransactions.CustomerTransactionDTO;
import com.example.creditCardPayment.entity.customerTransactions.CustomerTransaction;
import com.example.creditCardPayment.response.MessageResponse;
import com.example.creditCardPayment.security.UserDetailsImpl;
import com.example.creditCardPayment.services.customerTransactions.CustomerTransactionsService;

@RestController
@RequestMapping("/creditcardPayment/transactions")
public class CustomerTransactionsController {
	
	@Autowired
	CustomerTransactionsService customerTransactionsService;


	@PostMapping("/saveTransaction")
	public ResponseEntity<?> saveCustomerTransactions(@Valid @RequestBody CustomerTransactionDTO customerTransactionDTO,
			@AuthenticationPrincipal UserDetailsImpl loggeduser) throws ParseException  {
		customerTransactionsService.saveCustomerTransactions(customerTransactionDTO,loggeduser);
		return ResponseEntity.ok(new MessageResponse("Transaction completed Successfully!"));
	}

	@GetMapping("getCustomerTransactionList")
	public List<CustomerTransaction> getCustomerTransactionList(@AuthenticationPrincipal UserDetailsImpl loggeduser) {
		return customerTransactionsService.getCustomerTransactionList(loggeduser);
	}

}
