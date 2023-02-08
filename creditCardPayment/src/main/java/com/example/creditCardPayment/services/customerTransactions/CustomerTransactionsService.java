package com.example.creditCardPayment.services.customerTransactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.creditCardPayment.dto.customerTransactions.CustomerTransactionDTO;
import com.example.creditCardPayment.entity.customer.CustomerRegistration;
import com.example.creditCardPayment.entity.customerCreditCard.CustomerCreditCard;
import com.example.creditCardPayment.entity.customerTransactions.CustomerTransaction;
import com.example.creditCardPayment.exception.CardNotExistException;
import com.example.creditCardPayment.exception.CreditLimitException;
import com.example.creditCardPayment.exception.DateException;
import com.example.creditCardPayment.exception.ExpiredCreditCardException;
import com.example.creditCardPayment.exception.InvalidCVVException;
import com.example.creditCardPayment.exception.InvalidCustomerDetailsException;
import com.example.creditCardPayment.exception.InvalidExpiryDateException;
import com.example.creditCardPayment.repository.customer.CustomerRepository;
import com.example.creditCardPayment.repository.customerCreditCard.CustomerCreditCardRepository;
import com.example.creditCardPayment.repository.customerTransactions.CustomerTransactionRepository;
import com.example.creditCardPayment.security.UserDetailsImpl;

/**
 * 
 * @author Siya
 *
 */
@Service
public class CustomerTransactionsService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerTransactionRepository customerTransactionRepository;
	
	@Autowired
	private CustomerCreditCardRepository customerCreditCardRepository;

	public CustomerTransaction saveCustomerTransactions(@Valid CustomerTransactionDTO customerTransactionDTO,
			UserDetailsImpl loggeduser) throws  ParseException {
		CustomerRegistration user=customerRepository.findByUsernameIs(loggeduser.getUsername());
		CustomerCreditCard card = customerCreditCardRepository.findByCardNumberAndCustomerId(customerTransactionDTO.getCustomerCardNumber(),user.getCustomerId());
		if (card == null) {
			throw new CardNotExistException("Invalid Card Number");
		}
		if(! user.getCustomerId().equals(card.getCustomerId())) {
			throw new InvalidCustomerDetailsException("Customer detsils not matching");	
		}
		Calendar cl = Calendar.getInstance();
		if (!card.getCvv().equals(customerTransactionDTO.getCvv())) {
			throw new InvalidCVVException("Cvv number is not matching");
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
		if(!customerTransactionDTO.getCardExpiryDate().isEmpty()&& !customerTransactionDTO.getCardExpiryDate().isBlank()) {
			try {
				Date d1=format.parse(customerTransactionDTO.getCardExpiryDate());
			} catch (ParseException e) {
				throw new DateException("Given date is not valid according to the pattern yyyy-MM-dd");
			}
		}
		String newstring = new SimpleDateFormat("yyyy-MM-dd").format(card.getExpiryDate());
		if(!Objects.equals(newstring, customerTransactionDTO.getCardExpiryDate())) {
			throw new InvalidExpiryDateException("Expiry date is not matching");
		}	
	
		int result = format.parse(customerTransactionDTO.getCardExpiryDate()).compareTo(cl.getTime());
		if (result < 0) {
		throw new ExpiredCreditCardException("Credit Card is not valid!Validity expired...");
		}
		
		if (card.getAvailableCredit() < customerTransactionDTO.getAmount() || card.getAvailableCredit()==0) {
		throw new CreditLimitException("No sufficient credit limit for this request");	
		}

		CustomerTransaction entity = customerTransactionRepository
				.findByCustomerTransactionId(customerTransactionDTO.getCustomerTransactionId());
		if (entity == null) {
			entity = new CustomerTransaction();
			entity.setCustomerId(user.getCustomerId());
			entity.setCustomerCardNumber(customerTransactionDTO.getCustomerCardNumber());
			entity.setCvv(customerTransactionDTO.getCvv());
			entity.setAmount(customerTransactionDTO.getAmount());
			entity.setTransactionDate(cl.getTime());
		}
		CustomerTransaction yr = customerTransactionRepository.save(entity);
		Long availableCredit = card.getAvailableCredit() - customerTransactionDTO.getAmount();
		Long totalDue = card.getTotalDue() + customerTransactionDTO.getAmount();
		customerCreditCardRepository.updateCreditLimit(availableCredit, totalDue, user.getCustomerId(),card.getCardNumber());
		return yr;
	}

	public List<CustomerTransaction> getCustomerTransactionList(UserDetailsImpl loggeduser) {
		CustomerRegistration user=customerRepository.findByUsernameIs(loggeduser.getUsername());
		List<CustomerTransaction> cd = customerTransactionRepository
				.findByCustomerTransactions(user.getCustomerId());
		return cd.stream().collect(Collectors.toList());
	}
	
}