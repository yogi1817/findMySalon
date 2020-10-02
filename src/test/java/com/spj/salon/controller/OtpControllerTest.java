package com.spj.salon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.spj.salon.otp.facade.IMyOtpService;
import com.spj.salon.otp.facade.OtpService;
import com.spj.salon.customer.ports.in.ICustomerAdapter;

@ExtendWith(MockitoExtension.class)
public class OtpControllerTest {

	@InjectMocks
	private OtpController testSubject;
	
	@Spy
	private IMyOtpService myEmailService;
	
	@Spy
	private IMyOtpService myMobileService;

	@Spy
	private OtpService otpService;
	
	@Spy
	private ICustomerAdapter userFacade;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void shouldGenerateOtpEmailAndReturnString() {
		doNothing()
			.when(myEmailService)
			.sendOtpMessage();
		
		ResponseEntity<String> testResult = testSubject.generateOtpEmail();
		
		assertThat(testResult.getBody()!=null);
		verify(myEmailService, times(1))
			.sendOtpMessage();
		verifyNoMoreInteractions(myEmailService);
	}
	
	@Test
	void shouldGenerateOtpEmailWithEMailInputAndReturnString() {
		doNothing()
			.when(myEmailService)
			.sendOtpMessage("email@email.com");
		
		ResponseEntity<String> testResult = testSubject.generateOtpEmail("email@email.com");
		
		assertThat(testResult.getBody()!=null);
		verify(myEmailService, times(1))
			.sendOtpMessage("email@email.com");
		verifyNoMoreInteractions(myEmailService);
	}
	
	@Test
	void shouldGenerateOtpMobileAndReturnString() {
		doNothing()
			.when(myMobileService)
			.sendOtpMessage();
		
		ResponseEntity<String> testResult = testSubject.generateOtpMobile();
		
		assertThat(testResult.getBody()!=null);
		verify(myMobileService, times(1))
			.sendOtpMessage();
		verifyNoMoreInteractions(myEmailService);
	}
	
	@Test
	void shouldGenerateOtpMobileWithInputNumberAndReturnString() {
		ResponseEntity<String> testResult = testSubject.generateOtpMobile("412-226-2272");
		
		assertThat(testResult.getBody()!=null);
		verify(myMobileService, times(1))
			.sendOtpMessage("412-226-2272");
		verifyNoMoreInteractions(myMobileService);
	}
}