package com.spj.salon.checkin.adapters;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import com.spj.salon.barber.entities.Address;
import com.spj.salon.checkin.adapters.CheckInAdapterMapperImpl;
import com.spj.salon.exception.DuplicateEntityException;
import com.spj.salon.openapi.resources.BarberDetails;
import com.spj.salon.openapi.resources.BarberWaitTimeRequest;
import com.spj.salon.openapi.resources.BarbersWaitTimeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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

@ExtendWith(MockitoExtension.class)
class CheckInFacadeTest {

    private CheckInFacade testSubject;

    @Mock private UserRepository userRepository;
    @Mock private CheckInRepository checkInRepository;
    @Mock private ZipCodeRepository zipCodeRepo;
    @Mock private AddressRepository addressRepo;
    @Mock private GeoCoding googleGeoCodingClient;
    private CheckInAdapterMapper checkInAdapterMapper = new CheckInAdapterMapperImpl();
    private final String DATE_FORMAT = "hh:mm aa";

    final User customer = User.builder()
            .authorityId(1)
            .userId(2L)
            .email("customer@customer.com")
            .build();

    @BeforeEach
    void setUp(){
        testSubject = new CheckInFacade(userRepository, checkInRepository, zipCodeRepo, addressRepo, googleGeoCodingClient, checkInAdapterMapper);
    }

    @Test
    void waitTimeEstimateWhenNoBarberCalenderSet() {
        final User barber = User.builder()
                .authorityId(2)
                .email("barber@barber.com")
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
                .userId(1L)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), "encryptedPassword"));

        Mockito.doReturn(customer)
                .when(userRepository)
                .findByEmail(customer.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        Assertions.assertEquals("Unable to Checkin",testSubject.checkInCustomerByCustomer(Optional.of(1L)).getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(customer.getEmail());

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
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
                .dailyBarberSet(dailyBarbersSet)
                .barberCalendarSet(barberCalendarSet)
                .checkInSet(checkInSet)
                .userId(1L)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), "encryptedPassword"));

        Mockito.doReturn(customer)
                .when(userRepository)
                .findByEmail(customer.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        Mockito.doReturn(0)
                .when(checkInRepository)
                .countByUserMappingIdAndCheckedOutAndCreateDate(2L, false, LocalDate.now());

        CheckIn checkIn = new CheckIn(1L, 2L, 30, 2L);

        Mockito.doReturn(checkIn)
                .when(checkInRepository)
                .saveAndFlush(checkIn);

        Assertions.assertEquals("Check in with waitTime 30",testSubject.checkInCustomerByCustomer(Optional.of(1L)).getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(customer.getEmail());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .countByUserMappingIdAndCheckedOutAndCreateDate(2L, false, LocalDate.now());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .saveAndFlush(checkIn);

        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoMoreInteractions(checkInRepository);
    }

    @Test
    void checkInCustomerByCustomerForAlreadyCheckedInCustomer() {
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
                .dailyBarberSet(dailyBarbersSet)
                .barberCalendarSet(barberCalendarSet)
                .checkInSet(checkInSet)
                .userId(1L)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), "encryptedPassword"));

        Mockito.doReturn(customer)
                .when(userRepository)
                .findByEmail(customer.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(1L);

        Mockito.doReturn(1)
                .when(checkInRepository)
                .countByUserMappingIdAndCheckedOutAndCreateDate(2L, false, LocalDate.now());

        Exception exception = Assertions.assertThrows(DuplicateEntityException.class, () -> {
            testSubject.checkInCustomerByCustomer(Optional.of(1L));
        });
        Assertions.assertEquals("Customer is already checkedIn",exception.getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(customer.getEmail());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .countByUserMappingIdAndCheckedOutAndCreateDate(2L, false, LocalDate.now());

        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoMoreInteractions(checkInRepository);
    }

    @Test
    void checkInCustomerByBarber() {
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
                .dailyBarberSet(dailyBarbersSet)
                .barberCalendarSet(barberCalendarSet)
                .checkInSet(checkInSet)
                .userId(1L)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), "encryptedPassword"));

        Mockito.doReturn(customer)
                .when(userRepository)
                .findByEmail(customer.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByUserId(2L);

        Mockito.doReturn(0)
                .when(checkInRepository)
                .countByUserMappingIdAndCheckedOutAndCreateDate(1L, false, LocalDate.now());

        CheckIn checkIn = new CheckIn(2L, 1L, 30, 2L);

        Mockito.doReturn(checkIn)
                .when(checkInRepository)
                .saveAndFlush(checkIn);

        Assertions.assertEquals("Check in with waitTime 30",testSubject.checkInCustomerByBarber(2L).getMessage());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(customer.getEmail());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .countByUserMappingIdAndCheckedOutAndCreateDate(1L, false, LocalDate.now());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .saveAndFlush(checkIn);

        Mockito.verifyNoMoreInteractions(userRepository);

        Mockito.verifyNoMoreInteractions(checkInRepository);
    }

    @Test
    void checkOutNoUsers() {
        Mockito.doReturn(Arrays.asList(CheckIn.builder().build()))
                .when(checkInRepository)
                .findByUserMappingIdAndCheckedOutAndCreateDate(1L, false, LocalDate.now());

        Assertions.assertEquals("Customer has been checked out", testSubject.checkOut(1L, 1L).getMessage());

        Mockito.verify(checkInRepository, Mockito.times(1))
                .findByUserMappingIdAndCheckedOutAndCreateDate(1L, false, LocalDate.now());

        Mockito.verifyNoMoreInteractions(checkInRepository);
    }

    @Test
    void findBarbersAtZipWhenNoBarbersAvailable() {
        BarberWaitTimeRequest barberWaitTimeRequest = new BarberWaitTimeRequest()
                .latitude(40.5470947).longitude(-80.011957).distance(25D);

        double long1 = barberWaitTimeRequest.getLongitude() - barberWaitTimeRequest.getDistance() / Math.abs(Math.cos(Math.toRadians(barberWaitTimeRequest.getLatitude())) * 69);
        double long2 = barberWaitTimeRequest.getLongitude() + barberWaitTimeRequest.getDistance() / Math.abs(Math.cos(Math.toRadians(barberWaitTimeRequest.getLatitude())) * 69);
        double lat1 = barberWaitTimeRequest.getLatitude() - (barberWaitTimeRequest.getDistance() / 69);
        double lat2 = barberWaitTimeRequest.getLatitude() + (barberWaitTimeRequest.getDistance() / 69);

        Mockito.doReturn(new ArrayList<Address>())
                .when(addressRepo)
                .getBarbersId(barberWaitTimeRequest.getLongitude(), barberWaitTimeRequest.getLatitude(),
                        barberWaitTimeRequest.getDistance(), long1, long2, lat1, lat2);

        Assertions.assertEquals("No Barbers Found within the range",
                testSubject.findBarbersAtZip(barberWaitTimeRequest).getMessage());

        Mockito.verify(addressRepo, Mockito.times(1))
                .getBarbersId(barberWaitTimeRequest.getLongitude(), barberWaitTimeRequest.getLatitude(),
                        barberWaitTimeRequest.getDistance(), long1, long2, lat1, lat2);

        Mockito.verifyNoMoreInteractions(addressRepo);
    }

    /*@Test
    void findBarbersAtLongiAndLati() {
        BarberWaitTimeRequest barberWaitTimeRequest = new BarberWaitTimeRequest()
                .latitude(40.5470947).longitude(-80.011957).distance(25D);

        BarbersWaitTimeResponse barberWaitTimeResponse = new BarbersWaitTimeResponse()
                .message("2 Barbers Found")
                .addBarberDetailsItem(new BarberDetails()
                        .addressLineOne("Line One")
                        .distance(5.0)
                        .waitTime("Unable to find wait-time")
                        .email("user1")
                        .longitude(-80.011957)
                        .latitude(40.5470947)
                        .barberId(1L))
                .addBarberDetailsItem(new BarberDetails()
                        .addressLineOne("Line Two")
                        .distance(8.0)
                        .waitTime("Unable to find wait-time")
                        .email("user2")
                        .longitude(-80.011957)
                        .latitude(40.5470947)
                        .barberId(2L));

        double long1 = barberWaitTimeRequest.getLongitude() - barberWaitTimeRequest.getDistance() / Math.abs(Math.cos(Math.toRadians(barberWaitTimeRequest.getLatitude())) * 69);
        double long2 = barberWaitTimeRequest.getLongitude() + barberWaitTimeRequest.getDistance() / Math.abs(Math.cos(Math.toRadians(barberWaitTimeRequest.getLatitude())) * 69);
        double lat1 = barberWaitTimeRequest.getLatitude() - (barberWaitTimeRequest.getDistance() / 69);
        double lat2 = barberWaitTimeRequest.getLatitude() + (barberWaitTimeRequest.getDistance() / 69);

        Map<String, Object> map1 = Map.of("address_id", BigInteger.valueOf(1L), "distance", 5D);
        Map<String, Object> map2 = Map.of("address_id", BigInteger.valueOf(2L), "distance", 8D);
        List<Map<String, Object>> addressIds = List.of(map1, map2);

        Mockito.doReturn(addressIds)
                .when(addressRepo)
                .getBarbersId(barberWaitTimeRequest.getLongitude(), barberWaitTimeRequest.getLatitude(),
                        barberWaitTimeRequest.getDistance(), long1, long2, lat1, lat2);

        Mockito.doReturn(Optional.of(Address.builder().addressId(1L).addressLineOne("Line One").userId(1L).build()))
                .when(addressRepo)
                .findById(1L);

        Mockito.doReturn(Optional.of(Address.builder().addressId(2L).addressLineOne("Line Two").userId(2L).build()))
                .when(addressRepo)
                .findById(2L);

        Mockito.doReturn(Optional.of(User.builder().email("user1").userId(1L).build()))
                .when(userRepository)
                .findById(1L);

        Mockito.doReturn(Optional.of(User.builder().email("user2").userId(2L).build()))
                .when(userRepository)
                .findById(2L);

        Assertions.assertEquals(barberWaitTimeResponse,
                testSubject.findBarbersAtZip(barberWaitTimeRequest));

        Mockito.verify(addressRepo, Mockito.times(1))
                .getBarbersId(barberWaitTimeRequest.getLongitude(), barberWaitTimeRequest.getLatitude(),
                        barberWaitTimeRequest.getDistance(), long1, long2, lat1, lat2);

        Mockito.verify(addressRepo, Mockito.times(1))
                .findById(2L);

        Mockito.verify(addressRepo, Mockito.times(1))
                .findById(1L);

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(1L);

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(2L);

        Mockito.verifyNoMoreInteractions(addressRepo);
    }*/
}