package com.spj.salon.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.customer.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	long countByEmail(String email);
	long countByPhone(String phone);
	User findByEmail(String email);
	User findByPhone(String phoneNumber);
}
