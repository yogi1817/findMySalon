package com.spj.salon.user.dao;

import java.util.List;

import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IUserDao {
	public List<User> searchUserWithLoginIdAuthority(String loginId, String authority);
	public List<User> searchUserWithUserIdAndAuthority(Long userId, String authority);
}
