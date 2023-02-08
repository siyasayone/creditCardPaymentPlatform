package com.example.creditCardPayment.services.statement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.creditCardPayment.dto.customerTransactions.CustomerTransactionDTO;
import com.example.creditCardPayment.dto.statement.CustomerStatementDTO;
import com.example.creditCardPayment.dto.statement.PayBillsDTO;
import com.example.creditCardPayment.dto.statement.StatementRemainderDTO;
import com.example.creditCardPayment.entity.customerCreditCard.CustomerCreditCard;
import com.example.creditCardPayment.entity.customerTransactions.CustomerTransaction;
import com.example.creditCardPayment.entity.statement.CustomerStatement;
import com.example.creditCardPayment.repository.customer.CustomerRepository;
import com.example.creditCardPayment.repository.customerCreditCard.CustomerCreditCardRepository;
import com.example.creditCardPayment.repository.customerTransactions.CustomerTransactionRepository;
import com.example.creditCardPayment.repository.statement.CustomerStatementRepository;
import com.example.creditCardPayment.response.MessageResponse;
import com.example.creditCardPayment.util.mailAndpdf.CreditCardStatements;
import com.example.creditCardPayment.util.mailAndpdf.PaymentAcknowledgmentMail;
import com.example.creditCardPayment.util.mailAndpdf.PaymentRemainderMail;

/**
 * 
 * @author Siya
 *
 */
@Service
//@Slf4j
public class CustomerStatementService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerStatementRepository customerStatementRepository;

	@Autowired
	private CustomerTransactionRepository customerTransactionRepository;
	
	@Autowired
	private CustomerCreditCardRepository customerCreditCardRepository;
	
	private CreditCardStatements creditCardStatements;
	
	private PaymentRemainderMail paymentRemainderMail;

	@SuppressWarnings("static-access")
	public ResponseEntity<?>  generateStatements() throws MessagingException {
		Calendar cl = Calendar.getInstance();
		int currentDay = cl.get(Calendar.DAY_OF_MONTH);
		List<CustomerCreditCard> list = customerCreditCardRepository.findByStatementDay(Long.valueOf(currentDay));
		cl.set(Calendar.DAY_OF_MONTH, 1);
		if(list.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("No statements to send"));
		}
		List<Long> userIds = new ArrayList<>();
		for (CustomerCreditCard u : list) {
			userIds.add(u.getCustomerId());
		}
		ArrayList<CustomerStatementDTO> statement = new ArrayList<>();
		List<CustomerTransactionDTO> transactions = new ArrayList<>();
		Date startDate = cl.getTime();
		Calendar ct = Calendar.getInstance();
		Date endDate = ct.getTime();
		List<CustomerTransaction> tList = customerTransactionRepository.findByCustomerTransactionsList(userIds,
				startDate, endDate);
		CustomerStatement entity = new CustomerStatement();
		for (CustomerCreditCard u : list) {
			CustomerStatementDTO dto =new CustomerStatementDTO();
			dto.setMailId(u.getEmail());
			dto.setAvailableCredit(u.getAvailableCredit());
			dto.setTotalDue(u.getTotalDue());
			dto.setCreditLimit(u.getCreditLimit());
			dto.setMinimumAmountDue(u.getTotalDue() * 1 / 4);
			dto.setCustomerId(u.getCustomerId());
			
			entity = new CustomerStatement();
			entity.setCustomerId(dto.getCustomerId());
			entity.setMinimumAmountDue(dto.getMinimumAmountDue());
			entity.setTotalDue(dto.getTotalDue());
			entity.setCustomercardNumber(u.getCardNumber());
			Calendar dl = Calendar.getInstance();
			dl.add(Calendar.DATE, 20);
			Date dueDate = dl.getTime();
			entity.setDueDate(dueDate);
			entity.setIsPaid("N");
			entity.setCreatedDate(cl.getTime());
			customerStatementRepository.save(entity);
			statement.add(dto);
			
		}
			for(CustomerStatementDTO s:statement) {
				for(CustomerTransaction t:tList) {
					CustomerTransactionDTO dto1 = new CustomerTransactionDTO();
					if(s.getCustomerId().equals(t.getCustomerId())) {
						dto1.setCustomerId(t.getCustomerId());
						dto1.setAmount(t.getAmount());
						dto1.setTransactionDate(t.getTransactionDate());
						transactions.add(dto1);
					}
			}
				s.setCustomerTransactionDTO(transactions);
				creditCardStatements.sendStatementToUser(s);
		}
			
		return new ResponseEntity<>("Statements sended successfully", HttpStatus.CREATED);
	}

	@SuppressWarnings("static-access")
	public ResponseEntity<?> sendPaymentDueRemainders() {
		List<CustomerStatement> list= customerStatementRepository.listAllStatements();
		Calendar cl = Calendar.getInstance();
		int currentDay = cl.get(Calendar.DAY_OF_MONTH);
		ArrayList<StatementRemainderDTO> statement = new ArrayList<>();
		List<Long> cardNo = new ArrayList<>();
		for(CustomerStatement s:list) {
			StatementRemainderDTO dto =new StatementRemainderDTO();
			Date paymentDate=s.getDueDate();
			Calendar date = Calendar.getInstance();
			date.setTime(paymentDate);
			int day=date.get(Calendar.DAY_OF_MONTH);
			int diff=day-currentDay;
			if(diff==2) {
				cardNo.add(s.getCustomercardNumber());
				dto.setCustomerCardNo(s.getCustomercardNumber());
				dto.setTotalDue(s.getTotalDue());
				dto.setMinimumAmountDue(s.getMinimumAmountDue());
				dto.setDueDate(cl.getTime());
				dto.setPayBy(s.getDueDate());
				statement.add(dto);
			}
			else {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("No data"));
			}
		}
		List<CustomerCreditCard> card=customerCreditCardRepository.listByCardNumber(cardNo);
		for(StatementRemainderDTO s:statement) {
			for(CustomerCreditCard c:card) {
				if(s.getCustomerCardNo().equals(c.getCardNumber())) {
					s.setMailId(c.getEmail());
				}
			}
			paymentRemainderMail.remainderMail(s);
		}
		return new ResponseEntity<>("remainders sended successfully", HttpStatus.CREATED);
	}

	public ResponseEntity<?> payStatementBills(@Valid PayBillsDTO payBillsDTO) {
		CustomerCreditCard card = customerCreditCardRepository.findByCardNumber(payBillsDTO.getCustomerCardNumber());
		if(card==null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please check the card number"));
		}
		
		CustomerStatement c = customerStatementRepository.findLastStatementByCustomercardNumber(payBillsDTO.getCustomerCardNumber());
		if (payBillsDTO.getAmount() < c.getMinimumAmountDue()) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Minimum due amount as per your last statement is" + c.getMinimumAmountDue()));
		}
		
		if(payBillsDTO.getAmount()>c.getTotalDue()) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Total due amount as per your last statement is" + c.getTotalDue()));
		}
		Long totalDue = card.getTotalDue() - payBillsDTO.getAmount();
		Long minimumAmountDue=0L;
		if(totalDue==0) {
			minimumAmountDue=0L;
		}
		else {
			minimumAmountDue=card.getTotalDue() * 1 / 4;
		}
		Long availableCredit = card.getAvailableCredit() + payBillsDTO.getAmount();
		customerCreditCardRepository.updateTotalDueAndAvailablecreditLimit(totalDue, availableCredit, payBillsDTO.getCustomerCardNumber());
		customerStatementRepository.updateStatement(minimumAmountDue,totalDue, payBillsDTO.getCustomerCardNumber());
		customerStatementRepository.updateIsPaid(payBillsDTO.getCustomerCardNumber());
		PaymentAcknowledgmentMail.transactionAcknowledgment(payBillsDTO.getCustomerCardNumber(),payBillsDTO.getAmount(),card.getEmail());

		return new ResponseEntity<>("Payment completed,please check your email", HttpStatus.CREATED);
	}

	


}