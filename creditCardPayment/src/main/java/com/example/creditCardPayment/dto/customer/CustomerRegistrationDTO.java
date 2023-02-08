package com.example.creditCardPayment.dto.customer;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.example.creditCardPayment.entity.customer.ERole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CustomerRegistrationDTO {

	private Long customerId;

	@NotBlank
	@NotEmpty
	@NotNull
	@Size(min = 3, max = 20,message = "Username cannot be empty")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@NotEmpty
	private String dateOfBirth;

	@NotBlank
	@NotEmpty
	private String gender;

	@NotNull
	@NotEmpty
	private String firstName;

	@NotNull
	@NotEmpty
	private String lastName;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	private ERole userrole;

	private Set<String> role;

}
