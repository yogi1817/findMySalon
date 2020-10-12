package com.spj.salon.customer.adapters;

import com.spj.salon.barber.ports.out.OAuthClient;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.otp.adapters.OtpCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Yogesh Sharma
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomerAdapter implements ICustomerAdapter {

    private final OtpCache otpCache;
    private final PasswordEncoder passwordEncoder;
    private final OAuthClient oAuthClient;
    private final UserRepository userRepository;

    @Override
    public CustomerFavouriteBarberResponse addFavouriteSalon(Long barberId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        log.info("User found in jwt with email {}", email);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setFavouriteSalonId(barberId);
            //TODO: Add org.postgresql.util.PSQLException exception
            userRepository.saveAndFlush(user);
            return new CustomerFavouriteBarberResponse().name(user.getFirstName()).
                    message("Favourite barber record added");
        }

        log.error("User not found in DB with email {}", email);
        return new CustomerFavouriteBarberResponse().message("Unable to save data");
    }

    @Override
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest updatePasswordRequest, String clientHost) {
        if (updatePasswordRequest.getPhoneNumber() == null && updatePasswordRequest.getEmail() == null) {
            log.info("No Phone number or email address provided -> {}", updatePasswordRequest);
            throw new NotFoundCustomException("No Phone number or email address provided", "");
        }


        User persistedUser = null;
        if (updatePasswordRequest.getEmail() != null) {
            persistedUser = userRepository.findByEmail(updatePasswordRequest.getEmail());
        } else if (updatePasswordRequest.getPhoneNumber() != null) {
            persistedUser = userRepository.findByPhone(updatePasswordRequest.getPhoneNumber());
        }

        if (persistedUser == null) {
            log.info("No user found for updatePasswordRequest {}", updatePasswordRequest);
            throw new NotFoundCustomException("No user found", "");
        }


        if (updatePasswordRequest.getOtpNumber() != otpCache.getOtp(persistedUser.getEmail())) {
            log.info("Invalid OTP for user {}", updatePasswordRequest);
            throw new NotFoundCustomException("Invalid OTP", "");
        } else {
            return updatePassword(persistedUser, updatePasswordRequest.getNewPassword(), clientHost);
        }
    }

    @Override
    public AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHeader) {
        return new AuthenticationResponse().email(authenticationRequest.getEmail())
                .jwtToken(oAuthClient.getJwtToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword(), clientHeader));
    }

    private UpdatePasswordResponse updatePassword(User persistedUser, String password, String clientHost) {
        persistedUser.setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(persistedUser);

        return new UpdatePasswordResponse()
                .jwtToken(oAuthClient.getJwtToken(persistedUser.getEmail(), password, clientHost))
                .message("Password changed successfully");
    }
}
