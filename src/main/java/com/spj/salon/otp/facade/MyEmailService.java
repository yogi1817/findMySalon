package com.spj.salon.otp.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service("emailOtp")
public class MyEmailService implements IMyOtpService{

	private static final Logger logger = LogManager.getLogger(MyEmailService.class.getName());
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	public OtpService otpService;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * This method will send otp to email address, this will be used only by the registered member
	 */
	public void sendOtpMessage() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		logger.info("User found in jwt with loginId {}", loginId);
		User user = userRepository.findByLoginId(loginId);
		if(user==null) {
			logger.error("No OTP send because user does not exists for loginID {}", loginId);
			throw new NotFoundCustomException("User already Exists", "loginId");
		}
		sendEMail(user);
	}
	
	/**
	 * This method will be used to send otp while registering, 
	 * there is no way the user does not exists as it was just registered in the previous call
	 * @param loginId
	 */
	public void sendOtpMessage(String loginId) {
		User user = userRepository.findByLoginId(loginId);
		if(user==null)
			throw new NotFoundCustomException("User not found", loginId);
		sendEMail(user);
	}
	
	/**
	 * This method send ane meail to the email id.
	 * @param user
	 */
	private void sendEMail(User user) {
		int otp = otpService.generateOTP(user.getLoginId());

		logger.info("barber.getEmail() {}", user.getEmail());
		logger.info("OTP : {}",  otp);

		//Generate The Template to send OTP 
		//EmailTemplate template = new EmailTemplate("SendOtp.html");

		//String message = template.getTemplate(replacements);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Welcome to find my barber - OTP");
		simpleMailMessage.setText("Your OTP is "+otp+ " .OTP will expire in 30 mins");

		// Uncomment to send mail
		javaMailSender.send(simpleMailMessage);
	}
}