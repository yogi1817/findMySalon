package com.spj.salon.otp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spj.salon.otp.facade.IMyOtpService;
import com.spj.salon.otp.facade.OtpService;
import com.spj.salon.user.facade.IUserFacade;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Controller
@RequestMapping(value = "generateOtp", produces = MediaType.APPLICATION_JSON_VALUE)
public class OtpController {

	private static final Logger logger = LogManager.getLogger(OtpController.class.getName());

	@Autowired
	@Qualifier("emailOtp")
	private IMyOtpService myEmailService;
	
	@Autowired
	@Qualifier("mobileOtp")
	private IMyOtpService myMobileService;

	@Autowired
	private OtpService otpService;
	
	@Autowired
	private IUserFacade userFacade;
	
	private final String SUCCESS = "Entered Otp is valid";
	private final String FAIL = "Entered Otp is NOT valid. Please Retry!";
	
	/**
	 * This method will call OTP on an email of registered user
	 * @return
	 */
	@GetMapping("email")
	public ResponseEntity<String> generateOtpEmail() {
		logger.info("Inside OtpController calling generateOtpEmail");
		myEmailService.sendOtpMessage();
		return new ResponseEntity<>("Otp Send", HttpStatus.OK);
	}
	
	/**
	 * This method will call OTP on an email of user who forgot his password
	 * @return
	 */
	@GetMapping("forgotpassword/email")
	public ResponseEntity<String> generateOtpEmail(@RequestParam String email) {
		logger.info("Inside OtpController calling generateOtpEmail");
		myEmailService.sendOtpMessage(email);
		return new ResponseEntity<>("Otp Send", HttpStatus.OK);
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("mobile")
	public ResponseEntity<String> generateOtpMobile() {
		logger.info("Inside OtpController calling generateOtpMobile");
		myMobileService.sendOtpMessage();
		return new ResponseEntity<>("Otp Send", HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("forgotpassword/mobile")
	public ResponseEntity<String> generateOtpMobile(@RequestParam String phoneNumber) {
		logger.info("Inside OtpController calling generateOtpMobile");
		myMobileService.sendOtpMessage(phoneNumber);
		return new ResponseEntity<>("Otp Send", HttpStatus.OK);
	}
	
	@GetMapping("validateOtp")
	public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum) {
		logger.info("Inside OtpController calling validateOtp");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();

		logger.info("Otp Number : " + otpnum);

		//Validate the Otp 
		if (otpnum >= 0) {
			int serverOtp = otpService.getOtp(loginId);

			if (serverOtp > 0) {
				if (otpnum == serverOtp) {
					if(userFacade.updateVerifiedFlag(loginId)) {
						otpService.clearOTP(loginId);
						return SUCCESS;
					}
				}
			} 
		}
		return FAIL;
	}
	
	/**
	 * This method is moved in userController
	 * @param email
	 * @param otpnum
	 * @return
	 
	@GetMapping("forgotpassword/validateOtp")
	public @ResponseBody String validateOtp(@RequestParam String email, @RequestParam("otpnum") int otpnum) {
		logger.info("Otp Number : " + otpnum);

		//Validate the Otp 
		if (otpnum >= 0) {
			int serverOtp = otpService.getOtp(email);

			if (serverOtp > 0) {
				if (otpnum == serverOtp) {
					if(userFacade.updateVerifiedFlag(email)) {
						otpService.clearOTP(email);
						return SUCCESS;
					}
				}
			} 
		}
		return FAIL;
	}
	*/
}
