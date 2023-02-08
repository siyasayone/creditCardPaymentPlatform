package com.example.creditCardPayment.services.customerRegistration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.creditCardPayment.dto.customer.CustomerRegistrationDTO;
import com.example.creditCardPayment.dto.customer.LoginRequestDTO;
import com.example.creditCardPayment.dto.mail.MailDTO;
import com.example.creditCardPayment.entity.customer.CustomerRegistration;
import com.example.creditCardPayment.entity.customer.ERole;
import com.example.creditCardPayment.entity.customer.Role;
import com.example.creditCardPayment.repository.customer.CustomerRepository;
import com.example.creditCardPayment.repository.customer.RoleRepository;
import com.example.creditCardPayment.response.JwtResponse;
import com.example.creditCardPayment.response.MessageResponse;
import com.example.creditCardPayment.security.UserDetailsImpl;
import com.example.creditCardPayment.security.jwt.JwtUtils;
import com.example.creditCardPayment.util.mailAndpdf.RegisteredUsersMail;

/**
 * 
 * @author Siya
 *
 */
@Service
@Validated
//@Slf4j
public class CustomerRegistrationService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	public ResponseEntity<?> registerCustomer(@Valid CustomerRegistrationDTO customerRegistrationDTO)
			throws ParseException {

		if (Boolean.TRUE.equals(customerRepository.existsByUsername(customerRegistrationDTO.getUsername()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (Boolean.TRUE.equals(customerRepository.existsByEmail(customerRegistrationDTO.getEmail()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		if (!customerRegistrationDTO.getGender().toLowerCase().equalsIgnoreCase("female")
				&& !customerRegistrationDTO.getGender().toLowerCase().equalsIgnoreCase("other")) {
			return ResponseEntity.badRequest().body(
					new MessageResponse("Please enter any of these values as Gender Type 'Male','Female','Other'"));
		}

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		format.setLenient(false);

		String date = customerRegistrationDTO.getDateOfBirth();
		if (customerRegistrationDTO.getDateOfBirth() != null && !customerRegistrationDTO.getDateOfBirth().isEmpty()) {
			try {
				format.parse(date);

			} catch (ParseException e) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Given date is not valid according to the pattern dd/mm/yyyy"));
			}
		}
		Set<Role> roles = new HashSet<>();

		Role role = roleRepository.findByName(ERole.ROLE_CUSTOMER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(role);

		ERole userRole = null;
		for (Role r : roles) {
			userRole = r.getName();
		}
		customerRegistrationDTO.setUserrole(userRole);
		CustomerRegistration entity = customerRepository.findByUsernameIs(customerRegistrationDTO.getUsername());
		Calendar cl = Calendar.getInstance();
		if (entity == null) {
			entity = new CustomerRegistration();
			entity.setFirstName(customerRegistrationDTO.getFirstName());
			entity.setLastName(customerRegistrationDTO.getLastName());
			entity.setDateOfBirth(format.parse(date));
			entity.setGender(customerRegistrationDTO.getGender());
			entity.setEmail(customerRegistrationDTO.getEmail());
			entity.setUsername(customerRegistrationDTO.getUsername());
			entity.setUserrole(customerRegistrationDTO.getUserrole());
			entity.setPassword(encoder.encode(customerRegistrationDTO.getPassword()));
			entity.setIsActive("Y");
			entity.setIsDeleted("N");
			entity.setCreatedDate(cl.getTime());
		}
		CustomerRegistration yr = customerRepository.save(entity);
		MailDTO mailDTO = new MailDTO();
		mailDTO.setMailId(customerRegistrationDTO.getEmail());
		mailDTO.setName(customerRegistrationDTO.getFirstName() + " " + customerRegistrationDTO.getLastName());
		registeredUsers(mailDTO);
		yr.setRoles(roles);
		customerRepository.save(yr);
		return ResponseEntity.ok(new MessageResponse("Registration Completed Successfully!"));
	}

	public void registeredUsers(@RequestBody MailDTO mailDTO) {
		RegisteredUsersMail.registeredUsers(mailDTO);
	}

	public Object signIn(@Valid LoginRequestDTO loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

}