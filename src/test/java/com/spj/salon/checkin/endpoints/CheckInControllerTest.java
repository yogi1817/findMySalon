package com.spj.salon.checkin.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spj.salon.checkin.adapters.ICheckinFacade;
import com.spj.salon.openapi.endpoint.CheckInApiController;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckInControllerTest {

    @Mock
    private ICheckinFacade checkInFacade;
    private MockMvc mockMvc;
    private static final String CHECKIN_BY_USER = "/checkin/barber?barberId={barberId}";
    private static final String CHECKIN_BY_BARBER = "/checkin/customer/{customerId}";
    private static final String BARBER_WAIT_TIME = "/checkin/barber/waittime?barberId={barberId}";
    private static final String CHECKOUT_CUSTOMER_ENDPOINT = "/checkin/customer/checkout?customerId={customerId}";
    private static final String CHECKIN_BARBERS_LOCATION = "/checkin/barbers/waittime/forlocation";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TimeZone timeZone = TimeZone.getTimeZone("EST");
    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    @BeforeEach
    void setup() {
        CheckInController testSubject = new CheckInController(checkInFacade);
        mockMvc = MockMvcBuilders.standaloneSetup((new CheckInApiController(testSubject))).build();
    }

    @Test
    void shouldCheckInByCustomerAndReturnCustomerCheckInResponse() throws Exception {
        doReturn(new CustomerCheckInResponse().message("CheckedIn"))
                .when(checkInFacade)
                .checkInCustomerByCustomer(Optional.of(1L), timeZone);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CHECKIN_BY_USER, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new CustomerCheckInResponse().message("CheckedIn"))));

        verify(checkInFacade, times(1))
                .checkInCustomerByCustomer(Optional.of(1L), timeZone);
        verifyNoMoreInteractions(checkInFacade);
    }

    @Test
    void shouldCheckInByBarberAndReturnCustomerCheckInResponse() throws Exception {
        doReturn(new CustomerCheckInResponse().customerId("1234").message("checkin"))
                .when(checkInFacade)
                .checkInCustomerByBarber(1, timeZone);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CHECKIN_BY_BARBER, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new CustomerCheckInResponse().customerId("1234").message("checkin"))));

        verify(checkInFacade, times(1))
                .checkInCustomerByBarber(1, timeZone);
        verifyNoMoreInteractions(checkInFacade);
    }

    @Test
    void shouldFindWaitTimeEstimateAtBarberAndReturnString() throws Exception {
        doReturn(new BarberWaitTimeResponse().waitTime("15"))
                .when(checkInFacade)
                .waitTimeEstimate(Optional.of(1L), timeZone);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BARBER_WAIT_TIME, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new BarberWaitTimeResponse().waitTime("15"))));

        verify(checkInFacade, times(1))
                .waitTimeEstimate(Optional.of(1L), timeZone);
        verifyNoMoreInteractions(checkInFacade);
    }

    @Test
    void shouldCheckOutUserFromBarberAndReturnCustomerCheckoutResponse() throws Exception {
        doReturn(new CustomerCheckoutResponse().message("checkout"))
                .when(checkInFacade)
                .checkOut(Optional.of(1L));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CHECKOUT_CUSTOMER_ENDPOINT, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new CustomerCheckoutResponse().message("checkout"))));

        verify(checkInFacade, times(1))
                .checkOut(Optional.of(1L));
        verifyNoMoreInteractions(checkInFacade);
    }

    @Test
    void shouldFindBarbersAtZipAndReturnListOfBarbers() throws Exception {
        BarberWaitTimeRequest barberCheckInRequest = new BarberWaitTimeRequest()
                .zipCode("15220")
                .distance(25d);
        BarberDetails barberAddressDTO = new BarberDetails()
                .firstName("test")
                .lastName("testlast")
                .distance(123.22332);

        List<BarberDetails> list = new ArrayList<>();
        list.add(barberAddressDTO);
        BarbersWaitTimeResponse barberCheckInResponse = new BarbersWaitTimeResponse()
                .message("1 barber Found")
                .barberDetails(list);
        doReturn(barberCheckInResponse)
                .when(checkInFacade)
                .findBarbersAtZip(barberCheckInRequest, timeZone);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CHECKIN_BARBERS_LOCATION)
                .content(OBJECT_MAPPER.writeValueAsString(barberCheckInRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(barberCheckInResponse)));

        verify(checkInFacade, times(1))
                .findBarbersAtZip(barberCheckInRequest, timeZone);
        verifyNoMoreInteractions(checkInFacade);
    }
}
