package com.example.creditCardPayment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Siya
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditLimitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage;
	

}