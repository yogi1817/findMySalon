package com.spj.salon.customer.dao;

import java.util.List;

import com.spj.salon.customer.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IUserDao {
	List<User> searchUserWithEmailAndAuthority(String email, String authority);
	List<User> searchUserWithUserIdAndAuthority(Long userId, String authority);
}
