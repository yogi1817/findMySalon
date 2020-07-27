package com.spj.salon.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.spj.salon.barber.model.Authorities;
import com.spj.salon.user.model.User;

public class AuthorityUtils {

	public static Collection<GrantedAuthority> getUserAuthorities(User user) {
		Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
		
		Authorities authority = user.getAuthority();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
		grantedAuthoritiesList.add(grantedAuthority);
		
		return grantedAuthoritiesList;
	}
}
