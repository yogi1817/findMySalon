package com.spj.salon.customer.adapters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spj.salon.customer.entities.User;
import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;

class RegisterMapperTest {

    private RegisterMapper testSubject;

    final User barber = User.builder()
            .firstName("barber")
            .lastName("secret")
            .email("barber@barber.com")
            .password("barbersecret")
            .storeName("barberthebarber")
            .build();

    User customer = User.builder()
            .email("customer@customer.com")
            .firstName("customer")
            .lastName("last")
            .password("customersecret")
            .build();

    @BeforeEach
    void setUp(){
        testSubject = new com.spj.salon.customer.adapters.RegisterMapperImpl();
    }

    @Test
    void shouldMapRegisterCustomerRequesttoUserEntity() {
        RegisterCustomerRequest registerCustomerRequest = new RegisterCustomerRequest()
                .email("customer@customer.com")
                .firstName("customer")
                .lastName("last")
                .password("customersecret");

        Assertions.assertEquals(testSubject.toEntity(registerCustomerRequest), customer);
    }

    @Test
    void ShouldMapUsertoRegisterCustomerResponse() {
        User user = User.builder()
                .email("customer@customer.com")
                .firstName("customer")
                .lastName("last")
                .password("customersecret")
                .build();

        RegisterCustomerResponse expected = new RegisterCustomerResponse()
                .email("customer@customer.com")
                .firstName("customer")
                .lastName("last");

        Assertions.assertEquals(testSubject.toResponse(user), expected);
    }

    @Test
    void shouldMapRegisterBarberRequestToUser() {
        RegisterBarberRequest registerBarberRequest = new RegisterBarberRequest()
                .email("barber@barber.com")
                .firstName("barber")
                .lastName("secret")
                .password("barbersecret")
                .storeName("barberthebarber");

        Assertions.assertEquals(testSubject.toEntity(registerBarberRequest), barber);
    }

    @Test
    void toBarberResponse() {
        RegisterBarberResponse expected = new RegisterBarberResponse()
                .email("barber@barber.com")
                .firstName("barber")
                .lastName("secret");

        Assertions.assertEquals(testSubject.toBarberResponse(barber), expected);
    }
}