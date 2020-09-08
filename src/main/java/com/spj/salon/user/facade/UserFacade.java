package com.spj.salon.user.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spj.salon.client.OAuthClient;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.otp.facade.OtpService;
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

	@Autowired
	private OtpService otpService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OAuthClient oAuthClient;
	
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

	@Override
	public User updatePassword(User user, String clientHost) {
		if((user.getPhone()==null && user.getEmail()==null)
				|| user.getOtpnum()==null || user.getPassword()==null) {
			logger.info("No Phone number or email address provided or Otp is null or password is null for user {}", user);
			throw new NotFoundCustomException("No Phone number or email address provided or Otp is null", "");
		}
			
		
		User persistedUser = null;
		if(user.getEmail()!=null) {
			persistedUser = userRepository.findByEmail(user.getEmail());
		}else if(user.getPhone()!=null) {
			persistedUser = userRepository.findByPhone(user.getPhone());
		}
		
		if(persistedUser==null) {
			logger.info("No user found for user {}", user);
			throw new NotFoundCustomException("No user found", "");
		}
			
		
		if(user.getOtpnum()!=otpService.getOtp(persistedUser.getEmail())){
			logger.info("Invalid OTP for user {}", user);
			throw new NotFoundCustomException("Invalid OTP", "");
		}else {
			updatePassword(persistedUser, user.getPassword(), clientHost);
		}
		
		logger.info("password updated");
		return persistedUser;
	}

	private void updatePassword(User persistedUser, String password, String clientHost) {
		persistedUser.setPassword(passwordEncoder.encode(password));
		userRepository.saveAndFlush(persistedUser);
		
		persistedUser.setJwtToken(oAuthClient.getJwtToken(persistedUser.getEmail(),password, clientHost));
		persistedUser.setPassword("");
	}
}
