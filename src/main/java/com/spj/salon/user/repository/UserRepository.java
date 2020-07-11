package com.spj.salon.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	long countByEmail(String email);
	long countByPhone(String phone);
}
