package com.spj.salon.user.ports.out;

import com.spj.salon.openapi.client.resources.RegisterUserRequest;

public interface AuthorizationClient {
    public boolean registerUserOnAuthorizationServer(RegisterUserRequest registerUserRequest);
}
