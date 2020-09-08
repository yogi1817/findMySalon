package com.spj.salon.user.facade;

import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IUserFacade {

	public boolean addFavouriteSalon(Long userId);
	public boolean updateVerifiedFlag(String loginId);
	public User updatePassword(User user, String clientHost);
}