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

	public long countByEmail(String email);
	public long countByPhone(String phone);
	public User findByEmail(String email);
	public User findByLoginId(String loginId);
}
