package com.spj.salon.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.spj.salon.security.pojo.CustomBarber;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public class JWTTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    	Map<String, Object> info = new HashMap<>();
        
    	CustomBarber barber = (CustomBarber) authentication.getPrincipal();
		if (barber.getId() != null)
			info.put("id", barber.getId());
		if (barber.getFirst_name() != null)
			info.put("first_name", barber.getFirst_name());
		if (barber.getLast_name() != null)
			info.put("last_name", barber.getLast_name());
		if (barber.getStoreName() != null)
			info.put("email", barber.getEmail());
		if (barber.getMobile() != null)
			info.put("mobile", barber.getMobile());
		if (barber.getLoginId() != null)
			info.put("loginId", barber.getLoginId());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}