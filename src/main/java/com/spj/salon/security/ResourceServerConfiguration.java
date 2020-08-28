package com.spj.salon.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/barber/register").permitAll()
		.antMatchers("/user/register").permitAll()
		.antMatchers("/user/authenticate").permitAll()
		.antMatchers("/oauth/token").permitAll()
		.antMatchers("barber/*/waittime").permitAll()
		.antMatchers("/checkin/barbers/waittime/forlocation").permitAll()
		.antMatchers("/oauth/user").permitAll()
		.antMatchers("/barber/validate/prime-number").permitAll()
		.antMatchers("/barber/message").permitAll()
		.antMatchers("/user/favourite").hasAnyAuthority("USER")
		.antMatchers("/barber/barbersCount").hasAnyAuthority("BARBER")
		.antMatchers("/services/register").hasAnyAuthority("SUPERUSER")
		.antMatchers("/barber/calendar").hasAnyAuthority("BARBER")
		.antMatchers("/checkin/barber/*/time/*").hasAnyAuthority("USER")
		.antMatchers("/checkin/user/*/time/*").hasAnyAuthority("BARBER")
		.antMatchers("/checkin/user/checkout").hasAnyAuthority("BARBER")
		.antMatchers("/barber/address").hasAnyAuthority("BARBER")
		.anyRequest()
		.authenticated();
	}
}