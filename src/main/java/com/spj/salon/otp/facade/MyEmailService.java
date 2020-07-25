package com.spj.salon.otp.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
@Service("emailOtp")
public class MyEmailService implements IMyOtpService{

	private static final Logger logger = LoggerFactory.getLogger(MyEmailService.class.getName());

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	public OtpService otpService;
	
	@Autowired
	private UserRepository userRepository;
	
	public void sendOtpMessage() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();
		
		User user = userRepository.findByLoginId(loginId);
		sendEMail(user);
	}
	
	public void sendOtpMessage(String loginId) {
		
		User user = userRepository.findByLoginId(loginId);
		sendEMail(user);
	}
	
	private void sendEMail(User user) {
		int otp = otpService.generateOTP(user.getLoginId());

		logger.info("barber.getEmail() "+user.getEmail());
		logger.info("OTP : " + otp);

		//Generate The Template to send OTP 
		//EmailTemplate template = new EmailTemplate("SendOtp.html");

		//String message = template.getTemplate(replacements);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Welcome to find my barber - OTP");
		simpleMailMessage.setText("Your OTP is "+otp+ " .OTP will expire in 30 mins");

		logger.info("barber.getEmail() "+user.getEmail());

		// Uncomment to send mail
		javaMailSender.send(simpleMailMessage);
	}
}