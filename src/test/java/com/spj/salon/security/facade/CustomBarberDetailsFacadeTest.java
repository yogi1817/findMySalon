package com.spj.salon.security.facade;

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
import org.springframework.security.core.userdetails.UserDetails;

import com.spj.salon.barber.entities.Authorities;
import com.spj.salon.customer.model.User;
import com.spj.salon.customer.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomBarberDetailsFacadeTest {

	private CustomBarberDetailsFacade testSubject;
	
	@Mock
	private UserRepository userRepository;
	
	@BeforeEach
	void setup() {
		testSubject = new CustomBarberDetailsFacade(userRepository);
	}
	
	@Test
	void shouldLoadUserByUserNameAndReturnUserDetails() {
		Authorities authority = Authorities.builder()
										.authority("BARBER")
										.authorityId(1L)
										.build();
		User user = User.builder()
				.firstName("testfirst")
				.lastName("testlast")
				.email("email@test.com")
				.phone("1234567890")
				.password("testpassword")
				.storeName("teststore")
				.authority(authority)
				.build();
		
		doReturn(user)
			.when(userRepository)
			.findByEmail("email@test.com");
		
		UserDetails testResult = testSubject.loadUserByUsername("email@test.com");
		
		assertThat(testResult!=null);
		verify(userRepository, times(1))
			.findByEmail("email@test.com");
		verifyNoMoreInteractions(userRepository);
	}
}
