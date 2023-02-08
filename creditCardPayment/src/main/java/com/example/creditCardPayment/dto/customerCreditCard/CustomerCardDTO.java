package com.example.creditCardPayment.dto.customerCreditCard;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCardDTO {

	@NotNull
	@Digits(integer = 16, fraction = 0)
	private Long cardNumber;
	
	@NotNull
	@Digits(integer = 3, fraction = 0)
	private Long cvv;
	
	@NotBlank
	@NotEmpty
	private String customerName;
	
	@NotBlank
	@NotEmpty
	private String expiryDate;
	
	@Digits(integer = 10, fraction = 0)
	private Long moblileNumber;
	
	private Long creditLimit;
	
	private Long availableCredit;
	
}
