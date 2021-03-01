package com.spj.salon.otp.endpoints;

import com.spj.salon.openapi.endpoint.OtpApiDelegate;
import com.spj.salon.openapi.resources.OtpResponse;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * @author Yogesh Sharma
 */
@Controller
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class OtpController implements OtpApiDelegate {

    @Qualifier("emailOtp")
    private final IMyOtpAdapter myEmailService;
    @Qualifier("mobileOtp")
    private final IMyOtpAdapter myMobileService;

    @Override
    public ResponseEntity<OtpResponse> sendOtpOnEmailOrPhoneForLoggedInUser(String channel) {
        log.info("Inside OtpController calling sendOtpOnEmailOrPhoneForLoggedInUser");
        if ("email".equals(channel)) {
            return ResponseEntity.ok(myEmailService.sendOtpMessage());
        } else if ("mobile".equals(channel)) {
            return ResponseEntity.ok(myMobileService.sendOtpMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<OtpResponse> generateOtpOnEmailOrPhonePrePassword(Optional<String> email, Optional<String> phone) {
        log.info("Inside OtpController calling generateOtpOnEmailOrPhonePrePassword");
        OtpResponse otpResponse = null;
        if (email.isPresent()) {
            otpResponse = myEmailService.sendOtpMessage(email.get().toLowerCase());
            //return ResponseEntity.ok(myEmailService.sendOtpMessage(email.get()));
        } else if (phone.isPresent()) {
            otpResponse = myMobileService.sendOtpMessage(phone.get());
            //return ResponseEntity.ok(myMobileService.sendOtpMessage(phone.get()));
        }

        if("Otp sent".equals(otpResponse.getMessage())){
            return ResponseEntity.ok(otpResponse);
        }else{
            return ResponseEntity.badRequest().body(new OtpResponse().message("Invalid email or phone number"));
        }
    }

    @Override
    public ResponseEntity<OtpResponse> validateOtpPostLogin(Integer otpNumber) {
        log.info("Inside OtpController calling validateOtp");
        return ResponseEntity.ok(myEmailService.validateOtpPostLogin(otpNumber));
    }

    @Override
    public ResponseEntity<OtpResponse> validateOtpPreLogin(Integer otpNumber, String emailAddress) {
        log.info("Inside OtpController calling validateOtp");
        return ResponseEntity.ok(myEmailService.validateOtpPreLogin(otpNumber, emailAddress.toLowerCase()));
    }
}
