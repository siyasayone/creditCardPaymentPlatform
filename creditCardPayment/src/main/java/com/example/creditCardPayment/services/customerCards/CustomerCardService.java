package com.example.creditCardPayment.services.customerCards;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.creditCardPayment.dto.customerCreditCard.CardDTO;
import com.example.creditCardPayment.dto.customerCreditCard.CustomerCardDTO;
import com.example.creditCardPayment.dto.statement.StatementRemainderDTO;
import com.example.creditCardPayment.entity.customer.CustomerRegistration;
import com.example.creditCardPayment.entity.customerCreditCard.CustomerCreditCard;
import com.example.creditCardPayment.entity.statement.CustomerStatement;
import com.example.creditCardPayment.exception.CardException;
import com.example.creditCardPayment.exception.DateException;
import com.example.creditCardPayment.exception.InvalidCustomerDetailsException;
import com.example.creditCardPayment.repository.customer.CustomerRepository;
import com.example.creditCardPayment.repository.customerCreditCard.CustomerCreditCardRepository;
import com.example.creditCardPayment.repository.statement.CustomerStatementRepository;
import com.example.creditCardPayment.security.UserDetailsImpl;

/**
 * 
 * @author Siya
 *
 */
@Service
//@Slf4j
public class CustomerCardService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerCreditCardRepository customerCreditCardRepository;

	@Autowired
	private CustomerStatementRepository customerStatementRepository;

	public static int statementDate() {
		Random r = new Random();
		return r.nextInt(15, 30);
	}

	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public CustomerCreditCard addCustomerCard(@Valid CardDTO customerCardDTO, UserDetailsImpl loggeduser)
			throws ParseException, DateException {
		CustomerRegistration user = customerRepository.findByUsernameIs(loggeduser.getUsername());
		Calendar cl = Calendar.getInstance();
		format.setLenient(false);
		for (CustomerCardDTO c : customerCardDTO.getCustomerCardDetailsDTO()) {
			try {
				format.parse(c.getExpiryDate());
			} catch (ParseException e) {
				System.out.println(
						"Date " + c.getExpiryDate() + " is not valid according to " + format.toPattern() + " pattern.");
				throw new DateException("Given date is not valid according to the pattern dd/mm/yyyy");

			}
		}
		CustomerCreditCard yr = new CustomerCreditCard();
		for (CustomerCardDTO c : customerCardDTO.getCustomerCardDetailsDTO()) {
			CustomerCreditCard card = customerCreditCardRepository.findByCardNumber(c.getCardNumber());
			if (card == null) {
				card = new CustomerCreditCard();
				card.setCardNumber(c.getCardNumber());
				card.setCustomerName(c.getCustomerName());
				card.setCvv(c.getCvv());
				card.setEmail(user.getEmail());
				card.setMobileNumber(c.getMoblileNumber());
				card.setExpiryDate(format.parse(c.getExpiryDate()));
				card.setCustomerId(user.getCustomerId());
				card.setCreditLimit(c.getTotalCreditLimit());
				card.setAvailableCredit(c.getAvailableCredit());
				card.setTotalDue(0L);
				int day = statementDate();
				card.setStatementDay(Long.valueOf(day));
				card.setIsActive("Y");
				card.setIsDeleted("N");
				card.setCreatedDate(cl.getTime());
			}
			yr = customerCreditCardRepository.save(card);
		}
		return yr;
	}

	public CustomerCreditCard updateCard(CustomerCardDTO customerCardDTO, UserDetailsImpl loggeduser)
			throws ParseException {

		if (customerCardDTO.getCardNumber() == null) {
			throw new CardException("");
		}
		Calendar cl = Calendar.getInstance();
		CustomerCreditCard card = customerCreditCardRepository
				.findByCardNumber(customerCardDTO.getCardNumber());
		if (card != null) {
			card.setCardNumber(customerCardDTO.getCardNumber());
			if (customerCardDTO.getCustomerName() != null) {
				card.setCustomerName(customerCardDTO.getCustomerName());
			}

			if (customerCardDTO.getMoblileNumber() != null) {
				card.setMobileNumber(customerCardDTO.getMoblileNumber());
			}

			card.setModifiedDate(cl.getTime());
			customerCreditCardRepository.save(card);
		} else {
			throw new InvalidCustomerDetailsException("details not matching");
		}

		return card;
	}

	public ResponseEntity<Object> removeCard(Long cardNumber, UserDetailsImpl loggeduser) {
		Calendar cl = Calendar.getInstance();
		CustomerRegistration user = customerRepository.findByUsernameIs(loggeduser.getUsername());
		CustomerCreditCard yr = customerCreditCardRepository.findByCardNumberAndCustomerId(cardNumber,
				user.getCustomerId());
		if (yr != null) {
			yr.setIsActive("N");
			yr.setIsDeleted("Y");
			yr.setModifiedDate(cl.getTime());
			customerCreditCardRepository.save(yr);

		} else {
			return new ResponseEntity<>("Please check the card number", HttpStatus.OK);
		}
		return new ResponseEntity<>("Card removed Successfully", HttpStatus.OK);
	}

	public CardDTO showCardDetails(UserDetailsImpl loggeduser) {
		CustomerRegistration user = customerRepository.findByUsernameIs(loggeduser.getUsername());
		List<CustomerCreditCard> card = customerCreditCardRepository.findByCustomerId(user.getCustomerId());
		List<CustomerStatement> statement = customerStatementRepository
				.findLastStatementByCustomerId(user.getCustomerId());
		CardDTO dto = new CardDTO();
		dto.setCustomerName(user.getFirstName() + " " + user.getLastName());
		dto.setEmail(user.getEmail());
		ArrayList<CustomerCardDTO> cardDetails = new ArrayList<>();
		for (CustomerCreditCard c : card) {
			CustomerCardDTO cdto = new CustomerCardDTO();
			cdto.setCardNumber(c.getCardNumber());
			cdto.setCvv(c.getCvv());
			cdto.setCustomerName(c.getCustomerName());
			cdto.setExpiryDate(format.format(c.getExpiryDate()));
			cdto.setMoblileNumber(c.getMobileNumber());
			cdto.setTotalCreditLimit(c.getCreditLimit());
			cdto.setAvailableCredit(c.getAvailableCredit());
			cardDetails.add(cdto);
		}
		ArrayList<StatementRemainderDTO> statements = new ArrayList<>();
		for (CustomerStatement c : statement) {
			StatementRemainderDTO st = new StatementRemainderDTO();
			st.setCustomerCardNo(c.getCustomercardNumber());
			st.setTotalDue(c.getTotalDue());
			st.setMinimumAmountDue(c.getMinimumAmountDue());
			st.setDueDate(c.getDueDate());
			statements.add(st);
		}
		dto.setCustomerCardDetailsDTO(cardDetails);
		dto.setStatementRemainderDTO(statements);
		return dto;
	}

	public List<CustomerStatement> getStatementHistory(Long cardNumber) {
		List<CustomerStatement> cd = customerStatementRepository.listPayments(cardNumber);
		return cd.stream().collect(Collectors.toList());
	}

	public CustomerStatement viewGeneratedBills(Long cardNumber) {
		return customerStatementRepository.viewLastGeneratedbillByCardNumber(cardNumber);
	}

}
