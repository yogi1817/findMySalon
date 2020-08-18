package com.spj.salon.otp.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.spj.salon.config.EnvironmentConfig;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service("mobileOtp")
public class MyMobileService implements IMyOtpService{
	
	@Autowired
	private EnvironmentConfig envConfig;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OtpService otpService;
	
	private static final Logger logger = LogManager.getLogger(MyMobileService.class.getName());
	
	/**
	 * This method will send OTP on the mobile number of registered user
	 */
	public void sendOtpMessage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		logger.info("User found in jwt security {}", loginId);
		User user = userRepository.findByLoginId(loginId);

		if(StringUtils.isEmpty(user.getPhone())) {
			logger.error("Cannot send OTP, no phone number founf for user {}", loginId);
			throw new NotFoundCustomException("No phone number found for user "+user.getLoginId(), "Please add valid phone number");
		}
		
		int otp = otpService.generateOTP(user.getLoginId());

		logger.info("barber.getPhone() "+user.getPhone());
		logger.info("OTP : " + otp);
		
		Twilio.init(envConfig.getTwilioOtpSid(), envConfig.getTwilioOtpAuthToken());

        Message textMessage = Message
                .creator(new PhoneNumber(user.getPhone()), // to
                        new PhoneNumber("+14126153338"), // from
                        "Welcome to find my barber, your OTP is "+otp+" .Its valid for 30 minutes.")
                .create();
        
        logger.debug("message sent {}",textMessage.getSid());
	}
}
