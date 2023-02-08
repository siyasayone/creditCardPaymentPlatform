package com.example.creditCardPayment.dto.statement;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayBillsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long customerTransactionId;

	@NotNull
	private Long customerCardNumber;

	@NotNull
	private Long fromAccount;

	@NotEmpty
	private String branchName;

	@NotEmpty
	private String cardType;

	@NotNull
	private Long amount;

}
