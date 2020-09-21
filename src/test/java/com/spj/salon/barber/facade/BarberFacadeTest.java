package com.spj.salon.barber.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spj.salon.barber.model.Authorities;
import com.spj.salon.barber.repository.AuthoritiesRepository;
import com.spj.salon.client.GoogleGeoCodingClient;
import com.spj.salon.client.OAuthClient;
import com.spj.salon.otp.facade.MyEmailService;
import com.spj.salon.security.pojo.UserType;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.user.dao.IUserDao;
import com.spj.salon.user.model.User;
import com.spj.salon.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BarberFacadeTest {

	private BarberFacade testSubject;
	
	@Mock private UserRepository userRepository;
	@Mock private IUserDao userDao;
	@Mock private ServicesRepository serviceRepo;
	@Mock private GoogleGeoCodingClient googleGeoCodingClient;
	@Mock private MyEmailService myEmailService;
	@Mock private AuthoritiesRepository authoritiesRepository;
	@Mock private PasswordEncoder passwordEncoder;
	@Mock private OAuthClient oAuthClient;
	
	final User user = User.builder()
			.firstName("testfirst")
			.lastName("testlast")
			.email("email@test.com")
			.phone("1234567890")
			.password("testpassword")
			.storeName("teststore")
			.loginId("testlogin")
			.jwtToken("jwt")
			.build();
	
	@BeforeEach
	void setup() {
			testSubject = new BarberFacade(userRepository, userDao, serviceRepo, 
					googleGeoCodingClient, myEmailService, authoritiesRepository, passwordEncoder, oAuthClient);
	}
	
	@Test
	void shouldRegisterAndReturnUser() {
		doReturn(null)
			.when(userDao)
			.searchUserWithLoginIdAuthority("testlogin", UserType.USER.getResponse());
		
		Authorities authorities = Authorities.builder()
												.authority("user")
												.authorityId(1l)
												.build();
		doReturn(authorities)
			.when(authoritiesRepository)
			.findByAuthority(UserType.USER.getResponse());
		
		doReturn("testpassword")
			.when(passwordEncoder)
			.encode("testpassword");
		
		User userInput = User.builder()
							.loginId("testlogin")
							.password("testpassword")
							.build();
		doReturn(user)
			.when(oAuthClient)
			.getJwtToken(userInput, "test");
		
		User returnUser = testSubject.register(user, UserType.USER, "test");
		
		assertThat(returnUser!=null);
		verify(userDao, times(1))
			.searchUserWithLoginIdAuthority("testlogin", UserType.USER.getResponse());
		verify(authoritiesRepository, times(1))
			.findByAuthority(UserType.USER.getResponse());
		
		verifyNoMoreInteractions(userDao);
		verifyNoMoreInteractions(authoritiesRepository);
	}

	@Test
	void shouldThrowDuplicateExceptionWhileRegister() {
		List<User> list = new ArrayList<User>();
		list.add(user);
		doReturn(list)
			.when(userDao)
			.searchUserWithLoginIdAuthority("testlogin", UserType.USER.getResponse());
		
		Assertions.assertThrows(DuplicateKeyException.class, ()->testSubject.register(user, UserType.USER, "test"));
		
		verify(userDao, times(1))
			.searchUserWithLoginIdAuthority("testlogin", UserType.USER.getResponse());
		
		verifyNoMoreInteractions(userDao);
	}
}
