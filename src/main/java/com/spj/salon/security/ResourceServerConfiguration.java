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
		.antMatchers("/customer/register").permitAll()
		.antMatchers("/customer/authenticate").permitAll()
		.antMatchers("/oauth/token").permitAll()
		.antMatchers("/barber/*/waittime").permitAll()
		.antMatchers("/checkin/barbers/waittime/forlocation").permitAll()
		.antMatchers("/oauth/user").permitAll()
		.antMatchers("/barber/validate/prime-number").permitAll()
		//.antMatchers("/v3/api-docs").permitAll()
		//.antMatchers("/v3/api-docs/*.*").permitAll()
		//.antMatchers("/v3/api-docs.yaml").permitAll()
		//.antMatchers("/v3/api-docs/swagger-config").permitAll()
		//.antMatchers("/swagger-ui/*.*").permitAll()
		//.antMatchers("/swagger-ui.html").permitAll()
		.antMatchers("/customer/favourite").hasAnyAuthority("USER")
		.antMatchers("/barber/barbersCount").hasAnyAuthority("BARBER")
		.antMatchers("/services/register").hasAnyAuthority("SUPERUSER")
		.antMatchers("/barber/calendar").hasAnyAuthority("BARBER")
		.antMatchers("/checkin/barber/*/time/*").hasAnyAuthority("USER")
		.antMatchers("/checkin/customer/*/time/*").hasAnyAuthority("BARBER")
		.antMatchers("/checkin/customer/checkout").hasAnyAuthority("BARBER")
		.antMatchers("/barber/address").hasAnyAuthority("BARBER")
		.antMatchers("/generateOtp/email").hasAnyAuthority("USER")
		.antMatchers("/generateOtp/mobile").hasAnyAuthority("USER")
		.antMatchers("/generateOtp/forgotpassword/email").permitAll()
		.antMatchers("/generateOtp/forgotpassword/mobile").permitAll()
		.antMatchers("/generateOtp/validateOtp").hasAnyAuthority("USER")
		//.antMatchers("/generateOtp/forgotpassword/validateOtp").permitAll()
		.antMatchers("/customer/updatepassword").permitAll()
		.anyRequest()
		.authenticated();
	}
}