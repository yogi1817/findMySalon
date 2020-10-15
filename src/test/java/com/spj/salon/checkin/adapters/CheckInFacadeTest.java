package com.spj.salon.checkin.adapters;

import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.barber.repository.AddressRepository;
import com.spj.salon.barber.repository.ZipCodeRepository;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.checkin.ports.out.GeoCoding;
import com.spj.salon.checkin.repository.CheckInRepository;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.openapi.resources.BarberWaitTimeResponse;
import com.spj.salon.utils.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckInFacadeTest {

    private CheckInFacade testSubject;

    @Mock private UserRepository userRepository;
    @Mock private CheckInRepository checkInRepository;
    @Mock private ZipCodeRepository zipCodeRepo;
    @Mock private AddressRepository addressRepo;
    @Mock private GeoCoding googleGeoCodingClient;
    private final String DATE_FORMAT = "hh:mm aa";

    final User customer = User.builder()
            .authorityId(1)
            .userId(2L)
            .email("customer@customer.com")
            .password("customersecret")
            .build();

    @BeforeEach
    void setUp(){
        testSubject = new CheckInFacade(userRepository, checkInRepository, zipCodeRepo, addressRepo, googleGeoCodingClient);
    }

    @Test
    void waitTimeEstimateWhenNoBarberCalenderSet() {
        final User barber = User.builder()
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .build();

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        BarberWaitTimeResponse barberWaitTimeResponse = testSubject.waitTimeEstimate(1L);

        Assertions.assertEquals(barberWaitTimeResponse.getWaitTime(), "Barber Calender not set for today");
    }

    @Test
    void waitTimeEstimateWhenNoBarberDailyCountSet() {
        Set<BarberCalendar> barberCalendarSet = new HashSet<>(Arrays.asList(
                BarberCalendar.builder().calendarDay("Monday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Tuesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Wednesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Thursday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Friday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Saturday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Sunday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build()
        ));

        final User barber = User.builder()
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .barberCalendarSet(barberCalendarSet)
                .build();

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        BarberWaitTimeResponse barberWaitTimeResponse = testSubject.waitTimeEstimate(1L);

        Assertions.assertEquals(barberWaitTimeResponse.getWaitTime(), "No Barbers available at this time");
    }

    @Test
    void waitTimeEstimateWhenNoOneHasCheckedIn() {
        Set<BarberCalendar> barberCalendarSet = new HashSet<>(Arrays.asList(
                BarberCalendar.builder().calendarDay("Monday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Tuesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Wednesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Thursday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Friday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Saturday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Sunday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build()
        ));

        List<DailyBarbers> dailyBarbersSet = new ArrayList<>(Arrays.asList(
                DailyBarbers.builder()
                        .barbersCount(5)
                        .barbersDescription("5 barbers")
                        .build()
        ));

        final User barber = User.builder()
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .barberCalendarSet(barberCalendarSet)
                .dailyBarberSet(dailyBarbersSet)
                .build();

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        BarberWaitTimeResponse barberWaitTimeResponse = testSubject.waitTimeEstimate(1L);

        Assertions.assertEquals(barberWaitTimeResponse.getWaitTime(), "0");
    }

    @Test
    void waitTimeEstimateWhen3RdPersonCheckIn() {
        Set<BarberCalendar> barberCalendarSet = new HashSet<>(Arrays.asList(
                BarberCalendar.builder().calendarDay("Monday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Tuesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Wednesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Thursday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Friday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Saturday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Sunday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build()
        ));

        List<DailyBarbers> dailyBarbersSet = new ArrayList<>(Arrays.asList(
                DailyBarbers.builder()
                        .barbersCount(2)
                        .barbersDescription("2 barbers")
                        .build()
        ));

        Set<CheckIn> checkInSet = new HashSet<>(Arrays.asList(
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(2L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(3L).build(),
                CheckIn.builder().checkedOut(true).barberMappingId(1L).userMappingId(4L).build(),
                CheckIn.builder().checkedOut(true).barberMappingId(1L).userMappingId(5L).build()
        ));

        final User barber = User.builder()
                .userId(1L)
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .barberCalendarSet(barberCalendarSet)
                .dailyBarberSet(dailyBarbersSet)
                .checkInSet(checkInSet)
                .build();

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        BarberWaitTimeResponse barberWaitTimeResponse = testSubject.waitTimeEstimate(1L);

        Assertions.assertEquals(barberWaitTimeResponse.getWaitTime(), "15");
    }

    @Test
    void waitTimeEstimateWhen5thPersonCheckIn() {
        Set<BarberCalendar> barberCalendarSet = new HashSet<>(Arrays.asList(
                BarberCalendar.builder().calendarDay("Monday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Tuesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Wednesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Thursday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Friday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Saturday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Sunday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build()
        ));

        List<DailyBarbers> dailyBarbersSet = new ArrayList<>(Arrays.asList(
                DailyBarbers.builder()
                        .barbersCount(2)
                        .barbersDescription("2 barbers")
                        .build()
        ));

        Set<CheckIn> checkInSet = new HashSet<>(Arrays.asList(
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(2L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(3L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(4L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(5L).build()
        ));

        final User barber = User.builder()
                .userId(1L)
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .barberCalendarSet(barberCalendarSet)
                .dailyBarberSet(dailyBarbersSet)
                .checkInSet(checkInSet)
                .build();

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        BarberWaitTimeResponse barberWaitTimeResponse = testSubject.waitTimeEstimate(1L);

        Assertions.assertEquals(barberWaitTimeResponse.getWaitTime(), "30");
    }

    @Test
    void checkInCustomerByCustomerUnableToCheckInAsNoBarbersAvailable() {
        final User barber = User.builder()
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .userId(1L)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), customer.getPassword()));

        Mockito.doReturn(customer)
                .when(userRepository)
                .findByEmail(customer.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        Assertions.assertEquals("Unable to Checkin",testSubject.checkInCustomerByCustomer(1L).getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(customer.getEmail());

        Mockito.verifyNoMoreInteractions(userRepository);
    }

   /* @Test
    void checkInCustomerByCustomer() {
        Set<BarberCalendar> barberCalendarSet = new HashSet<>(Arrays.asList(
                BarberCalendar.builder().calendarDay("Monday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Tuesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Wednesday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Thursday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Friday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Saturday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build(),
                BarberCalendar.builder().calendarDay("Sunday")
                        .salonOpenTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .salonCloseTime(DateUtils.getFormattedDate("10:00 AM", DATE_FORMAT))
                        .build()
        ));

        List<DailyBarbers> dailyBarbersSet = new ArrayList<>(Arrays.asList(
                DailyBarbers.builder()
                        .barbersCount(2)
                        .barbersDescription("2 barbers")
                        .build()
        ));

        Set<CheckIn> checkInSet = new HashSet<>(Arrays.asList(
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(2L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(3L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(4L).build(),
                CheckIn.builder().checkedOut(false).barberMappingId(1L).userMappingId(5L).build()
        ));

        final User barber = User.builder()
                .authorityId(2)
                .email("barber@barber.com")
                .password("barbersecret")
                .dailyBarberSet(dailyBarbersSet)
                .barberCalendarSet(barberCalendarSet)
                .checkInSet(checkInSet)
                .userId(1L)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), customer.getPassword()));

        Mockito.doReturn(customer)
                .when(userRepository)
                .findByEmail(customer.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        Mockito.doReturn(0)
                .when(checkInRepository)
                .countByUserMappingId(1L);

        CheckIn checkIn = new CheckIn(1L, 2L, 30);

        Mockito.doReturn(checkIn)
                .when(checkInRepository)
                .saveAndFlush(checkIn);

        Assertions.assertEquals("Unable to Checkin",testSubject.checkInCustomerByCustomer(1L).getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(customer.getEmail());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .countByUserMappingId(1L);

        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoMoreInteractions(checkInRepository);
    }*/

    @Test
    void checkInCustomerByBarber() {
    }

    @Test
    void checkOut() {
    }

    @Test
    void findBarbersAtZip() {
    }
}