package com.spj.salon.customer.adapters;

import com.spj.salon.barber.ports.out.OAuthClient;
import com.spj.salon.checkin.adapters.ICheckinFacade;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.messaging.UserRegisterPayload;
import com.spj.salon.customer.messaging.UserRegisterPublisher;
import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private final OAuthClient oAuthClient;
    private final UserRepository userRepository;
    private final ICheckinFacade iCheckinFacade;

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

        if(myEmailService.validateOtpPreLogin(updatePasswordRequest.getOtpNumber(),
                updatePasswordRequest.getEmail()).getVerified()){
            return updatePassword(persistedUser, updatePasswordRequest.getNewPassword());
        }else{
            log.info("Invalid OTP for user {}", updatePasswordRequest);
            throw new NotFoundCustomException("Invalid OTP", "");
        }
    }

    @Override
    public AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHeader) {
        return oAuthClient.getAuthenticationData(authenticationRequest.getEmail().toLowerCase(),
                        authenticationRequest.getPassword(), clientHeader);
    }

    @Override
    public AuthenticationResponse getRefreshToken(RefreshRequest refreshRequest, String clientHost) {
        return oAuthClient.getRefreshToken(refreshRequest.getRefreshToken(), refreshRequest.getEmail(), clientHost);
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

    @Override
    public CustomerProfile getCustomerProfile(Optional<Long> customerId) {
        User customer;
        String email = null;
        if(customerId.isPresent()){
            customer = userRepository.findByUserId(customerId.get());
        }else{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            email = (String) auth.getPrincipal();

            customer = userRepository.findByEmail(email);
        }

        if(customer==null){
            throw new NotFoundCustomException("User Not Found",""+customerId+email);
        }

        CheckIn checkIn = iCheckinFacade.findCheckedInBarberId(customer.getUserId());

        return new CustomerProfile()
                .email(email)
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .verified(customer.isVerified())
                .checkedInBarberId(checkIn==null? null: checkIn.getBarberMappingId())
                .favouriteSalonId(customer.getFavouriteSalonId());
    }
}
