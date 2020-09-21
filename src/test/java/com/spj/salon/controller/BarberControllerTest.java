package com.spj.salon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.HashMap;
import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.spj.salon.barber.facade.IBarberFacade;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.security.pojo.UserType;
import com.spj.salon.user.model.User;

@ExtendWith(MockitoExtension.class)
public class BarberControllerTest {

	private BarberController testSubject;
	
	@Mock
	private IBarberFacade barberFacade;
	
	final User barber = User.builder()
			.firstName("testfirst")
			.lastName("testlast")
			.email("email@test.com")
			.phone("1234567890")
			.password("testpassword")
			.storeName("teststore")
			.build();
	
	@BeforeEach
	void setup() {
		testSubject = new BarberController(barberFacade);
	}
	
	@Test
	void shouldRegisterUserAndReturnUserObject() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("clienthost", "test");
		doReturn(barber)
			.when(barberFacade)
			.register(barber, UserType.BARBER, headers.get("clienthost"));
		
		ResponseEntity<User> testResult = testSubject.registerBarber(barber, headers);
		
		assertThat(testResult.getBody()!=null);
		verify(barberFacade, times(1))
			.register(barber, UserType.BARBER, "test");
		verifyNoMoreInteractions(barberFacade);
	}
	
	@Test
	void shouldAddBarbersCountToUserTodayAndReturnABooleanValue() {
		DailyBarbers dailyBarbers = DailyBarbers.builder()
										.barbersCount(5)
										.barbersDescription("Test Barbers")
										.build();
		doReturn(true)
			.when(barberFacade)
			.addBarbersCountToday(dailyBarbers);
		
		ResponseEntity<Boolean> testResult = testSubject.addBarbersCountToday(dailyBarbers);
		
		assertThat(testResult.getBody()==true);
		verify(barberFacade, times(1))
				.addBarbersCountToday(dailyBarbers);
		verifyNoMoreInteractions(barberFacade);
	}
	
	@Test
	void shouldAddServicesToUserAndReturnABooleanValue() {
		doReturn(true)
			.when(barberFacade)
			.addServices(1, 15, 15);
		
		ResponseEntity<Boolean> testResult = testSubject.addServices(1, 15, 15);
		
		assertThat(testResult.getBody()==true);
		verify(barberFacade, times(1))
				.addServices(1, 15, 15);
		verifyNoMoreInteractions(barberFacade);
	}
	
	@Test
	void shouldAddBarberCalendarAndReturnTrue() {
		BarberCalendar barberCalendar = BarberCalendar.builder()
											.salonClosesAt("7:00 PM")
											.salonOpensAt("11:00 AM")
											.build();
		doReturn(true)
			.when(barberFacade)
			.addBarberCalendar(barberCalendar);
		
		ResponseEntity<Boolean> testResult = testSubject.addBarberCalendar(barberCalendar);
		
		assertThat(testResult.getBody()==true);
		verify(barberFacade, times(1))
				.addBarberCalendar(barberCalendar);
		verifyNoMoreInteractions(barberFacade);
	}
	
	@Test
	void shouldAddBarberAddressAndReturnTrue() throws ServiceUnavailableException {
		Address address = Address.builder()
								.addressLineOne("Testaddress")
								.city("Testcity")
								.country("testcountry")
								.state("teststate")
								.zip("testzip")
								.build();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("hostname", "test");
		doReturn(true)
			.when(barberFacade)
			.addBarberAddress(address);
		
		ResponseEntity<Boolean> testResult = testSubject.addBarberAddress(address, headers);
		
		assertThat(testResult.getBody()==true);
		verify(barberFacade, times(1))
					.addBarberAddress(address);
		verifyNoMoreInteractions(barberFacade);
	}
}
