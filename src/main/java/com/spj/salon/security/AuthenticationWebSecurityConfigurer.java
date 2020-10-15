package com.spj.salon.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spj.salon.security.adapters.CustomBarberDetailsAdapter;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthenticationWebSecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomBarberDetailsAdapter userbarberFacade;
	
	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userbarberFacade).passwordEncoder(passwordEncoder);
	}
	
	/**
	 * This will configure oauth/token service
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**",
				"/actuator/**");
	}

	/**
	 * This will configure /auth/user service
	 */
	/*@Override
	@Bean
	public UserDetailsService userDetailsServiceBean() throws Exception {
		// TODO Auto-generated method stub
		return super.userDetailsServiceBean();
	}*/

	/**
	 * If you are not using any password encoder, you have to add {noop} 
	 * telling spring that password is not secured by any do you try to decode it.
	 */
	/*@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder)
			.withUser(serviceConfig.getUsername())
				.password(passwordEncoder.encode(serviceConfig.getPassword()))
				.roles("CUSTOMER")
		.and()
			.withUser("csp")
			.password(passwordEncoder.encode("Computer1"))
			.roles("CUSTOMER","ADMIN");
			
	}*/
}