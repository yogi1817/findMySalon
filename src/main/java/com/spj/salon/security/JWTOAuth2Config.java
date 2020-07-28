package com.spj.salon.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.spj.salon.config.ServiceConfig;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Configuration
public class JWTOAuth2Config extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
    
	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired
	private TokenEnhancer jwtTokenEnhancer;
	
	@Autowired
	private ServiceConfig serviceConfig;
	
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