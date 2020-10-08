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
        if (email.isPresent()) {
            return ResponseEntity.ok(myEmailService.sendOtpMessage(email.get()));
        } else if (phone.isPresent()) {
            return ResponseEntity.ok(myMobileService.sendOtpMessage(phone.get()));
        }
        return ResponseEntity.badRequest().body(new OtpResponse().message("Phone number or email is required"));
    }

    @Override
    public ResponseEntity<OtpResponse> validateOtp(Integer otpNumber) {
        log.info("Inside OtpController calling validateOtp");
        return ResponseEntity.ok(myEmailService.validateOtp(otpNumber));
    }
}
