package com.spj.salon.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthorityUtils {

	public static Collection<GrantedAuthority> getBarberAuthorities() {
		Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("BARBER");
		grantedAuthoritiesList.add(grantedAuthority);
		return grantedAuthoritiesList;
	}
	
	public static Collection<GrantedAuthority> getUserAuthorities() {
		Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
		grantedAuthoritiesList.add(grantedAuthority);
		return grantedAuthoritiesList;
	}
}
