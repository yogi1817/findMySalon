package com.spj.salon.barber.endpoints;

import com.spj.salon.barber.ports.in.IBarberAdapter;
import com.spj.salon.barber.ports.in.IRegisterBarber;
import com.spj.salon.openapi.resources.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberControllerTest {

    private BarberController testSubject;

    @Mock
    private IBarberAdapter barberAdapter;
    @Mock
    private IRegisterBarber registerBarber;

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

    @BeforeEach
    void setup() {
        testSubject = new BarberController(barberAdapter, registerBarber);
    }

    @Test
    void shouldRegisterUserAndReturnUserObject() {
        doReturn(barber)
                .when(registerBarber)
                .registerBarber(barberRequest);

        ResponseEntity<RegisterBarberResponse> testResult = testSubject.registerBarber(barberRequest);

        assertThat(testResult.getBody() != null);
        verify(registerBarber, times(1))
                .registerBarber(barberRequest);
        verifyNoMoreInteractions(registerBarber);
    }

    @Test
    void shouldAddBarbersCountToUserTodayAndReturnABooleanValue() {
        DailyBarbersRequest dailyBarbersRequest = new DailyBarbersRequest()
                .barbersCount(5)
                .barbersDescription("Test Barbers");
        doReturn(new DailyBarbersResponse().barbersCount(5).actionResult("added"))
                .when(barberAdapter)
                .addBarbersCountToday(dailyBarbersRequest);

        ResponseEntity<DailyBarbersResponse> testResult = testSubject.addBarberCountToday(dailyBarbersRequest);

        assertThat(testResult.getStatusCode() == HttpStatus.OK);
        verify(barberAdapter, times(1))
                .addBarbersCountToday(dailyBarbersRequest);
        verifyNoMoreInteractions(barberAdapter);
    }

    @Test
    void shouldAddServicesToUserAndReturnABooleanValue() {
        doReturn(new BarberServicesResponse().message("Added"))
                .when(barberAdapter)
                .addServices(1, 15, 15);

        ResponseEntity<BarberServicesResponse> testResult = testSubject.addServicesForBarber(1L, 15, 15);

        assertThat(testResult.getStatusCode() == HttpStatus.OK);
        verify(barberAdapter, times(1))
                .addServices(1, 15, 15);
        verifyNoMoreInteractions(barberAdapter);
    }

    @Test
    void shouldAddBarberCalendarAndReturnTrue() {
        BarberCalendarRequest barberCalendarRequest = new BarberCalendarRequest()
                .salonClosesAt("7:00 PM")
                .salonOpensAt("11:00 AM");
        doReturn(new BarberCalendarResponse().message("added"))
                .when(barberAdapter)
                .addBarberCalendar(barberCalendarRequest);

        ResponseEntity<BarberCalendarResponse> testResult = testSubject.addBarberCalendar(barberCalendarRequest);

        assertThat(testResult.getBody().getMessage()).isEqualTo("added");
        verify(barberAdapter, times(1))
                .addBarberCalendar(barberCalendarRequest);
        verifyNoMoreInteractions(barberAdapter);
    }

    @Test
    void shouldAddBarberAddressAndReturnTrue() throws ServiceUnavailableException {
        BarberAddressRequest barberAddressRequest = new BarberAddressRequest()
                .addressLineOne("Testaddress")
                .city("Testcity")
                .country("testcountry")
                .state("teststate")
                .zip(15220);
        Map<String, String> headers = new HashMap<>();
        headers.put("hostname", "test");
        doReturn(new BarberAddressResponse().message("added"))
                .when(barberAdapter)
                .addBarberAddress(barberAddressRequest);

        ResponseEntity<BarberAddressResponse> testResult = testSubject.addBarbersAddress(barberAddressRequest, Optional.empty());

        assertThat(testResult.getBody().getMessage()).isEqualTo("added");
        verify(barberAdapter, times(1))
                .addBarberAddress(barberAddressRequest);
        verifyNoMoreInteractions(barberAdapter);
    }
}
