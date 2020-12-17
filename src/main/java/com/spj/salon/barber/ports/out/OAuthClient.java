package com.spj.salon.barber.ports.out;

import com.spj.salon.openapi.resources.AuthenticationResponse;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public interface OAuthClient {
    AuthenticationResponse getAuthenticationData(String userName, String password, String clientHost) throws OAuth2Exception;
    AuthenticationResponse getRefreshToken(String refreshToken, String email, String clientHost) throws OAuth2Exception;
}
