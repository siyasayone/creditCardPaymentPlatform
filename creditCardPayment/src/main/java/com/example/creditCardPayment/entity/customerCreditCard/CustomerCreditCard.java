package com.example.creditCardPayment.entity.customerCreditCard;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customerCreditCard")
public class CustomerCreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customercardId;

	@Column(length = 50)
	private String customerName;

	@Column(length = 50)
	private String email;
	
	@Column(length = 20)
	private Long mobileNumber;

	@Column(length = 20)
	private Long cardNumber;

	@Column(length = 10)
	private Long customerId;

	@Column(length = 10)
	private Long cvv;
	
	private Date expiryDate;
	
	@Column(length = 50)
	private Long availableCredit;
	
	@Column(length = 50)
	private Long totalDue;
	
	@Column(length = 50)
	private Long creditLimit;

	@Column(length = 30)
	private Long statementDay;

	private Date createdDate;

	private Date modifiedDate;

	@Column(length = 1)
	private String isActive;

	@Column(length = 1)
	private String isDeleted;


}
