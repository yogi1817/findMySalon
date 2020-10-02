package com.spj.salon.controller;

import com.spj.salon.otp.facade.IMyOtpService;
import com.spj.salon.otp.facade.OtpService;
import com.spj.salon.customer.ports.in.ICustomerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Yogesh Sharma
 */
@Controller
@RequestMapping(value = "generateOtp", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@Slf4j
public class OtpController {

    @Autowired
    @Qualifier("emailOtp")
    private IMyOtpService myEmailService;

    @Autowired
    @Qualifier("mobileOtp")
    private IMyOtpService myMobileService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private ICustomerAdapter userFacade;

    /**
     * This method will call OTP on an email of registered user
     *
     * @return
     */
    @GetMapping("email")
    public ResponseEntity<String> generateOtpEmail() {
        log.info("Inside OtpController calling generateOtpEmail");
        myEmailService.sendOtpMessage();
        return new ResponseEntity<>("Otp Send", HttpStatus.OK);
    }

    /**
     * This method will call OTP on an email of user who forgot his password
     *
     * @return
     */
    @GetMapping("forgotpassword/email")
    public ResponseEntity<String> generateOtpEmail(@RequestParam String email) {
        log.info("Inside OtpController calling generateOtpEmail");
        myEmailService.sendOtpMessage(email);
        return new ResponseEntity<>("{\"Message\": \"Otp Send\"}", HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("mobile")
    public ResponseEntity<String> generateOtpMobile() {
        log.info("Inside OtpController calling generateOtpMobile");
        myMobileService.sendOtpMessage();
        return new ResponseEntity<>("Otp Send", HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("forgotpassword/mobile")
    public ResponseEntity<String> generateOtpMobile(@RequestParam String phoneNumber) {
        log.info("Inside OtpController calling generateOtpMobile");
        myMobileService.sendOtpMessage(phoneNumber);
        return new ResponseEntity<>("Otp Send", HttpStatus.OK);
    }

    @GetMapping("validateOtp")
    public ResponseEntity<String> validateOtp(@RequestParam("otpnum") int otpnum) {
        log.info("Inside OtpController calling validateOtp");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginId = (String) auth.getPrincipal();

        log.info("Otp Number : " + otpnum);

        //Validate the Otp
        if (otpnum >= 0) {
            int serverOtp = otpService.getOtp(loginId);

            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    if (userFacade.updateVerifiedFlag(loginId)) {
                        otpService.clearOTP(loginId);
                        String SUCCESS = "Entered Otp is valid";
                        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
                    }
                }
            }
        }
        String FAIL = "Entered Otp is NOT valid. Please Retry!";
        return new ResponseEntity<>(FAIL, HttpStatus.OK);
    }

    /**
     * This method is moved in userController
     * @param email
     * @param otpnum
     * @return

     @GetMapping("forgotpassword/validateOtp") public @ResponseBody String validateOtp(@RequestParam String email, @RequestParam("otpnum") int otpnum) {
     log.info("Otp Number : " + otpnum);

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
