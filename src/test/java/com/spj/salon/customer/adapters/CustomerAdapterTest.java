package com.spj.salon.customer.adapters;

import com.spj.salon.barber.ports.out.OAuthClient;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.messaging.UserRegisterPublisher;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.otp.adapters.OtpCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CustomerAdapterTest {

    @Mock
    private CustomerAdapter testSubject;
    @Mock
    private OtpCache otpCache;
    @Mock
    private UserRegisterPublisher userRegisterPublisher;
    @Mock
    private OAuthClient oAuthClient;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getEmail(), "encryptedPassword"));

        testSubject = new CustomerAdapter(otpCache, userRegisterPublisher, oAuthClient, userRepository);
    }

    final User user = User.builder()
            .authorityId(1)
            .email("barber@barber.com")
            .build();

    @Test
    void addFavouriteSalon() {
        User savedUser = User.builder()
                .authorityId(1)
                .email("barber@barber.com")
                .favouriteSalonId(1L)
                .build();

        Mockito.doReturn(user)
                .when(userRepository)
                .findByEmail(user.getEmail());

        Mockito.doReturn(savedUser)
                .when(userRepository)
                .saveAndFlush(savedUser);

        testSubject.addFavouriteSalon(1L);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(user);

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updatePasswordNoEmailNoPhone() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest().email("customer@customer.com").newPassword("12345");

        Assertions.assertThrows(NotFoundCustomException.class,
                () -> testSubject.updatePassword(updatePasswordRequest));
    }

    @Test
    void updatePasswordWrongOTP() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest().email("customer@customer.com")
                .newPassword("12345").otpNumber(12345);
        Mockito.doReturn(user)
                .when(userRepository)
                .findByEmail(updatePasswordRequest.getEmail());

        Mockito.doReturn(1234)
                .when(otpCache)
                .getOtp(user.getEmail());

        Assertions.assertThrows(NotFoundCustomException.class,
                () -> testSubject.updatePassword(updatePasswordRequest));
    }

    @Test
    void updatePasswordEmail() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest().email("customer@customer.com")
                .newPassword("12345").otpNumber(12345);
        User savedUser = User.builder()
                .authorityId(1)
                .email("barber@barber.com")
                .build();

        Mockito.doReturn(user)
                .when(userRepository)
                .findByEmail(updatePasswordRequest.getEmail());

        Mockito.doReturn(12345)
                .when(otpCache)
                .getOtp(savedUser.getEmail());

        UpdatePasswordResponse expected = new UpdatePasswordResponse().message("Password changed successfully");

        UpdatePasswordResponse updatePasswordResponse = testSubject.updatePassword(updatePasswordRequest);
        Assertions.assertEquals(updatePasswordResponse, expected);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(updatePasswordRequest.getEmail());

        Mockito.verify(otpCache, Mockito.times(1))
                .getOtp(savedUser.getEmail());

        Mockito.verifyNoMoreInteractions(oAuthClient);
        //Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(otpCache);
    }

    @Test
    void updatePasswordPhone() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest().phoneNumber("1234567890")
                .newPassword("12345").otpNumber(12345);
        User savedUser = User.builder()
                .authorityId(1)
                .email("barber@barber.com")
                .build();

        Mockito.doReturn(user)
                .when(userRepository)
                .findByPhone(updatePasswordRequest.getPhoneNumber());

        Mockito.doReturn(12345)
                .when(otpCache)
                .getOtp(savedUser.getEmail());

        UpdatePasswordResponse expected = new UpdatePasswordResponse().message("Password changed successfully");

        UpdatePasswordResponse updatePasswordResponse = testSubject.updatePassword(updatePasswordRequest);
        Assertions.assertEquals(updatePasswordResponse, expected);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByPhone(updatePasswordRequest.getPhoneNumber());

        Mockito.verify(otpCache, Mockito.times(1))
                .getOtp(savedUser.getEmail());

        Mockito.verifyNoMoreInteractions(oAuthClient);
        //Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(otpCache);
    }

    @Test
    void getJwtToken() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest()
                .password("newCustomer")
                .email("customer@customer.com");

        AuthenticationResponse expected = new AuthenticationResponse()
                .email("customer@customer.com")
                .jwtToken("encryptedPassword");

        Mockito.doReturn("encryptedPassword")
                .when(oAuthClient)
                .getJwtToken(authenticationRequest.getEmail(), authenticationRequest.getPassword(), null);

        AuthenticationResponse authenticationResponse = testSubject.getJwtToken(authenticationRequest, null);

        Assertions.assertEquals(authenticationResponse, expected);

        Mockito.verify(oAuthClient, Mockito.times(1))
                .getJwtToken(authenticationRequest.getEmail(), authenticationRequest.getPassword(), null);
        Mockito.verifyNoMoreInteractions(oAuthClient);
    }
}