package com.spj.salon.customer.adapters;

import com.spj.salon.barber.entities.Authorities;
import com.spj.salon.barber.repository.AuthoritiesRepository;
import com.spj.salon.customer.dao.IUserDao;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.messaging.UserRegisterPublisher;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.otp.adapters.MyEmailAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class RegisterFacadeTest {

    private RegisterFacade testSubject;

    @Mock private IUserDao userDao;
    @Mock private AuthoritiesRepository authoritiesRepository;
    @Mock private UserRegisterPublisher userRegisterPublisher;
    @Mock private UserRepository userRepository;
    @Mock private MyEmailAdapter myEmailAdapter;
    private RegisterMapper registerMapper = new com.spj.salon.customer.adapters.RegisterMapperImpl();

    final User customer = User.builder()
            .authorityId(1)
            .email("customer@customer.com")
            .firstName("customer")
            .lastName("last")
            .build();

    final User barber = User.builder()
            .authorityId(2)
            .firstName("barber")
            .lastName("secret")
            .email("barber@barber.com")
            .storeName("barberthebarber")
            .build();

    @BeforeEach
    void setUp() {
        testSubject = new RegisterFacade(userDao, authoritiesRepository, userRegisterPublisher, userRepository, myEmailAdapter, registerMapper);
    }

    @Test
    void registerCustomer() {
        RegisterCustomerRequest registerCustomerRequest = new RegisterCustomerRequest()
                .email("customer@customer.com")
                .firstName("customer")
                .lastName("last")
                .password("customersecret");

        RegisterCustomerResponse expected = new RegisterCustomerResponse()
                .email("customer@customer.com")
                .firstName("customer")
                .lastName("last");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), "encryptedPassword"));

        Mockito.doReturn(Collections.EMPTY_LIST)
                .when(userDao)
                .searchUserWithEmailAndAuthority(registerCustomerRequest.getEmail(), "CUSTOMER");

        Mockito.doReturn(Authorities.builder().authorityId(1L).authority("CUSTOMER").build())
                .when(authoritiesRepository)
                .findByAuthority("CUSTOMER");

        Mockito.doReturn(customer)
                .when(userRepository)
                .saveAndFlush(customer);

        Mockito.doReturn(new OtpResponse().message("otp sent"))
                .when(myEmailAdapter)
                .sendOtpMessage(registerCustomerRequest.getEmail());

        RegisterCustomerResponse registerCustomerResponse = testSubject.registerCustomer(registerCustomerRequest);

        Mockito.verify(userDao, Mockito.times(1))
                .searchUserWithEmailAndAuthority(registerCustomerRequest.getEmail(), "CUSTOMER");

        Assertions.assertEquals(expected, registerCustomerResponse);

        Mockito.verify(authoritiesRepository, Mockito.times(1))
                .findByAuthority("CUSTOMER");

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(customer);

        Mockito.verify(myEmailAdapter, Mockito.times(1))
                .sendOtpMessage(registerCustomerRequest.getEmail());

        Mockito.verifyNoMoreInteractions(myEmailAdapter);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(authoritiesRepository);
    }

    @Test
    void registerBarberThatExistsInDB() {
        RegisterBarberRequest registerBarberRequest = new RegisterBarberRequest()
                .email("barber@barber.com")
                .firstName("barber")
                .lastName("secret")
                .password("barbersecret")
                .storeName("barberthebarber");

        Mockito.doReturn(Arrays.asList(barber))
                .when(userDao)
                .searchUserWithEmailAndAuthority(registerBarberRequest.getEmail(), "BARBER");

        Assertions.assertThrows(DuplicateKeyException.class,
                () -> testSubject.registerBarber(registerBarberRequest));
    }

    @Test
    void registerBarber() {
        RegisterBarberRequest registerBarberRequest = new RegisterBarberRequest()
                .email("barber@barber.com")
                .firstName("barber")
                .lastName("secret")
                .password("barbersecret")
                .storeName("barberthebarber");

        RegisterBarberResponse expected = new RegisterBarberResponse()
                .email("barber@barber.com")
                .firstName("barber")
                .lastName("secret");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(barber.getEmail(), "encryptedPassword"));

        Mockito.doReturn(Collections.EMPTY_LIST)
                .when(userDao)
                .searchUserWithEmailAndAuthority(registerBarberRequest.getEmail(), "BARBER");

        Mockito.doReturn(Authorities.builder().authorityId(2L).authority("BARBER").build())
                .when(authoritiesRepository)
                .findByAuthority("BARBER");

        Mockito.doReturn(barber)
                .when(userRepository)
                .saveAndFlush(barber);

        RegisterBarberResponse registerBarberResponse = testSubject.registerBarber(registerBarberRequest);

        Mockito.verify(userDao, Mockito.times(1))
                .searchUserWithEmailAndAuthority(registerBarberRequest.getEmail(), "BARBER");

        Assertions.assertEquals(expected, registerBarberResponse);

        Mockito.verify(authoritiesRepository, Mockito.times(1))
                .findByAuthority("BARBER");

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(barber);

        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(authoritiesRepository);
    }
}