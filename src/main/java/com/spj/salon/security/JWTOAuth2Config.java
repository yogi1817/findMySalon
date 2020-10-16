package com.spj.salon.security;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.spj.salon.configs.ServiceConfig;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
@RequiredArgsConstructor
public class JWTOAuth2Config extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final TokenStore tokenStore;
	private final PasswordEncoder passwordEncoder;
	private final JwtAccessTokenConverter jwtAccessTokenConverter;
	private final TokenEnhancer jwtTokenEnhancer;
	private final ServiceConfig serviceConfig;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));
		
		endpoints.tokenStore(tokenStore)
			.tokenEnhancer(tokenEnhancerChain)
			.accessTokenConverter(jwtAccessTokenConverter)
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService);
	}
	
	@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
        		.withClient(serviceConfig.getApplicationId())
                .secret(passwordEncoder.encode(serviceConfig.getApplicationPassword()))
                .authorizedGrantTypes("refresh_token", "password")
                .accessTokenValiditySeconds(18000)
                .refreshTokenValiditySeconds(18000);
                //.scopes("webclient", "mobileclient");
    }
}