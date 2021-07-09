package com.spj.salon.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author Yogesh Sharma
 */
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/barber/register").permitAll()
                .antMatchers("/barber/*/waittime").permitAll()
                .antMatchers("/user/updatepassword").permitAll()
                .antMatchers("/customer/register").permitAll()
                .antMatchers("/customer/authenticate").permitAll()
                .antMatchers("/barber/authenticate").permitAll()
                .antMatchers("/customer/refresh").permitAll()
                .antMatchers("/barber/refresh").permitAll()
                .antMatchers("/checkin/barbers/waittime/forlocation").permitAll()
                .antMatchers("/otp/forgotpassword/email").permitAll()
                .antMatchers("/otp/forgotpassword/mobile").permitAll()
                .antMatchers("/otp/forgotpassword").permitAll()
                .antMatchers("/otp/validate/prepassword").permitAll()
                .antMatchers("/customer/favourite").hasAnyAuthority("CUSTOMER")
                .antMatchers("/barber/services/register").hasAnyAuthority("SUPERUSER", "BARBER")
                .antMatchers("/checkin/barber/*/time/*").hasAnyAuthority("CUSTOMER")
                .antMatchers("/checkin/barber").hasAnyAuthority("CUSTOMER", "BARBER")
                .antMatchers("/otp/postlogin").hasAnyAuthority("CUSTOMER")
                .antMatchers("/otp/validate/postlogin").hasAnyAuthority("CUSTOMER")
                .antMatchers("/checkin/customer/*/time/*").hasAnyAuthority("BARBER")
                .antMatchers("/checkin/customer").hasAnyAuthority("BARBER")
                .antMatchers("/checkin/customer/checkout").hasAnyAuthority("BARBER", "CUSTOMER", "SUPERUSER")
                .antMatchers("/checkin/customer/currentwaittime").hasAnyAuthority("CUSTOMER")
                .antMatchers("/barber/barbersCount").hasAnyAuthority("BARBER")
                .antMatchers("/barber/calendar").hasAnyAuthority("BARBER")
                .antMatchers("/user/address").hasAnyAuthority("BARBER", "CUSTOMER")
                .antMatchers("/user/profile").hasAnyAuthority("BARBER", "CUSTOMER")
                .anyRequest()
                .authenticated();
    }
}