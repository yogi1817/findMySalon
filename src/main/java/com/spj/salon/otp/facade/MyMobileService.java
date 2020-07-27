package com.spj.salon.otp.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(MyMobileService.class.getName());
    
	public void sendOtpMessage() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		User user = userRepository.findByLoginId(loginId);

		if(StringUtils.isEmpty(user.getPhone())) {
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
        
        logger.debug(textMessage.getSid());
	}
}
