package com.spj.salon.otp.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spj.salon.openapi.endpoint.OtpApiController;
import com.spj.salon.openapi.resources.OtpResponse;
import com.spj.salon.otp.ports.in.IMyOtpAdapter;
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
class OtpControllerTest {

    @Mock
    private IMyOtpAdapter myEmailService;
    @Mock
    private IMyOtpAdapter myMobileService;

    private MockMvc mockMvc;
    private static final String OTP_POST_LOGIN_ENDPOINT = "/otp/postlogin?channel={channelId}";
    private static final String OTP_PRE_LOGIN_ENDPOINT = "/otp/forgotpassword?email={email}";
    private static final String OTP_PRE_PHONE_LOGIN_ENDPOINT = "/otp/forgotpassword?phone={phone}";
    private static final String VALIDATE_OTP_ENDPOINT = "/otp/validate?otpNumber={otpNumber}";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    @BeforeEach
    void setup() {
        OtpController testSubject = new OtpController(myEmailService, myMobileService);
        mockMvc = MockMvcBuilders.standaloneSetup((new OtpApiController(testSubject))).build();
    }

    @Test
    void sendOtpOnEmailForLoggedInUser() throws Exception {

        doReturn(new OtpResponse().message("otp send"))
                .when(myEmailService)
                .sendOtpMessage();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(OTP_POST_LOGIN_ENDPOINT, "email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new OtpResponse().message("otp send"))));

        verify(myEmailService, times(1))
                .sendOtpMessage();
        verifyNoMoreInteractions(myEmailService);
    }

    @Test
    void generateOtpOnEmailPrePassword() throws Exception {
        String email = "test@test.com";
        doReturn(new OtpResponse().message("otp send"))
                .when(myEmailService)
                .sendOtpMessage(email);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(OTP_PRE_LOGIN_ENDPOINT, email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new OtpResponse().message("otp send"))));

        verify(myEmailService, times(1))
                .sendOtpMessage(email);
        verifyNoMoreInteractions(myEmailService);
    }

    @Test
    void validateOtp() throws Exception {
        doReturn(new OtpResponse().message("otp send"))
                .when(myEmailService)
                .validateOtp(12345);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(VALIDATE_OTP_ENDPOINT, 12345)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new OtpResponse().message("otp send"))));

        verify(myEmailService, times(1))
                .validateOtp(12345);
        verifyNoMoreInteractions(myEmailService);
    }

    @Test
    void sendOtpOnPhoneForLoggedInUser() throws Exception {
        OtpResponse response = new OtpResponse().message("otp send");
        doReturn(new OtpResponse().message("otp send"))
                .when(myMobileService)
                .sendOtpMessage();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(OTP_POST_LOGIN_ENDPOINT,"mobile")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new OtpResponse().message("otp send"))));

        verify(myMobileService, times(1))
                .sendOtpMessage();
        verifyNoMoreInteractions(myEmailService);
    }

    @Test
    void generateOtpOnMobileServicePrePassword() throws Exception {
        OtpResponse response = new OtpResponse().message("otp send");
        String phone = "412-482-4004";
        doReturn(new OtpResponse().message("otp send"))
                .when(myMobileService)
                .sendOtpMessage(phone);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(OTP_PRE_PHONE_LOGIN_ENDPOINT, phone)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(response)));

        verify(myMobileService, times(1))
                .sendOtpMessage(phone);
        verifyNoMoreInteractions(myEmailService);
    }
}