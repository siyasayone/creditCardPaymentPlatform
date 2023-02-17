package com.example.creditCardPayment.controllers.customerCreditCard;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.creditCardPayment.dto.customerCreditCard.CardDTO;
import com.example.creditCardPayment.dto.customerCreditCard.CustomerCardDTO;
import com.example.creditCardPayment.entity.customerCreditCard.CustomerCreditCard;
import com.example.creditCardPayment.entity.statement.CustomerStatement;
import com.example.creditCardPayment.exception.DateException;
import com.example.creditCardPayment.response.MessageResponse;
import com.example.creditCardPayment.security.UserDetailsImpl;
import com.example.creditCardPayment.services.customerCards.CustomerCardService;


@RestController
@RequestMapping("/creditcardPayment/card")
public class CustomerCreditCardController {
	
	@Autowired
	CustomerCardService customerCardService;


	@PostMapping("/addCard")
	public ResponseEntity<?> addCustomerCard(@Valid @RequestBody CardDTO customerCardDTO,
			@AuthenticationPrincipal UserDetailsImpl loggeduser) throws ParseException {
		customerCardService.addCustomerCard(customerCardDTO,loggeduser);
		return ResponseEntity.ok(new MessageResponse("card added Successfully!"));
	}

	@PostMapping("/updateCard")
	public ResponseEntity<CustomerCreditCard> updateCard(@RequestBody CustomerCardDTO customerCardDTO,
			@AuthenticationPrincipal UserDetailsImpl loggeduser) throws ParseException, DateException {
		return ResponseEntity.ok().body(customerCardService.updateCard(customerCardDTO,loggeduser));
	}
	
	@GetMapping("/removeCard")
	public ResponseEntity<Object> removeCard(@RequestParam Long cardNumber,@AuthenticationPrincipal UserDetailsImpl loggeduser) {
		return customerCardService.removeCard(cardNumber,loggeduser);

	}
	
	@GetMapping("/showCardDetails")
	public CardDTO showCardDetails(@AuthenticationPrincipal UserDetailsImpl loggeduser) {
		return customerCardService.showCardDetails(loggeduser);
	}
	
	@GetMapping("getStatementHistory")
	public List<CustomerStatement> getStatementHistory(@RequestParam Long cardNumber) {
		return customerCardService.getStatementHistory(cardNumber);
	}
	
	@GetMapping("viewGeneratedBills")
	public CustomerStatement viewGeneratedBills(@RequestParam Long cardNumber) {
		return customerCardService.viewGeneratedBills(cardNumber);
	}
	
	@PostMapping("/refundAmnt")
	public ResponseEntity<?> refundAmnt(@Valid @RequestParam Long cardNumber,@RequestParam Long amount) {
		return customerCardService.refundAmnt(cardNumber,amount);
	}


}
