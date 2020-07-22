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
		.antMatchers("/findmysalon/oauth/token").permitAll()
		.antMatchers("/distance").permitAll()
		.antMatchers("/waittime").permitAll()
		.anyRequest()
		.authenticated();
	}
}