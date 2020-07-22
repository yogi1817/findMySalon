package com.spj.salon.otp.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.repository.BarberRepository;
import com.spj.salon.config.ServiceConfig;
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
	private ServiceConfig serviceConfig;
	
	@Autowired
	private BarberRepository barberRepository;
	
	@Autowired
	private OtpService otpService;
	
	private static final Logger logger = LoggerFactory.getLogger(MyMobileService.class.getName());
    
	public void sendOtpMessage() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		Barber barber = barberRepository.findByLoginId(loginId);

		int otp = otpService.generateOTP(barber.getLoginId());

		logger.info("barber.getPhone() "+barber.getPhone());
		logger.info("OTP : " + otp);

		Twilio.init(serviceConfig.getTwilioSid(), serviceConfig.getTwilioAuthToken());

        Message textMessage = Message
                .creator(new PhoneNumber(barber.getPhone()), // to
                        new PhoneNumber("+14126153338"), // from
                        "Welcome to find my barber, your OTP is "+otp+" .Its valid for 30 minutes.")
                .create();
        
        logger.debug(textMessage.getSid());
	}
}
