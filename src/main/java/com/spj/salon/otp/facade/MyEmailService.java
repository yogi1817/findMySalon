package com.spj.salon.otp.facade;

import com.spj.salon.customer.model.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Yogesh Sharma
 */
@Service("emailOtp")
@Slf4j
public class MyEmailService implements IMyOtpService {

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
        String email = (String) auth.getPrincipal();

        log.info("User found in jwt with email {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("No OTP send because user does not exists for email {}", email);
            throw new NotFoundCustomException("User already Exists", "email");
        }
        sendEMail(user);
    }

    /**
     * This method will be used to send otp while registering,
     * there is no way the user does not exists as it was just registered in the previous call
     *
     * @param email
     */
    public void sendOtpMessage(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new NotFoundCustomException("User not found", email);
        sendEMail(user);
    }

    /**
     * This method send ane meail to the email id.
     *
     * @param user
     */
    private void sendEMail(User user) {
        int otp = otpService.generateOTP(user.getEmail());

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
    }
}