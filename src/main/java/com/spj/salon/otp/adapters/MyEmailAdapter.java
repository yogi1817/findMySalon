package com.spj.salon.otp.adapters;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.openapi.resources.OtpResponse;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yogesh Sharma
 */
@Service("emailOtp")
@Slf4j
@RequiredArgsConstructor
public class MyEmailAdapter implements IMyOtpAdapter {
    private final JavaMailSender javaMailSender;
    public final OtpCache otpCache;
    private final UserRepository userRepository;

    /**
     * This method will send otp to email address, this will be used only by the registered member
     */
    public OtpResponse sendOtpMessage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        log.info("User found in jwt with email {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("No OTP send because user does not exists for email {}", email);
            return new OtpResponse().message("User does not Exists").emailOrPhone(email);
        }
        return sendEMail(user);
    }

    /**
     * This method will be used to send otp while registering,
     * there is no way the user does not exists as it was just registered in the previous call
     *
     * @param email
     */
    public OtpResponse sendOtpMessage(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new OtpResponse().message("User not found" + email).emailOrPhone(email);
        return sendEMail(user);
    }

    @Override
    public OtpResponse validateOtpPreLogin(int otpNumber, String emailAddress) {
        return validateOtp(otpNumber, emailAddress);
    }

    @Override
    public OtpResponse validateOtpPostLogin(int otpNumber) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginId = (String) auth.getPrincipal();

        return validateOtp(otpNumber, loginId);
    }

    /**
     *
     * @param otpNumber
     * @param emailAddress
     * @return
     */
    private OtpResponse validateOtp(int otpNumber, String emailAddress){
        log.info("Otp Number : " + otpNumber);

        int serverOtp = otpCache.getOtp(emailAddress);
        if (otpNumber >= 0 && serverOtp >= 0 && otpNumber == serverOtp) {
            User user = userRepository.findByEmail(emailAddress);
            user.setVerified(true);

            userRepository.saveAndFlush(user);
            otpCache.clearOTP(emailAddress);
            return new OtpResponse()
                    .message("Entered Otp is valid")
                    .emailOrPhone(emailAddress);
        }

        return new OtpResponse()
                .message("Entered Otp is NOT valid. Please Retry!")
                .emailOrPhone(emailAddress);
    }
    /**
     * This method send ane meail to the email id.
     *
     * @param user
     */
    private OtpResponse sendEMail(User user) {
        int otp = otpCache.generateOTP(user.getEmail());

        log.info("barber.getEmail() {}", user.getEmail());
        log.info("OTP : {}", otp);

        //Generate The Template to send OTP
        //EmailTemplate template = new EmailTemplate("SendOtp.html");

        //String message = template.getTemplate(replacements);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Welcome to find my barber - OTP");
        simpleMailMessage.setText("Your OTP is " + otp + " .OTP will expire in 30 mins");

        // Uncomment to send mail
        javaMailSender.send(simpleMailMessage);
        return new OtpResponse().emailOrPhone(user.getEmail()).message("Otp sent");
    }
}