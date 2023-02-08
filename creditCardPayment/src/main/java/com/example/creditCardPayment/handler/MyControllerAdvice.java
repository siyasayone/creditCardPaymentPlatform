package com.example.creditCardPayment.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.creditCardPayment.exception.CardException;
import com.example.creditCardPayment.exception.CardNotExistException;
import com.example.creditCardPayment.exception.CreditLimitException;
import com.example.creditCardPayment.exception.DateException;
import com.example.creditCardPayment.exception.ExpiredCreditCardException;
import com.example.creditCardPayment.exception.InvalidCVVException;
import com.example.creditCardPayment.exception.InvalidCustomerDetailsException;
import com.example.creditCardPayment.exception.InvalidExpiryDateException;

/**
 * 
 * @author Siya
 *
 */

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("request=="+request);
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	  @Override
	  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	      String name = ex.getParameterName();
	      String message=name+" parameter is missing";
	      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	  }
	
	@ExceptionHandler(DateException.class)
	protected ResponseEntity<String> DateException(DateException dateException) {
		return new ResponseEntity<>("Given date is not valid according to the pattern dd/mm/yyyy", HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(InvalidCustomerDetailsException.class)
	protected ResponseEntity<String> InvalidCustomerDetailsException(InvalidCustomerDetailsException invalidCustomerDetailsException) {
		return new ResponseEntity<String>("Customer details not matching", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CardNotExistException.class)
	protected ResponseEntity<String> CardNotExistException(CardNotExistException cardNotExistException) {
		return new ResponseEntity<>("Invalid Card Number", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CreditLimitException.class)
	protected ResponseEntity<String> CreditLimitException(CreditLimitException creditLimitException) {
		return new ResponseEntity<>("No sufficient credit limit for this request", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidCVVException.class)
	protected ResponseEntity<String> InvalidCVVException(InvalidCVVException invalidCVVException) {
		return new ResponseEntity<>("Cvv number is not matching", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidExpiryDateException.class)
	protected ResponseEntity<String> InvalidExpiryDateException(InvalidExpiryDateException invalidExpiryDateException) {
		return new ResponseEntity<>("Expiry date is not matching", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ExpiredCreditCardException.class)
	protected ResponseEntity<String> ExpiredCreditCardException(ExpiredCreditCardException expiredCreditCardException) {
		return new ResponseEntity<>("Credit Card is not valid!Validity expired...", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CardException.class)
	protected ResponseEntity<String> CardException(CardException cardException) {
		return new ResponseEntity<>("please enter card number", HttpStatus.NOT_FOUND);
	}
}
