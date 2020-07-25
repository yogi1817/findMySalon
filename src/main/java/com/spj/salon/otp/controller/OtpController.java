package com.spj.salon.otp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(OtpController.class.getName());

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
	
	@GetMapping("email")
	public ResponseEntity<String> generateOtpEmail() {

		myEmailService.sendOtpMessage();
		return new ResponseEntity<>("Otp Send", HttpStatus.OK);
	}

	@GetMapping("mobile")
	public ResponseEntity<String> generateOtpMobile() {

		myMobileService.sendOtpMessage();
		return new ResponseEntity<>("Otp Send", HttpStatus.OK);
	}
	
	@GetMapping("validateOtp")
	public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String) auth.getPrincipal();

		logger.info(" Otp Number : " + otpnum);

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
}
