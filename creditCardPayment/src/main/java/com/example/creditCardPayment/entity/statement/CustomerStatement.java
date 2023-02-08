package com.example.creditCardPayment.entity.statement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Siya
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customerstatement")
public class CustomerStatement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "customerstatementid_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "customerstatementid_seq", sequenceName = "customerstatementid_seq", allocationSize = 1)
	@Column(name = "statementId")
	private Long statementId;

	@Column(length = 50)
	private Long customerId;
	
	@Column(length = 50)
	private Long customercardNumber;

	@Column(length = 50)
	private Long totalDue;

	@Column(length = 50)
	private Long minimumAmountDue;

	private Date dueDate;

	private Date createdDate;
	
	private Date paidDate;

	@Column(length = 1)
	private String isPaid;

}
