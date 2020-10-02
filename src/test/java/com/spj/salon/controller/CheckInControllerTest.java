package com.spj.salon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.spj.salon.barber.dto.BarberAddressDTO;
import com.spj.salon.barber.dto.BarberCheckInRequest;
import com.spj.salon.barber.dto.BarberCheckInResponse;
import com.spj.salon.checkin.facade.ICheckinFacade;

@ExtendWith(MockitoExtension.class)
public class CheckInControllerTest {

	private CheckInController testSubject;
	
	@Mock
	private ICheckinFacade checkInFacade;
	
	@BeforeEach
	void setup() {
		testSubject = new CheckInController(checkInFacade);
	}
	
	@Test
	void shouldCheckInByUserAndReturnStringResponse() {
		doReturn("CheckedIn")
			.when(checkInFacade)
			.checkInUser(1, "test");
		
		ResponseEntity<String> testResult = testSubject.checkInByUser(1, "test");
		
		assertThat(testResult.getBody()!=null);
		verify(checkInFacade, times(1))
			.checkInUser(1, "test");
		verifyNoMoreInteractions(checkInFacade);
	}
	
	@Test
	void shouldCheckInByBarberAndReturnStringResponse() {
		doReturn("CheckedIn")
			.when(checkInFacade)
			.checkInBarber(1, "test");
		
		ResponseEntity<String> testResult = testSubject.checkInByBarber(1, "test");
		
		assertThat(testResult.getBody()!=null);
		verify(checkInFacade, times(1))
			.checkInBarber(1, "test");
		verifyNoMoreInteractions(checkInFacade);
	}
	
	@Test
	void shouldFindWaitTimeEstimateAtBarberAndReturnString() {
		doReturn("15 mins")
			.when(checkInFacade)
			.waitTimeEstimate(1);
		
		ResponseEntity<String> testResult = testSubject.waitTimeEstimate(1);
		
		assertThat(testResult.getBody()!=null);
		verify(checkInFacade, times(1))
			.waitTimeEstimate(1);
		verifyNoMoreInteractions(checkInFacade);
	}
	
	@Test
	void shouldCheckOutUserFromBarberAndReturnBoolean() {
		Map<String, String> headers = new HashMap<>();
		headers.put("userid", "1");
		doReturn(true)
			.when(checkInFacade)
			.checkOut(1);
		
		ResponseEntity<Boolean> testResult = testSubject.checkOut(headers);
		
		assertThat(testResult.getBody()!=null);
		verify(checkInFacade, times(1))
			.checkOut(1);
		verifyNoMoreInteractions(checkInFacade);
	}
	
	@Test
	void shouldFindBarbersAtZipAndReturnListOfBarbers() throws ServiceUnavailableException {
		Map<String, String> headers = new HashMap<>();
		headers.put("hostname", "localhost");
		BarberCheckInRequest barberCheckInRequest = BarberCheckInRequest.builder()
															.zipCode("15220")
															.distance(25d)
															.build();
		BarberAddressDTO barberAddressDTO = BarberAddressDTO.builder()
													.firstName("test")
													.lastName("testlast")
													.distance(123.22332)
													.build();
		List<BarberAddressDTO> list = new ArrayList<>();
		list.add(barberAddressDTO);
		BarberCheckInResponse barberCheckInResponse = BarberCheckInResponse.builder()
																.message("1 barber Found")
																.barberAddressDTO(list)
																.build();
		doReturn(barberCheckInResponse)
			.when(checkInFacade)
			.findBarbersAtZip(barberCheckInRequest);
		
		ResponseEntity<BarberCheckInResponse> testResult = testSubject.findBarbersAtZip(barberCheckInRequest, headers);
		
		assertThat(testResult.getBody()!=null);
		verify(checkInFacade, times(1))
			.findBarbersAtZip(barberCheckInRequest);
		verifyNoMoreInteractions(checkInFacade);
	}
}
