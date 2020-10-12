package com.spj.salon.otp.adapters;

import com.spj.salon.configs.EnvironmentConfig;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.openapi.resources.OtpResponse;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Yogesh Sharma
 */
@Service("mobileOtp")
@Slf4j
@RequiredArgsConstructor
public class MyMobileAdapter implements IMyOtpAdapter {
    private final EnvironmentConfig envConfig;
    private final UserRepository userRepository;
    private final OtpCache otpCache;

    /**
     * This method will send OTP on the mobile number of registered user
     */
    public OtpResponse sendOtpMessage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        log.info("User found in jwt security {}", email);
        User user = userRepository.findByEmail(email);

        if (StringUtils.isEmpty(user.getPhone())) {
            log.error("Cannot send OTP, no phone number found for user {}", email);
            return new OtpResponse().emailOrPhone(email).message("No phone number found for user " + user.getEmail() + "Please add valid phone number");
        }

        return sendOtp(user);
    }

    private OtpResponse sendOtp(User user) {
        int otp = otpCache.generateOTP(user.getEmail());

        log.info("barber.getPhone() " + user.getPhone());
        log.info("OTP : " + otp);

        Twilio.init(envConfig.getTwilioOtpSid(), envConfig.getTwilioOtpAuthToken());

        Message textMessage = Message
                .creator(new PhoneNumber(user.getPhone()), // to
                        new PhoneNumber("+14126153338"), // from
                        "Welcome to find my barber, your OTP is " + otp + " .Its valid for 30 minutes.")
                .create();

        log.debug("message sent {}", textMessage.getSid());
        return new OtpResponse().emailOrPhone(user.getPhone()).message("message sent");
    }

    @Override
    public OtpResponse sendOtpMessage(String phoneNumber) {
        User user = userRepository.findByPhone(phoneNumber);
        log.info("User {} with phone number {}", user, phoneNumber);

        if (user == null) {
            return new OtpResponse().emailOrPhone(phoneNumber).message("User not found" + phoneNumber);
        }
        return sendOtp(user);
    }

    //Implementation in emailAdaptor
    @Override
    public OtpResponse validateOtpPostLogin(int otpNumber) {
        return null;
    }

    //Implementation in emailAdaptor
    @Override
    public OtpResponse validateOtpPreLogin(int otpNumber, String emailAddress) {
        return null;
    }
}