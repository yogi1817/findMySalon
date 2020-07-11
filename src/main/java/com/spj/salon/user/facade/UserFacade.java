package com.spj.salon.user.facade;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;
import com.spj.salon.utils.DateUtils;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class UserFacade implements IUserFacade {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean registerUser(User user) {
		validate(user);
		if(userRepository.countByEmail(user.getEmail())>0) {
			return false;
		}
		userRepository.saveAndFlush(user);
		return true;
	}
	
	/**
	 * Validate user before inserting in DB
	 * @param user
	 */
	private void validate(User user) {
		user.setCreateDate(DateUtils.getTodaysDate());
		user.setModifyDate(DateUtils.getTodaysDate());
		//TODO: validate phone
		//TODO: validate email
	}

	@Override
	public boolean addFavouriteSalon(Long userId, Long barberId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			user.setFavouriteSalonId(barberId);
			//TODO: Add org.postgresql.util.PSQLException exception
			userRepository.saveAndFlush(user);
			return true;
		}
		return false;
	}
}
