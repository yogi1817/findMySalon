package com.spj.salon.customer.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.customer.ports.in.IRegisterCustomer;
import com.spj.salon.openapi.endpoint.CustomerApiController;
import com.spj.salon.openapi.resources.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private ICustomerAdapter userFacade;
    @Mock
    private IRegisterCustomer registerFacade;
    private MockMvc mockMvc;
    private static final String REGISTER_ENDPOINT = "/customer/register";
    private static final String FAVOURITE_ENDPOINT = "/customer/favourite?barberId={barberId}";
    private static final String AUTHENTICATE_ENDPOINT = "/customer/authenticate";
    private static final String UPDATE_PASSWORD_ENDPOINT = "/customer/updatepassword";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    final RegisterCustomerRequest registerCustomerRequest = new RegisterCustomerRequest()
            .firstName("testfirst")
            .lastName("testlast")
            .email("email@test.com")
            .phone("1234567891")
            .password("testpass");

    final RegisterCustomerResponse registerCustomerResponse = new RegisterCustomerResponse()
            .firstName("testfirst")
            .lastName("testlast")
            .email("email@test.com");

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    @BeforeEach
    void setup() {
        CustomerController testSubject = new CustomerController(userFacade, registerFacade);
        mockMvc = MockMvcBuilders.standaloneSetup((new CustomerApiController(testSubject))).build();
    }

    @Test
    void shouldRegisterBarberAndReturnRegisterCustomerResponse() throws Exception {
        RegisterCustomerResponse expected = new RegisterCustomerResponse()
                .firstName("testfirst")
                .lastName("testlast")
                .email("email@test.com")
                .message("Member registered successfully");

        doReturn(registerCustomerResponse)
                .when(registerFacade)
                .registerCustomer(registerCustomerRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(REGISTER_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(registerCustomerRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(expected)));

        verify(registerFacade, times(1))
                .registerCustomer(registerCustomerRequest);
        verifyNoMoreInteractions(registerFacade);
    }

    @Test
    void shouldAddFavouriteSalonAndReturnCustomerFavouriteBarberResponse() throws Exception {
        doReturn(new CustomerFavouriteBarberResponse().message("updated"))
                .when(userFacade)
                .addFavouriteSalon(1L);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(FAVOURITE_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new CustomerFavouriteBarberResponse().message("updated"))));

        verify(userFacade, times(1))
                .addFavouriteSalon(1L);
        verifyNoMoreInteractions(registerFacade);
    }

    @Test
    void shouldAuthenticateAndReturnAuthenticationRequest() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest().email("test@test.com").password("pass");

        doReturn(new AuthenticationResponse().jwtToken("jwt").email("test@test.com"))
                .when(userFacade)
                .getJwtToken(authenticationRequest, null);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(AUTHENTICATE_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new AuthenticationResponse().jwtToken("jwt").email("test@test.com"))));

        verify(userFacade, times(1))
                .getJwtToken(authenticationRequest, null);
        verifyNoMoreInteractions(userFacade);
    }

    @Test
    void shouldUpdatePasswordAndReturnUpdatePasswordRequest() throws Exception{
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest().email("email@test.com")
                .otpNumber(1234).newPassword("newpassword");

        doReturn(new UpdatePasswordResponse().jwtToken("jwt"))
                .when(userFacade)
                .updatePassword(updatePasswordRequest, null);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(UPDATE_PASSWORD_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(updatePasswordRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new UpdatePasswordResponse().jwtToken("jwt"))));

        verify(userFacade, times(1))
                .updatePassword(updatePasswordRequest, null);
        verifyNoMoreInteractions(userFacade);
    }
}