package com.example.creditCardPayment.dto.customerCreditCard;

import java.util.List;
import javax.validation.Valid;

import com.example.creditCardPayment.dto.statement.StatementRemainderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
	
	@Valid
	private List<CustomerCardDTO> customerCardDetailsDTO;
	
	private String customerName;
	
	private String email;
	
	private List<StatementRemainderDTO> statementRemainderDTO;
	

}
