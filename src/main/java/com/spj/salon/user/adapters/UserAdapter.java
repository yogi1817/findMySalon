package com.spj.salon.user.adapters;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.entities.Address;
import com.spj.salon.checkin.adapters.ICheckinFacade;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.checkin.ports.out.GeoCoding;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;
import com.spj.salon.user.entities.User;
import com.spj.salon.user.messaging.UserRegisterPayload;
import com.spj.salon.user.messaging.UserRegisterPublisher;
import com.spj.salon.user.ports.in.IUserAdapter;
import com.spj.salon.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Yogesh Sharma
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserAdapter implements IUserAdapter {

    private final UserRepository userRepository;
    private final ICheckinFacade iCheckinFacade;
    private final GeoCoding googleGeoCodingAdapter;
    private final UserAdapterMapper userAdapterMapper;
    @Qualifier("emailOtp")
    private final IMyOtpAdapter myEmailService;
    private final UserRegisterPublisher userRegisterPublisher;

    @Override
    public UserProfile getUserProfile(Optional<Long> barberIdOptional, Optional<Long> customerId) {
        Long userId = null;
        String email;
        User user;
        if (barberIdOptional.isPresent() && customerId.isPresent()) {
            throw new NotFoundCustomException("Invalid request", "Make sure yu pass only 1 userID");
        } else if (barberIdOptional.isPresent()) {
            userId = barberIdOptional.get();
        } else if (customerId.isPresent()) {
            userId = customerId.get();
        }

        if (userId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            email = (String) auth.getPrincipal();

            user = userRepository.findByEmail(email);
        } else {
            user = userRepository.findByUserId(userId);
        }

        if (user.getAuthorityId() == 1) {
            CheckIn checkIn = iCheckinFacade.findCheckedInBarberId(user.getUserId());
            Long barberId = checkIn == null ? null : checkIn.getBarberMappingId();
            String waitTime = "Not Available";
            if (checkIn != null) {
                waitTime = iCheckinFacade.currentWaitTimeEstimateForCustomerAtBarber(user.getUserId(), barberId).getWaitTime();
            }
            return new UserProfile()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhone())
                    .verified(user.isVerified())
                    .checkedInBarberId(checkIn == null ? null : checkIn.getBarberMappingId())
                    .userId(user.getUserId())
                    .waitTime(waitTime)
                    .favouriteSalonId(user.getFavouriteSalonId());
        }

        return new UserProfile()
                .email(user.getEmail())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .storeName(user.getStoreName())
                .address(userAdapterMapper.toResponse(user.getAddress()))
                .verified(user.isVerified())
                .userId(user.getUserId())
                .calendar(iCheckinFacade.getUserCalendar(user.getBarberCalendarSet()));
    }

    /**
     * This method add the barber address in address table. It also add longitude and latitude into the table using geolocation api
     */
    @Override
    public UserAddressResponse addBarberAddress(UserAddressRequest userAddressRequest) {
        User user = getLoggedInUserDetails();

        Address address = userAdapterMapper.toDomain(userAddressRequest);
        try {
            /*
             * GeocodingResult[] results = GeocodingApi.geocode(context,
             * address.getAddress()).await();
             */

            log.info("calling googleGeoCodingAdapter");
            GeocodingResult[] results = googleGeoCodingAdapter
                    .findGeocodingResult(URLEncoder.encode(address.getAddress(), StandardCharsets.UTF_8));

            validateAddress(address, user.getUserId(), results);
            user.setAddress(address);

            userRepository.saveAndFlush(user);

            return new UserAddressResponse().email(user.getEmail()).message("Address saved to DB");
        } catch (IOException e1) {
            return new UserAddressResponse().email(user.getEmail()).message("error calling location api");
        }
    }

    private void validateAddress(Address address, Long barberId, GeocodingResult[] results) {
        address.setUserId(barberId);
        log.debug("longitude " + results[0].geometry.location.lng);
        log.debug("latitude " + results[0].geometry.location.lat);
        address.setLongitude(results[0].geometry.location.lng);
        address.setLatitude(results[0].geometry.location.lat);
    }

    public User getLoggedInUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found in DB with email {}", email);
            throw new NotFoundCustomException("User not found", email);
        }

        return user;
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
