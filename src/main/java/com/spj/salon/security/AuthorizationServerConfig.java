/*
package com.spj.salon.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

*/
/**
 * This will define cors for oauth service
 * @author Yogesh Sharma
 *
 *//*

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.applyPermitDefaultValues();

		// add allow-origin to the headers
		config.addAllowedHeader("access-control-allow-origin");

		source.registerCorsConfiguration("/oauth/token", config);
		source.registerCorsConfiguration("/oauth/authenticate", config);

		CorsFilter filter = new CorsFilter(source);
		security.addTokenEndpointAuthenticationFilter(filter);
	}
}*/
