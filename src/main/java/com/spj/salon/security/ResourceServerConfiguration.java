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
                .antMatchers("/barber/validate/prime-number").permitAll()
                .antMatchers("/customer/updatepassword").permitAll()
                .antMatchers("/customer/register").permitAll()
                .antMatchers("/customer/authenticate").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/oauth/user").permitAll()
                .antMatchers("/checkin/barbers/waittime/forlocation").permitAll()
                .antMatchers("/otp/forgotpassword/email").permitAll()
                .antMatchers("/otp/forgotpassword/mobile").permitAll()
                //.antMatchers("/v3/api-docs").permitAll()
                //.antMatchers("/v3/api-docs/*.*").permitAll()
                //.antMatchers("/v3/api-docs.yaml").permitAll()
                //.antMatchers("/v3/api-docs/swagger-config").permitAll()
                //.antMatchers("/swagger-ui/*.*").permitAll()
                //.antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/barber/services/register").hasAnyAuthority("SUPERUSER")
                .antMatchers("/customer/favourite").hasAnyAuthority("USER")
                .antMatchers("/checkin/barber/*/time/*").hasAnyAuthority("USER")
                .antMatchers("/otp/email").hasAnyAuthority("USER")
                .antMatchers("/otp/mobile").hasAnyAuthority("USER")
                .antMatchers("/otp/validateOtp").hasAnyAuthority("USER")
                .antMatchers("/checkin/customer/*/time/*").hasAnyAuthority("BARBER")
                .antMatchers("/checkin/customer/checkout").hasAnyAuthority("BARBER")
                .antMatchers("/barber/barbersCount").hasAnyAuthority("BARBER")
                .antMatchers("/barber/calendar").hasAnyAuthority("BARBER")
                .antMatchers("/barber/address").hasAnyAuthority("BARBER")
                //.antMatchers("/generateOtp/forgotpassword/validateOtp").permitAll()
                .anyRequest()
                .authenticated();
    }
}