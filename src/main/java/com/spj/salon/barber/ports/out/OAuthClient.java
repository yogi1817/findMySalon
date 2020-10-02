package com.spj.salon.barber.ports.out;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public interface OAuthClient {
    public String getJwtToken(String userName, String password, String clientHost) throws OAuth2Exception;
    
}
