package com.spj.salon.user.facade;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IUserFacade {

	public boolean addFavouriteSalon(Long userId);
	public boolean updateVerifiedFlag(String loginId);
}