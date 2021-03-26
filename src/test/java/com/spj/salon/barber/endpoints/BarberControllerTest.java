package com.spj.salon.barber.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spj.salon.barber.ports.in.IBarberAdapter;
import com.spj.salon.barber.ports.in.IRegisterBarber;
import com.spj.salon.openapi.endpoint.BarberApiController;
import com.spj.salon.openapi.resources.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberControllerTest {

    @Mock
    private IBarberAdapter barberAdapter;
    @Mock
    private IRegisterBarber registerBarber;

    private MockMvc mockMvc;

    private static final String REGISTER_ENDPOINT = "/barber/register";
    private static final String BARBER_COUNT_ENDPOINT = "/barber/barbersCount";
    private static final String ADD_SERVICE_ENDPOINT = "/barber/services/{serviceId}/cost/{cost}/time/{time}";
    private static final String BARBER_CALENDAR_ENDPOINT = "/barber/calendar";
    private static final String BARBER_ADDRESS_ENDPOINT = "/barber/address";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    final RegisterBarberRequest barberRequest = new RegisterBarberRequest()
            .firstName("testfirst")
            .lastName("testlast")
            .email("email@test.com")
            .phone("1234567890")
            .password("testpassword")
            .storeName("teststore");

    final RegisterBarberResponse barber = new RegisterBarberResponse()
            .firstName("testfirst")
            .lastName("testlast")
            .email("email@test.com");

    static {
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.registerModule(new JsonNullableModule());
    }

    @BeforeEach
    void setup() {
        BarberController testSubject = new BarberController(barberAdapter, registerBarber);
        mockMvc = MockMvcBuilders.standaloneSetup((new BarberApiController(testSubject))).build();
    }

    @Test
    void shouldRegisterUserAndReturnRegisterBarberResponse() throws Exception {
        doReturn(barber)
                .when(registerBarber)
                .registerBarber(barberRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(REGISTER_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(barberRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(barber)));

        verify(registerBarber, times(1))
                .registerBarber(barberRequest);
        verifyNoMoreInteractions(registerBarber);
    }

    @Test
    void shouldAddBarbersCountToDailyBarbersReturnDailyBarbersResponse() throws Exception {
        DailyBarbersRequest dailyBarbersRequest = new DailyBarbersRequest()
                .barbersCount(5)
                .barbersDescription("Test Barbers");
        doReturn(new DailyBarbersResponse().barbersCount(5).actionResult("added"))
                .when(barberAdapter)
                .addBarbersCountToday(dailyBarbersRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BARBER_COUNT_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(dailyBarbersRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new DailyBarbersResponse().barbersCount(5).actionResult("added"))));

        verify(barberAdapter, times(1))
                .addBarbersCountToday(dailyBarbersRequest);
        verifyNoMoreInteractions(barberAdapter);
    }

    @Test
    void shouldAddServicesToBarberServicesAndReturnBarberServicesResponse() throws Exception {
        doReturn(new BarberServicesResponse().message("Added"))
                .when(barberAdapter)
                .addServices(1, 15, 15);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ADD_SERVICE_ENDPOINT, 1L, 15, 15)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new BarberServicesResponse().message("Added"))));

        verify(barberAdapter, times(1))
                .addServices(1, 15, 15);
        verifyNoMoreInteractions(barberAdapter);
    }

    @Test
    void shouldAddBarberCalendarAndReturnBarberCalendarResponse() throws Exception {
        BarberCalendarRequest barberCalendarRequest = new BarberCalendarRequest()
                .salonClosesAt("7:00 PM")
                .salonOpensAt("11:00 AM");
        doReturn(new BarberCalendarResponse().message("added"))
                .when(barberAdapter)
                .addBarberCalendar(barberCalendarRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BARBER_CALENDAR_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(barberCalendarRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new BarberCalendarResponse().message("added"))));

        verify(barberAdapter, times(1))
                .addBarberCalendar(barberCalendarRequest);
        verifyNoMoreInteractions(barberAdapter);
    }

    /*@Test
    void shouldAddBarberAddressAndReturnBarberAddressResponse() throws Exception {
        BarberAddressRequest barberAddressRequest = new BarberAddressRequest()
                .addressLineOne("Testaddress")
                .city("Testcity")
                .country("testcountry")
                .state("teststate")
                .zip(15220);

        doReturn(new BarberAddressResponse().message("added"))
                .when(barberAdapter)
                .addBarberAddress(barberAddressRequest);


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BARBER_ADDRESS_ENDPOINT)
                .content(OBJECT_MAPPER.writeValueAsString(barberAddressRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(OBJECT_MAPPER.writeValueAsString(new BarberAddressResponse().message("added"))));

        verify(barberAdapter, times(1))
                .addBarberAddress(barberAddressRequest);
        verifyNoMoreInteractions(barberAdapter);
    }*/
}
