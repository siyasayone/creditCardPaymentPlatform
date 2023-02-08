package com.example.creditCardPayment.dto.customerTransactions;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.creditCardPayment.util.date.JsonDateDeserializer;
import com.example.creditCardPayment.util.date.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTransactionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long customerTransactionId;

	@NotNull
	private Long customerCardNumber;

	@NotNull
	private Long cvv;

	@NotEmpty
	@NotBlank
	private String cardExpiryDate;

	@NotNull
	private Long amount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date transactionDate;
	
	private Long customerId;
	
}
