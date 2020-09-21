package com.spj.salon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.spj.salon.services.facade.IServicesFacade;
import com.spj.salon.services.model.Services;

@ExtendWith(MockitoExtension.class)
public class ServiceControllerTest {

	private ServicesController testSubject;
	
	@Mock
	private IServicesFacade servicesFacade;
	
	@BeforeEach
	void setup() {
		testSubject = new ServicesController(servicesFacade);
	}
	
	@Test
	void shouldAddServiceAndReturbBoolean() {
		Services services = Services.builder()
									.serviceDescription("Haircut")
									.serviceName("haircut")
									.build();
		doReturn(true)
			.when(servicesFacade)
			.addService(services);
		
		ResponseEntity<Boolean> testResult = testSubject.addService(services);
		
		assertThat(testResult.getBody()!=null);
		verify(servicesFacade, times(1))
			.addService(services);
		verifyNoMoreInteractions(servicesFacade);
	}
}