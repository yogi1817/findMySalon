package com.spj.salon.user.facade;

import com.spj.salon.user.model.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public interface IUserFacade {

	public boolean registerUser(User user);

	public boolean addFavouriteSalon(Long userId, Long barberId);
}