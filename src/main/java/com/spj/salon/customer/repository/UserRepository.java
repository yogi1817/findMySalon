package com.spj.salon.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.customer.entities.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	User findByPhone(String phoneNumber);
	User findByUserId(Long userId);
}
