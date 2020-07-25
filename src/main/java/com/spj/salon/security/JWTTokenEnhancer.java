package com.spj.salon.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.spj.salon.security.pojo.CustomUser;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public class JWTTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    	Map<String, Object> info = new HashMap<>();
        
    	CustomUser user = (CustomUser) authentication.getPrincipal();
		if (user.getId() != null)
			info.put("id", user.getId());
		if (user.getFirst_name() != null)
			info.put("first_name", user.getFirst_name());
		if (user.getLast_name() != null)
			info.put("last_name", user.getLast_name());
		if (user.getStoreName() != null)
			info.put("email", user.getEmail());
		if (user.getMobile() != null)
			info.put("mobile", user.getMobile());
		if (user.getLoginId() != null)
			info.put("loginId", user.getLoginId());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}