package com.example.creditCardPayment.controllers.customer;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creditCardPayment.dto.customer.CustomerRegistrationDTO;
import com.example.creditCardPayment.dto.customer.LoginRequestDTO;
import com.example.creditCardPayment.services.customerRegistration.CustomerRegistrationService;


@RestController
@RequestMapping("/customer/registration")
public class CustomerRegistrationController {

	@Autowired
	CustomerRegistrationService customerRegistrationService;


	@PostMapping("/signup")
	public ResponseEntity<?> registerCustomer(@RequestBody @Valid CustomerRegistrationDTO customerRegistrationDTO) throws ParseException {
		
		return ResponseEntity.ok().body(customerRegistrationService.registerCustomer(customerRegistrationDTO));
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

		return ResponseEntity.ok().body(customerRegistrationService.signIn(loginRequest));
	}


}
