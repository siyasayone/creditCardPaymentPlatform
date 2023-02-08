package com.example.creditCardPayment.entity.customer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class CustomerRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@Column(length = 50)
	private String firstName;

	@Column(length = 50)
	private String lastName;

	private Date dateOfBirth;

	@Column(length = 50)
	private String gender;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole userrole;

	@Column(length = 50)
	private String modifiedBy;

	private Date createdDate;

	private Date modifiedDate;

	@Column(length = 1)
	private String isActive;

	@Column(length = 1)
	private String isDeleted;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "customer_customerId"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public CustomerRegistration(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

}
