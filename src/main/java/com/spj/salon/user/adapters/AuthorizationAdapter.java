package com.spj.salon.user.adapters;

import com.spj.salon.configs.AuthorizationClientConfig;
import com.spj.salon.openapi.client.api.RegisterUserApi;
import com.spj.salon.openapi.client.resources.RegisterUserRequest;
import com.spj.salon.user.ports.out.AuthorizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class AuthorizationAdapter implements AuthorizationClient {
    private final AuthorizationClientConfig authorizationClientConfig;
    private final RegisterUserApi registerUserApi;

    @PostConstruct
    public void postConstruct() {
        registerUserApi.getApiClient().setBasePath(authorizationClientConfig.getOauthHost());
    }

    @Override
    public boolean registerUserOnAuthorizationServer(RegisterUserRequest registerUserRequest) {
        registerUserApi.registerUser(registerUserRequest);
        return true;
    }
}
