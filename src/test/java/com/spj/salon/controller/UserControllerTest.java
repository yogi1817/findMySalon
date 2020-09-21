package com.spj.salon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.spj.salon.barber.facade.IBarberFacade;
import com.spj.salon.client.OAuthClient;
import com.spj.salon.security.pojo.UserType;
import com.spj.salon.user.facade.IUserFacade;
import com.spj.salon.user.model.User;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	private UserController testSubject;
	
	@Mock
	private IUserFacade userFacade;
	@Mock
	private IBarberFacade barberFacade;
	@Mock
	private OAuthClient oAuthClient;
	
	final User user = User.builder()
			.firstName("testfirst")
			.lastName("testlast")
			.email("email@test.com")
			.phone("1234567890")
			.password("testpassword")
			.storeName("teststore")
			.build();
	
	@BeforeEach
	void setup() {
		testSubject = new UserController(userFacade, barberFacade, oAuthClient);
	}
	
	@Test
	void shouldRegisterBarberAndReturnUserObject() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("clienthost", "test");
		doReturn(user)
			.when(barberFacade)
			.register(user, UserType.USER, headers.get("clienthost"));
		
		ResponseEntity<User> testResult = testSubject.registerBarber(user, headers);
		
		assertThat(testResult.getBody()!=null);
		verify(barberFacade, times(1))
			.register(user, UserType.USER, headers.get("clienthost"));
		verifyNoMoreInteractions(barberFacade);
	}
	
	@Test
	void shouldAddFavouriteSalonAndReturnBoolean() {
		doReturn(true)
			.when(userFacade)
			.addFavouriteSalon(1L);
		
		ResponseEntity<Boolean> testResult = testSubject.addFavouriteSalon(1L);
		
		assertThat(testResult.getBody()!=null);
		verify(userFacade, times(1))
			.addFavouriteSalon(1L);
		verifyNoMoreInteractions(barberFacade);
	}
	
	@Test
	void shouldAuthenticateAndReturnUser() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("clienthost", "test");
		doReturn(user)
			.when(oAuthClient)
			.getJwtToken(user, "test");
		
		ResponseEntity<User> testResult = testSubject.authenticate(user, headers);
		
		assertThat(testResult.getBody()!=null);
		verify(oAuthClient, times(1))
			.getJwtToken(user, "test");
		verifyNoMoreInteractions(oAuthClient);
	}
	
	@Test
	void shouldUpdatePasswordAndReturnUserObject() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("clienthost", "test");
		doReturn(user)
			.when(userFacade)
			.updatePassword(user, "test");
		
		ResponseEntity<User> testResult = testSubject.updatePassword(user, headers);
		
		assertThat(testResult.getBody()!=null);
		verify(userFacade, times(1))
			.updatePassword(user, "test");
		verifyNoMoreInteractions(oAuthClient);
	}
}