package com.spj.salon.user.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class UserFacade implements IUserFacade {

	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LogManager.getLogger(UserFacade.class.getName());
	
	@Override
	public boolean addFavouriteSalon(Long barberId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		logger.info("User found in jwt with loginId {}", loginId);
		User user = userRepository.findByLoginId(loginId);
		if (user!=null) {
			user.setFavouriteSalonId(barberId);
			//TODO: Add org.postgresql.util.PSQLException exception
			userRepository.saveAndFlush(user);
			return true;
		}
		
		logger.error("User not found in DB with loginId {}", loginId);
		return false;
	}

	@Override
	public boolean updateVerifiedFlag(String loginId) {
		User user = userRepository.findByLoginId(loginId);
		user.setVerified(true);
		
		userRepository.saveAndFlush(user);
		return true;
	}
}
