package com.spj.salon.customer.adapters;

import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.messaging.UserRegisterPayload;
import com.spj.salon.customer.messaging.UserRegisterPublisher;
import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.CustomerFavouriteBarberResponse;
import com.spj.salon.openapi.resources.UpdatePasswordRequest;
import com.spj.salon.openapi.resources.UpdatePasswordResponse;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Yogesh Sharma
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomerAdapter implements ICustomerAdapter {

    @Qualifier("emailOtp")
    private final IMyOtpAdapter myEmailService;
    private final UserRegisterPublisher userRegisterPublisher;
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
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
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

        if (myEmailService.validateOtpPreLogin(updatePasswordRequest.getOtpNumber(),
                updatePasswordRequest.getEmail()).getVerified()) {
            return updatePassword(persistedUser, updatePasswordRequest.getNewPassword());
        } else {
            log.info("Invalid OTP for user {}", updatePasswordRequest);
            throw new NotFoundCustomException("Invalid OTP", "");
        }
    }

    private UpdatePasswordResponse updatePassword(User persistedUser, String password) {
        userRegisterPublisher.sendUserRegisterDetails(UserRegisterPayload.builder()
                .email(persistedUser.getEmail().toLowerCase())
                .password(password)
                .updatePasswordRequest(true)
                .authorityId(persistedUser.getAuthorityId())
                .build());

        return new UpdatePasswordResponse()
                .message("Password changed successfully");
    }
}
