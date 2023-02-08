package com.example.creditCardPayment.repository.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.creditCardPayment.entity.customer.ERole;
import com.example.creditCardPayment.entity.customer.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);

}
