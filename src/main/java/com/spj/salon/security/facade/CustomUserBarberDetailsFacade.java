package com.spj.salon.security.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.repository.BarberRepository;
import com.spj.salon.security.pojo.CustomBarber;

@Service
public class CustomUserBarberDetailsFacade implements UserDetailsService{

	@Autowired
	private BarberRepository barberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Barber barber = null;
		try {
			barber = barberRepository.findByLoginId(username);
			if (barber != null) {
				CustomBarber customUser = new CustomBarber(barber);
				return customUser;
			} else {
				throw new UsernameNotFoundException("User " + username + " was not found in the database");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
	}
}
