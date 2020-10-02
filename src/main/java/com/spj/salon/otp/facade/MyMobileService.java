package com.spj.salon.otp.facade;

import com.spj.salon.config.EnvironmentConfig;
import com.spj.salon.customer.model.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Yogesh Sharma
 */
@Service("mobileOtp")
@Slf4j
public class MyMobileService implements IMyOtpService {

    @Autowired
    private EnvironmentConfig envConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    /**
     * This method will send OTP on the mobile number of registered user
     */
    public void sendOtpMessage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        log.info("User found in jwt security {}", email);
        User user = userRepository.findByEmail(email);

        if (StringUtils.isEmpty(user.getPhone())) {
            log.error("Cannot send OTP, no phone number founf for user {}", email);
            throw new NotFoundCustomException("No phone number found for user " + user.getEmail(), "Please add valid phone number");
        }

        sendOtp(user);
    }

    private void sendOtp(User user) {
        int otp = otpService.generateOTP(user.getEmail());

        log.info("barber.getPhone() " + user.getPhone());
        log.info("OTP : " + otp);

        Twilio.init(envConfig.getTwilioOtpSid(), envConfig.getTwilioOtpAuthToken());

        Message textMessage = Message
                .creator(new PhoneNumber(user.getPhone()), // to
                        new PhoneNumber("+14126153338"), // from
                        "Welcome to find my barber, your OTP is " + otp + " .Its valid for 30 minutes.")
                .create();

        log.debug("message sent {}", textMessage.getSid());

    }

    @Override
    public void sendOtpMessage(String phoneNumber) {
        User user = userRepository.findByPhone(phoneNumber);
        log.info("User {} with phone number {}", user, phoneNumber);

        if (user == null)
            throw new NotFoundCustomException("User not found", phoneNumber);

        sendOtp(user);
    }
}
