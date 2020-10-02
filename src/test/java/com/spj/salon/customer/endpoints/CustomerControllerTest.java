package com.spj.salon.customer.endpoints;

import com.spj.salon.customer.model.User;
import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.customer.ports.in.IRegisterCustomer;
import com.spj.salon.openapi.resources.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private CustomerController testSubject;

    @Mock
    private ICustomerAdapter userFacade;
    @Mock
    private IRegisterCustomer registerFacade;

    final RegisterCustomerRequest registerCustomerRequest = new RegisterCustomerRequest()
            .firstName("testfirst")
            .lastName("testlast")
            .email("email@test.com");

    final RegisterCustomerResponse user = new RegisterCustomerResponse()
            .firstName("testfirst")
            .lastName("testlast")
            .email("email@test.com");

    @BeforeEach
    void setup() {
        testSubject = new CustomerController(userFacade, registerFacade);
    }

    @Test
    void shouldRegisterBarberAndReturnUserObject() {
        Map<String, String> headers = new HashMap<>();
        headers.put("clienthost", "test");
        doReturn(user)
                .when(registerFacade)
                .registerCustomer(registerCustomerRequest);

        ResponseEntity<RegisterCustomerResponse> testResult = testSubject.registerCustomer(registerCustomerRequest, Optional.of("test"));

        assertThat(testResult.getBody() != null);
        verify(registerFacade, times(1))
                .registerCustomer(registerCustomerRequest);
        verifyNoMoreInteractions(registerFacade);
    }

    @Test
    void shouldAddFavouriteSalonAndReturnBoolean() {
        doReturn(new CustomerFavouriteBarberResponse().message("updated"))
                .when(userFacade)
                .addFavouriteSalon(1L);

        ResponseEntity<CustomerFavouriteBarberResponse> testResult = testSubject.customerFavourite(1L);

        assertThat(testResult.getBody() != null);
        verify(userFacade, times(1))
                .addFavouriteSalon(1L);
        verifyNoMoreInteractions(registerFacade);
    }

    @Test
    void shouldAuthenticateAndReturnUser() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest().email("test").password("pass");

        doReturn(new AuthenticationResponse().jwtToken("jwt"))
                .when(userFacade)
                .getJwtToken(authenticationRequest, "test1");

        ResponseEntity<AuthenticationResponse> testResult = testSubject.authenticateCustomer(authenticationRequest, Optional.of("test1"));

        assertThat(testResult.getBody() != null);
        verify(userFacade, times(1))
                .getJwtToken(authenticationRequest, "test1");
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    void shouldUpdatePasswordAndReturnUserObject() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest().email("email")
                .otpNumber(1234).newPassword("newpassword");

        doReturn(new UpdatePasswordResponse().jwtToken("jwt"))
                .when(userFacade)
                .updatePassword(updatePasswordRequest, "test");

        ResponseEntity<UpdatePasswordResponse> testResult = testSubject.updatePassword(updatePasswordRequest, Optional.of("test"));

        assertThat(testResult.getBody() != null);
        verify(userFacade, times(1))
                .updatePassword(updatePasswordRequest, "test");
        verifyNoMoreInteractions(userFacade);
    }
}